/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.io.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream used for debugging purposes.
 */
public class DebugOutputStream extends FilterOutputStream {

    // ---------------------------------------------------------------- ctors

    protected boolean passThrough = true;

    /**
     * Output stream that debugs to system out.
     */
    public DebugOutputStream() {
        super(System.out);
    }

    public DebugOutputStream(OutputStream out) {
        super(out);
    }

    public DebugOutputStream(boolean passThrough) {
        super(System.out);
        this.passThrough = passThrough;
    }

    public DebugOutputStream(OutputStream out, boolean passThrough) {
        super(out);
        this.passThrough = passThrough;
    }

    // ---------------------------------------------------------------- methods

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void write(int b) throws IOException {
        if (passThrough == true) {
            super.write(b);
        }
        dumpByte(b);
        System.out.println();
    }

    @Override
    public void write(byte b[]) throws IOException {
        super.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if (passThrough == true) {
            super.write(b, off, len);
        }
        int i = off;
        int count = len;
        while (count-- > 0) {
            dumpByte(b[i++]);
        }
        System.out.println();
    }

    /**
     * Dumps single byte to output stream.
     */
    protected void dumpByte(int b) {
        if (passThrough == true) {
            System.out.print('\t');
        }
        if (b < 0) {
            b += 128;
        }
        if (b < 0x10) {
            System.out.print('0');
        }

        System.out.print(' ');
        System.out.print(Integer.toHexString(b).toUpperCase());
    }
}
