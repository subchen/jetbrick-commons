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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jetbrick.typecast.Convertor;

public final class LocalDateTimeConvertor implements Convertor<LocalDateTime> {
    public static final LocalDateTimeConvertor INSTANCE = new LocalDateTimeConvertor();

    @Override
    public LocalDateTime convert(String value) {
        if (value == null) {
            return null;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalDateTime.class);
        return convertToLocalDateTime(milliseconds);
    }

    @Override
    public LocalDateTime convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalDateTime.class);
        return convertToLocalDateTime(milliseconds);
    }

    private static LocalDateTime convertToLocalDateTime(long milliseconds) {
        long days = Math.floorDiv(milliseconds, 24 * 60 * 60 * 1000);
        int ms = (int) Math.floorMod(milliseconds, 24 * 60 * 60 * 1000);
        LocalDate date = LocalDate.ofEpochDay(days);
        LocalTime time = LocalTime.ofNanoOfDay(ms * 1000 * 1000);
        return LocalDateTime.of(date, time);
    }
}
