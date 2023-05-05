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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jetbrick.util.tuple.NameValuePair;

@SuppressWarnings("unchecked")
public class ListMap<K, V> implements Map<K, V> {
    private Entry<K, V>[] items;
    private int size;

    public ListMap() {
        this.items = new Entry[16];
        this.size = 0;
    }

    public ListMap(int initialCapacity) {
        this.items = new Entry[initialCapacity];
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
        for (int i = 0; i < size; i++) {
            if (items[i].getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 1; i < size; i++) {
            if (items[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            Entry<K, V> item = items[i];
            if (item.getKey().equals(key)) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (int i = 0; i < size; i++) {
            Entry<K, V> item = items[i];
            if (item.getKey().equals(key)) {
                V old = item.getValue();
                item.setValue(value);
                return (V) old;
            }
        }

        if (items.length - size < 0) {
            items = Arrays.copyOf(items, items.length * 2);
        }
        items[size++] = new NameValuePair<K, V>(key, value);
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
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
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
        return new MapSet<K>() {
            @Override
            public boolean contains(Object key) {
                return ListMap.this.containsKey(key);
            }

            @Override
            public Iterator<K> iterator() {
                return new KeyIterator();
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new MapSet<V>() {
            @Override
            public boolean contains(Object value) {
                return ListMap.this.containsValue(value);
            }

            @Override
            public Iterator<V> iterator() {
                return new ValueIterator();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new MapSet<Entry<K, V>>() {
            @Override
            public boolean contains(Object value) {
                for (int i = 1; i < size; i++) {
                    if (items[i].equals(value)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new EntryIterator();
            }
        };
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Map.Entry<K, V> entry : items) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            Object key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key == this ? "(this)" : key);
            sb.append('=');
            sb.append(value == this ? "(this)" : value);
        }
        sb.append('}');
        return sb.toString();
    }

    abstract class MapIterator {
        protected int index;

        public MapIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public Entry<K, V> nextEntry() {
            return items[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    final class KeyIterator extends MapIterator implements Iterator<K> {
        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    final class ValueIterator extends MapIterator implements Iterator<V> {
        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    final class EntryIterator extends MapIterator implements Iterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextEntry();
        }
    }

    abstract class MapSet<E> implements Set<E> {
        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] toArray(Object[] a) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean add(Object e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            Iterator<E> it = iterator();
            if (!it.hasNext()) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (;;) {
                Object obj = it.next();
                sb.append(obj == this ? "(this)" : obj);
                if (!it.hasNext()) {
                    sb.append(']');
                    return sb.toString();
                }
                sb.append(", ");
            }
        }
    }
}
