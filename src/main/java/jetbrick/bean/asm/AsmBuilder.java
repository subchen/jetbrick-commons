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
package jetbrick.bean.asm;

import java.lang.reflect.Modifier;
import java.util.List;

import jetbrick.asm.ClassWriter;
import jetbrick.asm.Label;
import jetbrick.asm.MethodVisitor;
import jetbrick.asm.Type;
import jetbrick.bean.ConstructorInfo;
import jetbrick.bean.Executable;
import jetbrick.bean.FieldInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;

import static jetbrick.asm.Opcodes.*;

final class AsmBuilder {
    private static final String SUN_MAGIC_ACCESSOR_KLASS = "sun/reflect/MagicAccessorImpl";
    private static final String FIELD_EXPECTED_CONSTRUCTOR_ARGUMENT_LENGTHS = "ctors";
    private static final String FIELD_EXPECTED_METHOD_ARGUMENT_LENGTHS = "methods";
    private static final String METHOD_CHECK_ARGUMENTS = "checkArguments";

    private final ClassWriter cw;
    private final String generatedKlassNameInternal;
    private final String delegateKlassNameInternal;

    public AsmBuilder(String generatedKlassName, String delegateKlassName, Class<?> interfaceKlass) {
        generatedKlassNameInternal = generatedKlassName.replace('.', '/');
        delegateKlassNameInternal = delegateKlassName.replace('.', '/');

        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        String[] interfaces = new String[] { interfaceKlass.getName().replace('.', '/') };
        cw.visit(V1_1, ACC_PUBLIC + ACC_SUPER + ACC_FINAL, generatedKlassNameInternal, null, SUN_MAGIC_ACCESSOR_KLASS, interfaces);
    }

    public static byte[] create(String generatedKlassName, KlassInfo delegateKlass) {
        AsmBuilder builder = new AsmBuilder(generatedKlassName, delegateKlass.getName(), AsmAccessor.class);
        builder.insertArgumentsLengthField(delegateKlass.getDeclaredConstructors(), delegateKlass.getDeclaredMethods());
        builder.insertCheckArgumentsMethod();
        builder.insertConstructor();
        builder.insertNewInstance();
        builder.insertNewInstance(delegateKlass.getDeclaredConstructors());
        builder.insertInvoke(delegateKlass.getDeclaredMethods());
        builder.insertGetField(delegateKlass.getDeclaredFields());
        builder.insertSetField(delegateKlass.getDeclaredFields());
        return builder.asByteCode();
    }

