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

import java.io.Writer;

public class NullWriter extends Writer {
    public static final NullWriter INSTANCE = new NullWriter();

    @Override
    public Writer append(char c) {
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) {
        return this;
    }

    @Override
    public Writer append(CharSequence csq) {
        return this;
    }

    @Override
    public void write(int idx) {
    }

    @Override
    public void write(char[] chr) {
    }

    @Override
    public void write(char[] chr, int st, int end) {
    }

    @Override
    public void write(String str) {
    }

    @Override
    public void write(String str, int st, int end) {
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
