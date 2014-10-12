/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.io.finder;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jetbrick.io.IoUtils;
import jetbrick.io.resource.*;
import jetbrick.util.*;

/**
 * 查找指定路径下面的所有匹配得文件.
 *
 * 子类需要实现自己的 visitXXX() 方法来搜集相关的内容.
 *
 * @author Guoqiang Chen
 */
public abstract class FileFinder {

    public void lookupFileSystem(File dir, boolean recursive) {
        Validate.notNull(dir);
        doLookupInFileSystem(dir, null, null, recursive);
    }

    public void lookupClasspath() {
        lookupClasspath((String[]) null, true);
    }

    public void lookupClasspath(List<String> packageNames, boolean recursive) {
        String[] pkgs = null;
        if (packageNames != null) {
            pkgs = packageNames.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        }
        lookupClasspath(pkgs, recursive);
    }

    public void lookupClasspath(String[] packageNames, boolean recursive) {
        ClassLoader loader = ClassLoaderUtils.getDefault();
        if (packageNames == null || packageNames.length == 0) {
            Collection<URL> urls = ClasspathUtils.getClasspathURLs(loader);
            doGetClasspathResources(urls, null, recursive);
        } else {
            for (String pkg : packageNames) {
                Collection<URL> urls = ClasspathUtils.getClasspathURLs(loader, pkg);
                doGetClasspathResources(urls, pkg, recursive);
            }
        }
    }

    public void lookupZipFile(File zipFile, String entryName, boolean recursive) {
        Validate.notNull(zipFile);

        if (entryName != null) {
            entryName = StringUtils.removeEnd(entryName, "/");
        }
        entryName = StringUtils.trimToEmpty(null);

        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            doLookupInZipFile(zip, entryName, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(zip);
        }
    }

