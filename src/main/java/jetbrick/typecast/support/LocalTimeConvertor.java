/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
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

import java.time.LocalTime;
import jetbrick.typecast.Convertor;

public final class LocalTimeConvertor implements Convertor<LocalTime> {
    public static final LocalTimeConvertor INSTANCE = new LocalTimeConvertor();

    @Override
    public LocalTime convert(String value) {
        if (value == null) {
            return null;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalTime.class);
        return convertToLocalTime(milliseconds);
    }

    @Override
    public LocalTime convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalTime) {
            return (LocalTime) value;
        }
        long milliseconds = DateConvertor.toMilliseconds(value, LocalTime.class);
        return convertToLocalTime(milliseconds);
    }

    private static LocalTime convertToLocalTime(long milliseconds) {
        long ms = Math.floorMod(milliseconds, 24 * 60 * 60 * 1000);
        return LocalTime.ofNanoOfDay(ms * 1000 * 1000);
    }
}
