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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import jetbrick.util.ArrayUtils;

/**
 * 根据类型定义，获取泛型信息.
 *
 * @author Guoqiang Chen
 */
public final class TypeResolverUtils {

    /**
     * Returns raw class for given <code>type</code>. Use this method with both
     * regular and generic types.
     *
     * @param type - the type to convert
     * @return the closest class representing the given <code>type</code>
     * @see #getRawType(java.lang.reflect.Type, Class)
     */
    public static Class<?> getRawType(Type type) {
        return getRawType(type, null);
    }

    /**
     * Returns raw class for given <code>type</code> when implementation class is known
     * and it makes difference.
     *
     * @param type - given type
     * @param implClass - implementation clas
     * @return raw class
     * @see #resolveVariable(java.lang.reflect.TypeVariable, Class)
     */
    public static Class<?> getRawType(Type type, Class<?> implClass) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return getRawType(pType.getRawType(), implClass);
        }
        if (type instanceof WildcardType) {
            WildcardType wType = (WildcardType) type;

            Type[] lowerTypes = wType.getLowerBounds();
            if (lowerTypes.length > 0) {
                return getRawType(lowerTypes[0], implClass);
            }

            Type[] upperTypes = wType.getUpperBounds();
            if (upperTypes.length != 0) {
                return getRawType(upperTypes[0], implClass);
            }

            return Object.class;
        }
        if (type instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> rawType = getRawType(genericComponentType, implClass);

            return Array.newInstance(rawType, 0).getClass();
        }
        if (type instanceof TypeVariable) {
            TypeVariable<?> varType = (TypeVariable<?>) type;
            if (implClass != null) {
                Type resolvedType = resolveVariable(varType, implClass);
                if (resolvedType != null) {
                    return getRawType(resolvedType, null);
                }
            }
            Type[] boundsTypes = varType.getBounds();
            if (boundsTypes.length == 0) {
                return Object.class;
            }
            return getRawType(boundsTypes[0], implClass);
        }
        return null;
    }

    /**
     * Resolves <code>TypeVariable</code> with given implementation class.
     *
     * @param variable - variable
     * @param implClass - implementation class
     * @return resolved type
     */
    public static Type resolveVariable(TypeVariable<?> variable, Class<?> implClass) {
        Class<?> rawType = getRawType(implClass, null);

        int index = ArrayUtils.indexOf(rawType.getTypeParameters(), variable);
        if (index >= 0) {
            return variable;
        }

        Class<?>[] interfaces = rawType.getInterfaces();
        Type[] genericInterfaces = rawType.getGenericInterfaces();

        for (int i = 0; i <= interfaces.length; i++) {
            Class<?> rawInterface;
            if (i < interfaces.length) {
                rawInterface = interfaces[i];
            } else {
                rawInterface = rawType.getSuperclass();
                if (rawInterface == null) {
                    continue;
                }
            }
            Type resolved = resolveVariable(variable, rawInterface);
            if (resolved instanceof Class || resolved instanceof ParameterizedType) {
                return resolved;
            }

            if (resolved instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) resolved;
                index = ArrayUtils.indexOf(rawInterface.getTypeParameters(), typeVariable);
                if (index < 0) {
                    throw new IllegalArgumentException("Can't resolve type variable:" + typeVariable);
                }
                Type type = i < genericInterfaces.length ? genericInterfaces[i] : rawType.getGenericSuperclass();
                if (type instanceof Class) {
                    return Object.class;
                }
                if (type instanceof ParameterizedType) {
                    return ((ParameterizedType) type).getActualTypeArguments()[index];
                }
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
        return null;
    }

    public static Class<?> getComponentType(Type type) {
        return getComponentType(type, null, -1);
    }

    public static Class<?> getComponentType(Type type, Class<?> implClass) {
        return getComponentType(type, implClass, -1);
    }

    public static Class<?> getComponentType(Type type, int index) {
        return getComponentType(type, null, index);
    }

    /**
     * Returns the component type of the given type.
     * Returns <code>null</code> if given type does not have a single
     * component type. For example the following types all have the
     * component-type MyClass:
     * <ul>
     * <li>MyClass[]</li>
     * <li>List&lt;MyClass&gt;</li>
     * <li>Foo&lt;? extends MyClass&gt;</li>
     * <li>Bar&lt;? super MyClass&gt;</li>
     * <li>&lt;T extends MyClass&gt; T[]</li>
     * </ul>
     *
     * Index represents the index of component type, when class supports more then one.
     * For example, <code>Map&lt;A, B&gt;</code> has 2 component types. If index is 0 or positive,
     * than it represents order of component type. If the value is negative, then it represents
     * component type counted from the end! Therefore, the default value of <code>-1</code>
     * always returns the <b>last</b> component type.
     *
     * @param type - type
     * @param implClass - implementation class
     * @param index - index of component type
     * @return component type
     */
    public static Class<?> getComponentType(Type type, Class<?> implClass, int index) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                return clazz.getComponentType();
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] generics = pt.getActualTypeArguments();
            if (index < 0) {
                index = generics.length + index;
            }
            if (index < generics.length) {
                return getRawType(generics[index], implClass);
            }
        } else if (type instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) type;
            return getRawType(gat.getGenericComponentType(), implClass);
        }
        return null;
    }

    /**
     * @see #getComponentType(java.lang.reflect.Type)
     */
    public static Class<?> getGenericSupertype(Class<?> type) {
        return getComponentType(type.getGenericSuperclass());
    }

    /**
     * Returns generic supertype for given class and 0-based index.
     * @see #getComponentType(java.lang.reflect.Type, int)
     */
    public static Class<?> getGenericSupertype(Class<?> type, int index) {
        return getComponentType(type.getGenericSuperclass(), null, index);
    }

}
