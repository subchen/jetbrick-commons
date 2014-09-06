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
package jetbrick.typecast.support;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.file.Path;
import jetbrick.io.resource.Resource;
import jetbrick.typecast.Convertor;
import jetbrick.typecast.TypeCastException;
import jetbrick.util.JdkVersion;

public final class FileConvertor implements Convertor<File> {
    public static final FileConvertor INSTANCE = new FileConvertor();

    @Override
    public File convert(String value) {
        if (value == null) {
            return null;
        }
        return new File(value);
    }

    @Override
    public File convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof File) {
            return (File) value;
        }
        if (value.getClass() == URL.class) {
            try {
                String s = URLDecoder.decode(((URL) value).getPath(), "utf-8");
                return new File(s);
            } catch (UnsupportedEncodingException e) {
                throw TypeCastException.create(value, URI.class, e);
            }
        }
        if (value.getClass() == URI.class) {
            try {
                String s = URLDecoder.decode(((URI) value).getPath(), "utf-8");
                return new File(s);
            } catch (UnsupportedEncodingException e) {
                throw TypeCastException.create(value, URI.class, e);
            }
        }
        if (value instanceof Resource) {
            return ((Resource) value).getFile();
        }
        if (JdkVersion.IS_AT_LEAST_JAVA_7) {
            if (value instanceof Path) {
                return ((Path) value).toFile();
            }
        }
        return convert(value.toString());
    }

    public static void main(String[] args) {
        File f = new FileConvertor().convert(new Object());
        System.out.println(f.getClass().getName() + ": " + f);
    }
}
