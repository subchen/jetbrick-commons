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
package jetbrick.typecast.support;

import jetbrick.typecast.Convertor;
import jetbrick.typecast.TypeCastException;

public final class DoubleConvertor implements Convertor<Double> {
    public static final DoubleConvertor INSTANCE = new DoubleConvertor();

    @Override
    public Double convert(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw TypeCastException.create(value, Double.class, e);
        }
    }

    @Override
    public Double convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Double.class) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        return convert(value.toString());
    }
}
