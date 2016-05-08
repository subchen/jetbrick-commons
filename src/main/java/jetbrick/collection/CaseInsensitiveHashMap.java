/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/***
 * 不区分大小写的 HashMap 实现.
 */
public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {
    private static final long serialVersionUID = 1L;

    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public CaseInsensitiveHashMap() {
        this(16, null, false);
    }

    public CaseInsensitiveHashMap(boolean useOriginalKey) {
        this(16, null, useOriginalKey);
    }

    public CaseInsensitiveHashMap(Locale locale) {
        this(16, locale, false);
    }

    public CaseInsensitiveHashMap(Locale locale, boolean useOriginalKey) {
        this(16, locale, useOriginalKey);
    }

    public CaseInsensitiveHashMap(int initialCapacity) {
        this(initialCapacity, null, false);
    }

    public CaseInsensitiveHashMap(int initialCapacity, boolean useOriginalKey) {
        this(initialCapacity, null, useOriginalKey);
    }

    public CaseInsensitiveHashMap(int initialCapacity, Locale locale) {
        this(initialCapacity, locale, false);
    }

    public CaseInsensitiveHashMap(int initialCapacity, Locale locale, boolean useOriginalKey) {
        super(initialCapacity);
        if (useOriginalKey) {
            this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
        } else {
            this.caseInsensitiveKeys = null;
        }
        this.locale = (locale != null ? locale : Locale.getDefault());
    }

    @Override
    public V put(String key, V value) {
        if (caseInsensitiveKeys == null) {
            return super.put(getCaseInsensitiveKey(key), value);
        } else {
            caseInsensitiveKeys.put(getCaseInsensitiveKey(key), key);
            return super.put(key, value);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            key = getCaseInsensitiveKey((String) key);
            if (caseInsensitiveKeys == null) {
                return super.containsKey(key);
            } else {
                return caseInsensitiveKeys.containsKey(key);
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (key instanceof String) {
            key = getCaseInsensitiveKey((String) key);
            if (caseInsensitiveKeys == null) {
                return super.get(key);
            } else {
                return super.get(caseInsensitiveKeys.get(key));
            }
        } else {
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String) {
            key = getCaseInsensitiveKey((String) key);
            if (caseInsensitiveKeys == null) {
                return super.remove(key);
            } else {
                return super.remove(caseInsensitiveKeys.remove(key));
            }
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        if (caseInsensitiveKeys != null) {
            this.caseInsensitiveKeys.clear();
        }
        super.clear();
    }

    /***
     * Convert the given key to a case-insensitive key.
     * <p>The default implementation converts the key
     * to lower-case according to this Map's Locale.
     * @param key the user-specified key
     * @return the key to use for storing
     * @see java.lang.String#toLowerCase(java.util.Locale)
     */
    protected String getCaseInsensitiveKey(String key) {
        return key.toLowerCase(locale);
    }
}
