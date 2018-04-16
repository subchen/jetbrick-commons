/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import jetbrick.util.PathUtils;
import jetbrick.util.Validate;

public final class ServletResource extends AbstractResource {
    private final String path;
    private final File file;
    private final URL url;

    public ServletResource(ServletContext sc, String path) {
        Validate.notNull(sc);
        Validate.notNull(path);

        this.path = path;
        this.relativePathName = path;

        try {
            String realPath = sc.getRealPath(path);
            if (realPath != null) {
                this.file = new File(realPath);
                this.url = null;
            } else {
                this.file = null;
                this.url = sc.getResource(path);
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        if (file == null && url == null) {
            throw new ResourceNotFoundException(path);
        }
        try {
            if (file != null) {
                return new FileInputStream(file);
            } else {
                return url.openStream();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File getFile() throws ResourceNotFoundException {
        if (file != null) {
            return file;
        } else if (url != null) {
            return PathUtils.urlAsFile(url);
        }
        throw new ResourceNotFoundException(path);
    }

    @Override
    public URL getURL() {
        if (url != null) {
            return url;
        }
        if (file != null) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    @Override
    public boolean exist() {
        return (file != null && file.exists()) || url != null;
    }

    @Override
    public boolean isDirectory() {
        return path.endsWith("/");
    }

    @Override
    public boolean isFile() {
        return !path.endsWith("/");
    }

    @Override
    public long length() {
        if (file != null) {
            return file.length();
        }
        if (url != null) {
            try {
                return url.openConnection().getContentLengthLong();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return -1;
    }

    @Override
    public long lastModified() {
        if (file != null) {
            return file.lastModified();
        }
        if (url != null) {
            try {
                return url.openConnection().getLastModified();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "webroot:" + path;
    }
}
