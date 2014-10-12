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

import java.io.InputStream;
import java.net.URL;
import jetbrick.util.Validate;

public final class InputStreamResource extends AbstractResource {
    private final InputStream is;

    public InputStreamResource(InputStream is) {
        this(is, "(stream)");
    }

    public InputStreamResource(InputStream is, String path) {
        Validate.notNull(is);
        this.is = is;
        setPath(path);
    }

    @Override
    public InputStream openStream() {
        return is;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exist() {
        return is != null;
    }

    @Override
    public String toString() {
        return "(stream):" + is.toString();
    }
}
