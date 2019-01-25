/**
 * Copyright 2013-2019 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.collection.bidimap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

abstract class AbstractBidiMap<K, V> implements BidiMap<K, V> {
    transient final Map<K, V> normalMap;
    transient final Map<V, K> reverseMap;
    transient BidiMap<V, K> inverseBidiMap;

    protected AbstractBidiMap(final Map<K, V> normalMap, final Map<V, K> reverseMap, final BidiMap<V, K> inverseBidiMap) {
        this.normalMap = normalMap;
        this.reverseMap = reverseMap;
        this.inverseBidiMap = inverseBidiMap;
    }

    protected abstract BidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseMap);

    @Override
    public V get(final Object key) {
        return normalMap.get(key);
    }

    @Override
    public K getKey(final Object value) {
        return reverseMap.get(value);
    }

    @Override
    public int size() {
        return normalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return normalMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return normalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return reverseMap.containsKey(value);
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return normalMap.entrySet();
    }

    @Override
    public Set<K> keySet() {
        return normalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return normalMap.values();
    }

    @Override
    public V put(final K key, final V value) {
        if (normalMap.containsKey(key)) {
            reverseMap.remove(normalMap.get(key));
        }
        if (reverseMap.containsKey(value)) {
            normalMap.remove(reverseMap.get(value));
        }
        V obj = normalMap.put(key, value);
        reverseMap.put(value, key);
        return obj;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(final Object key) {
        V value = null;
        if (normalMap.containsKey(key)) {
            value = normalMap.remove(key);
            reverseMap.remove(value);
        }
        return value;
    }

    @Override
    public K removeValue(final Object value) {
        K key = null;
        if (reverseMap.containsKey(value)) {
            key = reverseMap.remove(value);
            normalMap.remove(key);
        }
        return key;
    }

    @Override
    public void clear() {
        normalMap.clear();
        reverseMap.clear();
    }

    @Override
    public BidiMap<V, K> inverse() {
        if (inverseBidiMap == null) {
            inverseBidiMap = createBidiMap(reverseMap, normalMap, this);
        }
        return inverseBidiMap;
    }

    @Override
    public boolean equals(final Object obj) {
        return normalMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return normalMap.hashCode();
    }

    @Override
    public String toString() {
        return normalMap.toString();
    }
}
