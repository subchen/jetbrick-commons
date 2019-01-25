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

import java.util.Comparator;
import java.util.SortedMap;

abstract class AbstractBidiSortedMap<K, V> extends AbstractBidiMap<K, V> implements SortedMap<K, V> {
    transient final SortedMap<K, V> normalMap;
    transient final Comparator<? super K> keyComparator;
    transient final Comparator<? super V> valueComparator;

    protected AbstractBidiSortedMap(SortedMap<K, V> normalMap, SortedMap<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
        this.normalMap = normalMap;
        this.keyComparator = normalMap.comparator();
        this.valueComparator = reverseMap.comparator();
    }

    @Override
    public Comparator<? super K> comparator() {
        return keyComparator;
    }

    public Comparator<? super V> valueComparator() {
        return valueComparator;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return normalMap.subMap(fromKey, toKey);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return normalMap.headMap(toKey);

    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return normalMap.tailMap(fromKey);

    }

    @Override
    public K firstKey() {
        return normalMap.firstKey();

    }

    @Override
    public K lastKey() {
        return normalMap.lastKey();
    }
}
