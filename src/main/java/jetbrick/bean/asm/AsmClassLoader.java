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

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

final class AsmClassLoader extends ClassLoader {
    private static final String ASMACCESSOR_CLASS_NAME = AsmAccessor.class.getName();
    private static final List<AsmClassLoader> ASM_CLASS_LOADERS = new ArrayList<AsmClassLoader>();

    // Fast-path for classes loaded in the same ClassLoader as this class.
    private static final ClassLoader PARENT_ASM_CLASS_LOADER = AsmClassLoader.class.getClassLoader();
    private static final AsmClassLoader DEFAULT_ASM_CLASS_LOADER = new AsmClassLoader(PARENT_ASM_CLASS_LOADER);

    public static AsmClassLoader get(Class<?> type) {
        ClassLoader parent = type.getClassLoader();
        // 1. fast-path:
        if (PARENT_ASM_CLASS_LOADER == parent) {
            return DEFAULT_ASM_CLASS_LOADER;
        }
        // 2. normal search:
        synchronized (ASM_CLASS_LOADERS) {
            for (int i = 0, n = ASM_CLASS_LOADERS.size(); i < n; i++) {
                AsmClassLoader loader = ASM_CLASS_LOADERS.get(i);
                if (loader.getParent() == parent) {
                    return loader;
                }
            }
            AsmClassLoader loader = new AsmClassLoader(parent);
            ASM_CLASS_LOADERS.add(loader);
            return loader;
        }
    }

    private AsmClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // These classes come from the class loader that loaded AccessClassLoader.
        if (name.equals(ASMACCESSOR_CLASS_NAME)) return AsmAccessor.class;

        // All other classes come from the class loader that loaded the type we are accessing.
        return super.loadClass(name, resolve);
    }

    protected Class<?> defineClass(String qualifiedClassName, byte[] bytes, ProtectionDomain protectionDomain) throws ClassFormatError {
        // method 1:
        return super.defineClass(qualifiedClassName, bytes, 0, bytes.length, protectionDomain);

        // method 2:
        /*
        try {
            return jetbrick.util.UnsafeUtils.defineClass(qualifiedClassName, bytes, 0, bytes.length, this, protectionDomain);
        } catch (Exception e) {
        }
        */

        // method 3:
        /*
        try {
            // Attempt to load the access class in the same loader, which makes protected and default access members accessible.
            // this method shoud be cached.
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
            method.setAccessible(true);
            return (Class<?>) method.invoke(getParent(), new Object[] { name, bytes, 0, bytes.length, protectionDomain });
        } catch (Throwable e) {
        }
         */
    }
}
