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
import java.net.*;
import jetbrick.io.ResourceNotFoundException;
import jetbrick.util.Validate;

public final class FileSystemResource extends Resource {
    private final File file;

    public static FileSystemResource create(URL url) {
        Validate.notNull(url);
        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        return new FileSystemResource(new File(file));
    }

    public FileSystemResource(File file) {
        this.file = file;
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
    public String getFileName() {
        return file.getName();
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
