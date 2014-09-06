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

import java.io.File;
import java.io.InputStream;
import java.net.*;
import javax.servlet.ServletContext;
import jetbrick.util.Validate;

public class ServletResource extends Resource {
    private final ServletContext sc;
    private final String path;
    private final URL url;

    public ServletResource(ServletContext sc, String path) {
        Validate.notNull(sc);
        Validate.notNull(path);

        this.sc = sc;
        this.path = path;

        try {
            this.url = sc.getResource(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream openStream() {
        return sc.getResourceAsStream(path);
    }

    @Override
    public File getFile() {
        String file = sc.getRealPath(path);
        if (file != null) {
            return new File(file);
        } else if (url != null) {
            return Resource.create(url).getFile();
        }
        return null;
    }

    @Override
    public URI getURI() {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
        return Resource.create(url).isDirectory();
    }

    @Override
    public boolean isFile() {
        return Resource.create(url).isFile();
    }

    @Override
    public String getFileName() {
        int slash = path.lastIndexOf('/');
        if (slash >= 0) {
            return path.substring(slash + 1);
        }
        return path;
    }

    @Override
    public long length() {
        return Resource.create(url).length();
    }

    @Override
    public long lastModified() {
        return Resource.create(url).lastModified();
    }

    @Override
    public String toString() {
        return "servlet:" + path;
    }
}
