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
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.PropertyInfo;
import jetbrick.typecast.TypeCastUtils;
import jetbrick.util.*;

/**
 * properties 配置文件抽象处理类.
 *
 * @author Guoqiang Chen
 */
public abstract class AbstractConfig {
    protected final Map<String, String> config;

    public AbstractConfig(Map<String, String> map) {
        config = new HashMap<String, String>(map);
    }

    public AbstractConfig(Properties props) {
        config = new HashMap<String, String>(props.size());
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            config.put(key, value);
        }
    }

    // -----------------------------------------------------------------
    public boolean isEmpty() {
        return config.isEmpty();
    }
    
    public int size() {
        return config.size();
    }

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
    protected <T> T doGetValue(String name, Class<T> targetClass, String defaultValue) {
        String value = config.get(name);
        return stringAsObject(value, targetClass, defaultValue);
    }

    protected <T> List<T> doGetValueList(String name, Class<T> elementType) {
        String valueList = config.get(name);
        if (valueList == null || valueList.length() == 0) {
            return Collections.<T> emptyList();
        }

        String[] values = StringUtils.split(valueList, ',');
        List<T> results = new ArrayList<T>(values.length);
        for (String value : values) {
            T object = stringAsObject(value, elementType, null);
            if (object != null) {
                results.add(object);
            }
        }
        return results;
    }
    
    protected <T> T doGetObject(String name, ObjectBuilder builder) {
        String value = config.get(name);
        return newInstance(value, builder);
    }

    protected <T> List<T> doGetObjectList(String name, ObjectBuilder builder) {
        String valueList = config.get(name);
        if (valueList == null || valueList.length() == 0) {
            return Collections.<T> emptyList();
        }

        String[] values = StringUtils.split(valueList, ',');
        List<T> results = new ArrayList<T>(values.length);
        for (String value : values) {
            T object = newInstance(value, builder);
            if (object != null) {
                results.add(object);
            }
        }
        return results;
    }

    // -----------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private <T> T stringAsObject(String value, Class<T> targetClass, String defaultValue) {
        value = StringUtils.trimToNull(value);

        if (value == null) {
            value = defaultValue;
        }
        
        if (value == null) {
            value = null;
        }

        value = resolve(value);

        if (targetClass == String.class) {
            return (T) value;
        }

        return TypeCastUtils.convert(value, targetClass);
    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(String aliasName, ObjectBuilder builder) {
        aliasName = StringUtils.trimToNull(aliasName);
        if (aliasName == null) {
            return null;
        }

        // 1. get class name and props
        String className;
        Set<String> propNames;

        if (aliasName.startsWith("$")) {
            // this is a reference object
            className = doGetValue(aliasName, String.class, null);
            propNames = keySet(aliasName.concat("."));
        } else {
            // this is a classname
            className = aliasName;
            propNames = null;
        }

        // 2. create instance
        Object obj;
        Class<?> cls;

        try {
            cls = ClassLoaderUtils.loadClassEx(className);
            obj = builder.newInstance(cls);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        // 3. set properties
        if (propNames != null && propNames.size() > 0) {
            KlassInfo klass = KlassInfo.create(cls);
            for (String name : propNames) {
                name = name.substring(aliasName.length() + 1); // get setter name
                PropertyInfo p = klass.getProperty(name);
                if (p == null || !p.writable()) {
                    throw new IllegalStateException("No set" + IdentifiedNameUtils.capitalize(name) + " found in " + cls);
                }

                Class<?> type = p.getRawType(klass);
                Object value;
                if (type == List.class || type == Collection.class || type.isArray()) {
                    Class<?> elementType;
                    if (type.isArray()) {
                        elementType = type.getComponentType();
                    } else {
                        elementType = p.getRawComponentType(cls, 0);
                    }

                    if (TypeCastUtils.support(elementType)) {
                        value = doGetValueList(name, elementType);
                    } else {
                        value = doGetObjectList(name, builder);
                    }

                    // list to array
                    if (type.isArray()) {
                        value = TypeCastUtils.convertToArray(value, elementType);
                    }
                } else {
                    value = doGetValue(name, type, null);
                }

                p.set(obj, value);
            }
        }

        // 4. init
        builder.initialize(obj);
        
        // 5. return
        return (T) obj;
    }

    // -----------------------------------------------------------------
    private static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");

    public String resolve(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        Matcher matcher = PLACE_HOLDER_PATTERN.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String val = null;
            if (name.startsWith("ENV.")) {
                name = name.substring(4);
                val = System.getenv(name);
            } else {
                val = doGetValue(name, String.class, null);
                if (val == null) {
                    val = System.getProperty(name);
                }
            }
            if (val == null) {
                throw new IllegalStateException("cannot find variable `" + value + "` in environment variables");
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    // -----------------------------------------------------------------
    static interface ObjectBuilder {
        
        public <T> T newInstance(Class<T> cls) throws Exception;
        
        public void initialize(Object object);

    }

}
