/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.io.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jetbrick.util.ExceptionUtils;
import jetbrick.util.StringUtils;
import jetbrick.util.Validate;

public final class ZipEntryResource extends AbstractResource {
    private final URL url;
    private final ZipFile zip;
    private final ZipEntry entry;
    private final String entryName;

    public ZipEntryResource(URL url) {
        this.url = url;

        String protocol = url.getProtocol();
        if (URL_PROTOCOL_JAR.equals(protocol)) {
            try {
                URLConnection conn = url.openConnection();
                if (conn instanceof JarURLConnection) {
                    JarURLConnection connection = (JarURLConnection) conn;
                    this.zip = connection.getJarFile();
                    this.entry = connection.getJarEntry();
                    this.entryName = entry.getName();
                    this.relativePathName = entryName;
                    return;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (URL_PROTOCOL_ZIP.equals(protocol)) {
            try {
                URLConnection conn = url.openConnection();
                if ("weblogic.utils.zip.ZipURLConnection".equals(conn.getClass().getName())) {
                    this.zip = WeblogicZipURLConnection.getZipFile(conn);
                    this.entry = WeblogicZipURLConnection.getZipEntry(conn);
                    this.entryName = entry.getName();
                    this.relativePathName = entryName;
                    return;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        throw new IllegalStateException("Unknown url format: " + url);
    }

    public ZipEntryResource(ZipFile zip, String entryName) {
        Validate.notNull(zip);

        this.url = null;
        this.zip = zip;
        this.entry = zip.getEntry(entryName);
        this.entryName = entryName;
        this.relativePathName = entryName;
    }

    public ZipEntryResource(ZipFile zip, ZipEntry entry) {
        Validate.notNull(zip);

        this.url = null;
        this.zip = zip;
        this.entry = entry;
        this.entryName = entry.getName();
        this.relativePathName = entryName;
    }

    public ZipFile getZipFile() {
        return zip;
    }

    public ZipEntry getZipEntry() {
        return entry;
    }

    public String getZipEntryName() {
        return entryName;
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        try {
            InputStream is = zip.getInputStream(entry);
            if (is == null) {
                throw new ResourceNotFoundException(entryName);
            }
            return is;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File getFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getURI() {
        try {
            return getURL().toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public URL getURL() {
        if (url != null) {
            return url;
        }

        try {
            String path = URL_PREFIX_FILE + zip.getName() + URL_SEPARATOR_JAR + entryName;
            return new URL(URL_PROTOCOL_JAR, null, path);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean exist() {
        return entry != null;
    }

    @Override
    public boolean isDirectory() {
        return entry == null ? false : entry.isDirectory();
    }

    @Override
    public boolean isFile() {
        return entry == null ? false : !entry.isDirectory();
    }

    @Override
    public String getFileName() {
        String name = StringUtils.removeEnd(entryName, "/");
        int slash = name.lastIndexOf('/');
        if (slash >= 0) {
            return name.substring(slash + 1);
        }
        return name;
    }

    @Override
    public long length() {
        return entry == null ? -1 : entry.getSize();
    }

    @Override
    public long lastModified() {
        return entry == null ? 0 : entry.getTime();
    }

    @Override
    public String toString() {
        if (url != null) {
            return url.toString();
        } else {
            return "jar:" + zip.getName() + URL_SEPARATOR_JAR + entryName;
        }
    }

    static class WeblogicZipURLConnection {
        static final Field FIELD_ZIP_FILE;
        static final Field FIELD_ZIP_ENTRY;

        static {
            ClassLoader loader = WeblogicZipURLConnection.class.getClassLoader();
            try {
                Class<?> klass = loader.loadClass("weblogic.utils.zip.ZipURLConnection");
                FIELD_ZIP_FILE = klass.getDeclaredField("zip");
                FIELD_ZIP_ENTRY = klass.getDeclaredField("ze");

                FIELD_ZIP_FILE.setAccessible(true);
                FIELD_ZIP_ENTRY.setAccessible(true);
            } catch (Exception e) {
                throw new IllegalStateException("Could not detect Weblogic zip url infrastructure", e);
            }
        }

        static ZipFile getZipFile(URLConnection conn) {
            try {
                return (ZipFile) FIELD_ZIP_FILE.get(conn);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        }

        static ZipEntry getZipEntry(URLConnection conn) {
            try {
                return (ZipEntry) FIELD_ZIP_ENTRY.get(conn);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        }
    }
}
