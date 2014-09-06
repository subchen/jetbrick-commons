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
package jetbrick.typecast;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

public class TypeCastUtilsTest {

    @Test
    public void testConvertString() {
        Assert.assertEquals(Integer.valueOf(123), TypeCastUtils.convert("123", Integer.class));
        Assert.assertEquals(Boolean.TRUE, TypeCastUtils.convert("true", Boolean.class));
        Assert.assertEquals(Boolean.TRUE, TypeCastUtils.convert("true", Boolean.TYPE));
        Assert.assertEquals(new File("/tmp/123.txt"), TypeCastUtils.convert("/tmp/123.txt", File.class));
        Assert.assertEquals(new Date(123456789), TypeCastUtils.convert("123456789", Date.class));
    }

    @Test
    public void testConvertObject() throws Throwable {
        Assert.assertEquals(Integer.valueOf(123), TypeCastUtils.convert(123, Integer.class));
        Assert.assertEquals(Boolean.TRUE, TypeCastUtils.convert(true, Boolean.class));
        Assert.assertEquals(Boolean.TRUE, TypeCastUtils.convert(1, Boolean.TYPE));
        Assert.assertEquals(new File("/tmp/123.txt"), TypeCastUtils.convert(new URL("file:/tmp/123.txt"), File.class));
        Assert.assertEquals(new Date(123456789), TypeCastUtils.convert(123456789L, Date.class));
    }

    @Test
    public void testConvertToArrayString() {
        Assert.assertArrayEquals(new Integer[] { 1, 2, 3 }, (Object[]) TypeCastUtils.convertToArray("1,2,3", Integer.class));
        Assert.assertArrayEquals(new int[] { 1, 2, 3 }, (int[]) TypeCastUtils.convertToArray("1,2,3", Integer.TYPE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertToArrayObject() {
        Assert.assertArrayEquals(new int[] { 1, 2, 3 }, (int[]) TypeCastUtils.convertToArray(Arrays.asList("1", 2, 3.0), Integer.TYPE));
    }
}
