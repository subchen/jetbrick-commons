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
import jetbrick.typecast.TypeCastException;

public final class FloatConvertor implements Convertor<Float> {
    public static final FloatConvertor INSTANCE = new FloatConvertor();

    @Override
    public Float convert(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            throw TypeCastException.create(value, Float.class, e);
        }
    }

    @Override
    public Float convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Float.class) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        return convert(value.toString());
    }
}
