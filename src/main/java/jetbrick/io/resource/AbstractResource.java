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
import java.nio.charset.Charset;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipFile;
import jetbrick.io.IoUtils;
import jetbrick.util.*;

public abstract class AbstractResource implements Resource {
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public byte[] toByteArray() throws ResourceNotFoundException {
        return IoUtils.toByteArray(openStream());
    }

    @Override
    public char[] toCharArray(Charset charset) throws ResourceNotFoundException {
        return IoUtils.toCharArray(openStream(), charset);
    }

    @Override
    public String toString(Charset charset) throws ResourceNotFoundException {
        return IoUtils.toString(openStream(), charset);
    }

    @Override
    public File getFile() throws UnsupportedOperationException {
        return PathUtils.urlAsFile(getURL());
    }

    @Override
    public URI getURI() throws UnsupportedOperationException {
        try {
            return getURL().toURI();
        } catch (java.net.URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public URL getURL() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
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
    public boolean exist() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public long length() {
        return -1;
    }

    @Override
    public long lastModified() {
        return 0;
    }
}
