/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
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
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import jetbrick.bean.asm.AsmAccessor;
import jetbrick.util.ExceptionUtils;

/**
 * 代表一个构造函数.
 *
 * @author Guoqiang Chen
 */
public final class ConstructorInfo extends Executable implements Creater, Comparable<ConstructorInfo> {
    private final KlassInfo declaringKlass;
    private final Constructor<?> constructor;
    private final int offset;

    /**
     * 将 Constructor 对象转成 ConstructorInfo 对象.
     * @param constructor - 原始对象
     * @return ConstructorInfo 对象
     */
    public static ConstructorInfo create(Constructor<?> constructor) {
        KlassInfo klass = KlassInfo.create(constructor.getDeclaringClass());
        return klass.getDeclaredConstructor(constructor);
    }

    protected ConstructorInfo(KlassInfo declaringKlass, Constructor<?> constructor, int offset) {
        this.declaringKlass = declaringKlass;
        this.constructor = constructor;
        this.offset = offset;
        constructor.setAccessible(true);
    }

    @Override
    public KlassInfo getDeclaringKlass() {
        return declaringKlass;
    }

    @Override
    public String getName() {
        return "<init>";
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterTypes().length;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Type[] getGenericParameterTypes() {
        return constructor.getGenericParameterTypes();
    }

    public boolean isDefault() {
        return constructor.getParameterTypes().length == 0;
    }

    @Override
    public boolean isVarArgs() {
        return constructor.isVarArgs();
    }

    @Override
    public Annotation[] getAnnotations() {
        return constructor.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return constructor.getAnnotation(annotationClass);
    }

    @Override
    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return constructor.isAnnotationPresent(annotationClass);
    }

    @Override
    public Annotation[][] getParameterAnnotations() {
        return constructor.getParameterAnnotations();
    }

    @Override
    public int getModifiers() {
        return constructor.getModifiers();
    }

    @Override
    public Object newInstance(Object... args) {
        AsmAccessor accessor = declaringKlass.getASMAccessor();
        if (accessor == null) {
            try {
                return constructor.newInstance(args);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        } else {
            return accessor.newInstance(offset, args);
        }
    }

    @Override
    public int compareTo(ConstructorInfo o) {
        return getSignature().compareTo(o.getSignature());
    }
}
