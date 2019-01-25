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
package jetbrick.bean;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import jetbrick.util.JdkUtils;

// Avoid unnecessary `setAccessible()` calls.
// From JDK9+, following warnings will output when call `setAccessible()` or `isAccessible()`
//
//   WARNING: An illegal reflective access operation has occurred
//   WARNING: Illegal reflective access by jetbrick.bean.MethodInfo to method java.lang.String.value()
//   WARNING: Please consider reporting this to the maintainers of jetbrick.bean.MethodInfo
//   WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
//   WARNING: All illegal access operations will be denied in a future release
//
// same issue: https://github.com/mybatis/mybatis-3/issues/1156
//   solution: https://github.com/mybatis/mybatis-3/pull/1321/files
public class JdkReflectionUtils {
    public static boolean CAN_CONTROL_MEMBER_ACCESSIBLE = canControlMemberAccessible();

    public static void setAccessible(AccessibleObject accessibleObject) {
        if (JdkUtils.JAVA_MAJOR_VERSION < 9) {
            try {
                if (!accessibleObject.isAccessible()) {
                    accessibleObject.setAccessible(true);
                }
            } catch (SecurityException e) {
                // A SecurityException is raised if flag is true but accessibility of
                // this object may not be changed (for example, if this element object
                // is a Constructor object for the class Class).
            }
        }
    }

    public static Object get(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            if (CAN_CONTROL_MEMBER_ACCESSIBLE) {
                field.setAccessible(true);
                return field.get(obj);
            } else {
                throw e;
            }
        }
    }

    public static void set(Field field, Object obj, Object value) throws IllegalArgumentException, IllegalAccessException {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            if (CAN_CONTROL_MEMBER_ACCESSIBLE) {
                field.setAccessible(true);
                field.set(obj, value);
            } else {
                throw e;
            }
        }
    }

    public static Object invoke(Method method, Object obj, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            if (CAN_CONTROL_MEMBER_ACCESSIBLE) {
                method.setAccessible(true);
                return method.invoke(obj, args);
            } else {
                throw e;
            }
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... initargs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            return constructor.newInstance(initargs);
        } catch (IllegalAccessException e) {
            if (CAN_CONTROL_MEMBER_ACCESSIBLE) {
                constructor.setAccessible(true);
                return constructor.newInstance(initargs);
            } else {
                throw e;
            }
        }
    }

    /**
     * Checks whether can control member accessible.
     *
     * @return If can control member accessible, it return {@literal true}
     */
    private static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }
}
