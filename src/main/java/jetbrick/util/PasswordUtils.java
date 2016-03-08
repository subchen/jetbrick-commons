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
package jetbrick.util;

import jetbrick.util.codec.MD5Utils;

public final class PasswordUtils {

    private static final int SALT_LENGTH = 8;

    /**
     * Hash a password string.
     *
     * @param passwd - the password to hash
     * @return hashed string
     */
    public static String hash(String passwd) {
        String salt = RandomStringUtils.randomHex(SALT_LENGTH);
        String hashed = MD5Utils.md5Hex(salt + passwd);
        return salt + hashed.substring(SALT_LENGTH);
    }

    /**
     * Validate the password is currently.
     *
     * @param passwd - the password to check
     * @param hashed - the result of `passwd.hash()`
     * @return
     */
    public static boolean check(String passwd, String hashed) {
        String salt = hashed.substring(0, SALT_LENGTH);
        String rehashed = MD5Utils.md5Hex(salt + passwd);
        return hashed.substring(SALT_LENGTH).equals(rehashed.substring(SALT_LENGTH));
    }
}
