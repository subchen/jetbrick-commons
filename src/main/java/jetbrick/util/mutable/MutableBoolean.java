/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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

public final class MutableBoolean implements Comparable<MutableBoolean>, Cloneable {
    public boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean(String value) {
        this.value = Boolean.valueOf(value).booleanValue();
    }

    public MutableBoolean(Boolean value) {
        this.value = value.booleanValue();
    }

    public MutableBoolean(Number number) {
        value = (number.intValue() != 0);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(Boolean value) {
        this.value = value.booleanValue();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof MutableBoolean) {
                return value == ((MutableBoolean) obj).value;
            }
        }
        return false;
    }

    @Override
    public int compareTo(MutableBoolean o) {
        return !value ? -1 : value == o.value ? 0 : 1;
    }

    @Override
    public MutableBoolean clone() {
        return new MutableBoolean(value);
    }
}
