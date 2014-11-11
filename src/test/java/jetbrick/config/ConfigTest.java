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
package jetbrick.config;

import java.nio.charset.Charset;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {
    private static Config c;

    @BeforeClass
    public static void beforeClass() {
        ConfigLoader loader = new ConfigLoader();
        loader.load("classpath:/jetbrick/config/test.properties");
        c = loader.asConfig();
    }

    @Test
    public void testSimple() {
        Assert.assertEquals(Boolean.TRUE, c.asBoolean("webapp.debug"));
        Assert.assertEquals("jetbrick_demo_webapp", c.asString("webapp.name"));
        Assert.assertEquals(Charset.forName("utf-8"), c.asCharset("webapp.encoding"));
    }

    @Test
    public void testPlaceholder() {
        Assert.assertEquals(System.getProperty("java.io.tmpdir"), c.asString("webapp.tmpdir"));
        Assert.assertEquals(System.getenv("PATH"), c.asString("webapp.path"));
    }

    @Test
    public void testObject() {
        Assert.assertThat(c.asObject("webapp.formatter"), CoreMatchers.instanceOf(java.text.SimpleDateFormat.class));

        Thread thread1 = c.asObject("webapp.thread.1", Thread.class);
        Assert.assertEquals("jetbrick_demo_webapp", thread1.getName());
        Assert.assertEquals(Boolean.TRUE, thread1.isDaemon());
        Assert.assertEquals(5, thread1.getPriority());
        
        Thread thread2 = c.asObject("webapp.thread.2", Thread.class);
        Assert.assertTrue(thread1 == thread2);
    }

}
