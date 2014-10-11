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
package jetbrick.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import jetbrick.bean.asm.AsmAccessor;
import jetbrick.util.ExceptionUtils;
import jetbrick.util.IdentifiedNameUtils;

/**
 * 代表一个方法.
 *
 * @author Guoqiang Chen
 */
public final class MethodInfo extends Executable implements Invoker, Comparable<MethodInfo> {
    private final KlassInfo declaringKlass;
    private final Method method;
    private final int offset;

    public static MethodInfo create(Method method) {
        KlassInfo klass = KlassInfo.create(method.getDeclaringClass());
        return klass.getDeclaredMethod(method);
    }

    protected MethodInfo(KlassInfo declaringKlass, Method method, int offset) {
        this.declaringKlass = declaringKlass;
        this.method = method;
        this.offset = offset;
        method.setAccessible(true);
    }

    @Override
    public KlassInfo getDeclaringKlass() {
        return declaringKlass;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getParameterCount() {
        return method.getParameterTypes().length;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Type[] getGenericParameterTypes() {
        return method.getGenericParameterTypes();
    }

    @Override
    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public Type getGenericReturnType() {
        return method.getGenericReturnType();
    }

    public Class<?> getRawReturnType(KlassInfo declaringKlass) {
        return getRawReturnType(declaringKlass.getType());
    }

    public Class<?> getRawReturnType(Class<?> declaringClass) {
        return TypeResolverUtils.getRawType(method.getGenericReturnType(), declaringClass);
    }

    public Class<?> getRawReturnComponentType(Class<?> declaringClass, int componentIndex) {
        return TypeResolverUtils.getComponentType(method.getGenericReturnType(), declaringClass, componentIndex);
    }

    @Override
    public Annotation[] getAnnotations() {
        return method.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    @Override
    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    @Override
    public Annotation[][] getParameterAnnotations() {
        return method.getParameterAnnotations();
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public boolean isReadMethod() {
        Class<?> resultType = method.getReturnType();
        if (method.getParameterTypes().length == 0 && resultType != Void.TYPE) {
            String name = method.getName();
            if (name.length() > 3 && name.startsWith("get")) {
                return true;
            }
            if (name.length() > 2 && name.startsWith("is")) {
                return resultType == Boolean.TYPE || resultType == Boolean.class;
            }
        }
        return false;
    }

    public boolean isWriteMethod() {
        Class<?> resultType = method.getReturnType();
        if (method.getParameterTypes().length == 1 && resultType == Void.TYPE) {
            String name = method.getName();
            if (name.length() > 3 && name.startsWith("set")) {
                return true;
            }
        }
        return false;
    }

    public String getPropertyName() {
        if (!isReadMethod() && !isWriteMethod()) {
            throw new IllegalStateException("method is not a getter/setter: " + this.toString());
        }

        String name = method.getName();
        if (name.startsWith("get")) {
            name = name.substring(3);
        } else if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("set")) {
            name = name.substring(3);
        }
        return IdentifiedNameUtils.decapitalize(name);
    }

    @Override
    public Object invoke(Object object, Object... args) {
        AsmAccessor accessor = declaringKlass.getAsmAccessor();
        if (accessor == null) {
            try {
                return method.invoke(object, args);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        } else {
            return accessor.invoke(object, offset, args);
        }
    }

    @Override
    public int compareTo(MethodInfo o) {
        return getSignature().compareTo(o.getSignature());
    }
}
