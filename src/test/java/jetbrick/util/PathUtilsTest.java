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

public class PathUtilsTest {

    @Test
    public void normalize() {
        Assert.assertEquals("/a", PathUtils.normalize("/a"));
        Assert.assertEquals("/a/b", PathUtils.normalize("/a/b"));
        Assert.assertEquals("/a/b/c", PathUtils.normalize("/a/b/c"));
        Assert.assertEquals("a/b/", PathUtils.normalize("a/b/"));
        Assert.assertEquals("a/b/c", PathUtils.normalize("a/b/c"));

        Assert.assertEquals("/a/b/d", PathUtils.normalize("/a/b/c/../d"));
        Assert.assertEquals("/a/d", PathUtils.normalize("/a/b/c/../../d"));
        Assert.assertEquals("a/d/e/", PathUtils.normalize("a/b/c/../../d/e/"));
        Assert.assertEquals("/a/b", PathUtils.normalize("//a/b"));

        Assert.assertEquals("../b/c", PathUtils.normalize("../a/../b/c"));
        Assert.assertEquals("b/c", PathUtils.normalize("./a/.//../b/c"));
        Assert.assertEquals("", PathUtils.normalize("a//b/./../c/../../"));
        Assert.assertEquals("../../", PathUtils.normalize("a/../b/./../c/../../../"));

        Assert.assertEquals("/path/file.ext", PathUtils.normalize("/path/file.ext"));
        Assert.assertEquals("/path/file.ext", PathUtils.normalize("\\path\\file.ext"));
        Assert.assertEquals("/file.ext", PathUtils.normalize("/path/..\\file.ext"));
        Assert.assertEquals("path/file.ext", PathUtils.normalize("path\\.\\file.ext"));
    }

    @Test
    public void concat() {
        Assert.assertEquals("/a/b", PathUtils.concat("", "a/b"));
        Assert.assertEquals("/a/b", PathUtils.concat("", "/a/b"));

        Assert.assertEquals("a/b/c", PathUtils.concat("./a", "b/c"));
        Assert.assertEquals("/a/b/c/d/", PathUtils.concat("/a/", "/b/c/d/"));
        Assert.assertEquals("/a/c/", PathUtils.concat("/a/b/", "../c/"));
        Assert.assertEquals("a/b/c/d", PathUtils.concat("./a/b", "/c/d"));
    }
}
