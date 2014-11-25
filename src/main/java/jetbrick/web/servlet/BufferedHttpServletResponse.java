/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.web.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import jetbrick.io.stream.UnsafeByteArrayOutputStream;
import jetbrick.io.stream.UnsafeCharArrayWriter;
import jetbrick.util.ArrayUtils;

public final class BufferedHttpServletResponse extends HttpServletResponseWrapper {
    private UnsafeByteArrayOutputStream originStream;
    private ServletOutputStream stream;

    private UnsafeCharArrayWriter originWriter;
    private PrintWriter writer;

    public BufferedHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        if (os != null) {
            throw new IllegalStateException("the getOutputStream method has already been called for this response object");
        }

        originWriter = new UnsafeCharArrayWriter();
        writer = new PrintWriter(originWriter);
        return writer;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (os != null) {
            return os;
        }
        if (writer != null) {
            throw new IllegalStateException("the getWriter method has already been called for this response object");
        }

        originStream = new UnsafeByteArrayOutputStream();
        os = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                originStream.write(b);
            }
        };
        return os;
    }

    public byte[] toByteArray() {
        if (originStream != null) {
            return originStream.toByteArray();
        } else if (originWriter != null) {
            return originWriter.toString().getBytes(getCharacterEncoding());
        } {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    public char[] toCharArray() {
        if (originWriter != null) {
            return originWriter.toCharArray();
        } else if (originStream != null) {
            return originStream.toString(getCharacterEncoding()).toCharArray();
        } else {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
    }

    @Override
    public String toString() {
        if (originWriter != null) {
            return originWriter.toString();
        } else if (originStream != null) {
            return originStream.toString(getCharacterEncoding());
        } else {
            return "";
        }
    }

}
