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
package jetbrick.io.resource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import jetbrick.util.Validate;

public final class ServletResource extends AbstractResource {
    private final ServletContext sc;
    private final String path;
    private final URL url;

    public ServletResource(ServletContext sc, String path) {
        Validate.notNull(sc);
        Validate.notNull(path);

        this.sc = sc;
        this.path = path;
        setPath(path);

        try {
            this.url = sc.getResource(path);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        if (url == null) {
            throw new ResourceNotFoundException(path);
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File getFile() throws ResourceNotFoundException {
        String file = sc.getRealPath(path);
        if (file != null) {
            return new File(file);
        } else if (url != null) {
            return ResourceUtils.create(url).getFile();
        }
        throw new ResourceNotFoundException(path);
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean exist() {
        return url != null;
    }

    @Override
    public boolean isDirectory() {
        return ResourceUtils.create(url).isDirectory();
    }

    @Override
    public boolean isFile() {
        return ResourceUtils.create(url).isFile();
    }

    @Override
    public long length() {
        return ResourceUtils.create(url).length();
    }

    @Override
    public long lastModified() {
        return ResourceUtils.create(url).lastModified();
    }

    @Override
    public String toString() {
        return "webroot:" + path;
    }
}
