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
package jetbrick.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public final class ListUtils {

    public static <T> List<T> asList(T[] items) {
        if (items == null) {
            return null;
        }
        return Arrays.asList(items);
    }

    public static <T> List<T> asList(Collection<T> items) {
        if (items == null) {
            return null;
        }
        if (items instanceof List) {
            return (List<T>) items;
        }
        return new ArrayList<T>(items);
    }

    public static <T> List<T> asList(Iterator<T> items) {
        if (items == null) {
            return null;
        }
        List<T> results = new ArrayList<T>();
        while (items.hasNext()) {
            results.add(items.next());
        }
        return results;
    }

    public static <T> List<T> asList(Iterable<T> items) {
        if (items == null) {
            return null;
        }
        if (items instanceof List) {
            return (List<T>) items;
        } else if (items instanceof Collection) {
            return new ArrayList<T>((Collection<T>) items);
        } else {
            return asList(items.iterator());
        }
    }

    public static <T> List<T> asList(Enumeration<T> items) {
        if (items == null) {
            return null;
        }
        List<T> results = new ArrayList<T>();
        while (items.hasMoreElements()) {
            results.add(items.nextElement());
        }
        return results;
    }

    // -----------------------------------------------------------------------

    public static <T> T[] asArray(Collection<T> items, Class<T> elementType) {
        if (items == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] results = (T[]) Array.newInstance(elementType, items.size());
        int i = 0;
        for (T item : items) {
            results[i++] = item;
        }
        return results;
    }

    public static <T> T[] asArray(Iterator<T> items, Class<T> elementType) {
        if (items == null) {
            return null;
        }
        return asArray(asList(items), elementType);
    }

    public static <T> T[] asArray(Iterable<T> items, Class<T> elementType) {
        if (items == null) {
            return null;
        }
        if (items instanceof Collection) {
            return asArray((Collection<T>) items, elementType);
        } else {
            return asArray(asList(items.iterator()), elementType);
        }
    }

    public static <T> T[] asArray(Enumeration<T> items, Class<T> elementType) {
        if (items == null) {
            return null;
        }
        return asArray(asList(items), elementType);
    }
}
