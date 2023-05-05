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

import java.util.LinkedHashMap;
import java.util.Map;

public class BidiLinkedHashMap<K, V> extends AbstractBidiMap<K, V> {

    public BidiLinkedHashMap() {
        super(new LinkedHashMap<K, V>(), new LinkedHashMap<V, K>(), null);
    }

    public BidiLinkedHashMap(int initialCapacity) {
        super(new LinkedHashMap<K, V>(initialCapacity), new LinkedHashMap<V, K>(initialCapacity), null);
    }

    public BidiLinkedHashMap(Map<? extends K, ? extends V> map) {
        super(new LinkedHashMap<K, V>(), new LinkedHashMap<V, K>(), null);
        putAll(map);
    }

    private BidiLinkedHashMap(Map<K, V> normalMap, Map<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
    }

    @Override
    protected BidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseMap) {
        return new BidiLinkedHashMap<V, K>(normalMap, reverseMap, inverseMap);
    }
}
