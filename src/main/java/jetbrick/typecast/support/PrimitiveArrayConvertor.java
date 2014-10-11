/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.util.*;
import jetbrick.collection.ListUtils;
import jetbrick.typecast.Convertor;
import jetbrick.util.StringUtils;

@SuppressWarnings("unchecked")
public final class PrimitiveArrayConvertor<T> implements Convertor<T> {
    public static final PrimitiveArrayConvertor<byte[]> BYTE_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<byte[]>(Byte.TYPE);
    public static final PrimitiveArrayConvertor<short[]> SHORT_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<short[]>(Short.TYPE);
    public static final PrimitiveArrayConvertor<int[]> INTEGER_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<int[]>(Integer.TYPE);
    public static final PrimitiveArrayConvertor<long[]> LONG_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<long[]>(Long.TYPE);
    public static final PrimitiveArrayConvertor<float[]> FLOAT_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<float[]>(Float.TYPE);
    public static final PrimitiveArrayConvertor<double[]> DOUBLE_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<double[]>(Double.TYPE);
    public static final PrimitiveArrayConvertor<char[]> CHAR_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<char[]>(Character.TYPE);
    public static final PrimitiveArrayConvertor<boolean[]> BOOLEAN_ARRAY_CONVERTOR = new PrimitiveArrayConvertor<boolean[]>(Boolean.TYPE);

    private final Class<?> elementType;

    public PrimitiveArrayConvertor(Class<?> elementType) {
        this.elementType = elementType;
    }

    @Override
    public T convert(String value) {
        if (value == null) {
            return null;
        }
        String[] values = StringUtils.split(value.toString(), ',');
        return convertToArray(values);
    }

    @Override
    public T convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            if (elementType == value.getClass().getComponentType()) {
                return (T) value;
            }
            Object[] arrays = ArrayConvertor.toObjectArray(value);
            return convertToArray(arrays);
        }

        if (value instanceof Iterable) {
            List<?> results = ListUtils.asList((Iterable<?>) value);
            return convertToArray(results.toArray());
        }
        if (value instanceof Iterator) {
            List<?> results = ListUtils.asList((Iterator<?>) value);
            return convertToArray(results.toArray());
        }
        if (value instanceof Enumeration) {
            List<?> results = ListUtils.asList((Enumeration<?>) value);
            return convertToArray(results.toArray());
        }

        return convert(value.toString());
    }

    private T convertToArray(Object[] arrays) {
        if (elementType == Integer.TYPE) {
            return (T) convertToIntArray(arrays);
        } else if (elementType == Long.TYPE) {
            return (T) convertToLongArray(arrays);
        } else if (elementType == Float.TYPE) {
            return (T) convertToFloatArray(arrays);
        } else if (elementType == Double.TYPE) {
            return (T) convertToDoubleArray(arrays);
        } else if (elementType == Boolean.TYPE) {
            return (T) convertToBooleanArray(arrays);
        } else if (elementType == Byte.TYPE) {
            return (T) convertToByteArray(arrays);
        } else if (elementType == Short.TYPE) {
            return (T) convertToShortArray(arrays);
        } else if (elementType == Character.TYPE) {
            return (T) convertToCharArray(arrays);
        }
        throw new IllegalStateException("Unreachable code");
    }

    private int[] convertToIntArray(Object[] items) {
        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = IntegerConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private long[] convertToLongArray(Object[] items) {
        long[] results = new long[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = LongConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private byte[] convertToByteArray(Object[] items) {
        byte[] results = new byte[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = ByteConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private short[] convertToShortArray(Object[] items) {
        short[] results = new short[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = ShortConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private float[] convertToFloatArray(Object[] items) {
        float[] results = new float[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = FloatConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private double[] convertToDoubleArray(Object[] items) {
        double[] results = new double[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = DoubleConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private boolean[] convertToBooleanArray(Object[] items) {
        boolean[] results = new boolean[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = BooleanConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }

    private char[] convertToCharArray(Object[] items) {
        char[] results = new char[items.length];

        for (int i = 0; i < items.length; i++) {
            Object value = items[i];
            if (value == null) {
                throw new NullPointerException();
            } else {
                results[i] = CharacterConvertor.INSTANCE.convert(value);
            }
        }
        return results;
    }
}
