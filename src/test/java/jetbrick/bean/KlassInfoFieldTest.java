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

import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class KlassInfoFieldTest {

    @Test
    public void getDeclaredFields() {
        KlassInfo klass = KlassInfo.create(UUID.class);
        Assert.assertTrue(klass.getDeclaredFields().size() > 0);
    }

    @Test
    public void getDeclaredFieldsWithFilter() {
        KlassInfo klass = KlassInfo.create(Integer.class);
        List<FieldInfo> fields = klass.getDeclaredFields(Filters.PUBLIC_STATIC_FINAL_FIELD);
        Assert.assertTrue(fields.size() < klass.getDeclaredFields().size());
    }

    @Test
    public void getDeclaredField() {
        KlassInfo klass = KlassInfo.create(Integer.class);
        Assert.assertNotNull(klass.getDeclaredField("MIN_VALUE"));
    }

    @Test
    public void getFields() {
        KlassInfo klass = KlassInfo.create(ArrayList.class);
        Assert.assertTrue(klass.getFields().size() > klass.getDeclaredFields().size());
    }

    @Test
    public void getFieldsWithFilter() {
        KlassInfo klass = KlassInfo.create(ArrayList.class);
        List<FieldInfo> fields = klass.getFields(Filters.STATIC_FIELD);
        Assert.assertTrue(fields.size() >= 1);
    }

    @Test
    public void getField() {
        KlassInfo klass = KlassInfo.create(ArrayList.class);
        Assert.assertNotNull(klass.getField("size"));
        Assert.assertNotNull(klass.getField("modCount"));
    }
}
