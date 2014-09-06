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
package jetbrick.util.builder;

public final class HashCodeBuilder {
    private final int constant;
    private int result = 0;

    public HashCodeBuilder() {
        constant = 37;
        result = 17;
    }

    public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
        }
        if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        }
        if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
        }
        if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        }
        constant = multiplierNonZeroOddNumber;
        result = initialNonZeroOddNumber;
    }

    public HashCodeBuilder append(boolean value) {
        result = (result * constant + (value ? 0 : 1));
        return this;
    }

    public HashCodeBuilder append(boolean[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (boolean element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(byte value) {
        result = (result * constant + value);
        return this;
    }

    public HashCodeBuilder append(byte[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (byte element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(char value) {
        result = (result * constant + value);
        return this;
    }

    public HashCodeBuilder append(char[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (char element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(double value) {
        return append(Double.doubleToLongBits(value));
    }

    public HashCodeBuilder append(double[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (double element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(float value) {
        result = (result * constant + Float.floatToIntBits(value));
        return this;
    }

    public HashCodeBuilder append(float[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (float element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(int value) {
        result = (result * constant + value);
        return this;
    }

    public HashCodeBuilder append(int[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (int element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(long value) {
        result = (result * constant + (int) (value ^ value >> 32));
        return this;
    }

    public HashCodeBuilder append(long[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (long element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            result *= constant;
        } else if (object.getClass().isArray()) {
            if (object instanceof long[]) {
                append((long[]) object);
            } else if (object instanceof int[]) {
                append((int[]) object);
            } else if (object instanceof short[]) {
                append((short[]) object);
            } else if (object instanceof char[]) {
                append((char[]) object);
            } else if (object instanceof byte[]) {
                append((byte[]) object);
            } else if (object instanceof double[]) {
                append((double[]) object);
            } else if (object instanceof float[]) {
                append((float[]) object);
            } else if (object instanceof boolean[]) {
                append((boolean[]) object);
            } else {
                append((Object[]) object);
            }
        } else {
            result = (result * constant + object.hashCode());
        }

        return this;
    }

    public HashCodeBuilder append(Object[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (Object element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(short value) {
        result = (result * constant + value);
        return this;
    }

    public HashCodeBuilder append(short[] array) {
        if (array == null) {
            result *= constant;
        } else {
            for (short element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder appendSuper(int superHashCode) {
        result = (result * constant + superHashCode);
        return this;
    }

    public int toHashCode() {
        return result;
    }
}
