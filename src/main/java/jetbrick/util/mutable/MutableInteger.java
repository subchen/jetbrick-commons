/**
 * Copyright 2013-2019 Guoqiang Chen, Shanghai, China. All rights reserved.
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
public final class MutableInteger extends Number implements Comparable<MutableInteger>, Cloneable {
    public int value;

    public MutableInteger() {
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    public MutableInteger(String value) {
        this.value = Integer.parseInt(value);
    }

    public MutableInteger(Number number) {
        value = number.intValue();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.intValue();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof MutableInteger) {
                return value == ((MutableInteger) obj).value;
            }
        }
        return false;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int compareTo(MutableInteger other) {
        return value == other.value ? 0 : value < other.value ? -1 : 1;
    }

    @Override
    public MutableInteger clone() {
        return new MutableInteger(value);
    }
}
