/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.util;

import java.lang.reflect.InvocationTargetException;

public final class ExceptionUtils {

    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        if (e instanceof InvocationTargetException) {
            return unchecked(((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(e);
    }

    /**
     * Throw checked exceptions like runtime exceptions.
     *
     * see: http://blog.jooq.org/2012/09/14/throw-checked-exceptions-like-runtime-exceptions-in-java/
     */
    public static void rethrow(Throwable e) {
        ExceptionUtils.<RuntimeException> rethrow0(e);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void rethrow0(Throwable e) throws E {
        throw (E) e;
    }
}