    public void insertArgumentsLengthField(List<? extends Executable> constructors, List<? extends Executable> methods) {
        cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, FIELD_EXPECTED_CONSTRUCTOR_ARGUMENT_LENGTHS, "[I", null, null).visitEnd();
        cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, FIELD_EXPECTED_METHOD_ARGUMENT_LENGTHS, "[I", null, null).visitEnd();

        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();

        int size;

        // constructors
        size = constructors.size();
        pushIntValue(mv, size);
        mv.visitIntInsn(NEWARRAY, T_INT);
        for (int i = 0; i < size; i++) {
            mv.visitInsn(DUP);
            pushIntValue(mv, i);
            pushIntValue(mv, constructors.get(i).getParameterCount());
            mv.visitInsn(IASTORE);
        }
        mv.visitFieldInsn(PUTSTATIC, generatedKlassNameInternal, FIELD_EXPECTED_CONSTRUCTOR_ARGUMENT_LENGTHS, "[I");

        //  methods
        size = methods.size();
        pushIntValue(mv, size);
        mv.visitIntInsn(NEWARRAY, T_INT);
        for (int i = 0; i < size; i++) {
            mv.visitInsn(DUP);
            pushIntValue(mv, i);
            pushIntValue(mv, methods.get(i).getParameterCount());
            mv.visitInsn(IASTORE);
        }
        mv.visitFieldInsn(PUTSTATIC, generatedKlassNameInternal, FIELD_EXPECTED_METHOD_ARGUMENT_LENGTHS, "[I");

        mv.visitInsn(RETURN);
        mv.visitMaxs(size > 0 ? 4 : 1, 0);
        mv.visitEnd();
    }

    public void insertCheckArgumentsMethod() {
        // private static final void checkArguments(int[] argumentsLength, int offset, Object[] args);
        MethodVisitor mv = cw.visitMethod(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, METHOD_CHECK_ARGUMENTS, "([II[Ljava/lang/Object;)V", null, null);
        mv.visitCode();

        Label labelStep2 = new Label();
        Label labelError2 = new Label();
        Label labelStep3 = new Label();
        Label labelSucc = new Label();

        // step1: if (args == null)
        mv.visitVarInsn(ALOAD, 2);
        mv.visitJumpInsn(IFNONNULL, labelStep2);

        throwIllegalArgumentException(mv, "arguments must be not null");

        // step2: if (offset < 0 || offset >= argumentsLength.length)
        mv.visitLabel(labelStep2);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitJumpInsn(IFLT, labelError2);

        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IF_ICMPLT, labelStep3);

        mv.visitLabel(labelError2);
        throwIllegalArgumentException(mv, "wrong offset of member");

        // step3: if (args.length != argumentsLength[which]) {
        mv.visitLabel(labelStep3);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitInsn(IALOAD);
        mv.visitJumpInsn(IF_ICMPEQ, labelSucc);

        throwIllegalArgumentException(mv, "wrong number of arguments");

        //
        mv.visitLabel(labelSucc);
        mv.visitInsn(RETURN);

        mv.visitMaxs(4, 3);
        mv.visitEnd();
    }

    public void insertConstructor() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, SUN_MAGIC_ACCESSOR_KLASS, "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void insertNewInstance() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "newInstance", "()Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitTypeInsn(NEW, delegateKlassNameInternal);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, delegateKlassNameInternal, "<init>", "()V", false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    public void insertNewInstance(List<ConstructorInfo> constructors) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "newInstance", "(I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();

        // checkArgument(expectedArgumentLengths, offset, arguments);
        mv.visitFieldInsn(GETSTATIC, generatedKlassNameInternal, FIELD_EXPECTED_CONSTRUCTOR_ARGUMENT_LENGTHS, "[I");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKESTATIC, generatedKlassNameInternal, METHOD_CHECK_ARGUMENTS, "([II[Ljava/lang/Object;)V", false);

        int n = constructors.size();
        if (n != 0) {
            mv.visitVarInsn(ILOAD, 1);
            Label[] labels = new Label[n];
            for (int i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            StringBuilder buffer = new StringBuilder(128);
            for (int i = 0; i < n; i++) {
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);

                mv.visitTypeInsn(NEW, delegateKlassNameInternal);
                mv.visitInsn(DUP);

                buffer.setLength(0);
                buffer.append('(');
                Class<?>[] paramTypes = constructors.get(i).getParameterTypes();
                for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                    mv.visitVarInsn(ALOAD, 2);
                    pushIntValue(mv, paramIndex);
                    mv.visitInsn(AALOAD);

                    Type type = Type.getType(paramTypes[paramIndex]);
                    insertUnbox(mv, type);
                    buffer.append(type.getDescriptor());
                }
                buffer.append(")V");
                mv.visitMethodInsn(INVOKESPECIAL, delegateKlassNameInternal, "<init>", buffer.toString(), false);
                mv.visitInsn(ARETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        throwIllegalArgumentException(mv, "wrong offset of constructor");

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    public void insertInvoke(List<MethodInfo> methods) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "invoke", "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();

        // checkArgument(expectedArgumentLengths, offset, arguments);
        mv.visitFieldInsn(GETSTATIC, generatedKlassNameInternal, FIELD_EXPECTED_METHOD_ARGUMENT_LENGTHS, "[I");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKESTATIC, generatedKlassNameInternal, METHOD_CHECK_ARGUMENTS, "([II[Ljava/lang/Object;)V", false);

        int n = methods.size();
        if (n != 0) {
            mv.visitVarInsn(ILOAD, 2);
            Label[] labels = new Label[n];
            for (int i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            StringBuilder buffer = new StringBuilder(128);
            for (int i = 0; i < n; i++) {
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);

                MethodInfo method = methods.get(i);
                boolean isInterface = method.getDeclaringKlass().isInterface();
                boolean isStatic = method.isStatic();

                if (!isStatic) {
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, delegateKlassNameInternal);
                }

                buffer.setLength(0);
                buffer.append('(');

                String methodName = method.getName();
                Class<?>[] paramTypes = method.getParameterTypes();
                Class<?> returnType = method.getReturnType();
                for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                    mv.visitVarInsn(ALOAD, 3);
                    pushIntValue(mv, paramIndex);
                    mv.visitInsn(AALOAD);
                    Type type = Type.getType(paramTypes[paramIndex]);
                    insertUnbox(mv, type);
                    buffer.append(type.getDescriptor());
                }
                buffer.append(')');
                buffer.append(Type.getDescriptor(returnType));

                int opcode;
                if (isInterface) {
                    opcode = INVOKEINTERFACE;
                } else if (isStatic) {
                    opcode = INVOKESTATIC;
                } else if (method.isPrivate() || method.isFinal()) {
                    opcode = INVOKESPECIAL;
                } else {
                    opcode = INVOKEVIRTUAL;
                }
                mv.visitMethodInsn(opcode, delegateKlassNameInternal, methodName, buffer.toString(), isInterface);

                insertBox(mv, Type.getType(returnType));
                mv.visitInsn(ARETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        throwIllegalArgumentException(mv, "wrong offset of method");

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    public void insertGetField(List<FieldInfo> fields) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getField", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();

        int n = fields.size();
        if (n != 0) {
            mv.visitVarInsn(ILOAD, 2);
            Label[] labels = new Label[n];
            for (int i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0; i < n; i++) {
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);

                FieldInfo field = fields.get(i);
                Type type = Type.getType(field.getType());
                if (Modifier.isStatic(field.getModifiers())) {
                    mv.visitFieldInsn(GETSTATIC, delegateKlassNameInternal, field.getName(), type.getDescriptor());
                } else {
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, delegateKlassNameInternal);
                    mv.visitFieldInsn(GETFIELD, delegateKlassNameInternal, field.getName(), type.getDescriptor());
                }
                insertBox(mv, type);
                mv.visitInsn(ARETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        throwIllegalArgumentException(mv, "wrong offset of field");

        int maxStack = fields.isEmpty() ? 5 : 6;
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    public void insertSetField(List<FieldInfo> fields) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "setField", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();

        int n = fields.size();
        if (n != 0) {
            mv.visitVarInsn(ILOAD, 2);
            Label[] labels = new Label[n];
            for (int i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0; i < n; i++) {
                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);

                FieldInfo field = fields.get(i);
                Type type = Type.getType(field.getType());
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (!isStatic) {
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, delegateKlassNameInternal);
                }
                mv.visitVarInsn(ALOAD, 3);
                insertUnbox(mv, type);
                mv.visitFieldInsn(isStatic ? PUTSTATIC : PUTFIELD, delegateKlassNameInternal, field.getName(), type.getDescriptor());
                mv.visitInsn(RETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        throwIllegalArgumentException(mv, "wrong offset of field");

        int maxStack = fields.isEmpty() ? 5 : 6;
        mv.visitMaxs(maxStack, 4);
        mv.visitEnd();
    }

    private static void throwIllegalArgumentException(MethodVisitor mv, String message) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
    }

    private static void insertBox(MethodVisitor mv, Type type) {
        switch (type.getSort()) {
        case Type.VOID:
            mv.visitInsn(ACONST_NULL);
            break;
        case Type.BOOLEAN:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            break;
        case Type.BYTE:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
            break;
        case Type.CHAR:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
            break;
        case Type.SHORT:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
            break;
        case Type.INT:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            break;
        case Type.FLOAT:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
            break;
        case Type.LONG:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            break;
        case Type.DOUBLE:
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            break;
        }
    }

    private static void insertUnbox(MethodVisitor mv, Type type) {
        switch (type.getSort()) {
        case Type.BOOLEAN:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
            break;
        case Type.BYTE:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "byteValue", "()B", false);
            break;
        case Type.CHAR:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
            break;
        case Type.SHORT:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "shortValue", "()S", false);
            break;
        case Type.INT:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "intValue", "()I", false);
            break;
        case Type.FLOAT:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "floatValue", "()F", false);
            break;
        case Type.LONG:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "longValue", "()J", false);
            break;
        case Type.DOUBLE:
            mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number", "doubleValue", "()D", false);
            break;
        case Type.ARRAY:
            mv.visitTypeInsn(CHECKCAST, type.getDescriptor());
            break;
        case Type.OBJECT:
            String internalName = type.getInternalName();
            if (!"java/lang/Object".equals(internalName)) {
                mv.visitTypeInsn(CHECKCAST, internalName);
            }
            break;
        }
    }

    public byte[] asByteCode() {
        cw.visitEnd();
        return cw.toByteArray();
    }

    private void pushIntValue(MethodVisitor mv, int value) {
        switch (value) {
        case 0:
            mv.visitInsn(ICONST_0);
            return;
        case 1:
            mv.visitInsn(ICONST_1);
            return;
        case 2:
            mv.visitInsn(ICONST_2);
            return;
        case 3:
            mv.visitInsn(ICONST_3);
            return;
        case 4:
            mv.visitInsn(ICONST_4);
            return;
        case 5:
            mv.visitInsn(ICONST_5);
            return;
        }
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            mv.visitIntInsn(BIPUSH, value);
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            mv.visitIntInsn(SIPUSH, value);
        } else {
            mv.visitLdcInsn(Integer.valueOf(value));
        }
    }
}
