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
package jetbrick.io.stream;

import java.io.*;

public class UnicodeInputStream extends InputStream {
    public static final int MAX_BOM_SIZE = 4;

    private PushbackInputStream internalInputStream;
    private boolean initialized;
    private int bomSize = -1;
    private String encoding;
    private String targetEncoding;
    public static final byte[] BOM_UTF8 = { -17, -69, -65 };
    public static final byte[] BOM_UTF16_BE = { -2, -1 };
    public static final byte[] BOM_UTF16_LE = { -1, -2 };
    public static final byte[] BOM_UTF32_BE = { 0, 0, -2, -1 };
    public static final byte[] BOM_UTF32_LE = { -1, -2, 0, 0 };

    public UnicodeInputStream(InputStream in, String targetEncoding) {
        this.internalInputStream = new PushbackInputStream(in, 4);
        this.targetEncoding = targetEncoding;
    }

    public String getDetectedEncoding() {
        if (!initialized) {
            try {
                init();
            } catch (IOException ioex) {
                throw new IllegalStateException(ioex);
            }
        }
        return encoding;
    }

    protected void init() throws IOException {
        if (initialized) {
            return;
        }

        if (targetEncoding == null) {
            byte[] bom = new byte[4];
            int n = internalInputStream.read(bom, 0, bom.length);
            int unread;
            if ((bom[0] == BOM_UTF32_BE[0]) && (bom[1] == BOM_UTF32_BE[1]) && (bom[2] == BOM_UTF32_BE[2]) && (bom[3] == BOM_UTF32_BE[3])) {
                encoding = "UTF-32BE";
                unread = n - 4;
            } else {
                if ((bom[0] == BOM_UTF32_LE[0]) && (bom[1] == BOM_UTF32_LE[1]) && (bom[2] == BOM_UTF32_LE[2]) && (bom[3] == BOM_UTF32_LE[3])) {
                    encoding = "UTF-32LE";
                    unread = n - 4;
                } else {
                    if ((bom[0] == BOM_UTF8[0]) && (bom[1] == BOM_UTF8[1]) && (bom[2] == BOM_UTF8[2])) {
                        encoding = "UTF-8";
                        unread = n - 3;
                    } else {
                        if ((bom[0] == BOM_UTF16_BE[0]) && (bom[1] == BOM_UTF16_BE[1])) {
                            encoding = "UTF-16BE";
                            unread = n - 2;
                        } else {
                            if ((bom[0] == BOM_UTF16_LE[0]) && (bom[1] == BOM_UTF16_LE[1])) {
                                encoding = "UTF-16LE";
                                unread = n - 2;
                            } else {
                                unread = n;
                            }
                        }
                    }
                }
            }
            bomSize = (4 - unread);

            if (unread > 0) {
                internalInputStream.unread(bom, n - unread, unread);
            }

        } else {
            byte[] bom = null;

            if (targetEncoding.equals("UTF-8"))
                bom = BOM_UTF8;
            else if (targetEncoding.equals("UTF-16LE"))
                bom = BOM_UTF16_LE;
            else if ((targetEncoding.equals("UTF-16BE")) || (targetEncoding.equals("UTF-16")))
                bom = BOM_UTF16_BE;
            else if (targetEncoding.equals("UTF-32LE"))
                bom = BOM_UTF32_LE;
            else if ((targetEncoding.equals("UTF-32BE")) || (targetEncoding.equals("UTF-32"))) {
                bom = BOM_UTF32_BE;
            }

            if (bom != null) {
                byte[] fileBom = new byte[bom.length];
                int n = internalInputStream.read(fileBom, 0, bom.length);

                boolean bomDetected = true;
                for (int i = 0; i < n; i++) {
                    if (fileBom[i] != bom[i]) {
                        bomDetected = false;
                        break;
                    }
                }

                if (!bomDetected) {
                    internalInputStream.unread(fileBom, 0, fileBom.length);
                }
            }
        }

        initialized = true;
    }

    @Override
    public void close() throws IOException {
        internalInputStream.close();
    }

    @Override
    public int read() throws IOException {
        init();
        return internalInputStream.read();
    }

    public int getBOMSize() {
        return bomSize;
    }
}
