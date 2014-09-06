/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
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

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipFile;
import jetbrick.util.Validate;

public abstract class Resource {
    public static final String URL_PREFIX_CLASSPATH = "classpath:";
    public static final String URL_PREFIX_FILE = "file:";
    public static final String URL_PREFIX_JAR = "jar:";
    public static final String URL_PREFIX_ZIP = "zip:";
    public static final String URL_PROTOCOL_FILE = "file";
    public static final String URL_PROTOCOL_JAR = "jar";
    public static final String URL_PROTOCOL_ZIP = "zip";
    public static final String URL_PROTOCOL_VFS = "vfs";
    public static final String URL_SEPARATOR_JAR = "!/";

    public static final long NOT_FOUND = -1;

    public static Resource create(String location) {
        Validate.notNull(location);

        if (location.startsWith(URL_PREFIX_CLASSPATH)) {
            return new ClasspathResource(location.substring(URL_PREFIX_CLASSPATH.length()));
        }
        if (location.startsWith(URL_PREFIX_FILE)) {
            String file = location.substring(URL_PREFIX_FILE.length());
            return new FileSystemResource(new File(file));
        }
        if (location.startsWith(URL_PREFIX_JAR)) {
            int pos = location.indexOf(URL_SEPARATOR_JAR);
            String file = location.substring(URL_PREFIX_JAR.length(), pos);
            String entry = location.substring(pos + 1);
            try {
                return ZipEntryResource.create(new ZipFile(file), entry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (location.startsWith(URL_PREFIX_ZIP)) {
            int pos = location.indexOf(URL_SEPARATOR_JAR);
            String file = location.substring(URL_PREFIX_ZIP.length(), pos);
            String entry = location.substring(pos + 1);
            try {
                return ZipEntryResource.create(new ZipFile(file), entry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // default is file
        return new FileSystemResource(new File(location));
    }

    public static Resource create(URL url) {
        Validate.notNull(url);

        String protocol = url.getProtocol();
        if (URL_PROTOCOL_FILE.equals(protocol)) {
            return FileSystemResource.create(url);
        } else if (URL_PROTOCOL_JAR.equals(protocol)) {
            return ZipEntryResource.create(url);
        } else if (URL_PROTOCOL_ZIP.equals(protocol)) {
            return ZipEntryResource.create(url);
        } else if (URL_PROTOCOL_VFS.equals(protocol)) {
            return JbossVfsResource.create(url);
        }
        throw new IllegalStateException("Unknown url format: " + url);
    }

    public abstract InputStream openStream() throws RuntimeException;

    public abstract File getFile();

    public abstract URI getURI();

    public abstract URL getURL();

    public abstract boolean exist();

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    // file name without path
    public abstract String getFileName();

    public abstract long length();

    public abstract long lastModified();
}
