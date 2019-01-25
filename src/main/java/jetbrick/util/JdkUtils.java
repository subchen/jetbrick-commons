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
package jetbrick.util;

public final class JdkUtils {
    public static final int JAVA_MAJOR_VERSION;

    public static final String JAVA_VERSION;

    public static final boolean IS_JAVA_5;
    public static final boolean IS_JAVA_6;
    public static final boolean IS_JAVA_7;
    public static final boolean IS_JAVA_8;
    public static final boolean IS_JAVA_9;
    public static final boolean IS_JAVA_10;
    public static final boolean IS_JAVA_11;
    public static final boolean IS_JAVA_12;

    public static final boolean IS_AT_LEAST_JAVA_5;
    public static final boolean IS_AT_LEAST_JAVA_6;
    public static final boolean IS_AT_LEAST_JAVA_7;
    public static final boolean IS_AT_LEAST_JAVA_8;
    public static final boolean IS_AT_LEAST_JAVA_9;
    public static final boolean IS_AT_LEAST_JAVA_10;
    public static final boolean IS_AT_LEAST_JAVA_11;
    public static final boolean IS_AT_LEAST_JAVA_12;

    static {
        // get java major version: 5,6,7,8,9,10,11,...
        String majorVersion = System.getProperty("java.specification.version");
        if (majorVersion.startsWith("1.")) {
            majorVersion = majorVersion.substring(2);
        }
        JAVA_MAJOR_VERSION = Integer.valueOf(majorVersion);

        JAVA_VERSION = System.getProperty("java.version");

        IS_JAVA_5 = JAVA_VERSION.startsWith("1.5.");
        IS_JAVA_6 = JAVA_VERSION.startsWith("1.6.");
        IS_JAVA_7 = JAVA_VERSION.startsWith("1.7.");
        IS_JAVA_8 = JAVA_VERSION.startsWith("1.8.");
        IS_JAVA_9 = JAVA_VERSION.startsWith("1.9.") || JAVA_MAJOR_VERSION == 9;
        IS_JAVA_10 = JAVA_MAJOR_VERSION == 10;
        IS_JAVA_11 = JAVA_MAJOR_VERSION == 11;
        IS_JAVA_12 = JAVA_MAJOR_VERSION == 12;

        IS_AT_LEAST_JAVA_12 = JAVA_MAJOR_VERSION >= 12;
        IS_AT_LEAST_JAVA_11 = JAVA_MAJOR_VERSION >= 11;
        IS_AT_LEAST_JAVA_10 = JAVA_MAJOR_VERSION >= 10;
        IS_AT_LEAST_JAVA_9 = IS_JAVA_9 || IS_AT_LEAST_JAVA_10;
        IS_AT_LEAST_JAVA_8 = IS_JAVA_8 || IS_AT_LEAST_JAVA_9;
        IS_AT_LEAST_JAVA_7 = IS_JAVA_7 || IS_AT_LEAST_JAVA_8;
        IS_AT_LEAST_JAVA_6 = IS_JAVA_6 || IS_AT_LEAST_JAVA_7;
        IS_AT_LEAST_JAVA_5 = IS_JAVA_5 || IS_AT_LEAST_JAVA_6;
    }
}
