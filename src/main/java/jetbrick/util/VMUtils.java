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
package jetbrick.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public final class VMUtils {

    public static String getProcessId() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return StringUtils.substringBefore(name, "@");
    }

    public static String getThreadDump() {
        if (JdkUtils.IS_AT_LEAST_JAVA_6) {
            return "Java AppVersionUtils must be equal or larger than 1.6";
        }
        String jstack = "../bin/jstack";
        if (SystemUtils.IS_OS_WINDOWS) {
            jstack = jstack + ".exe";
        }

        File jstackFile = new File(System.getProperty("java.home"), jstack);
        String command = jstackFile.getAbsolutePath() + " " + getProcessId();
        ShellUtils.Result result = ShellUtils.shell(command);
        if (result.success()) {
            return result.stdout();
        }
        throw new IllegalStateException(result.getException());
    }

    public static boolean detectDeadlock() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadBean.findDeadlockedThreads();
        return (threadIds != null && threadIds.length > 0);
    }
}
