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
import jetbrick.io.ResourceNotFoundException;
import jetbrick.util.*;

public final class ClasspathResource extends Resource {
    private final ClassLoader loader;
    private final String path;

    public ClasspathResource(String path) {
        this(path, null);
    }

    public ClasspathResource(String path, ClassLoader loader) {
        Validate.notNull(path);

        this.loader = (loader != null) ? loader : ClassLoaderUtils.getDefault();
        this.path = StringUtils.removeStart(path, "/");
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        InputStream is = loader.getResourceAsStream(path);
        if (is == null) {
            throw new ResourceNotFoundException(path);
        }
        return is;
    }

    @Override
    public File getFile() {
        return Resource.create(getURL()).getFile();
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
        return loader.getResource(path);
    }

    @Override
    public boolean exist() {
        return getURL() != null;
    }

    @Override
    public boolean isDirectory() {
        return Resource.create(getURL()).isDirectory();
    }

    @Override
    public boolean isFile() {
        return Resource.create(getURL()).isFile();
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
        return Resource.create(getURL()).length();
    }

    @Override
    public long lastModified() {
        return Resource.create(getURL()).lastModified();
    }

    @Override
    public String toString() {
        return URL_PREFIX_CLASSPATH + path;
    }
}
