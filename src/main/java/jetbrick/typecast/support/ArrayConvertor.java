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

import java.lang.reflect.Array;
import java.util.*;
import jetbrick.collection.ListUtils;
import jetbrick.typecast.Convertor;
import jetbrick.util.ArrayUtils;
import jetbrick.util.StringUtils;

@SuppressWarnings("unchecked")
public final class ArrayConvertor<T> implements Convertor<T[]> {
    private final Class<T> elementType;
    private final Convertor<T> elementConvertor;

    public ArrayConvertor(Class<T> elementType, Convertor<T> elementConvertor) {
        this.elementType = elementType;
        this.elementConvertor = elementConvertor;
    }

    @Override
    public T[] convert(String value) {
        if (value == null) {
            return null;
        }
        String[] values = StringUtils.split(value.toString(), ',');
        if (elementType == String.class) {
            return (T[]) values;
        } else {
            return convertToArray(values);
        }
    }

    @Override
    public T[] convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            Object[] arrays = toObjectArray(value);
            if (elementType.isAssignableFrom(arrays.getClass().getComponentType())) {
                return (T[]) arrays;
            }
            return convertToArray(arrays);
        }

        if (value instanceof Iterable) {
            List<?> results = ListUtils.asList((Iterable<?>) value);
            return convertToArray(results);
        }
        if (value instanceof Iterator) {
            List<?> results = ListUtils.asList((Iterator<?>) value);
            return convertToArray(results);
        }
        if (value instanceof Enumeration) {
            List<?> results = ListUtils.asList((Enumeration<?>) value);
            return convertToArray(results);
        }

        return convert(value.toString());
    }

    private T[] convertToArray(Object[] items) {
        T[] results = (T[]) Array.newInstance(elementType, items.length);

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (elementType.isInstance(value)) {
                results[i] = (T) value;
            } else {
                results[i] = elementConvertor.convert(value);
            }
        }
        return results;
    }

    @SuppressWarnings({ "rawtypes" })
    private T[] convertToArray(List items) {
        T[] results = (T[]) Array.newInstance(elementType, items.size());

        int i = 0;
        for (Object item : items) {
            if (elementType.isInstance(item)) {
                results[i++] = (T) item;
            } else {
                results[i++] = elementConvertor.convert(item);
            }
        }
        return results;
    }

    protected static Object[] toObjectArray(Object value) {
        Class<?> componentType = value.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            if (componentType == Integer.TYPE) {
                return ArrayUtils.toObject((int[]) value);
            } else if (componentType == Long.TYPE) {
                return ArrayUtils.toObject((long[]) value);
            } else if (componentType == Float.TYPE) {
                return ArrayUtils.toObject((float[]) value);
            } else if (componentType == Double.TYPE) {
                return ArrayUtils.toObject((double[]) value);
            } else if (componentType == Boolean.TYPE) {
                return ArrayUtils.toObject((boolean[]) value);
            } else if (componentType == Byte.TYPE) {
                return ArrayUtils.toObject((byte[]) value);
            } else if (componentType == Short.TYPE) {
                return ArrayUtils.toObject((short[]) value);
            } else if (componentType == Character.TYPE) {
                return ArrayUtils.toObject((char[]) value);
            }
            throw new IllegalStateException("Unreachable code");
        } else {
            return (Object[]) value;
        }
    }
}
