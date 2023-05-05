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
package jetbrick.collection.bidimap;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BidiTreeMap<K, V> extends AbstractBidiSortedMap<K, V> {

    public BidiTreeMap() {
        super(new TreeMap<K, V>(), new TreeMap<V, K>(), null);
    }

    public BidiTreeMap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
        super(new TreeMap<K, V>(keyComparator), new TreeMap<V, K>(valueComparator), null);
    }

    public BidiTreeMap(Map<? extends K, ? extends V> map) {
        super(new TreeMap<K, V>(), new TreeMap<V, K>(), null);
        putAll(map);
    }

    private BidiTreeMap(SortedMap<K, V> normalMap, SortedMap<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
    }

    @Override
    protected BidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseMap) {
        return new BidiTreeMap<V, K>((SortedMap<V, K>) normalMap, (SortedMap<K, V>) reverseMap, inverseMap);
    }
}
