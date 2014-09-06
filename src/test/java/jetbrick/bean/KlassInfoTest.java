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
package jetbrick.bean;

import java.util.AbstractMap;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;

public class KlassInfoTest {

    @Test
    public void create() {
        KlassInfo k1 = KlassInfo.create(HashMap.class);
        KlassInfo k2 = KlassInfo.create(HashMap.class);
        KlassInfo k3 = KlassInfo.create(AbstractMap.class);

        Assert.assertTrue(k1 == k2);
        Assert.assertEquals(HashMap.class, k1.getType());
        Assert.assertEquals("java.util.HashMap", k1.getName());
        Assert.assertEquals("HashMap", k1.getSimpleName());

        Assert.assertEquals(k3, k1.getSuperKlass());
        Assert.assertEquals(3, k1.getInterfaces().size());
    }
}
