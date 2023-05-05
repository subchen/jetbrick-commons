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
package jetbrick.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;

/**
 * 简单实现的 Object 转 JSON 字符串.
 */
public final class JSONUtils {

    public static String toJSONString(Object object) {
        if (object == null) return "null";
        if (object instanceof Number) return ((Number) object).toString();
        if (object instanceof Boolean) return ((Boolean) object).toString();
        if (object instanceof CharSequence) return stringToJSONString(object.toString());
        if (object instanceof Date) return dateToJSONString((Date) object);
        if (object instanceof Iterable) return iteratorToJSONString(((Iterable<?>) object).iterator());
        if (object instanceof Map) return mapToJSONString((Map<?, ?>) object);
        if (object.getClass().isArray()) return iteratorToJSONString(Arrays.asList((Object[]) object).iterator());
        if (object instanceof Enumeration) return iteratorToJSONString(Collections.list((Enumeration<?>) object).iterator());
        if (object instanceof Iterator) return iteratorToJSONString((Iterator<?>) object);
        if (object instanceof Character) return stringToJSONString(object.toString());
        return beanToJSONString(object);
    }

    private static String stringToJSONString(String str) {
        return '\"' + StringEscapeUtils.escapeJavaScript(str) + '\"';
    }

    private static String dateToJSONString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return stringToJSONString(dateFormat.format(date));
    }

    private static String iteratorToJSONString(Iterator<?> it) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        while (it.hasNext()) {
            if (sb.length() > 1) sb.append(',');
            sb.append(toJSONString(it.next()));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String mapToJSONString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        Iterator<?> it = map.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) it.next();
            if (sb.length() > 1) sb.append(',');
            sb.append(stringToJSONString(entry.getKey().toString()));
            sb.append(':');
            sb.append(toJSONString(entry.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }

    private static String beanToJSONString(Object bean) {
        try {
            KlassInfo klass = KlassInfo.create(bean.getClass());
            MethodInfo method = klass.getMethod("toJSONString");
            if (method == null) {
                return stringToJSONString(bean.toString());
            } else {
                Object result = method.invoke(bean);
                return result == null ? "null" : result.toString();
            }
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
}
