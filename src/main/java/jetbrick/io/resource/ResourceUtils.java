/**
 * Copyright 2013-2015 Guoqiang Chen, Shanghai, China. All rights reserved.
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
import java.net.URL;
import java.util.zip.ZipFile;
import jetbrick.util.Validate;

public final class ResourceUtils {

    public static Resource create(String location) {
        Validate.notNull(location);

        if (location.startsWith(Resource.URL_PREFIX_CLASSPATH)) {
            return new ClasspathResource(location.substring(Resource.URL_PREFIX_CLASSPATH.length()));
        }
        if (location.startsWith(Resource.URL_PREFIX_FILE)) {
            String file = location.substring(Resource.URL_PREFIX_FILE.length());
            return new FileSystemResource(new File(file));
        }
        if (location.startsWith(Resource.URL_PREFIX_JAR) || location.startsWith(Resource.URL_PREFIX_ZIP)) {
            int pos = location.indexOf(Resource.URL_SEPARATOR_JAR);
            String file = location.substring(3, pos);
            String entry = location.substring(pos + 1);
            try {
                return new ZipEntryResource(new ZipFile(file), entry);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // default is file
        return new FileSystemResource(new File(location));
    }

    public static Resource create(URL url) {
        Validate.notNull(url);

        String protocol = url.getProtocol();
        if (Resource.URL_PROTOCOL_FILE.equals(protocol)) {
            return new FileSystemResource(url);
        } else if (Resource.URL_PROTOCOL_JAR.equals(protocol) || Resource.URL_PROTOCOL_ZIP.equals(protocol)) {
            return new ZipEntryResource(url);
        } else if (Resource.URL_PROTOCOL_VFS.equals(protocol)) {
            return new JbossVfsResource(url);
        } else {
            return new UrlResource(url);
        }
    }

}
