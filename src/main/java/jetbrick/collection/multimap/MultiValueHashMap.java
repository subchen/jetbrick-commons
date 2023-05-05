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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiValueHashMap<K, V> extends AbstractMultiValueMap<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    public MultiValueHashMap() {
        super(new HashMap<K, List<V>>());
    }

    public MultiValueHashMap(int initialCapacity) {
        super(new HashMap<K, List<V>>(initialCapacity));
    }

    public MultiValueHashMap(Map<K, V> otherMap) {
        super(new HashMap<K, List<V>>(otherMap.size()));
        putAll(otherMap);
    }
}
