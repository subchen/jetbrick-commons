/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jetbrick.bean.Filters.FieldFilter;
import jetbrick.bean.Filters.MethodFilter;
import jetbrick.bean.asm.AsmAccessor;
import jetbrick.bean.asm.AsmFactory;
import jetbrick.util.concurrent.ConcurrentInitializer;
import jetbrick.util.concurrent.LazyInitializer;

/**
 * 代表一个 Class.
 *
 * @author Guoqiang Chen
 */
public final class KlassInfo {
    private static final ConcurrentHashMap<Class<?>, KlassInfo> pool = new ConcurrentHashMap<Class<?>, KlassInfo>(128);

    /**
     * 将 Class 对象转成 KlassInfo 对象 (有缓存).
     *
     * @param clazz - 原始对象
     * @return KlassInfo 对象
     */
    public static KlassInfo create(final Class<?> clazz) {
        KlassInfo klass = pool.get(clazz);
        if (klass == null) {
            klass = new KlassInfo(clazz);
            KlassInfo old = pool.putIfAbsent(clazz, klass);
            if (old != null) {
                klass = old;
            }
        }
        return klass;
    }

    private final Class<?> clazz;

    private KlassInfo(Class<?> type) {
        this.clazz = type;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getType() {
        return (Class<T>) clazz;
    }

    public String getName() {
        return clazz.getName();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public KlassInfo getSuperKlass() {
        Class<?> superKlass = clazz.getSuperclass();
        return (superKlass == null) ? null : KlassInfo.create(superKlass);
    }

    public List<KlassInfo> getInterfaces() {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            return Collections.emptyList();
        }
        List<KlassInfo> results = new ArrayList<KlassInfo>(interfaces.length);
        for (Class<?> intf : interfaces) {
            results.add(KlassInfo.create(intf));
        }
        return results;
    }

    // ------------------------------------------------------------------
    public Annotation[] getAnnotations() {
        return clazz.getAnnotations();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    // ------------------------------------------------------------------
    public int getModifiers() {
        return clazz.getModifiers();
    }

    public boolean isInterface() {
        return Modifier.isInterface(getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
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

    // ------------------------------------------------------------------
    private final ConcurrentInitializer<List<ConstructorInfo>> declaredConstructorsGetter = new LazyInitializer<List<ConstructorInfo>>() {
        @Override
        protected List<ConstructorInfo> initialize() {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors.length == 0) {
                return Collections.emptyList();
            } else {
                ArrayList<ConstructorInfo> results = new ArrayList<ConstructorInfo>(constructors.length);
                for (int i = 0; i < constructors.length; i++) {
                    results.add(new ConstructorInfo(KlassInfo.this, constructors[i], i));
                }
                results.trimToSize();
                return Collections.unmodifiableList(results);
            }
        }
    };

    public List<ConstructorInfo> getDeclaredConstructors() {
        return declaredConstructorsGetter.get();
    }

    /**
     * 根据目标参数类型，查找完全匹配的构造函数.
     *
     * @param parameterTypes - 匹配的构造函数参数
     * @return 找到的构造函数对象，找不到返回 null.
     */
    public ConstructorInfo getDeclaredConstructor(Class<?>... parameterTypes) {
        return ExecutableUtils.getExecutable(declaredConstructorsGetter.get(), null, parameterTypes);
    }

    /**
     * 根据目标参数类型，查找最佳匹配的构造函数.
     *
     * @param parameterTypes - 匹配的构造函数参数
     * @return 找到的构造函数对象，找不到返回 null.
     */
    public ConstructorInfo searchDeclaredConstructor(Class<?>... parameterTypes) {
        ConstructorInfo constructor = ExecutableUtils.getExecutable(declaredConstructorsGetter.get(), null, parameterTypes);
        if (constructor == null) {
            constructor = ExecutableUtils.searchExecutable(declaredConstructorsGetter.get(), null, parameterTypes);
        }
        return constructor;
    }

    public ConstructorInfo getDeclaredConstructor(Constructor<?> constructor) {
        for (ConstructorInfo info : declaredConstructorsGetter.get()) {
            if (info.getConstructor() == constructor) {
                return info;
            }
        }
        return null;
    }

    public ConstructorInfo getDefaultConstructor() {
        for (ConstructorInfo info : declaredConstructorsGetter.get()) {
            if (info.isDefault()) {
                return info;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    private final ConcurrentInitializer<List<MethodInfo>> declaredMethodsGetter = new LazyInitializer<List<MethodInfo>>() {
        @Override
        protected List<MethodInfo> initialize() {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods.length == 0) {
                return Collections.emptyList();
            } else {
                ArrayList<MethodInfo> results = new ArrayList<MethodInfo>(methods.length);
                for (int i = 0; i < methods.length; i++) {
                    results.add(new MethodInfo(KlassInfo.this, methods[i], i));
                }
                results.trimToSize();
                return Collections.unmodifiableList(results);
            }
        }
    };

    public List<MethodInfo> getDeclaredMethods() {
        return declaredMethodsGetter.get();
    }

    public List<MethodInfo> getDeclaredMethods(MethodFilter filter) {
        List<MethodInfo> methods = declaredMethodsGetter.get();
        if (methods.size() == 0) {
            return methods;
        }
        List<MethodInfo> results = new ArrayList<MethodInfo>(methods.size());
        for (MethodInfo method : methods) {
            if (filter.accept(method)) {
                results.add(method);
            }
        }
        return results;
    }

    public MethodInfo getDeclaredMethod(String name, Class<?>... parameterTypes) {
        return ExecutableUtils.getExecutable(declaredMethodsGetter.get(), name, parameterTypes);
    }

    public MethodInfo getDeclaredMethod(Method method) {
        for (MethodInfo info : declaredMethodsGetter.get()) {
            if (info.getMethod() == method) {
                return info;
            }
        }
        return null;
    }

    private final ConcurrentInitializer<List<MethodInfo>> methodsGetter = new LazyInitializer<List<MethodInfo>>() {
        @Override
        protected List<MethodInfo> initialize() {
            KlassInfo klass = KlassInfo.this;
            List<MethodInfo> declaredMethods = klass.getDeclaredMethods();

            ArrayList<MethodInfo> results = new ArrayList<MethodInfo>(declaredMethods.size() + 16);

            // 1. add interfaces
            for (KlassInfo parent : klass.getInterfaces()) {
                for (MethodInfo method : parent.getMethods()) {
                    results.add(method);
                }
            }

            // 2. add super class
            KlassInfo parent = klass.getSuperKlass();
            if (parent != null) {
                for (MethodInfo method : parent.getMethods()) {
                    // 只加入 super class 中 public, 非 static 的  (这些方法一般是被认为可继承的)
                    if (method.isPublic() && !method.isStatic()) {
                        results.add(method);
                    }
                }
            }

            // 3. add self
            results.addAll(declaredMethods);

            results.trimToSize();
            return Collections.unmodifiableList(results);
        }
    };

    public List<MethodInfo> getMethods() {
        return methodsGetter.get();
    }

    public List<MethodInfo> getMethods(MethodFilter filter) {
        List<MethodInfo> methods = methodsGetter.get();
        if (methods.size() == 0) {
            return methods;
        }
        List<MethodInfo> results = new ArrayList<MethodInfo>(methods.size());
        for (MethodInfo method : methods) {
            if (filter.accept(method)) {
                results.add(method);
            }
        }
        return results;
    }

    /**
     * 根据目标参数类型，查找完全匹配的方法.
     *
     * @param parameterTypes - 匹配的方法参数
     * @return 找到的方法对象，找不到返回 null.
     */
    public MethodInfo getMethod(String name, Class<?>... parameterTypes) {
        return ExecutableUtils.getExecutable(methodsGetter.get(), name, parameterTypes);
    }

    /**
     * 根据目标参数类型，查找最佳匹配的方法.
     *
     * @param parameterTypes - 匹配的方法参数
     * @return 找到的方法对象，找不到返回 null.
     */
    public MethodInfo searchMethod(String name, Class<?>... parameterTypes) {
        MethodInfo method = ExecutableUtils.getExecutable(methodsGetter.get(), name, parameterTypes);
        if (method == null) {
            method = ExecutableUtils.searchExecutable(methodsGetter.get(), name, parameterTypes);
        }
        return method;
    }

    // ------------------------------------------------------------------
    private final ConcurrentInitializer<List<FieldInfo>> declaredFieldsGetter = new LazyInitializer<List<FieldInfo>>() {
        @Override
        protected List<FieldInfo> initialize() {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0) {
                return Collections.emptyList();
            }
            ArrayList<FieldInfo> results = new ArrayList<FieldInfo>(fields.length);
            for (int i = 0; i < fields.length; i++) {
                results.add(new FieldInfo(KlassInfo.this, fields[i], i));
            }
            results.trimToSize();
            return Collections.unmodifiableList(results);
        }
    };

    public List<FieldInfo> getDeclaredFields() {
        return declaredFieldsGetter.get();
    }

    public List<FieldInfo> getDeclaredFields(FieldFilter filter) {
        List<FieldInfo> fields = declaredFieldsGetter.get();
        if (fields.size() == 0) {
            return fields;
        }
        List<FieldInfo> results = new ArrayList<FieldInfo>(fields.size());
        for (FieldInfo field : fields) {
            if (filter.accept(field)) {
                results.add(field);
            }
        }
        return results;
    }

    public FieldInfo getDeclaredField(String name) {
        for (FieldInfo field : declaredFieldsGetter.get()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public FieldInfo getDeclaredField(Field field) {
        for (FieldInfo info : declaredFieldsGetter.get()) {
            if (info.getField() == field) {
                return info;
            }
        }
        return null;
    }

    private final ConcurrentInitializer<List<FieldInfo>> fieldsGetter = new LazyInitializer<List<FieldInfo>>() {
        @Override
        protected List<FieldInfo> initialize() {
            KlassInfo klass = KlassInfo.this;
            List<FieldInfo> declaredFields = klass.getDeclaredFields();

            ArrayList<FieldInfo> results = new ArrayList<FieldInfo>(declaredFields.size() + 8);
            results.addAll(declaredFields);

            for (KlassInfo parent : klass.getInterfaces()) {
                for (FieldInfo field : parent.getFields()) {
                    // 只加入 super interface 中 public， 非 static 的字段 (这些字段一般是被认为可继承的)
                    if (field.isPublic() && !field.isStatic()) {
                        results.add(field);
                    }
                }
            }

            KlassInfo parent = klass.getSuperKlass();
            if (parent != null) {
                for (FieldInfo field : parent.getFields()) {
                    // 只加入 super class 中 public， 非 static 的字段 (这些字段一般是被认为可继承的)
                    if (field.isPublic() && !field.isStatic()) {
                        results.add(field);
                    }
                }
            }

            results.trimToSize();
            return Collections.unmodifiableList(results);
        }
    };

    public List<FieldInfo> getFields() {
        return fieldsGetter.get();
    }

    public List<FieldInfo> getFields(FieldFilter filter) {
        List<FieldInfo> fields = fieldsGetter.get();
        if (fields.size() == 0) {
            return fields;
        }
        List<FieldInfo> results = new ArrayList<FieldInfo>(fields.size());
        for (FieldInfo field : fields) {
            if (filter.accept(field)) {
                results.add(field);
            }
        }
        return results;
    }

    public FieldInfo getField(String name) {
        for (FieldInfo field : fieldsGetter.get()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    private final ConcurrentInitializer<List<PropertyInfo>> propertiesGetter = new LazyInitializer<List<PropertyInfo>>() {
        @Override
        protected List<PropertyInfo> initialize() {
            List<MethodInfo> methods = methodsGetter.get();
            Map<String, PropertyInfo> map = new HashMap<String, PropertyInfo>(methods.size());
            for (MethodInfo method : methods) {
                if (method.isStatic() || !method.isPublic()) continue; // 属性必须是 public 的

                if (method.isReadMethod()) {
                    String name = method.getPropertyName();
                    PropertyInfo propertyInfo = map.get(name);
                    if (propertyInfo == null) {
                        propertyInfo = new PropertyInfo(KlassInfo.this, name);
                        map.put(name, propertyInfo);
                    }
                    propertyInfo.setGetter(method);
                } else if (method.isWriteMethod()) {
                    String name = method.getPropertyName();
                    PropertyInfo propertyInfo = map.get(name);
                    if (propertyInfo == null) {
                        propertyInfo = new PropertyInfo(KlassInfo.this, name);
                        map.put(name, propertyInfo);
                    }
                    propertyInfo.setSetter(method);
                }
            }
            if (map.size() == 0) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(new ArrayList<PropertyInfo>(map.values()));
        }
    };

    public List<PropertyInfo> getProperties() {
        return propertiesGetter.get();
    }

    public PropertyInfo getProperty(String name) {
        for (PropertyInfo prop : propertiesGetter.get()) {
            if (prop.getName().equals(name)) {
                return prop;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    private AsmAccessor asmAccessor; // ASM 生成的类的实例
    private int asmCallNumber = 0; //  反射调用计数器，超过阈值，则使用 ASM 字节码增强技术

    protected AsmAccessor getAsmAccessor() {
        if (asmAccessor == null) {
            if (asmCallNumber >= AsmFactory.getThreshold()) {
                asmAccessor = AsmFactory.generateAccessor(this);
            } else {
                asmCallNumber++;
            }
        }
        return asmAccessor;
    }

    /**
     * 调用默认的构造函数生成对象实例.
     *
     * @return 生成的对象实例.
     * @throws IllegalStateException - 如果找不到默认的构造函数或者没有访问权限，那么抛出该异常.
     */
    public Object newInstance() throws IllegalStateException {
        AsmAccessor accessor = getAsmAccessor();
        if (accessor == null) {
            ConstructorInfo ctor = getDefaultConstructor();
            if (ctor != null) {
                return ctor.newInstance();
            }
            throw new IllegalStateException("No default constructor");
        } else {
            return accessor.newInstance();
        }
    }

    // ------------------------------------------------------------------
    @Override
    public String toString() {
        return clazz.toString();
    }
}
