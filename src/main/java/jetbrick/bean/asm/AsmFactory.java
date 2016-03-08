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

import java.io.File;
import jetbrick.bean.KlassInfo;
import jetbrick.io.IoUtils;
import jetbrick.util.SystemUtils;
import org.slf4j.LoggerFactory;

public final class AsmFactory {
    private static int ASM_THRESHOLD_VALUE = 5;
    private static boolean ASM_DEBUG_ENABLED = false;

    public static int getThreshold() {
        return ASM_THRESHOLD_VALUE;
    }

    public static void setThreshold(int value) {
        ASM_THRESHOLD_VALUE = value;
    }

    public static void setDebugEnabled(boolean enabled) {
        ASM_DEBUG_ENABLED = enabled;
    }

    public static AsmAccessor generateAccessor(Class<?> delegateKlass) {
        return generateAccessor(KlassInfo.create(delegateKlass));
    }

    public static AsmAccessor generateAccessor(KlassInfo delegateKlass) {
        Class<?> delegateType = delegateKlass.getType();
        String generatedKlassName = AsmFactory.class.getPackage().getName() + ".delegate." + delegateType.getName().replace('.', '_');

        Class<?> generatedKlass;
        AsmClassLoader loader = AsmClassLoader.get(delegateType);
        synchronized (loader) {
            try {
                generatedKlass = loader.loadClass(generatedKlassName);
            } catch (ClassNotFoundException e) {
                byte[] byteCode = AsmBuilder.create(generatedKlassName, delegateKlass);
                if (ASM_DEBUG_ENABLED) {
                    File file = new File(SystemUtils.JAVA_IO_TMPDIR, ".asm/" + generatedKlassName.replace('.', '/') + ".class");
                    file.getParentFile().mkdirs();

                    LoggerFactory.getLogger(AsmFactory.class).info("AsmFactory generated {}", file);
                    IoUtils.write(byteCode, file);
                }
                generatedKlass = loader.defineClass(generatedKlassName, byteCode, delegateType.getProtectionDomain());
            }
        }

        try {
            return (AsmAccessor) generatedKlass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Error constructing access class: " + generatedKlassName, e);
        }
    }
}
