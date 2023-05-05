/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
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
public final class MutableByte extends Number implements Comparable<MutableByte>, Cloneable {
    public byte value;

    public MutableByte() {
    }

    public MutableByte(byte value) {
        this.value = value;
    }

    public MutableByte(String value) {
        this.value = Byte.parseByte(value);
    }

    public MutableByte(Number number) {
        value = number.byteValue();
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.byteValue();
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
            if (obj instanceof MutableByte) {
                return value == ((MutableByte) obj).value;
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
    public int compareTo(MutableByte other) {
        return value == other.value ? 0 : value < other.value ? -1 : 1;
    }

    @Override
    public MutableByte clone() {
        return new MutableByte(value);
    }
}
