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
package jetbrick.collection.multimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<K, List<V>> map;

    protected AbstractMultiValueMap(Map<K, List<V>> map) {
        this.map = map;
    }

    @Override
    public int size() {
        int total = 0;
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            total += entry.getValue().size();
        }
        return total;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            if (entry.getValue().contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        List<V> values = map.get(key);
        return values != null ? values.get(0) : null;
    }

    @Override
    public V put(K key, V value) {
        List<V> values = map.get(key);
        if (values == null) {
            values = new ArrayList<V>();
            map.put(key, values);
        }
        values.add(value);
        return null;
    }

    @Override
    public V remove(Object key) {
        map.remove(key);
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
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<V>(map.size() * 3);
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            values.addAll(entry.getValue());
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrys = new HashSet<Entry<K, V>>(map.size() * 3);
        for (final Map.Entry<K, List<V>> entry : map.entrySet()) {
            final K key = entry.getKey();
            for (final V value : entry.getValue()) {
                entrys.add(new Map.Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return key;
                    }

                    @Override
                    public V getValue() {
                        return value;
                    }

                    @Override
                    public V setValue(V value) {
                        throw new UnsupportedOperationException();
                    }
                });
            }
        }
        return entrys;
    }

    @Override
    public boolean containsValue(Object key, Object value) {
        List<V> values = map.get(key);
        return values == null ? false : values.contains(value);
    }

    @Override
    public Collection<List<V>> valuesList() {
        return map.values();
    }

    @Override
    public List<V> getList(Object key) {
        return map.get(key);
    }

    @Override
    public Set<Entry<K, List<V>>> multiEntrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
