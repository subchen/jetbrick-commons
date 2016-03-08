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

import jetbrick.typecast.Convertor;

public final class SqlTimeConvertor implements Convertor<java.sql.Time> {
    public static final SqlTimeConvertor INSTANCE = new SqlTimeConvertor();

    @Override
    public java.sql.Time convert(String value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Time(DateConvertor.toMilliseconds(value, java.sql.Time.class));
    }

    @Override
    public java.sql.Time convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Time) {
            return (java.sql.Time) value;
        }
        return new java.sql.Time(DateConvertor.toMilliseconds(value, java.sql.Time.class));
    }
}
