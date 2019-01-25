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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import jetbrick.bean.asm.AsmAccessor;
import jetbrick.util.ExceptionUtils;

/**
 * 代表一个字段.
 *
 * @author Guoqiang Chen
 */
public final class FieldInfo implements Comparable<FieldInfo>, Getter, Setter {
    private final KlassInfo declaringKlass;
    private final Field field;
    private final int offset;

    public static FieldInfo create(Field field) {
        KlassInfo klass = KlassInfo.create(field.getDeclaringClass());
        return klass.getDeclaredField(field);
    }

    protected FieldInfo(KlassInfo declaringKlass, Field field, int offset) {
        this.declaringKlass = declaringKlass;
        this.field = field;
        this.offset = offset;

        JdkReflectionUtils.setAccessible(field);
    }

    public KlassInfo getDeclaringKlass() {
        return declaringKlass;
    }

    public String getName() {
        return field.getName();
    }

    public Field getField() {
        return field;
    }

    public int getOffset() {
        return offset;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Type getGenericType() {
        return field.getGenericType();
    }

    public Class<?> getRawType(KlassInfo declaringKlass) {
        return getRawType(declaringKlass.getType());
    }

    public Class<?> getRawType(Class<?> declaringClass) {
        return TypeResolverUtils.getRawType(field.getGenericType(), declaringClass);
    }

    public Class<?> getRawComponentType(Class<?> declaringClass, int componentIndex) {
        return TypeResolverUtils.getComponentType(field.getGenericType(), declaringClass, componentIndex);
    }

    public Annotation[] getAnnotations() {
        return field.getAnnotations();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }

    public int getModifiers() {
        return field.getModifiers();
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    public boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    @Override
    public Object get(Object object) {
        AsmAccessor accessor = declaringKlass.getAsmAccessor();
        if (accessor == null) {
            try {
                return JdkReflectionUtils.get(field, object);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        } else {
            return accessor.getField(object, offset);
        }
    }

    @Override
    public void set(Object object, Object value) {
        AsmAccessor accessor = declaringKlass.getAsmAccessor();
        if (accessor == null) {
            try {
                JdkReflectionUtils.set(field, object, value);
            } catch (Exception e) {
                throw ExceptionUtils.unchecked(e);
            }
        } else {
            accessor.setField(object, offset, value);
        }
    }

    @Override
    public int compareTo(FieldInfo o) {
        return field.getName().compareTo(o.field.getName());
    }

    @Override
    public String toString() {
        return field.toString();
    }
}
