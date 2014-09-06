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
package jetbrick.collection;

import java.io.Serializable;
import java.util.*;

public class ListOrderedMap<K, V> extends AbstractMap<K, V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private final Map<K, LinkedEntry<K, V>> map;
    private LinkedEntry<K, V> header;

    public ListOrderedMap() {
        map = new HashMap<K, LinkedEntry<K, V>>();
        createHeader();
    }

    public ListOrderedMap(int initialCapacity) {
        map = new HashMap<K, LinkedEntry<K, V>>(initialCapacity);
        createHeader();
    }

    public ListOrderedMap(int initialCapacity, float loadFactor) {
        map = new HashMap<K, LinkedEntry<K, V>>(initialCapacity, loadFactor);
        createHeader();
    }

    public ListOrderedMap(Map<? extends K, ? extends V> m) {
        this();
        putAll(m);
    }

    private void createHeader() {
        header = new LinkedEntry<K, V>();
        header.prev = header.next = header;
    }

    // OrderedMap interface
    public Entry<K, V> getEntry(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        LinkedEntry<K, V> entry = header.next;
        for (int i = 0; i < index; i++) {
            entry = entry.next;
        }
        return entry;
    }

    public K getKey(int index) {
        return getEntry(index).getKey();
    }

    public V getValue(int index) {
        return getEntry(index).getValue();
    }

    public Entry<K, V> put(int index, K key, V value) {
        if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
        LinkedEntry<K, V> old = header.next;
        boolean before = false;
        for (int i = 0; i < index; i++) {
            old = old.next;
            if (key == old.key || (key != null && key.equals(old.key))) before = true;
        }
        if (before) old = old.next;
        LinkedEntry<K, V> entry = map.get(key);
        if (entry != null) {
            if (index == size()) throw new IndexOutOfBoundsException();
            if (entry != old) entry.moveBefore(old);
            entry.value = value;
        } else {
            entry = new LinkedEntry<K, V>(key, value);
            entry.insertBefore(old);
            map.put(key, entry);
        }
        return old;
    }

    public void putAll(int index, Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet())
            put(index++, entry.getKey(), entry.getValue());
    }

    public Entry<K, V> remove(int index) {
        Entry<K, V> entry = getEntry(index);
        remove(entry.getKey());
        return entry;
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return new EntriesIterator();
    }

    public Entry<K, V>[] toArray() {
        int size = size();
        @SuppressWarnings("unchecked")
        Entry<K, V>[] entries = new LinkedEntry[size];
        LinkedEntry<K, V> entry = header.next;
        for (int i = 0; i < size; i++) {
            entries[i] = entry;
            entry = entry.next;
        }
        return entries;
    }

    // Map interface
    @Override
    public V get(Object key) {
        LinkedEntry<K, V> entry = map.get(key);
        return entry != null ? entry.value : null;
    }

    @Override
    public V put(K key, V value) {
        LinkedEntry<K, V> entry = map.get(key);
        if (entry != null) {
            V old = entry.value;
            entry.value = value;
            return old;
        } else {
            entry = new LinkedEntry<K, V>(key, value);
            entry.insertBefore(header);
            map.put(key, entry);
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        LinkedEntry<K, V> entry = map.get(key);
        if (entry != null) {
            entry.remove();
            map.remove(key);
            return entry.value;
        } else
            return null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null) {
            for (V v : values())
                if (value.equals(v)) return true;
        } else {
            for (V v : values())
                if (v == null) return true;
        }
        return false;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
        createHeader();
    }

    private transient Set<Entry<K, V>> entries;

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entries == null) entries = new AbstractSet<Entry<K, V>>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new EntriesIterator();
            }
        };
        return entries;
    }

    private transient Set<K> keys;

    @Override
    public Set<K> keySet() {
        if (keys == null) keys = new AbstractSet<K>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public Iterator<K> iterator() {
                return new KeysIterator();
            }
        };
        return keys;
    }

    private transient Collection<V> values;

    @Override
    public Collection<V> values() {
        if (values == null) values = new AbstractCollection<V>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public Iterator<V> iterator() {
                return new ValuesIterator();
            }
        };
        return values;
    }

    private transient List<Entry<K, V>> entryList;

    public List<Entry<K, V>> entryList() {
        if (entryList == null) entryList = new AbstractList<Entry<K, V>>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public Entry<K, V> get(int index) {
                return getEntry(index);
            }
        };
        return entryList;
    }

    private transient List<K> keyList;

    public List<K> keyList() {
        if (keyList == null) keyList = new AbstractList<K>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public K get(int index) {
                return getKey(index);
            }
        };
        return keyList;
    }

    private transient List<V> valueList;

    public List<V> valueList() {
        if (valueList == null) valueList = new AbstractList<V>() {

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public V get(int index) {
                return getValue(index);
            }

            @Override
            public V set(int index, V element) {
                return getEntry(index).setValue(element);
            }
        };
        return valueList;
    }

    // Creates swallow clone. Keys and values are not cloned.
    @Override
    public ListOrderedMap<K, V> clone() {
        return new ListOrderedMap<K, V>(this);
    }

    static final class LinkedEntry<K, V> extends MapEntry<K, V> {
        private static final long serialVersionUID = 1L;
        private LinkedEntry<K, V> prev;
        private LinkedEntry<K, V> next;

        // Needed for serialization
        public LinkedEntry() {
            super(null, null);
        }

        public LinkedEntry(K key, V value) {
            super(key, value);
        }

        public void remove() {
            prev.next = next;
            next.prev = prev;
        }

        public void insertBefore(LinkedEntry<K, V> entry) {
            next = entry;
            prev = entry.prev;
            entry.prev = this;
            prev.next = this;
        }

        public void moveBefore(LinkedEntry<K, V> entry) {
            remove();
            insertBefore(entry);
        }

        @Override
        public String toString() {
            return value != null ? value.toString() : "null";
        }
    }

    abstract class OrderedMapIterator<E> implements Iterator<E> {
        private LinkedEntry<K, V> curr, prev;

        public OrderedMapIterator() {
            super();
            curr = header.next;
        }

        @Override
        public boolean hasNext() {
            return curr != header;
        }

        @Override
        public void remove() {
            if (prev == null) throw new IllegalStateException();
            map.remove(prev.key);
            prev.remove();
            prev = null;
        }

        protected LinkedEntry<K, V> nextEntry() {
            if (curr == header) throw new NoSuchElementException();
            prev = curr;
            curr = curr.next;
            return prev;
        }
    }

    final class EntriesIterator extends OrderedMapIterator<Map.Entry<K, V>> {
        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    final class KeysIterator extends OrderedMapIterator<K> {
        @Override
        public K next() {
            return nextEntry().key;
        }
    }

    final class ValuesIterator extends OrderedMapIterator<V> {
        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    static class MapEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = 1L;
        protected K key;
        protected V value;

        /**
         * Creates new <tt>MapEntry</tt> instance with specified key and value.
         * @param key key.
         * @param value value.
         */
        public MapEntry(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

        /**
         * @return entry key.
         */

        @Override
        public K getKey() {
            return key;
        }

        /**
         * @return entry value.
         */

        @Override
        public V getValue() {
            return value;
        }

        /**
         * Sets new value for this entry. Underlying <tt>Map</tt> should reflect the changes to this entry.
         * @param value new value.
         * @return old value.
         */

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Map.Entry)) return false;
            @SuppressWarnings("unchecked")
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            K k = entry.getKey();
            if (k == key || (k != null && k.equals(key))) {
                V v = entry.getValue();
                return v == value || (v != null && v.equals(value));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
