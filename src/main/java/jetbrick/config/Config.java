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
package jetbrick.config;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.PropertyInfo;
import jetbrick.io.resource.Resource;
import jetbrick.typecast.TypeCastUtils;
import jetbrick.util.*;

public final class Config {
    private final Map<String, String> config;

    public Config(Map<String, String> map) {
        config = new HashMap<String, String>(map);
    }

    public Config(Properties props) {
        config = new HashMap<String, String>(props.size());
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            config.put(key, value);
        }
    }

    // -----------------------------------------------------------------
    public Set<String> keySet() {
        return config.keySet();
    }

    public Set<String> keySet(String prefix) {
        Set<String> keys = new LinkedHashSet<String>();
        for (String key : config.keySet()) {
            if (key.startsWith(prefix)) {
                keys.add(key);
            }
        }
        return keys.isEmpty() ? Collections.<String> emptySet() : keys;
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(config);
    }

    // -----------------------------------------------------------------
    public String asString(String name) {
        return getValue(name, String.class, null);
    }

    public String asString(String name, String defaultValue) {
        return getValue(name, String.class, defaultValue);
    }

    public List<String> asStringList(String name) {
        return getValueList(name, String.class);
    }

    public Integer asInteger(String name) {
        return getValue(name, Integer.class, null);
    }

    public Integer asInteger(String name, int defaultValue) {
        return getValue(name, Integer.class, defaultValue);
    }

    public List<Integer> asIntegerList(String name) {
        return getValueList(name, Integer.class);
    }

    public Long asLong(String name) {
        return getValue(name, Long.class, null);
    }

    public Long asLong(String name, long defaultValue) {
        return getValue(name, Long.class, defaultValue);
    }

    public List<Long> asLongList(String name) {
        return getValueList(name, Long.class);
    }

    public Double asDouble(String name) {
        return getValue(name, Double.class, null);
    }

    public Double asDouble(String name, double defaultValue) {
        return getValue(name, Double.class, defaultValue);
    }

    public List<Double> asDoubleList(String name) {
        return getValueList(name, Double.class);
    }

    public Boolean asBoolean(String name) {
        return getValue(name, Boolean.class, null);
    }

    public Boolean asBoolean(String name, boolean defaultValue) {
        return getValue(name, Boolean.class, defaultValue);
    }

    public List<Boolean> asBooleanList(String name) {
        return getValueList(name, Boolean.class);
    }

    public Date asDate(String name) {
        return getValue(name, Date.class, null);
    }

    public Date asDate(String name, Date defaultValue) {
        return getValue(name, Date.class, defaultValue);
    }

    public List<Date> asDateList(String name) {
        return getValueList(name, Date.class);
    }

    public Charset asCharset(String name) {
        return getValue(name, Charset.class, null);
    }

    public Charset asCharset(String name, Charset defaultValue) {
        return getValue(name, Charset.class, defaultValue);
    }

    public List<Charset> asCharsetList(String name) {
        return getValueList(name, Charset.class);
    }

    public File asFile(String name) {
        return getValue(name, File.class, null);
    }

    public File asFile(String name, File defaultValue) {
        return getValue(name, File.class, defaultValue);
    }

    public List<File> asFileList(String name) {
        return getValueList(name, File.class);
    }

    public Resource asResource(String name) {
        return getValue(name, Resource.class, null);
    }

    public Resource asResource(String name, Resource defaultValue) {
        return getValue(name, Resource.class, defaultValue);
    }

    public List<Resource> asResourceList(String name) {
        return getValueList(name, Resource.class);
    }

    public Class<?> asClass(String name) {
        return getValue(name, Class.class, null);
    }

    public Class<?> asClass(String name, Class<?> defaultValue) {
        return getValue(name, Class.class, defaultValue);
    }

    @SuppressWarnings("rawtypes")
    public List<Class> asClassList(String name) {
        return getValueList(name, Class.class);
    }

    public Object asObject(String name) {
        return getValue(name, Object.class, null);
    }

    public List<Object> asObjectList(String name) {
        return getValueList(name, Object.class);
    }

    // -----------------------------------------------------------------
    public <T> T getValue(String name, Class<T> targetClass, T defaultValue) {
        String value = config.get(name);
        if (value == null) {
            return defaultValue;
        }
        return castToObject(value, targetClass);
    }

    public <T> List<T> getValueList(String name, Class<T> elementType) {
        String valueList = config.get(name);
        if (valueList == null || valueList.length() == 0) {
            return Collections.<T> emptyList();
        }

        String[] values = StringUtils.split(valueList, ',');
        List<T> results = new ArrayList<T>(values.length);
        for (String value : values) {
            T object = castToObject(value, elementType);
            results.add(object);
        }
        return Collections.unmodifiableList(results);
    }

    public Object getValueArray(String name, Class<?> elementType) {
        String valueList = config.get(name);
        if (valueList == null || valueList.length() == 0) {
            return Array.newInstance(elementType, 0);
        }

        String[] values = StringUtils.split(valueList, ',');
        Object results = Array.newInstance(elementType, values.length);
        for (int i = 0; i < values.length; i++) {
            Object object = castToObject(values[i], elementType);
            Array.set(results, i, object);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private <T> T castToObject(String value, Class<T> targetClass) {
        value = value.trim();

        if (targetClass != Object.class) {
            // simple cast to target type
            if (value.startsWith("$")) {
                throw new IllegalStateException("Unsupported reference object " + value);
            }

            return TypeCastUtils.convert(value, targetClass);
        } else {
            // create a new object instance
            String className;
            Set<String> propNames;

            if (value.startsWith("$")) {
                // this is a reference object
                className = asString(value);
                propNames = keySet(value.concat("."));
            } else {
                // this is a classname
                className = value;
                propNames = null;
            }

            return (T) newInstance(className, propNames);
        }
    }

    // 根据类名和自定义属性，创建出一个对象
    private Object newInstance(String className, Collection<String> propNames) {
        Object obj;
        Class<?> cls;

        // create instance
        try {
            cls = ClassLoaderUtils.loadClassEx(className);
            obj = cls.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        // set properties
        if (propNames != null && propNames.size() > 0) {
            KlassInfo klass = KlassInfo.create(cls);
            for (String name : propNames) {
                PropertyInfo p = klass.getProperty(name);
                if (p == null || !p.writable()) {
                    throw new IllegalStateException("No set" + IdentifiedNameUtils.capitalize(name) + " found in " + obj.getClass());
                }

                Class<?> type = p.getRawType(klass);
                Object value;
                if (List.class.isAssignableFrom(type)) {
                    type = p.getRawComponentType(cls, 0);
                    value = getValueList(name, type);
                } else if (type.isArray()) {
                    type = type.getComponentType();
                    value = getValueArray(name, type);
                } else {
                    value = getValue(name, type, null);
                }
                p.set(obj, value);
            }
        }

        return obj;
    }

}
