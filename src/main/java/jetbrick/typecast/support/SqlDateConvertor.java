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
package jetbrick.typecast.support;

import jetbrick.typecast.Convertor;

public final class SqlDateConvertor implements Convertor<java.sql.Date> {
    public static final SqlDateConvertor INSTANCE = new SqlDateConvertor();

    @Override
    public java.sql.Date convert(String value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Date(DateConvertor.toMilliseconds(value, java.sql.Date.class));
    }

    @Override
    public java.sql.Date convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        return new java.sql.Date(DateConvertor.toMilliseconds(value, java.sql.Date.class));
    }
}
