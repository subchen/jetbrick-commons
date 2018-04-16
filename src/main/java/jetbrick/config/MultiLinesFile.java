/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import jetbrick.io.IoUtils;

/**
 * 由于 java 自带的 properties 文件格式对于多行的文本支持的不是很完善，这里以更优雅的格式进行支持。
 */
public class MultiLinesFile {
    protected Properties props = new Properties();

    public MultiLinesFile(File file, String encoding) {
        InputStream fs = null;
        try {
            fs = new FileInputStream(file);
            load(new BufferedReader(new InputStreamReader(fs, encoding)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(fs);
        }
    }

    public MultiLinesFile(InputStream is, String encoding) {
        try {
            load(new BufferedReader(new InputStreamReader(is, encoding)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MultiLinesFile(Reader reader) {
        try {
            if (!(reader instanceof BufferedReader)) {
                reader = new BufferedReader(reader);
            }
            load((BufferedReader) reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load(BufferedReader reader) throws IOException {
        String line = null;
        String key = null;
        StringBuilder values = new StringBuilder(64);

        while ((line = reader.readLine()) != null) {
            String str = line.trim();
            if (str.startsWith("#")) {
                continue;
            }
            if (str.startsWith("[") && str.endsWith("]")) {
                if (key != null) { // save last key/value
                    props.put(key, values.toString());
                }
                key = str.substring(1, str.length() - 1).trim();
                values.setLength(0);
            } else {
                values.append(line).append("\n");
            }
        }

        if (key != null) { // save last key/value
            props.put(key, values.toString());
        }
    }

    public boolean exist(String key) {
        return props.containsKey(key);
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public Properties getProperties() {
        return props;
    }
}
