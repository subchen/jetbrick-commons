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

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import jetbrick.typecast.Convertor;
import jetbrick.typecast.TypeCastException;
import jetbrick.util.DateUtils;
import jetbrick.util.JdkUtils;

public final class DateConvertor implements Convertor<Date> {
    public static final DateConvertor INSTANCE = new DateConvertor();

    @Override
    public Date convert(String value) {
        if (value == null) {
            return null;
        }
        return new Date(DateConvertor.toMilliseconds(value, Date.class));
    }

    @Override
    public Date convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        return new Date(DateConvertor.toMilliseconds(value, Date.class));
    }

    protected static long toMilliseconds(String value, Class<?> targetClass) {
        if (value.matches("[0-9]+")) {
            try {
                long milliseconds = Long.parseLong(value);
                return milliseconds;
            } catch (NumberFormatException e) {
                throw TypeCastException.create(value, targetClass, e);
            }
        }

        Date date = DateUtils.parse(value);
        if (date == null) {
            throw TypeCastException.create(value, targetClass, null);
        } else {
            return date.getTime();
        }
    }

    protected static long toMilliseconds(Object value, Class<?> targetClass) {
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (JdkUtils.IS_AT_LEAST_JAVA_8) {
            if (value instanceof Instant) {
                return ((Instant) value).toEpochMilli();
            }
            if (value instanceof LocalDateTime) {
                LocalDateTime dt = (LocalDateTime) value;
                ZoneOffset zone = ZoneOffset.of(ZoneId.systemDefault().getId());
                return dt.toInstant(zone).toEpochMilli();
            }
            if (value instanceof LocalDate) {
                LocalDate dt = (LocalDate) value;
                Calendar cal = Calendar.getInstance();
                cal.set(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                return cal.getTimeInMillis();
            }
            if (value instanceof LocalTime) {
                LocalTime dt = (LocalTime) value;
                Calendar cal = Calendar.getInstance();
                cal.set(0, 0, 0, dt.getHour(), dt.getMinute(), dt.getSecond());
                cal.set(Calendar.MILLISECOND, dt.getNano() / 1000000);
                return cal.getTimeInMillis();
            }
        }
        return toMilliseconds(value.toString(), targetClass);
    }
}
