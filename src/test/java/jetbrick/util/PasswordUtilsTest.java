/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
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

import org.junit.Assert;
import org.junit.Test;

public class PasswordUtilsTest {

    @Test
    public void hash() {
        Assert.assertEquals(32, PasswordUtils.hash("").length);
        Assert.assertEquals(32, PasswordUtils.hash("admin").length);
        Assert.assertEquals(32, PasswordUtils.hash("123").length);
        Assert.assertEquals(32, PasswordUtils.hash("中文").length);
    }

    @Test
    public void check() {
        Assert.assertTrue(PasswordUtils.check("", "03c879c6e0b9739d0754f881bb7e5f58"));
        Assert.assertTrue(PasswordUtils.check("admin", "7e798faac2a12f83ecbfc3e360cc738f"));
        Assert.assertTrue(PasswordUtils.check("123", "3ed2154055328c8c6a577fc6c2478012"));
        Assert.assertTrue(PasswordUtils.check("中文", "3c9116e0a8ca88212c9d0b97dbcc6d1b"));
    }
}
