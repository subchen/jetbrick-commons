/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jetbrick.util.ArrayUtils;
import jetbrick.util.concurrent.ConcurrentInitializer;
import jetbrick.util.concurrent.LazyInitializer;

/**
 * 构造函数和方法的公共基类.
 *
 * @author Guoqiang Chen
 */
public abstract class Executable {

    public abstract KlassInfo getDeclaringKlass();

    public abstract String getName();

    public abstract int getOffset();

    private final ConcurrentInitializer<List<ParameterInfo>> parametersGetter = new LazyInitializer<List<ParameterInfo>>() {
        @Override
        protected List<ParameterInfo> initialize() {
            Executable object = Executable.this;

            Class<?>[] parameterTypes = object.getParameterTypes();
            if (parameterTypes.length == 0) {
                return Collections.emptyList();
            }
            Type[] genericParameterTypes = object.getGenericParameterTypes();
            Annotation[][] parameterAnnotations = object.getParameterAnnotations();

            ParameterInfo[] parameters = new ParameterInfo[genericParameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new ParameterInfo(object, parameterTypes[i], genericParameterTypes[i], parameterAnnotations[i], i);
            }
            return Arrays.asList(parameters);
        }
    };

    public List<ParameterInfo> getParameters() {
        return parametersGetter.get();
    }

    public abstract int getParameterCount();

    public abstract Class<?>[] getParameterTypes();

    public abstract Type[] getGenericParameterTypes();

    public Class<?>[] getRawParameterTypes(KlassInfo declaringKlass) {
        return getRawParameterTypes(declaringKlass.getType());
    }

    public Class<?>[] getRawParameterTypes(Class<?> declaringClass) {
        List<ParameterInfo> parameters = getParameters();
        int size = parameters.size();
        if (size == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class<?>[] types = new Class<?>[size];
        for (int i = 0; i < size; i++) {
            types[i] = parameters.get(i).getRawType(declaringClass);
        }
        return types;
    }

    public abstract boolean isVarArgs();

    public abstract Annotation[] getAnnotations();

    public abstract <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    public abstract <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass);

    public abstract Annotation[][] getParameterAnnotations();

    public abstract int getModifiers();

    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    private volatile String signature;

    public String getSignature() {
        if (signature == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getDeclaringKlass().getType().getName());
            sb.append('#').append(getName()).append('(');
            Class<?>[] parameterTypes = getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(parameterTypes[i].getName());
            }
            sb.append(')');
            signature = sb.toString();
        }
        return signature;
    }

    @Override
    public String toString() {
        return getSignature();
    }

}
