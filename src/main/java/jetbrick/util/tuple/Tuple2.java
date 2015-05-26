/**
 * Copyright 2013-2015 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.util.tuple;

import java.io.Serializable;

public class Tuple2<T1, T2> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T1 v1;
    private T2 v2;

    public Tuple2() {
    }

    public Tuple2(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 v1() {
        return v1;
    }

    public T2 v2() {
        return v2;
    }

    public void v1(T1 value) {
        this.v1 = value;
    }

    public void v2(T2 value) {
        this.v2 = value;
    }

    @SuppressWarnings("serial")
    public Tuple2<T1, T2> unmodifiedTuple2() {
        return new Tuple2<T1, T2>(v1, v2) {
            @Override
            public void v1(T1 value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void v2(T2 value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (v1 != null ? !v1.equals(tuple2.v1) : tuple2.v1 != null) {
            return false;
        }
        if (v2 != null ? !v2.equals(tuple2.v2) : tuple2.v2 != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{v1=" + v1 + ", v2=" + v2 + '}';
    }
}
