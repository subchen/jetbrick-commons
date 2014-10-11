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

/**
 * 代表一个对象的 public 属性 (Getter, Setter).
 * 
 * @author Guoqiang Chen
 */
public final class PropertyInfo implements Getter, Setter {
    private final KlassInfo declaringKlass;
    private final String name;
    private MethodInfo getter;
    private MethodInfo setter;

    protected PropertyInfo(KlassInfo declaringKlass, String name) {
        this.declaringKlass = declaringKlass;
        this.name = name;
    }

    public KlassInfo getDeclaringKlass() {
        return declaringKlass;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        if (getter != null) {
            return getter.getReturnType();
        }
        if (setter != null) {
            return setter.getParameterTypes()[0];
        }
        throw new IllegalStateException("Invalid PropertyInfo: " + toString());
    }

    public Class<?> getRawType(KlassInfo declaringKlass) {
        return getRawType(declaringKlass.getType());
    }

    public Class<?> getRawType(Class<?> declaringClass) {
        if (getter != null) {
            return getter.getRawReturnType(declaringClass);
        }
        if (setter != null) {
            return setter.getParameters().get(0).getRawType(declaringClass);
        }
        throw new IllegalStateException("Invalid PropertyInfo: " + toString());
    }

    public Class<?> getRawComponentType(Class<?> declaringClass, int componentIndex) {
        if (getter != null) {
            return getter.getRawReturnComponentType(declaringClass, componentIndex);
        }
        if (setter != null) {
            return setter.getParameters().get(0).getRawComponentType(declaringClass, componentIndex);
        }
        throw new IllegalStateException("Invalid PropertyInfo: " + toString());
    }

    public MethodInfo getGetter() {
        return getter;
    }

    public MethodInfo getSetter() {
        return setter;
    }

    protected void setGetter(MethodInfo getter) {
        this.getter = getter;
    }

    protected void setSetter(MethodInfo setter) {
        this.setter = setter;
    }

    public boolean readable() {
        return getter != null;
    }

    public boolean writable() {
        return setter != null;
    }

    @Override
    public Object get(Object object) {
        if (getter == null) {
            throw new IllegalStateException("Property is not readable: " + name);
        }
        return getter.invoke(object);
    }

    @Override
    public void set(Object object, Object value) {
        if (setter == null) {
            throw new IllegalStateException("Property is not writable: " + name);
        }
        setter.invoke(object, value);
    }
}
