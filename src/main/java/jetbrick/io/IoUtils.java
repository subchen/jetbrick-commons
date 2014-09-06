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
package jetbrick.io;

import java.io.*;
import java.net.*;
import java.nio.channels.Selector;
import java.util.zip.ZipFile;
import jetbrick.io.stream.*;

public final class IoUtils {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static byte[] toByteArray(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    public static byte[] toByteArray(InputStream is) {
        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static byte[] toByteArray(Reader reader, String encoding) {
        try {
            InputStream is = new ReaderInputStream(reader, encoding);
            return toByteArray(is);
        } finally {
            closeQuietly(reader);
        }
    }

    public static char[] toCharArray(File file, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), encoding);
            return toCharArray(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static char[] toCharArray(InputStream is, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(is, encoding);
            return toCharArray(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static char[] toCharArray(Reader reader) {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static String toString(File file, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), encoding);
            return toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static String toString(InputStream is, String encoding) {
        try {
            InputStreamReader reader = new InputStreamReader(is, encoding);
            return toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    public static String toString(Reader reader) {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static void write(byte[] data, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(os);
        }
    }

    public static void write(char[] data, File file, String encoding) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(new String(data).getBytes(encoding));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(os);
        }
    }

    public static void write(String data, File file, String encoding) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(encoding));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(os);
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copy(InputStream input, Writer output, String encoding) throws IOException {
        return copy(new InputStreamReader(input, encoding), output);
    }

    public static long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copy(Reader input, OutputStream output, String encoding) throws IOException {
        return copy(new ReaderInputStream(input, encoding), output);
    }

    public static void closeQuietly(Closeable obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(ZipFile obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(ServerSocket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(Selector selector) {
        try {
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(URLConnection conn) {
        if (conn != null) {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }
}
