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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jetbrick.bean.KlassInfo;
import jetbrick.bean.MethodInfo;

public class AsmBigClassTest {
    @Before
    public void setup() {
        AsmFactory.setThreshold(0);
    }

    @After
    public void cleanup() {
        AsmFactory.setThreshold(5);
    }

    @Test
    public void testMoreMethods() {
        BigClass a = new BigClass();
        KlassInfo klass = KlassInfo.create(BigClass.class);
        MethodInfo method = klass.getDeclaredMethod("a150");
        Assert.assertEquals(0, method.invoke(a));
    }

    static class BigClass {
        private void a1() {
        };

        private void a2() {
        };

        private void a3() {
        };

        private void a4() {
        };

        private void a5() {
        };

        private void a6() {
        };

        private void a7() {
        };

        private void a8() {
        };

        private void a9() {
        };

        private void a10() {
        };

        private void a11() {
        };

        private void a12() {
        };

        private void a13() {
        };

        private void a14() {
        };

        private void a15() {
        };

        private void a16() {
        };

        private void a17() {
        };

        private void a18() {
        };

        private void a19() {
        };

        private void a20() {
        };

        private void a21() {
        };

        private void a22() {
        };

        private void a23() {
        };

        private void a24() {
        };

        private void a25() {
        };

        private void a26() {
        };

        private void a27() {
        };

        private void a28() {
        };

        private void a29() {
        };

        private void a30() {
        };

        private void a31() {
        };

        private void a32() {
        };

        private void a33() {
        };

        private void a34() {
        };

        private void a35() {
        };

        private void a36() {
        };

        private void a37() {
        };

        private void a38() {
        };

        private void a39() {
        };

        private void a40() {
        };

        private void a41() {
        };

        private void a42() {
        };

        private void a43() {
        };

        private void a44() {
        };

        private void a45() {
        };

        private void a46() {
        };

        private void a47() {
        };

        private void a48() {
        };

        private void a49() {
        };

        private void a50() {
        };

        private void a51() {
        };

        private void a52() {
        };

        private void a53() {
        };

        private void a54() {
        };

        private void a55() {
        };

        private void a56() {
        };

        private void a57() {
        };

        private void a58() {
        };

        private void a59() {
        };

        private void a60() {
        };

        private void a61() {
        };

        private void a62() {
        };

        private void a63() {
        };

        private void a64() {
        };

        private void a65() {
        };

        private void a66() {
        };

        private void a67() {
        };

        private void a68() {
        };

        private void a69() {
        };

        private void a70() {
        };

        private void a71() {
        };

        private void a72() {
        };

        private void a73() {
        };

        private void a74() {
        };

        private void a75() {
        };

        private void a76() {
        };

        private void a77() {
        };

        private void a78() {
        };

        private void a79() {
        };

        private void a80() {
        };

        private void a81() {
        };

        private void a82() {
        };

        private void a83() {
        };

        private void a84() {
        };

        private void a85() {
        };

        private void a86() {
        };

        private void a87() {
        };

        private void a88() {
        };

        private void a89() {
        };

        private void a90() {
        };

        private void a91() {
        };

        private void a92() {
        };

        private void a93() {
        };

        private void a94() {
        };

        private void a95() {
        };

        private void a96() {
        };

        private void a97() {
        };

        private void a98() {
        };

        private void a99() {
        };

        private void a100() {
        };

        private void a101() {
        };

        private void a102() {
        };

        private void a103() {
        };

        private void a104() {
        };

        private void a105() {
        };

        private void a106() {
        };

        private void a107() {
        };

        private void a108() {
        };

        private void a109() {
        };

        private void a110() {
        };

        private void a111() {
        };

        private void a112() {
        };

        private void a113() {
        };

        private void a114() {
        };

        private void a115() {
        };

        private void a116() {
        };

        private void a117() {
        };

        private void a118() {
        };

        private void a119() {
        };

        private void a120() {
        };

        private void a121() {
        };

        private void a122() {
        };

        private void a123() {
        };

        private void a124() {
        };

        private void a125() {
        };

        private void a126() {
        };

        private void a127() {
        };

        private void a128() {
        };

        private void a129() {
        };

        private void a130() {
        };

        private void a131() {
        };

        private void a132() {
        };

        private void a133() {
        };

        private void a134() {
        };

        private void a135() {
        };

        private void a136() {
        };

        private void a137() {
        };

        private void a138() {
        };

        private void a139() {
        };

        private void a140() {
        };

        private void a141() {
        };

        private void a142() {
        };

        private void a143() {
        };

        private void a144() {
        };

        private void a145() {
        };

        private void a146() {
        };

        private void a147() {
        };

        private void a148() {
        };

        private void a149() {
        };

        private int a150() {
            return 0;
        };
    }
}
