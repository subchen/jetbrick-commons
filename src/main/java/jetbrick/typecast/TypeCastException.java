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
package jetbrick.typecast;

public final class TypeCastException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public TypeCastException() {
        super();
    }

    public TypeCastException(String message) {
        super(message);
    }

    public TypeCastException(Throwable cause) {
        super(cause);
    }

    public TypeCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TypeCastException create(Object value, Class<?> targetClass, Throwable e) {
        return new TypeCastException("Unable to convert value to " + targetClass.getName() + " : " + String.valueOf(value), e);
    }

}
