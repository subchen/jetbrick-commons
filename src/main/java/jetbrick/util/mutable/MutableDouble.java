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
package jetbrick.util.mutable;

@SuppressWarnings("serial")
public final class MutableDouble extends Number implements Comparable<MutableDouble>, Cloneable {
    public double value;

    public MutableDouble() {
    }

    public MutableDouble(double value) {
        this.value = value;
    }

    public MutableDouble(String value) {
        this.value = Double.parseDouble(value);
    }

    public MutableDouble(Number number) {
        value = number.doubleValue();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.doubleValue();
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ bits >>> 32);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof MutableDouble) {
                return Double.doubleToLongBits(value) == Double.doubleToLongBits(((MutableDouble) obj).value);
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
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    @Override
    public int compareTo(MutableDouble other) {
        return Double.compare(value, other.value);
    }

    @Override
    public MutableDouble clone() {
        return new MutableDouble(value);
    }
}
