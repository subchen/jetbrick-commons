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
package jetbrick.util.builder;

import java.lang.reflect.*;

public class ToStringBuilder {
    private final StringBuilder sb = new StringBuilder(32);
    private final Object object;
    private final boolean formatted;

    public static String reflection(Object object) {
        return reflection(object, true);
    }

    public static String reflection(Object object, boolean formatted) {
        ToStringBuilder builder = new ToStringBuilder(object, formatted);
        Field[] fields = object.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field.getName().indexOf('$') != -1) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers()) || (Modifier.isStatic(field.getModifiers()))) {
                continue;
            }
            try {
                Object fieldValue = field.get(object);
                builder.append(fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();
    }

    public ToStringBuilder(Object object) {
        this(object, true);
    }

    public ToStringBuilder(Object object, boolean formatted) {
        this.object = object;
        this.formatted = formatted;
    }

    public void append(String fieldName, Object value) {
        if (value == null) {
            value = "<null>";
        } else if (value.getClass().isArray()) {
            int length = Array.getLength(value);

            StringBuilder str = new StringBuilder(32);
            str.append('[');
            for (int i = 0; i < length; i++) {
                if (i >= 10) {
                    str.append(",...");
                    break;
                }
                if (i > 0) {
                    str.append(',');
                }
                str.append(Array.get(value, i));
            }
            str.append(']');
            value = str.toString();
        }

        if (formatted) {
            sb.append("  ").append(fieldName).append(" = ").append(value).append('\n');
        } else {
            if (sb.length() > 0) {
                sb.append(';');
            }
            sb.append(fieldName).append('=').append(value);
        }
    }

    public String build() {
        return toString();
    }

    @Override
    public String toString() {
        if (formatted) {
            return object.getClass().getName() + "[\n" + sb + ']';
        } else {
            return object.getClass().getSimpleName() + '[' + sb + ']';
        }
    }
}
