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
package jetbrick.config;

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

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(config);
    }

    // -----------------------------------------------------------------

    /**
     * 获取属性值.
     *
     * @param name              键值
     * @param targetClass       目标类(不允许为空)，
     *                          如果 targetClass 为 Object.class，那么自动将 classname 转换为 instance.
     *                          如果 value 是 "$" 开头的引用，则返回引用对象 instance
     * @param defaultValue      默认值
     * @return                  属性值对象
     */
    protected <T> T doGetValue(String name, Class<T> targetClass, String defaultValue) {
        String value = config.get(name);
        return stringAsObject(value, targetClass, defaultValue);
    }

    /**
     * 获取属性值，将逗号分隔的属性值转换为 List 对象.
     *
     * @param name              键值
     * @param elementType       目标类(不允许为空)，
     *                          如果 elementType 为 Object.class，那么自动将 classname 转换为 instance.
     *                          如果 value 是 "$" 开头的引用，则返回引用对象 instance
     * @param defaultValues     默认值
     * @return                  属性值对象 List
     */
    protected <T> List<T> doGetList(String name, Class<T> elementType, String defaultValues) {
        String valueList = config.get(name);

        valueList = StringUtils.trimToNull(valueList);
        if (valueList == null) {
            valueList = defaultValues;
        }
        if (valueList == null || valueList.length() == 0) {
            return Collections.<T> emptyList();
        }

        valueList = resolve(valueList);

        String[] values = StringUtils.split(valueList, ',');
        List<T> results = new ArrayList<T>(values.length);
        for (String value : values) {
            T object = stringAsObject(value, elementType, null);
            if (object != null) {
                results.add(object);
            }
        }
        return Collections.unmodifiableList(results);
    }

    // -----------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private <T> T stringAsObject(String value, Class<T> targetClass, String defaultValue) {
        value = StringUtils.trimToNull(value);
        if (value == null) {
            value = defaultValue;
        }
        if (value == null || value.length() == 0) {
            return null;
        }

        value = resolve(value);

        if (targetClass == String.class) {
            return (T) value;
        }

        if (value.startsWith("$")) {
            // this is a reference name
            return aliasNameAsObject(value, targetClass);
        }

        if (targetClass == Object.class) {
            return aliasNameAsObject(value, targetClass);
        }

        if (TypeCastUtils.support(targetClass)) {
            return TypeCastUtils.convert(value, targetClass);
        }

        if (ClassUtils.available(value)) {
            // this is a class name
            return aliasNameAsObject(value, targetClass);
        }

        throw new IllegalStateException("Cannot convert to " + targetClass + " from `" + value + "`");
    }

    protected <T> T aliasNameAsObject(String aliasName, Class<T> targetClass) {
        // 1. get class name and props
        String className;
        Set<String> propNames;

        if (aliasName.startsWith("$")) {
            // this is a reference name
            className = doGetValue(aliasName, String.class, null);
            propNames = keySet(aliasName.concat("."));
        } else {
            // this is a class name
            className = aliasName;
            propNames = null;
        }

        // 2. load class
        Class<?> cls;

        try {
            cls = ClassLoaderUtils.loadClassEx(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        if (!targetClass.isAssignableFrom(cls)) {
            throw new IllegalStateException("cannot convert `" + className + "` to " + targetClass);
        }

        // 3. create instance
        return newInstance(aliasName, cls, propNames);
    }

    // -----------------------------------------------------------------

    /**
     * 根据类名或者别名，创建对象.
     *
     * @param aliasName     别名
     * @param cls           类名
     * @param propNames     要设置的属性名称
     * @return              创建的对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T newInstance(String aliasName, Class<?> cls, Set<String> propNames) {
        // 1. create instance
        Object obj;

        try {
            obj = objectNewInstance(cls);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        // 2. set properties
        if (propNames != null && propNames.size() > 0) {
            KlassInfo klass = KlassInfo.create(cls);
            for (String propName : propNames) {
                String name = propName.substring(aliasName.length() + 1); // get setter name
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

                    value = doGetList(propName, elementType, null);

                    // convert list to array
                    if (type.isArray()) {
                        value = TypeCastUtils.convertToArray(value, elementType);
                    }
                } else {
                    value = doGetValue(propName, type, null);
                }

                p.set(obj, value);
            }
        }

        // 3. init
        objectInitialize(obj);

        // 4. return
        return (T) obj;
    }

    /**
     * 根据类名，创建对象.
     *
     * @param cls   类名
     * @return      创建的对象
     */
    protected <T> T objectNewInstance(Class<T> cls) throws Exception {
        return cls.newInstance();
    }

    /**
     * 对象创建后，进行初始化.
     *
     * @param object    要初始化的对象
     */
    protected void objectInitialize(Object object) {
    }

    // -----------------------------------------------------------------
    private static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");

    /**
     * 根据 Config 的内容，自动解析 ${...} 的内容.
     *
     * @param value     要解析的内容
     * @return          返回解析后的内容
     */
    public String resolve(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        Matcher matcher = PLACE_HOLDER_PATTERN.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String val = null;
            if (name.startsWith("env:")) {
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

}
