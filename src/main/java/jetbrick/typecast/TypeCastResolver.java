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
package jetbrick.typecast;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import jetbrick.io.resource.Resource;
import jetbrick.typecast.support.ArrayConvertor;
import jetbrick.typecast.support.BigDecimalConvertor;
import jetbrick.typecast.support.BigIntegerConvertor;
import jetbrick.typecast.support.BooleanConvertor;
import jetbrick.typecast.support.ByteConvertor;
import jetbrick.typecast.support.CalendarConvertor;
import jetbrick.typecast.support.CharacterConvertor;
import jetbrick.typecast.support.CharsetConvertor;
import jetbrick.typecast.support.ClassConvertor;
import jetbrick.typecast.support.DateConvertor;
import jetbrick.typecast.support.DoubleConvertor;
import jetbrick.typecast.support.FileConvertor;
import jetbrick.typecast.support.FloatConvertor;
import jetbrick.typecast.support.InstantConvertor;
import jetbrick.typecast.support.IntegerConvertor;
import jetbrick.typecast.support.ListConvertor;
import jetbrick.typecast.support.LocalDateConvertor;
import jetbrick.typecast.support.LocalDateTimeConvertor;
import jetbrick.typecast.support.LocalTimeConvertor;
import jetbrick.typecast.support.LocaleConvertor;
import jetbrick.typecast.support.LongConvertor;
import jetbrick.typecast.support.PathConvertor;
import jetbrick.typecast.support.PrimitiveArrayConvertor;
import jetbrick.typecast.support.ResourceConvertor;
import jetbrick.typecast.support.ShortConvertor;
import jetbrick.typecast.support.SqlDateConvertor;
import jetbrick.typecast.support.SqlTimeConvertor;
import jetbrick.typecast.support.SqlTimestampConvertor;
import jetbrick.typecast.support.StringConvertor;
import jetbrick.typecast.support.TimeZoneConvertor;
import jetbrick.typecast.support.URIConvertor;
import jetbrick.typecast.support.URLConvertor;
import jetbrick.util.JdkUtils;

@SuppressWarnings("unchecked")
public final class TypeCastResolver {
    private final Map<Class<?>, Convertor<?>> pool;
    private final Map<Class<?>, ListConvertor<?>> listPool;
    private final Map<Class<?>, ArrayConvertor<?>> objectArrayPool;
    private final Map<Class<?>, PrimitiveArrayConvertor<?>> primitiveArrayPool;

    public TypeCastResolver() {
        this.pool = new IdentityHashMap<Class<?>, Convertor<?>>(64);
        this.listPool = new IdentityHashMap<Class<?>, ListConvertor<?>>(64);
        this.objectArrayPool = new IdentityHashMap<Class<?>, ArrayConvertor<?>>(64);
        this.primitiveArrayPool = new IdentityHashMap<Class<?>, PrimitiveArrayConvertor<?>>(16);

        // number
        register(Byte.class, ByteConvertor.INSTANCE);
        register(Byte.TYPE, ByteConvertor.INSTANCE);
        register(Short.class, ShortConvertor.INSTANCE);
        register(Short.TYPE, ShortConvertor.INSTANCE);
        register(Integer.class, IntegerConvertor.INSTANCE);
        register(Integer.TYPE, IntegerConvertor.INSTANCE);
        register(Long.class, LongConvertor.INSTANCE);
        register(Long.TYPE, LongConvertor.INSTANCE);
        register(Float.class, FloatConvertor.INSTANCE);
        register(Float.TYPE, FloatConvertor.INSTANCE);
        register(Double.class, DoubleConvertor.INSTANCE);
        register(Double.TYPE, DoubleConvertor.INSTANCE);
        register(Character.class, CharacterConvertor.INSTANCE);
        register(Character.TYPE, CharacterConvertor.INSTANCE);
        register(Boolean.class, BooleanConvertor.INSTANCE);
        register(Boolean.TYPE, BooleanConvertor.INSTANCE);
        register(BigInteger.class, BigIntegerConvertor.INSTANCE);
        register(BigDecimal.class, BigDecimalConvertor.INSTANCE);

        // commons
        register(String.class, StringConvertor.INSTANCE);
        register(Class.class, ClassConvertor.INSTANCE);
        register(Charset.class, CharsetConvertor.INSTANCE);

        // date time
        register(java.util.Date.class, DateConvertor.INSTANCE);
        register(java.sql.Date.class, SqlDateConvertor.INSTANCE);
        register(java.sql.Time.class, SqlTimeConvertor.INSTANCE);
        register(java.sql.Timestamp.class, SqlTimestampConvertor.INSTANCE);
        register(Calendar.class, CalendarConvertor.INSTANCE);
        register(GregorianCalendar.class, CalendarConvertor.INSTANCE);
        register(Locale.class, LocaleConvertor.INSTANCE);
        register(TimeZone.class, TimeZoneConvertor.INSTANCE);

        // file path
        register(File.class, FileConvertor.INSTANCE);
        register(URI.class, URIConvertor.INSTANCE);
        register(URL.class, URLConvertor.INSTANCE);
        register(Resource.class, ResourceConvertor.INSTANCE);

        // others
        if (JdkUtils.IS_AT_LEAST_JAVA_7) {
            registerWhenJdk7();
        }
        if (JdkUtils.IS_AT_LEAST_JAVA_8) {
            registerWhenJdk8();
        }
    }

