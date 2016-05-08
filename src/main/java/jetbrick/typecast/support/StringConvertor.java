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
package jetbrick.typecast.support;

import java.sql.Clob;
import java.sql.SQLException;

import jetbrick.typecast.Convertor;
import jetbrick.typecast.TypeCastException;
import jetbrick.util.ArrayUtils;

public final class StringConvertor implements Convertor<String> {
    public static StringConvertor INSTANCE = new StringConvertor();

    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public String convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return value.toString();
        }

        Class<?> type = value.getClass();
        if (type == Class.class) {
            return ((Class<?>) value).getName();
        }

        if (type.isArray()) {
            return ArrayUtils.toString(value);
        }

        if (value instanceof Clob) {
            Clob clob = (Clob) value;
            try {
                long length = clob.length();
                if (length > Integer.MAX_VALUE) {
                    throw new TypeCastException("Clob is too big.");
                }
                return clob.getSubString(1L, (int) length);
            } catch (SQLException e) {
                throw TypeCastException.create(value, String.class, e);
            }
        }

        return value.toString();
    }
}
