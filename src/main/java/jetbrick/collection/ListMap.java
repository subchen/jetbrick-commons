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
package jetbrick.collection;

import java.util.*;

@SuppressWarnings("unchecked")
public class ListMap<K, V> implements Map<K, V> {
    private Object[] items;
    private int size;

    public ListMap() {
        this.items = new Object[32];
        this.size = 0;
    }

    public ListMap(int initialCapacity) {
        this.items = new Object[initialCapacity * 2];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i += 2) {
            if (items[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 1; i < size; i += 2) {
            if (items[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i += 2) {
            if (items[i].equals(key)) {
                return (V) items[i + 1];
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (int i = 0; i < size; i += 2) {
            if (items[i].equals(key)) {
                Object old = items[i + 1];
                items[i] = key;
                items[i + 1] = value;
                return (V) old;
            }
        }

        if (items.length - size < 2) {
            items = Arrays.copyOf(items, items.length + 16);
        }
        items[size++] = key;
        items[size++] = value;
        return null;
    }

    @Override
    public V remove(Object key) {
        for (int i = 0; i < size; i += 2) {
            if (items[i].equals(key)) {
                Object old = items[i + 1];
                size -= 2;
                for (int n = i; n < size; n++) {
                    items[n] = items[n + 2];
                }
                return (V) old;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>(size);
        for (int i = 0; i < size; i += 2) {
            keys.add((K) items[i]);
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<V>(size);
        for (int i = 1; i < size; i += 2) {
            values.add((V) items[i]);
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
