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
package jetbrick.bean.asm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jetbrick.bean.FieldInfo;
import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;

public class AsmFactoryTest {
    @Before
    public void setup() {
        AsmFactory.setThreshold(0);
    }

    @After
    public void cleanup() {
        AsmFactory.setThreshold(999);
    }

    @Test
    public void generateConstructorAccessor() {
        AsmAccessor accessor = AsmFactory.generateAccessor(ArrayList.class);
        Assert.assertNotNull(accessor.newInstance());
        Assert.assertNotNull(accessor.newInstance(0, Arrays.asList()));
        Assert.assertNotNull(accessor.newInstance(1));
        Assert.assertNotNull(accessor.newInstance(2, 32));
    }

    @Test
    public void generateMethodAccessor() {
        List<Integer> list = Arrays.asList(11, 22, 33);

        KlassInfo klass = KlassInfo.create(List.class);
        AsmAccessor accessor = AsmFactory.generateAccessor(List.class);
        Assert.assertEquals(list.size(), accessor.invoke(list, klass.getDeclaredMethod("size").getOffset()));
        Assert.assertEquals(list.isEmpty(), accessor.invoke(list, klass.getDeclaredMethod("isEmpty").getOffset()));
        Assert.assertEquals(list.get(1), accessor.invoke(list, klass.getDeclaredMethod("get", int.class).getOffset(), 1));
    }

    @Test
    public void generateMethodAccessor2() {
        List<Integer> list = Arrays.asList(11, 22, 33);

        KlassInfo klass = KlassInfo.create(List.class);
        Assert.assertEquals(list.size(), klass.getMethod("size").invoke(list));
        Assert.assertEquals(list.isEmpty(), klass.getMethod("isEmpty").invoke(list));
        Assert.assertEquals(list.get(1), klass.getMethod("get", int.class).invoke(list, 1));
    }

    @Test
    public void testPrivateField() {
        String s = "abc";
        KlassInfo klass = KlassInfo.create(String.class);
        Assert.assertArrayEquals(s.toCharArray(), (char[]) klass.getField("value").get(s));
    }

    @Test
    public void testAsmGetFields() {
        SimpleDateFormat object = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        KlassInfo klass = KlassInfo.create(SimpleDateFormat.class);
        for (FieldInfo field : klass.getFields()) {
            field.get(object);
        }
    }

    @Test
    public void testReflectGetFields() throws Exception {
        SimpleDateFormat object = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        KlassInfo klass = KlassInfo.create(SimpleDateFormat.class);
        for (FieldInfo field : klass.getFields()) {
            field.getField().get(object);
        }
    }

    @Test
    public void testVarargs() {
        KlassInfo klass = KlassInfo.create(String.class);
        MethodInfo method = klass.getDeclaredMethod("format", String.class, Object[].class);
        Assert.assertEquals("aaa", method.invoke(null, "aaa", null));
        Assert.assertEquals("aaa123999", method.invoke(null, "aaa%s%s", new Object[] { 123, 999 }));
    }
}
