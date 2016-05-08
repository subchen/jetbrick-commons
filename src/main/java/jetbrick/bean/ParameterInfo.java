/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import jetbrick.asm.ClassReader;
import jetbrick.asm.ClassVisitor;
import jetbrick.asm.Label;
import jetbrick.asm.MethodVisitor;
import jetbrick.asm.Opcodes;
import jetbrick.util.ClassLoaderUtils;

/**
 * 表示一个方法参数或者构造函数参数.
 *
 * @author Guoqiang Chen
 */
public final class ParameterInfo {
    private final Executable declaringExecutable;
    private final Class<?> type;
    private final Type genericType;
    private final Annotation[] annotations;
    private final int offset;
    private String name;

    protected ParameterInfo(Executable declaringExecutable, Class<?> type, Type genericType, Annotation[] annotations, int offset) {
        this.declaringExecutable = declaringExecutable;
        this.type = type;
        this.genericType = genericType;
        this.annotations = annotations;
        this.offset = offset;
    }

    public String getName() {
        if (name == null) {
            KlassInfo declaringklass = declaringExecutable.getDeclaringKlass();
            synchronized (declaringklass) {
                receiveParameterNames(declaringklass);
                if (name == null) {
                    name = "arg".concat(String.valueOf(offset));
                }
            }
        }
        return name;
    }

    public Executable getDeclaringExecutable() {
        return declaringExecutable;
    }

    public int getOffset() {
        return offset;
    }

    public Class<?> getType() {
        return type;
    }

    public Type getGenericType() {
        return genericType;
    }

    public Class<?> getRawType(KlassInfo declaringKlass) {
        return getRawType(declaringKlass.getType());
    }

    public Class<?> getRawType(Class<?> declaringClass) {
        return TypeResolverUtils.getRawType(genericType, declaringClass);
    }

    public Class<?> getRawComponentType(Class<?> declaringClass, int componentIndex) {
        return TypeResolverUtils.getComponentType(genericType, declaringClass, componentIndex);
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass == annotation.annotationType()) {
                return (T) annotation;
            }
        }
        return null;
    }

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return (getAnnotation(annotationClass) != null);
    }

    @Override
    public String toString() {
        return type.getName() + " " + (name == null ? "arg" + String.valueOf(offset) : name);
    }

    // 使用 ASM 获取参数名称
    private static void receiveParameterNames(final KlassInfo declaringklass) {
        if (declaringklass.getType().getClassLoader() == null) {
            // We can not find parameter name for class which is in JDK
            return;
        }

        ClassReader cr = null;
        try {
            InputStream stream = ClassLoaderUtils.getClassAsStream(declaringklass.getType());
            cr = new ClassReader(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cr.accept(new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final MethodInfo method = searchMethod(declaringklass, name, desc);
                if (method == null) {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM5, mv) {
                    List<ParameterInfo> parameters = method.getParameters();
                    boolean isStatic = method.isStatic();

                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        int offset = isStatic ? index : index - 1;
                        if (offset >= 0 && offset < parameters.size()) {
                            parameters.get(offset).name = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                    int visitParameterIndex = 0; // JDK8 parameter name 是按照循序存储的，这里需要一个计数器

                    @Override
                    public void visitParameter(String name, int access) {
                        parameters.get(visitParameterIndex++).name = name;
                        super.visitParameter(name, access);
                    }
                };
            }

            private MethodInfo searchMethod(KlassInfo declaringklass, String name, String desc) {
                if ("<cinit>".equals(name)) return null;
                if ("<init>".equals(name)) return null;

                jetbrick.asm.Type[] argumentTypes = jetbrick.asm.Type.getArgumentTypes(desc);
                for (MethodInfo method : declaringklass.getDeclaredMethods()) {
                    if (method.getName().equals(name) && argumentTypes.length == method.getParameterCount()) {
                        Class<?>[] types = method.getParameterTypes();
                        boolean matched = true;
                        for (int i = 0; i < argumentTypes.length; i++) {
                            if (!jetbrick.asm.Type.getType(types[i]).equals(argumentTypes[i])) {
                                matched = false;
                                break;
                            }
                        }
                        if (matched) {
                            return method;
                        }
                    }
                }
                return null;
            }
        }, ClassReader.SKIP_FRAMES);
    }
}
