/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;

import jetbrick.io.IoUtils;
import jetbrick.util.ClassLoaderUtils;

/**
 * 专门负责载入配置文件.
 *
 * @author Guoqiang Chen
 */
public final class ConfigLoader {
    private static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");
    private final Map<String, String> config;

    public ConfigLoader() {
        config = new HashMap<String, String>(32);
    }

    // -----------------------------------------------------------------
    public ConfigLoader load(String name, String value) {
        config.put(name, value);
        return this;
    }

    public ConfigLoader load(Properties props) {
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            config.put(key, value);
        }
        return this;
    }

    public ConfigLoader load(Map<String, String> map) {
        config.putAll(map);
        return this;
    }

    /**
     * 从文件路径或者classpath路径中载入配置.
     * @param location － 配置文件路径
     * @return this
     */
    public ConfigLoader load(String location) {
        if (location.startsWith("classpath:")) {
            location = location.substring("classpath:".length());
            return loadClasspath(location);
        } else if (location.startsWith("file:")) {
            location = location.substring("file:".length());
            return load(new File(location));
        } else {
            return load(new File(location));
        }
    }

    // 从 URL 载入
    public ConfigLoader load(URL url) {
        String location = url.getPath();
        try {
            location = URLDecoder.decode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        try {
            return loadInputStream(url.openStream(), location);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // 从 classpath 下面载入
    private ConfigLoader loadClasspath(String classpath) {
        if (classpath.startsWith("/")) {
            classpath = classpath.substring(1);
        }
        InputStream is = ClassLoaderUtils.getDefault().getResourceAsStream(classpath);
        return loadInputStream(is, classpath);
    }

    // 从 File 载入
    public ConfigLoader load(File file) {
        try {
            return loadInputStream(new FileInputStream(file), file.getName());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // 载入 web 资源文件
    public ConfigLoader load(String location, ServletContext sc) {
        if (location.startsWith("classpath:") || location.startsWith("file:")) {
            return load(location);
        } else {
            if (location.startsWith("webroot:")) {
                location = location.substring("webroot:".length());
            }
            if (!location.startsWith("/")) {
                location = "/" + location;
            }
            InputStream is = sc.getResourceAsStream(location);
            return loadInputStream(is, location);
        }
    }

    private ConfigLoader loadInputStream(InputStream is, String location) {
        if (is == null) {
            throw new IllegalStateException("InputStream not found: " + location);
        }

        location = location.toLowerCase();
        if (location.endsWith(".xml")) {
            Properties config = XmlPropertiesLoader.load(is, false);
            return load(config);
        } else if (location.endsWith(".props")) {
            Properties config = ExtendPropertiesLoader.load(is);
            return load(config);
        } else {
            try {
                Properties config = new Properties();
                config.load(is);
                load(config);
                return this;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            } finally {
                IoUtils.closeQuietly(is);
            }
        }
    }

    public ConfigLoader loadSystemProperties() {
        return load(System.getProperties());
    }

    public ConfigLoader loadSystemEnvs() {
        return load(System.getenv());
    }

    /**
     * 解析并替换配置文件中 ${name}表达式对应的内容.
     * @return this
     */
    public ConfigLoader resolvePlaceholder() {
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String value = entry.getValue();

            if (value.contains("${")) {
                Matcher matcher = PLACE_HOLDER_PATTERN.matcher(value);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String name = matcher.group(1);
                    String val = null;
                    if (name.startsWith("ENV.")) {
                        name = name.substring(4);
                        val = System.getenv(name);
                    } else {
                        val = config.get(name);
                        if (val == null) {
                            val = System.getProperty(name);
                        }
                    }
                    if (val == null) {
                        throw new IllegalStateException("cannot find variable `" + value + "` in environment variables");
                    }
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
                }
                matcher.appendTail(sb);
                // reset value
                entry.setValue(sb.toString());
            }
        }
        return this;
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(config);
    }

    public Properties asProperties() {
        Properties props = new Properties();
        props.putAll(config);
        return props;
    }

    public Config asConfig() {
        return new Config(config);
    }
}
