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

public final class EqualsBuilder {
    private boolean result = true;

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (!lhs.getClass().isArray()) {
            result = lhs.equals(rhs);
        } else if (lhs.getClass() != rhs.getClass()) {
            result = false;
        } else if ((lhs instanceof long[])) {
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
            append((Object[]) lhs, (Object[]) rhs);
        }
        return this;
    }

    public EqualsBuilder append(long lhs, long rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(int lhs, int rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(short lhs, short rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(char lhs, char rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(double lhs, double rhs) {
        if (!result) {
            return this;
        }
        return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    public EqualsBuilder append(float lhs, float rhs) {
        if (!result) {
            return this;
        }
        return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        if (!result) {
            return this;
        }
        result = (lhs == rhs);
        return this;
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (!result) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if ((lhs == null) || (rhs == null)) {
            result = false;
            return this;
        }
        if (lhs.length != rhs.length) {
            result = false;
            return this;
        }
        for (int i = 0; (i < lhs.length) && (result); i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public boolean isEquals() {
        return result;
    }
}
