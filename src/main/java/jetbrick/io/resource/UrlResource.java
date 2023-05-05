/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import jetbrick.util.Validate;

public class UrlResource extends AbstractResource {
    protected final URL url;

    public UrlResource(URL url) {
        Validate.notNull(url);

        this.url = url;
        this.relativePathName = url.toString();
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        if (url == null) {
            throw new ResourceNotFoundException();
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
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
        return relativePathName.endsWith("/");
    }

    @Override
    public boolean isFile() {
        return !relativePathName.endsWith("/");
    }

    @Override
    public long length() {
        try {
            return url.openConnection().getContentLengthLong();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public long lastModified() {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
