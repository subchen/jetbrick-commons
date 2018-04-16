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
package jetbrick.typecast.support;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import jetbrick.io.resource.Resource;
import jetbrick.typecast.Convertor;
import jetbrick.typecast.TypeCastException;
import jetbrick.util.JdkUtils;

public final class URLConvertor implements Convertor<URL> {
    public static final URLConvertor INSTANCE = new URLConvertor();

    @Override
    public URL convert(String value) {
        if (value == null) {
            return null;
        }
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw TypeCastException.create(value, URL.class, e);
        }
    }

    @Override
    public URL convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == URL.class) {
            return (URL) value;
        }
        if (value instanceof File) {
            try {
                return ((File) value).toURI().toURL();
            } catch (MalformedURLException e) {
                throw TypeCastException.create(value, URL.class, e);
            }
        }
        if (value.getClass() == URI.class) {
            try {
                return ((URI) value).toURL();
            } catch (MalformedURLException e) {
                throw TypeCastException.create(value, URL.class, e);
            }
        }
        if (value instanceof Resource) {
            return ((Resource) value).getURL();
        }
        if (JdkUtils.IS_AT_LEAST_JAVA_7) {
            if (value instanceof Path) {
                try {
                    return ((Path) value).toUri().toURL();
                } catch (MalformedURLException e) {
                    throw TypeCastException.create(value, URL.class, e);
                }
            }
        }
        return convert(value.toString());
    }
}