    private void registerWhenJdk7() {
        register(Path.class, PathConvertor.INSTANCE);
    }

    private void registerWhenJdk8() {
        register(Instant.class, InstantConvertor.INSTANCE);
        register(LocalDateTime.class, LocalDateTimeConvertor.INSTANCE);
        register(LocalDate.class, LocalDateConvertor.INSTANCE);
        register(LocalTime.class, LocalTimeConvertor.INSTANCE);
    }

    // -------- register / lookup -----------------------------------------------------------

    @SuppressWarnings("rawtypes")
    public <T> void register(Class<?> type, Convertor<?> convertor) {
        pool.put(type, convertor);

        if (type.isPrimitive()) {
            primitiveArrayPool.put(type, new PrimitiveArrayConvertor(type));
        } else {
            objectArrayPool.put(type, new ArrayConvertor(type, convertor));
            listPool.put(type, new ListConvertor(type, convertor));
        }
    }

    public void unregister(Class<?> type) {
        pool.remove(type);

        if (type.isPrimitive()) {
            primitiveArrayPool.remove(type);
        } else {
            objectArrayPool.remove(type);
            listPool.remove(type);
        }
    }

    public <T> Convertor<T> lookup(Class<T> type) {
        return (Convertor<T>) pool.get(type);
    }

    // -------- convert string -----------------------------------------------------------

    public <T> T convert(String value, Class<T> type) {
        // fast-path
        if (value == null) {
            return null;
        }
        if (type == String.class) {
            return (T) value;
        }
        // normal-path
        Convertor<T> c = (Convertor<T>) pool.get(type);
        if (c != null) {
            return c.convert(value);
        }
        if (type.isArray()) {
            return (T) convertToArray(value, type.getComponentType());
        }
        throw new IllegalStateException("Unsupported cast class: " + type.getName());
    }

    public <T> T convertToArray(String value, Class<?> elementType) {
        if (elementType.isPrimitive()) {
            PrimitiveArrayConvertor<?> c = primitiveArrayPool.get(elementType);
            if (c != null) {
                return (T) c.convert(value);
            }
        } else {
            ArrayConvertor<?> c = objectArrayPool.get(elementType);
            if (c != null) {
                return (T) c.convert(value);
            }
        }
        throw new IllegalStateException("Cannot cast to array: " + elementType.getName() + "[]");
    }

    public <T> List<T> convertToList(String value, Class<T> elementType) {
        ListConvertor<T> c = (ListConvertor<T>) listPool.get(elementType);
        if (c != null) {
            return c.convert(value);
        }
        throw new IllegalStateException("Cannot cast to class: List<" + elementType.getName() + ">");
    }

    // ------ convert object -------------------------------------------------------------

    public <T> T convert(Object value, Class<T> type) {
        // fast-path
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        // normal-path
        Convertor<T> c = (Convertor<T>) pool.get(type);
        if (c != null) {
            return c.convert(value);
        }
        if (type.isArray()) {
            return (T) convertToArray(value, type.getComponentType());
        }
        throw new IllegalStateException("Unsupported cast class: " + type.getName());
    }

    public <T> T convertToArray(Object value, Class<?> elementType) {
        if (elementType.isPrimitive()) {
            PrimitiveArrayConvertor<?> c = primitiveArrayPool.get(elementType);
            if (c != null) {
                return (T) c.convert(value);
            }
        } else {
            ArrayConvertor<?> c = objectArrayPool.get(elementType);
            if (c != null) {
                return (T) c.convert(value);
            }
        }
        throw new IllegalStateException("Cannot cast to array: " + elementType.getName() + "[]");
    }

    public <T> List<T> convertToList(Object value, Class<T> elementType) {
        ListConvertor<T> c = (ListConvertor<T>) listPool.get(elementType);
        if (c != null) {
            return c.convert(value);
        }
        throw new IllegalStateException("Cannot cast to class: List<" + elementType.getName() + ">");
    }
}
