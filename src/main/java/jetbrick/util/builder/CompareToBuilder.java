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
package jetbrick.util.builder;

import java.util.Comparator;

public final class CompareToBuilder {
    private int result = 0;

    public CompareToBuilder append(Object lhs, Object rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(Object lhs, Object rhs, Comparator<?> comparator) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.getClass().isArray()) {
            if ((lhs instanceof long[])) {
                append((long[]) lhs, (long[]) rhs);
            } else if ((lhs instanceof int[])) {
                append((int[]) lhs, (int[]) rhs);
            } else if ((lhs instanceof short[])) {
                append((short[]) lhs, (short[]) rhs);
            } else if ((lhs instanceof char[])) {
                append((char[]) lhs, (char[]) rhs);
            } else if ((lhs instanceof byte[])) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if ((lhs instanceof double[])) {
                append((double[]) lhs, (double[]) rhs);
            } else if ((lhs instanceof float[])) {
                append((float[]) lhs, (float[]) rhs);
            } else if ((lhs instanceof boolean[])) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                append((Object[]) lhs, (Object[]) rhs, comparator);
            }

        } else if (comparator == null) {
            @SuppressWarnings("unchecked")
            Comparable<Object> comparable = (Comparable<Object>) lhs;
            result = comparable.compareTo(rhs);
        } else {
            @SuppressWarnings("unchecked")
            Comparator<Object> comparator2 = (Comparator<Object>) comparator;
            result = comparator2.compare(lhs, rhs);
        }

        return this;
    }

    public CompareToBuilder append(long lhs, long rhs) {
        if (result != 0) {
            return this;
        }
        result = (lhs > rhs ? 1 : lhs < rhs ? -1 : 0);
        return this;
    }

    public CompareToBuilder append(int lhs, int rhs) {
        if (result != 0) {
            return this;
        }
        result = (lhs > rhs ? 1 : lhs < rhs ? -1 : 0);
        return this;
    }

    public CompareToBuilder append(short lhs, short rhs) {
        if (result != 0) {
            return this;
        }
        result = (lhs > rhs ? 1 : lhs < rhs ? -1 : 0);
        return this;
    }

    public CompareToBuilder append(char lhs, char rhs) {
        if (result != 0) {
            return this;
        }
        result = (lhs > rhs ? 1 : lhs < rhs ? -1 : 0);
        return this;
    }

    public CompareToBuilder append(byte lhs, byte rhs) {
        if (result != 0) {
            return this;
        }
        result = (lhs > rhs ? 1 : lhs < rhs ? -1 : 0);
        return this;
    }

    public CompareToBuilder append(double lhs, double rhs) {
        if (result != 0) {
            return this;
        }
        result = Double.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(float lhs, float rhs) {
        if (result != 0) {
            return this;
        }
        result = Float.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(boolean lhs, boolean rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (!lhs)
            result = -1;
        else {
            result = 1;
        }
        return this;
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator<?> comparator) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }

    public CompareToBuilder append(long[] lhs, long[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(int[] lhs, int[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(short[] lhs, short[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(char[] lhs, char[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(byte[] lhs, byte[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(double[] lhs, double[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(float[] lhs, float[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
        if (result != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            result = -1;
            return this;
        }
        if (rhs == null) {
            result = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = (lhs.length < rhs.length ? -1 : 1);
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result == 0); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public int toComparison() {
        return result;
    }
}
