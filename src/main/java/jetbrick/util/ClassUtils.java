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
package jetbrick.util;

import java.util.*;

public final class ClassUtils {
    private static final Set<Class<?>> boxed_class_set;
    private static final Set<Class<?>> unboxed_class_set;
    private static final Map<Class<?>, Class<?>> boxed_class_map;
    private static final Map<Class<?>, Class<?>> unboxed_class_map;

    /**
     * 判断一个 Class 是否存在并且可用.
     */
    public static boolean available(String qualifiedClassName) {
        return available(qualifiedClassName, null);
    }

    /**
     * 判断一个 Class 是否存在并且可用.
     */
    public static boolean available(String qualifiedClassName, ClassLoader loader) {
        return ClassLoaderUtils.loadClass(qualifiedClassName, loader) != null;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        if (clazz == null) return false;
        return (clazz.isPrimitive()) || (isPrimitiveWrapper(clazz));
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return boxed_class_set.contains(clazz);
    }

    public static Class<?> primitiveToWrapper(Class<?> clazz) {
        if (clazz != null && clazz.isPrimitive()) {
            return boxed_class_map.get(clazz);
        }
        return clazz;
    }

    public static Class<?>[] primitivesToWrappers(Class<?>... clazz) {
        if (clazz == null) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (clazz.length == 0) {
            return clazz;
        }
        Class<?>[] convertedClasses = new Class[clazz.length];
        for (int i = 0; i < clazz.length; i++) {
            convertedClasses[i] = primitiveToWrapper(clazz[i]);
        }
        return convertedClasses;
    }

    public static Class<?> wrapperToPrimitive(Class<?> clazz) {
        return unboxed_class_map.get(clazz);
    }

