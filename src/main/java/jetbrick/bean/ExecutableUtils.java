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

import java.util.List;
import jetbrick.util.ClassUtils;

public final class ExecutableUtils {

    /**
     * 查找完全匹配的方法或者构造函数.
     *
     * @param executables - 带查找的 list
     * @param name - 方法名称(如果查找构造函数，那么name可以是null)
     * @param parameterTypes － 方法或者构造函数参数(完全匹配)
     * @return 找到的方法或者构造函数，找不到返回 null.
     */
    public static <T extends Executable> T getExecutable(List<T> executables, String name, Class<?>... parameterTypes) {
        for (T info : executables) {
            if (name == null || info.getName().equals(name)) {
                Class<?>[] types = info.getParameterTypes();
                if (parameterTypes.length == types.length) {
                    boolean match = true;
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (types[i] != parameterTypes[i]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        return info;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查找最佳匹配的方法或者构造函数。
     *
     * @param executables - 带查找的 list
     * @param name - 方法名称(如果查找构造函数，那么name可以是null)
     * @param parameterTypes － 方法或者构造函数参数(支持类型转换)
     * @return 找到的方法或者构造函数，找不到返回 null.
     */
    public static <T extends Executable> T searchExecutable(List<T> executables, String name, Class<?>... parameterTypes) {
        T best = null;
        Class<?>[] bestParametersTypes = null;

        for (T execute : executables) {
            if (name != null && !execute.getName().equals(name)) continue;

            Class<?>[] types = execute.getParameterTypes();
            if (isParameterTypesCompatible(types, parameterTypes, execute.isVarArgs(), false)) {
                // 可能有多个方法与实际参数类型兼容。采用就近兼容原则。
                if (best == null) {
                    best = execute;
                    bestParametersTypes = types;
                } else if (best.isVarArgs() && (!execute.isVarArgs())) {
                    best = execute; // 不可变参数的函数优先
                    bestParametersTypes = types;
                } else if ((!best.isVarArgs()) && execute.isVarArgs()) {
                    // no change
                } else {
                    if (isParameterTypesCompatible(bestParametersTypes, types, best.isVarArgs(), execute.isVarArgs())) {
                        best = execute;
                        bestParametersTypes = types;
                    }
                }
            }
        }
        return best;
    }

    /**
     * 判断参数列表是否兼容, 支持可变参数.
     *
     * @param lhs - left operand
     * @param rhs - right operand
     * @param lhsVarArgs － lhs 是否是可变参数
     * @param rhsVarArgs － rhs 是否是可变参数
     * @return 是否兼容
     */
    public static boolean isParameterTypesCompatible(Class<?>[] lhs, Class<?>[] rhs, boolean lhsVarArgs, boolean rhsVarArgs) {
        if (lhs == null) {
            return rhs == null || rhs.length == 0;
        }
        if (rhs == null) {
            return lhs.length == 0;
        }

        if (lhsVarArgs && rhsVarArgs) {
            if (lhs.length != rhs.length) {
                return false;
            }
            //校验前面的固定参数
            for (int i = 0; i < lhs.length - 1; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
            // 校验最后一个可变参数
            Class<?> c1 = lhs[lhs.length - 1].getComponentType();
            Class<?> c2 = rhs[rhs.length - 1].getComponentType();
            if (!ClassUtils.isAssignable(c1, c2)) {
                return false;
            }
        } else if (lhsVarArgs) {
            if (lhs.length - 1 > rhs.length) {
                return false;
            }
            //校验前面的固定参数
            for (int i = 0; i < lhs.length - 1; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
            // 校验最后一个可变参数
            Class<?> varType = lhs[lhs.length - 1].getComponentType();
            for (int i = lhs.length - 1; i < rhs.length; i++) {
                if (!ClassUtils.isAssignable(varType, rhs[i])) {
                    return false;
                }
            }
        } else {
            if (lhs.length != rhs.length) {
                return false;
            }
            for (int i = 0; i < lhs.length; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
