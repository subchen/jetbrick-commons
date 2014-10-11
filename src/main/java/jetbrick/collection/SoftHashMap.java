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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

public class SoftHashMap<K, V> extends AbstractMap<K, V> {
    private final Map<K, SoftValue<V>> map;
    private final ReferenceQueue<V> queue;

    public SoftHashMap() {
        this.map = new HashMap<K, SoftValue<V>>();
        this.queue = new ReferenceQueue<V>();
    }

    public SoftHashMap(int initialCapacity) {
        this.map = new HashMap<K, SoftValue<V>>(initialCapacity);
        this.queue = new ReferenceQueue<V>();
    }

    @SuppressWarnings("unchecked")
    private void processQueue() {
        SoftValue<V> o;
        while ((o = (SoftValue<V>) queue.poll()) != null) {
            map.remove(o.key);
        }
    }

    @Override
    public V get(Object key) {
        processQueue();
        SoftValue<V> ref = map.get(key);
        return (ref != null) ? ref.get() : null;
    }

    @Override
    public V put(K key, V softValue) {
        processQueue();
        SoftValue<V> ref = map.put(key, new SoftValue<V>(softValue, key, queue));
        return (ref != null) ? ref.get() : null;
    }

    @Override
    public V remove(Object key) {
        processQueue();
        SoftValue<V> ref = map.remove(key);
        return (ref != null) ? ref.get() : null;
    }

    @Override
    public void clear() {
        processQueue();
        map.clear();
    }

    @Override
    public int size() {
        processQueue();
        return map.size();
    }

    /**
     * Returns a copy of the key/values in the map at the point of
     * calling.  However, setValue still sets the value in the
     * actual SoftHashMap.
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        processQueue();
        Set<Entry<K, V>> result = new LinkedHashSet<Entry<K, V>>();
        for (final Entry<K, SoftValue<V>> entry : map.entrySet()) {
            final V value = entry.getValue().get();
            if (value != null) {
                result.add(new Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public V getValue() {
                        return value;
                    }

                    @Override
                    public V setValue(V v) {
                        entry.setValue(new SoftValue<V>(v, entry.getKey(), queue));
                        return value;
                    }
                });
            }
        }
        return result;
    }

    /**
     * A soft reference that has a hard reference to the key.
     */
    private static class SoftValue<T> extends SoftReference<T> {
        private final Object key;

        private SoftValue(T ref, Object key, ReferenceQueue<T> queue) {
            super(ref, queue);
            this.key = key;
        }
    }
}
