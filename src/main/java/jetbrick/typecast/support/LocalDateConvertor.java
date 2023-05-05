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

import java.time.LocalDate;
import jetbrick.typecast.Convertor;

public final class LocalDateConvertor implements Convertor<LocalDate> {
    public static final LocalDateConvertor INSTANCE = new LocalDateConvertor();

    @Override
    public LocalDate convert(String value) {
        if (value == null) {
            return null;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalDate.class);
        return convertToLocalDate(milliseconds);
    }

    @Override
    public LocalDate convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalDate.class);
        return convertToLocalDate(milliseconds);
    }

    private static LocalDate convertToLocalDate(long milliseconds) {
        long days = Math.floorDiv(milliseconds, 24 * 60 * 60 * 1000);
        return LocalDate.ofEpochDay(days);
    }
}
