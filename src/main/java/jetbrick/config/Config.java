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
package jetbrick.config;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jetbrick.io.resource.Resource;

/**
 * properties 配置文件高级处理类.
 *
 * @author Guoqiang Chen
 */
public final class Config extends AbstractConfig {

    public Config(Map<String, String> map) {
        super(map);
    }

    public Config(Properties props) {
        super(props);
    }

    // -----------------------------------------------------------------
    public String asString(String name) {
        return doGetValue(name, String.class, null);
    }

    public String asString(String name, String defaultValue) {
        return doGetValue(name, String.class, defaultValue);
    }

    public List<String> asStringList(String name) {
        return doGetList(name, String.class, null);
    }

    public List<String> asStringList(String name, String defaultValue) {
        return doGetList(name, String.class, defaultValue);
    }

    public Integer asInteger(String name) {
        return doGetValue(name, Integer.class, null);
    }

    public Integer asInteger(String name, String defaultValue) {
        return doGetValue(name, Integer.class, defaultValue);
    }

    public List<Integer> asIntegerList(String name) {
        return doGetList(name, Integer.class, null);
    }

    public List<Integer> asIntegerList(String name, String defaultValue) {
        return doGetList(name, Integer.class, defaultValue);
    }

    public Long asLong(String name) {
        return doGetValue(name, Long.class, null);
    }

    public Long asLong(String name, String defaultValue) {
        return doGetValue(name, Long.class, defaultValue);
    }

    public List<Long> asLongList(String name) {
        return doGetList(name, Long.class, null);
    }

    public List<Long> asLongList(String name, String defaultValue) {
        return doGetList(name, Long.class, defaultValue);
    }

    public Double asDouble(String name) {
        return doGetValue(name, Double.class, null);
    }

    public Double asDouble(String name, String defaultValue) {
        return doGetValue(name, Double.class, defaultValue);
    }

    public List<Double> asDoubleList(String name) {
        return doGetList(name, Double.class, null);
    }

    public List<Double> asDoubleList(String name, String defaultValue) {
        return doGetList(name, Double.class, defaultValue);
    }

    public Boolean asBoolean(String name) {
        return doGetValue(name, Boolean.class, null);
    }

    public Boolean asBoolean(String name, String defaultValue) {
        return doGetValue(name, Boolean.class, defaultValue);
    }

    public List<Boolean> asBooleanList(String name) {
        return doGetList(name, Boolean.class, null);
    }

    public List<Boolean> asBooleanList(String name, String defaultValue) {
        return doGetList(name, Boolean.class, defaultValue);
    }

    public Date asDate(String name) {
        return doGetValue(name, Date.class, null);
    }

    public Date asDate(String name, String defaultValue) {
        return doGetValue(name, Date.class, defaultValue);
    }

    public List<Date> asDateList(String name) {
        return doGetList(name, Date.class, null);
    }

    public List<Date> asDateList(String name, String defaultValue) {
        return doGetList(name, Date.class, defaultValue);
    }

    public Charset asCharset(String name) {
        return doGetValue(name, Charset.class, null);
    }

    public Charset asCharset(String name, String defaultValue) {
        return doGetValue(name, Charset.class, defaultValue);
    }

    public List<Charset> asCharsetList(String name) {
        return doGetList(name, Charset.class, null);
    }

    public List<Charset> asCharsetList(String name, String defaultValue) {
        return doGetList(name, Charset.class, defaultValue);
    }

    public File asFile(String name) {
        return doGetValue(name, File.class, null);
    }

    public File asFile(String name, String defaultValue) {
        return doGetValue(name, File.class, defaultValue);
    }

    public List<File> asFileList(String name) {
        return doGetList(name, File.class, null);
    }

    public List<File> asFileList(String name, String defaultValue) {
        return doGetList(name, File.class, defaultValue);
    }

    public Resource asResource(String name) {
        return doGetValue(name, Resource.class, null);
    }

    public Resource asResource(String name, String defaultValue) {
        return doGetValue(name, Resource.class, defaultValue);
    }

    public List<Resource> asResourceList(String name) {
        return doGetList(name, Resource.class, null);
    }

    public List<Resource> asResourceList(String name, String defaultValue) {
        return doGetList(name, Resource.class, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> asClass(String name) {
        return (Class<T>) doGetValue(name, Class.class, null);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> asClass(String name, String defaultValue) {
        return (Class<T>) doGetValue(name, Class.class, defaultValue);
    }

    @SuppressWarnings("rawtypes")
    public List<Class> asClassList(String name) {
        return doGetList(name, Class.class, null);
    }

    @SuppressWarnings("rawtypes")
    public List<Class> asClassList(String name, String defaultValue) {
        return doGetList(name, Class.class, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T asObject(String name) {
        return (T) doGetValue(name, Object.class, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T asObject(String name, String defaultValue) {
        return (T) doGetValue(name, Object.class, defaultValue);
    }

    public <T> T asObject(String name, Class<T> targetClass) {
        return doGetValue(name, targetClass, null);
    }

    public <T> T asObject(String name, Class<T> targetClass, String defaultValue) {
        return doGetValue(name, targetClass, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> asObjectList(String name) {
        return (List<T>) doGetList(name, Object.class, null);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> asObjectList(String name, String defaultValue) {
        return (List<T>) doGetList(name, Object.class, defaultValue);
    }

    public <T> List<T> asObjectList(String name, Class<T> elementType) {
        return doGetList(name, elementType, null);
    }

    public <T> List<T> asObjectList(String name, Class<T> elementType, String defaultValue) {
        return doGetList(name, elementType, defaultValue);
    }

    // -----------------------------------------------------------------

    private Map<String, Object> objectPool;

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T aliasNameAsObject(String aliasName, Class<T> targetClass) {
        if (aliasName.startsWith("$")) {
            if (objectPool == null) {
                objectPool = new HashMap<String, Object>();
            }

            // get from pool
            Object object = objectPool.get(aliasName);
            if (object != null) {
                return (T) object;
            }

            object = super.aliasNameAsObject(aliasName, targetClass);
            objectPool.put(aliasName, object); // put into pool
            return (T) object;
        }

        return super.aliasNameAsObject(aliasName, targetClass);
    }

}
