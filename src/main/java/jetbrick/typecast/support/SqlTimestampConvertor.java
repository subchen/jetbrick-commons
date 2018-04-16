/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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

public final class SqlTimestampConvertor implements Convertor<java.sql.Timestamp> {
    public static final SqlTimestampConvertor INSTANCE = new SqlTimestampConvertor();

    @Override
    public java.sql.Timestamp convert(String value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Timestamp(DateConvertor.toMilliseconds(value, java.sql.Timestamp.class));
    }

    @Override
    public java.sql.Timestamp convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }
        return new java.sql.Timestamp(DateConvertor.toMilliseconds(value, java.sql.Timestamp.class));
    }
}
