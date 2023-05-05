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
package jetbrick.web.servlet.map;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class StringEnumeratedMap<V> implements Map<String, V> {
    private volatile Map<String, V> map;

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return getAsMap().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return getAttribute(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return getAsMap().isEmpty();
    }

    @Override
    public int size() {
        return getAsMap().size();
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        return getAsMap().entrySet();
    }

    @Override
    public Set<String> keySet() {
        return getAsMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return getAsMap().values();
    }

    @Override
    public synchronized V put(String key, V value) {
        map = null;
        V previous = get(key);
        setAttribute(key, value);
        return previous;
    }

    @Override
    public synchronized void putAll(Map<? extends String, ? extends V> m) {
        map = null;
        for (Map.Entry<? extends String, ? extends V> e : m.entrySet()) {
            setAttribute(e.getKey(), e.getValue());
        }
    }

    @Override
    public synchronized V remove(Object key) {
        map = null;
        V value = get(key);
        removeAttribute(key.toString());
        return value;
    }

    @Override
    public synchronized void clear() {
        map = null;
        Enumeration<String> keys = getAttributeNames();
        while (keys.hasMoreElements()) {
            removeAttribute(keys.nextElement());
        }
    }

    protected abstract Enumeration<String> getAttributeNames();

    protected abstract V getAttribute(String name);

    protected abstract void setAttribute(String name, V value);

    protected abstract void removeAttribute(String name);

    // double check for map
    protected Map<String, V> getAsMap() {
        Map<String, V> result = map;
        if (result == null) {
            synchronized (this) {
                result = map;
                if (result == null) {
                    map = (result = initialize());
                }
            }
        }
        return result;
    }

    private Map<String, V> initialize() {
        Map<String, V> map = new HashMap<String, V>();
        for (Enumeration<String> e = getAttributeNames(); e.hasMoreElements();) {
            String key = e.nextElement();
            V value = getAttribute(key);
            map.put(key, value);
        }
        return map;
    }
}
