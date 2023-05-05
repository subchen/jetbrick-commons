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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import jetbrick.util.Validate;

public final class FileSystemResource extends AbstractResource {
    private final File file;

    public FileSystemResource(File file) {
        this.file = file;
        this.relativePathName = file.getPath();
    }

    public FileSystemResource(URL url) {
        Validate.notNull(url);

        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        this.file = new File(file);
        this.relativePathName = file;
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(e);
        }
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public URI getURI() {
        return file.toURI();
    }

    @Override
    public URL getURL() {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public boolean exist() {
        return file.exists();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
