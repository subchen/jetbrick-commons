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

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import jetbrick.io.IoUtils;
import jetbrick.util.CharsetUtils;
import jetbrick.util.StringEscapeUtils;

// https://gist.github.com/subchen/8470940
final class ExtendPropertiesLoader {
    private static final Charset DEFAULT_CHARSET = CharsetUtils.UTF_8;

    public static Properties load(InputStream is) {
        return load(is, DEFAULT_CHARSET);
    }

    public static Properties load(InputStream is, Charset charset) {
        if (is == null) {
            return EmptyProperties.INSTANCE;
        }
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        return load(new BufferedReader(new InputStreamReader(is, charset)));
    }

    public static Properties load(BufferedReader reader) {
        Properties props = new Properties();

        String line = null;
        String key = null;

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                } else if (line.startsWith("[") && line.endsWith("]")) {
                    key = line.substring(1, line.length() - 1).trim();
                } else {
                    int pos = line.indexOf('=');
                    if (pos <= 0) {
                        continue;
                    }
                    String name = line.substring(0, pos).trim();
                    if (key != null && key.length() > 0) {
                        if ("@".equals(name)) {
                            name = key;
                        } else {
                            name = key + '.' + name;
                        }
                    }
                    String value = line.substring(pos + 1).trim();
                    if (value.startsWith("'''")) {
                        if (value.length() >= 6 && value.endsWith("'''")) {
                            props.put(name, value.substring(3, value.length() - 3));
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(value, 3, value.length()).append('\n');
                            while ((line = reader.readLine()) != null) {
                                line = line.trim();
                                if (line.endsWith("'''")) {
                                    sb.append(line, 0, line.length() - 3);
                                    break;
                                } else {
                                    sb.append(line).append('\n');
                                }
                            }
                            props.put(name, sb.toString());
                        }
                    } else if (value.endsWith("\\") && !value.endsWith("\\\\")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(value, 0, value.length() - 1);
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.startsWith("#")) {
                                continue;
                            } else if (line.endsWith("\\") && !line.endsWith("\\\\")) {
                                sb.append(line, 0, line.length() - 1);
                            } else {
                                sb.append(line);
                                break;
                            }
                        }
                        props.put(name, StringEscapeUtils.unescapeJava(sb.toString()));
                    } else {
                        props.put(name, StringEscapeUtils.unescapeJava(value));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(reader);
        }
        return props;
    }
}
