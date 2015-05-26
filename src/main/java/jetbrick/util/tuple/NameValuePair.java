/**
 * Copyright 2013-2015 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.util.tuple;

import java.io.Serializable;
import java.util.Map;

public final class NameValuePair<K, V> implements Serializable, Map.Entry<K, V> {
    private static final long serialVersionUID = 1L;
    public final K name;
    public V value;

    public static <K, V> NameValuePair<K, V> create(K name, V value) {
        return new NameValuePair<K, V>(name, value);
    }

    public NameValuePair(K name, V value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public K getKey() {
        return name;
    }

    public K getName() {
        return name;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        NameValuePair<?, ?> pair = (NameValuePair<?, ?>) o;

        if (name != null ? !name.equals(pair.name) : pair.name != null) {
            return false;
        }
        if (value != null ? !value.equals(pair.value) : pair.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{name=" + name + ", value=" + value + '}';
    }
}
