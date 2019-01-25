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
package jetbrick.web.servlet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.PropertyInfo;
import jetbrick.typecast.TypeCastUtils;
import jetbrick.util.StringUtils;
import jetbrick.util.Validate;

/**
 * 将 Request 中的参数，全部注入到一个 form 对象中.
 * 
 * @author Guoqiang Chen
 */
public abstract class RequestIntrospectUtils {

    @SuppressWarnings("unchecked")
    public static <T> T introspect(Class<T> formClass, ServletRequest request) {
        Validate.notNull(formClass);

        KlassInfo klass = KlassInfo.create(formClass);
        T form = (T) klass.newInstance();
        return introspect(form, request);
    }

    public static <T> T introspect(T form, ServletRequest request) {
        Validate.notNull(form);
        Validate.notNull(request);

        Class<? extends Object> formClass = form.getClass();
        KlassInfo klass = KlassInfo.create(formClass);
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String name = entry.getKey();
            PropertyInfo prop = klass.getProperty(name);
            if (prop == null) continue;

            String values[] = entry.getValue();
            if (values == null) continue;

            Class<?> type = prop.getRawType(formClass);
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                Object data = TypeCastUtils.convertToArray(values, componentType);
                prop.set(form, data);
            } else if (type == List.class || type == Collection.class) {
                Class<?> componentType = prop.getRawComponentType(formClass, 0);
                Object data = TypeCastUtils.convertToList(values, componentType);
                prop.set(form, data);
            } else {
                String value = StringUtils.trimToNull(values[0]);
                if (value == null) {
                    if (type.isPrimitive()) {
                        continue;
                    } else {
                        prop.set(form, null);
                    }
                } else {
                    Object data = TypeCastUtils.convert(value, type);
                    prop.set(form, data);
                }
            }
        }
        return form;
    }
}