    private void doGetClasspathResources(Collection<URL> urls, String pkg, boolean recursive) {
        for (URL url : urls) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                File file = ResourceUtils.create(url).getFile();
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkg, null, recursive);
                } else if (file.isFile()) {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        ZipFile zip = null;
                        try {
                            zip = new ZipFile(file);
                            doLookupInZipFile(zip, null, recursive);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            IoUtils.closeQuietly(zip);
                        }
                    }
                }
            } else if ("jar".equals(protocol)) {
                ZipFile zip = null;
                try {
                    zip = ((JarURLConnection) url.openConnection()).getJarFile();
                    doLookupInZipFile(zip, pkg, recursive);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IoUtils.closeQuietly(zip);
                }
            } else if ("zip".equals(protocol)) {
                ZipEntryResource resource = new ZipEntryResource(url);
                try {
                    doLookupInZipFile(resource.getZipFile(), pkg, recursive);
                } finally {
                    IoUtils.closeQuietly(resource.getZipFile());
                }
            } else if ("vfs".equals(protocol)) {
                JbossVfsResource resource = new JbossVfsResource(url);
                doLookupInVfsFile(resource, pkg, null, recursive);
            } else {
                throw new IllegalStateException("Unsupported url format: " + url.toString());
            }
        }
    }

    private void doLookupInFileSystem(File dir, String pkg, String relativePathName, boolean recursive) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            String javaName = file.getName();
            if (pkg != null) {
                javaName = pkg + '.' + javaName;
            }
            if (file.isFile()) {
                javaName = StringUtils.removeEnd(javaName, ".class");
            }
            String pathName = '/' + file.getName();
            if (relativePathName != null) {
                pathName = relativePathName + pathName;
            }

            ResourceEntry entry = new ResourceEntry(new FileSystemResource(file), javaName, pathName);
            if (file.isDirectory()) {
                if (visitDirectory(entry)) {
                    if (recursive) {
                        doLookupInFileSystem(file, javaName, pathName, true);
                    }
                }
            } else {
                visitFile(entry);
            }
        }
    }

    private void doLookupInZipFile(ZipFile zip, String pkg, boolean recursive) {
        List<String> skipDirs = null;
        String rootdir = (pkg == null) ? null : pkg.replace('.', '/') + '/';

        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();

            String qualifiedJavaName;
            if (entry.isDirectory()) {
                qualifiedJavaName = entryName.substring(0, entryName.length() - 1).replace('/', '.');
            } else if (entryName.endsWith(".class")) {
                qualifiedJavaName = entryName.substring(0, entryName.length() - 6).replace('/', '.');
            } else {
                qualifiedJavaName = null;
            }

            ResourceEntry resourceEntry = null;
            if (rootdir == null) {
                ZipEntryResource resource = new ZipEntryResource(zip, entry);
                String relativePathName = StringUtils.removeEnd(entryName, "/");
                resourceEntry = new ResourceEntry(resource, qualifiedJavaName, relativePathName);
            } else {
                if (entryName.startsWith(rootdir)) {
                    String relativePathName = entryName.substring(rootdir.length() - 1);
                    if (relativePathName.length() > 1) {
                        relativePathName = StringUtils.removeEnd(relativePathName, "/");
                        if (recursive || relativePathName.lastIndexOf('/') <= 0) {
                            ZipEntryResource resource = new ZipEntryResource(zip, entry);
                            resourceEntry = new ResourceEntry(resource, qualifiedJavaName, relativePathName);
                        }
                    }
                }
            }

            if (resourceEntry != null) {
                if (entry.isDirectory()) {
                    if (!visitDirectory(resourceEntry)) {
                        if (skipDirs == null) {
                            skipDirs = new ArrayList<String>();
                        }
                        skipDirs.add(entryName);
                    }
                } else {
                    if (skipDirs != null) {
                        for (String dir : skipDirs) {
                            if (entryName.startsWith(dir)) {
                                resourceEntry = null;
                                break;
                            }
                        }
                    }
                    if (resourceEntry != null) {
                        visitFile(resourceEntry);
                    }
                }
            }
        }
    }

    private void doLookupInVfsFile(JbossVfsResource resource, String pkg, String relativePathName, boolean recursive) {
        List<JbossVfsResource> children = resource.getChildren();
        for (JbossVfsResource file : children) {
            String javaName = file.getFileName();
            if (pkg != null) {
                javaName = pkg + '.' + javaName;
            }
            if (file.isFile()) {
                javaName = StringUtils.removeEnd(javaName, ".class");
            }
            String pathName = '/' + file.getFileName();
            if (relativePathName != null) {
                pathName = relativePathName + pathName;
            }

            ResourceEntry entry = new ResourceEntry(file, javaName, pathName);
            if (file.isDirectory()) {
                if (visitDirectory(entry)) {
                    if (recursive) {
                        doLookupInVfsFile(file, javaName, pathName, true);
                    }
                }
            } else {
                visitFile(entry);
            }
        }
    }

    //----------------------------------------------------------------
    // following visitXXX methods should be overrided by subclass.
    //
    protected boolean visitDirectory(ResourceEntry dir) {
        return true;
    }

    protected void visitFile(ResourceEntry file) {
    }

    public static final class ResourceEntry extends AbstractResource {
        private final Resource resource;
        private final String qualifiedJavaName;
        private final String relativePathName;

        public ResourceEntry(Resource resource, String qualifiedJavaName, String relativePathName) {
            this.resource = resource;
            this.qualifiedJavaName = qualifiedJavaName;
            this.relativePathName = relativePathName;
        }

        @Override
        public String getPath() {
            return resource.getPath();
        }

        @Override
        public InputStream openStream() throws ResourceNotFoundException {
            return resource.openStream();
        }

        @Override
        public byte[] toByteArray() throws ResourceNotFoundException {
            return resource.toByteArray();
        }

        @Override
        public char[] toCharArray(Charset charset) throws ResourceNotFoundException {
            return resource.toCharArray(charset);
        }

        @Override
        public String toString(Charset charset) throws ResourceNotFoundException {
            return resource.toString(charset);
        }

        @Override
        public File getFile() {
            return resource.getFile();
        }

        @Override
        public URI getURI() {
            return resource.getURI();
        }

        @Override
        public URL getURL() {
            return resource.getURL();
        }

        @Override
        public boolean exist() {
            return resource.exist();
        }

        @Override
        public boolean isDirectory() {
            return resource.isDirectory();
        }

        @Override
        public boolean isFile() {
            return resource.isFile();
        }

        @Override
        public String getFileName() {
            return resource.getFileName();
        }

        @Override
        public long length() {
            return resource.length();
        }

        @Override
        public long lastModified() {
            return resource.lastModified();
        }

        public String getRelativePathName() {
            return relativePathName;
        }

        public String getQualifiedJavaName() {
            return qualifiedJavaName;
        }

        public boolean isJavaClass() {
            return resource.getPath().endsWith(".class");
        }

        @Override
        public String toString() {
            return resource.toString();
        }
    }
}
