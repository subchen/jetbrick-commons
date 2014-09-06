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
package jetbrick.util.mutable;

@SuppressWarnings("serial")
public final class MutableFloat extends Number implements Comparable<MutableFloat>, Cloneable {
    public float value;

    public MutableFloat() {
    }

    public MutableFloat(float value) {
        this.value = value;
    }

    public MutableFloat(String value) {
        this.value = Float.parseFloat(value);
    }

    public MutableFloat(Number number) {
        value = number.floatValue();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.floatValue();
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof MutableFloat) {
                return Float.floatToIntBits(value) == Float.floatToIntBits(((MutableFloat) obj).value);
            }
        }
        return false;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public boolean isNaN() {
        return Float.isNaN(value);
    }

    public boolean isInfinite() {
        return Float.isInfinite(value);
    }

    @Override
    public int compareTo(MutableFloat other) {
        return Float.compare(value, other.value);
    }

    @Override
    public MutableFloat clone() {
        return new MutableFloat(value);
    }
}