    public static Class<?>[] wrappersToPrimitives(Class<?>... clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.length == 0) {
            return clazz;
        }
        Class<?>[] convertedClasses = new Class[clazz.length];
        for (int i = 0; i < clazz.length; i++) {
            convertedClasses[i] = wrapperToPrimitive(clazz[i]);
        }
        return convertedClasses;
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return (clazz != null) && (clazz.getEnclosingClass() != null);
    }

    /**
     * Class.isAssignableFrom() 的增强版本。 支持 null, 自动装箱,
     * 以及数字类型的隐私转换.
     */
    public static boolean isAssignable(Class<?> lhs, Class<?> rhs) {
        if (lhs == null) return false;
        if (rhs == null) return (!(lhs.isPrimitive()));

        if (unboxed_class_set.contains(lhs)) {
            lhs = boxed_class_map.get(lhs);
        }
        if (unboxed_class_set.contains(rhs)) {
            rhs = boxed_class_map.get(rhs);
        }
        if (lhs.isAssignableFrom(rhs)) {
            return true;
        }

        lhs = unboxed_class_map.get(lhs);
        rhs = unboxed_class_map.get(rhs);
        if (lhs == null || rhs == null) {
            return false;
        }
        if (Integer.TYPE.equals(rhs)) {
            return (Long.TYPE.equals(lhs) || Float.TYPE.equals(lhs) || Double.TYPE.equals(lhs));
        }
        if (Long.TYPE.equals(rhs)) {
            return (Float.TYPE.equals(lhs) || Double.TYPE.equals(lhs));
        }
        if (Float.TYPE.equals(rhs)) {
            return Double.TYPE.equals(lhs);
        }
        if (Double.TYPE.equals(rhs)) {
            return false;
        }
        if (Boolean.TYPE.equals(rhs)) {
            return false;
        }
        if (Byte.TYPE.equals(rhs)) {
            return (Short.TYPE.equals(lhs) || Integer.TYPE.equals(lhs) || Long.TYPE.equals(lhs) || Float.TYPE.equals(lhs) || Double.TYPE.equals(lhs));
        }
        if (Short.TYPE.equals(rhs)) {
            return (Integer.TYPE.equals(lhs) || Long.TYPE.equals(lhs) || Float.TYPE.equals(lhs) || Double.TYPE.equals(lhs));
        }
        if (Character.TYPE.equals(rhs)) {
            return (Integer.TYPE.equals(lhs) || Long.TYPE.equals(lhs) || Float.TYPE.equals(lhs) || Double.TYPE.equals(lhs));
        }
        return false;
    }

    public static boolean isInstance(Class<?> type, Object object) {
        if (object == null) {
            return true;
        }
        if (type.isPrimitive()) {
            Class<?> cls = object.getClass();
            if (Number.class.isAssignableFrom(cls)) {
                if (Integer.TYPE == type) {
                    return (Integer.class == cls || Short.class == cls || Byte.class == cls);
                }
                if (Long.TYPE == type) {
                    return (Long.class == cls || Integer.class == cls || Short.class == cls || Byte.class == cls);
                }
                if (Float.TYPE == type) {
                    return (Float.class == cls || Long.class == cls || Integer.class == cls || Short.class == cls || Byte.class == cls);
                }
                if (Double.TYPE == type) {
                    return (Double.class == cls || Float.class == cls || Long.class == cls || Integer.class == cls || Short.class == cls || Byte.class == cls);
                }
                if (Short.TYPE == type) {
                    return (Short.class == cls || Byte.class == cls);
                }
                if (Byte.TYPE == type) {
                    return (Byte.class == cls);
                }
            }
            if (Boolean.TYPE == type) {
                return Boolean.class == cls;
            }
            if (Character.TYPE == type) {
                return Character.class == cls;
            }
        } else if (type.isInstance(object)) {
            return true;
        }
        return false;
    }

    static {
        boxed_class_set = new HashSet<Class<?>>();
        boxed_class_set.add(Boolean.class);
        boxed_class_set.add(Byte.class);
        boxed_class_set.add(Short.class);
        boxed_class_set.add(Character.class);
        boxed_class_set.add(Integer.class);
        boxed_class_set.add(Long.class);
        boxed_class_set.add(Float.class);
        boxed_class_set.add(Double.class);

        unboxed_class_set = new HashSet<Class<?>>();
        unboxed_class_set.add(Boolean.TYPE);
        unboxed_class_set.add(Byte.TYPE);
        unboxed_class_set.add(Short.TYPE);
        unboxed_class_set.add(Character.TYPE);
        unboxed_class_set.add(Integer.TYPE);
        unboxed_class_set.add(Long.TYPE);
        unboxed_class_set.add(Float.TYPE);
        unboxed_class_set.add(Double.TYPE);

        unboxed_class_map = new IdentityHashMap<Class<?>, Class<?>>(32);
        unboxed_class_map.put(Boolean.class, Boolean.TYPE);
        unboxed_class_map.put(Byte.class, Byte.TYPE);
        unboxed_class_map.put(Short.class, Short.TYPE);
        unboxed_class_map.put(Character.class, Character.TYPE);
        unboxed_class_map.put(Integer.class, Integer.TYPE);
        unboxed_class_map.put(Long.class, Long.TYPE);
        unboxed_class_map.put(Float.class, Float.TYPE);
        unboxed_class_map.put(Double.class, Double.TYPE);
        unboxed_class_map.put(Boolean.TYPE, Boolean.TYPE);
        unboxed_class_map.put(Byte.TYPE, Byte.TYPE);
        unboxed_class_map.put(Short.TYPE, Short.TYPE);
        unboxed_class_map.put(Character.TYPE, Character.TYPE);
        unboxed_class_map.put(Integer.TYPE, Integer.TYPE);
        unboxed_class_map.put(Long.TYPE, Long.TYPE);
        unboxed_class_map.put(Float.TYPE, Float.TYPE);
        unboxed_class_map.put(Double.TYPE, Double.TYPE);

        boxed_class_map = new IdentityHashMap<Class<?>, Class<?>>(32);
        boxed_class_map.put(Boolean.TYPE, Boolean.class);
        boxed_class_map.put(Byte.TYPE, Byte.class);
        boxed_class_map.put(Short.TYPE, Short.class);
        boxed_class_map.put(Character.TYPE, Character.class);
        boxed_class_map.put(Integer.TYPE, Integer.class);
        boxed_class_map.put(Long.TYPE, Long.class);
        boxed_class_map.put(Float.TYPE, Float.class);
        boxed_class_map.put(Double.TYPE, Double.class);
        boxed_class_map.put(Boolean.class, Boolean.class);
        boxed_class_map.put(Byte.class, Byte.class);
        boxed_class_map.put(Short.class, Short.class);
        boxed_class_map.put(Character.class, Character.class);
        boxed_class_map.put(Integer.class, Integer.class);
        boxed_class_map.put(Long.class, Long.class);
        boxed_class_map.put(Float.class, Float.class);
        boxed_class_map.put(Double.class, Double.class);
    }
}
