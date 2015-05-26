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

public final class BooleanConvertor implements Convertor<Boolean> {
    public static final BooleanConvertor INSTANCE = new BooleanConvertor();

    @Override
    public Boolean convert(String value) {
        String s = value.toLowerCase();

        //@formatter:off
        if (s.equals("true")
         || s.equals("1")
         || s.equals("yes")
         || s.equals("y")
         || s.equals("on")) {
           return Boolean.TRUE;
        }
        if (s.equals("false")
         || s.equals("0")
         || s.equals("no")
         || s.equals("n")
         || s.equals("off")) {
           return Boolean.FALSE;
        }
        //@formatter:on

        throw TypeCastException.create(value, Boolean.class, null);
    }

    @Override
    public Boolean convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Boolean.class) {
            return (Boolean) value;
        }
        return convert(value.toString());
    }
}
