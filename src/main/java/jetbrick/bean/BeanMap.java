/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jetbrick.util.Validate;

/**
 * 将一个普通的 Bean 对象转成 Map 接口访问.
 *
 * @author Guoqiang Chen
 */
public final class BeanMap implements Map<String, Object> {
    private final KlassInfo klass;
    private final Object object;

    public BeanMap(Object object) {
        Validate.notNull(object);
        this.klass = KlassInfo.create(object.getClass());
        this.object = object;
    }

    @Override
    public int size() {
        return klass.getProperties().size();
    }

    @Override
    public boolean isEmpty() {
        return klass.getProperties().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return klass.getProperty((String) key) != null;
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key instanceof String) {
            PropertyInfo prop = klass.getProperty((String) key);
            if (prop != null) {
                return prop.get(object);
            }
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        PropertyInfo prop = klass.getProperty(key);
        if (prop != null) {
            Object old = prop.get(object);
            prop.set(object, value);
            return old;
        }
        return null;
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        for (PropertyInfo prop : klass.getProperties()) {
            set.add(prop.getName());
        }
        return set;
    }

    @Override
    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }
}
