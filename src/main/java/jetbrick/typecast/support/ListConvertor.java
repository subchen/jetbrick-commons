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
package jetbrick.typecast.support;

import java.util.*;
import jetbrick.collection.ListUtils;
import jetbrick.typecast.Convertor;
import jetbrick.util.StringUtils;

public final class ListConvertor<T> implements Convertor<List<T>> {
    private final Class<T> elementType;
    private final Convertor<T> elementConvertor;

    public ListConvertor(Class<T> elementType, Convertor<T> elementConvertor) {
        this.elementType = elementType;
        this.elementConvertor = elementConvertor;
    }

    @Override
    public List<T> convert(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        String[] values = StringUtils.split(value.toString(), ',');
        return convertToList(Arrays.asList(values));
    }

    @Override
    public List<T> convert(Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof Iterable) {
            List<?> results = ListUtils.asList((Iterable<?>) value);
            return convertToList(results);
        }
        if (value instanceof Iterator) {
            List<?> results = ListUtils.asList((Iterator<?>) value);
            return convertToList(results);
        }
        if (value instanceof Enumeration) {
            List<?> results = ListUtils.asList((Enumeration<?>) value);
            return convertToList(results);
        }
        if (value.getClass().isArray()) {
            Object[] arrays = ArrayConvertor.toObjectArray(value);
            return convertToList(Arrays.asList(arrays));
        }
        return convert(value.toString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<T> convertToList(List items) {
        int length = items.size();
        if (length == 0) {
            return Collections.emptyList();
        }
        ListIterator<Object> it = items.listIterator();
        while (it.hasNext()) {
            Object value = it.next();
            if (!elementType.isInstance(value)) {
                value = elementConvertor.convert(value);
                it.set(value);
            }
        }
        return items;
    }
}
