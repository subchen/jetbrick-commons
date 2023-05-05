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
package jetbrick.typecast;

import java.util.List;

public final class TypeCastUtils {
    private static final TypeCastResolver resolver = new TypeCastResolver();

    public static <T> void register(Class<T> type, Convertor<T> convertor) {
        resolver.register(type, convertor);
    }

    public static boolean support(Class<?> type) {
        return resolver.lookup(type) != null;
    }

    public static <T> Convertor<T> lookup(Class<T> type) {
        return resolver.lookup(type);
    }

    public static <T> T convert(String value, Class<T> type) {
        return resolver.convert(value, type);
    }

    public static <T> T convert(Object value, Class<T> type) {
        return resolver.convert(value, type);
    }

    public static <T> T convertToArray(String value, Class<?> elementType) {
        return resolver.convertToArray(value, elementType);
    }

    public static <T> T convertToArray(Object value, Class<?> elementType) {
        return resolver.convertToArray(value, elementType);
    }

    public static <T> List<T> convertToList(String value, Class<T> elementType) {
        return resolver.convertToList(value, elementType);
    }

    public static <T> List<T> convertToList(Object value, Class<T> elementType) {
        return resolver.convertToList(value, elementType);
    }
}
