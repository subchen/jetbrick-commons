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
package jetbrick.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassLoaderUtils {
    private static final Map<String, String> abbreviationMap;

    /**
     * Returns default class loader (current thread's context)
     */
    public static ClassLoader getDefault() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
        }
        if (loader == null) {
            loader = ClassLoaderUtils.class.getClassLoader();
            if (loader == null) {
                try {
                    // getClassLoader() returning null indicates the bootstrap ClassLoader
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with
                    // null...
                }
            }
        }
        return loader;
    }

    /**
     * Returns all default class loaders
     */
    public static List<ClassLoader> getDefaults() {
        List<ClassLoader> loaders = new ArrayList<ClassLoader>(3);
        ClassLoader loader = null;

        try {
            loader = Thread.currentThread().getContextClassLoader();
            loaders.add(loader);
        } catch (Exception e) {
        }

        loader = ClassLoaderUtils.class.getClassLoader();
        if (loader != null) {
            // getClassLoader() returning null indicates the bootstrap ClassLoader
            loaders.add(loader);
        }

        try {
            loader = ClassLoader.getSystemClassLoader();
            loaders.add(loader);
        } catch (Exception e) {
            // Cannot access system ClassLoader - oh well, maybe the caller can live with
            // null...
        }

        return loaders;
    }

    /**
     * 使用默认的 ClassLoader 去载入类.
     * 
     * @return null if class not found
     */
    public static Class<?> loadClass(final String qualifiedClassName) {
        try {
            return loadClassEx(qualifiedClassName, Collections.<ClassLoader> emptyList());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 使用默认的 ClassLoader 去载入类.
     * 
     * @return null if class not found
     */
    public static Class<?> loadClass(final String qualifiedClassName, ClassLoader loader) {
        try {
            return loadClassEx(qualifiedClassName, Collections.singletonList(loader));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 使用默认的 ClassLoader 去载入类.
     * 
     * @return null if class not found
     */
    public static Class<?> loadClass(final String qualifiedClassName, List<ClassLoader> loaders) {
        try {
            return loadClassEx(qualifiedClassName, loaders);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 使用默认的 ClassLoader 去载入类.
     * 
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClassEx(final String qualifiedClassName) throws ClassNotFoundException {
        return loadClassEx(qualifiedClassName, Collections.<ClassLoader> emptyList());
    }

    /**
     * 使用指定的 ClassLoader 去载入类.
     * 
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClassEx(final String qualifiedClassName, ClassLoader loader) throws ClassNotFoundException {
        return loadClassEx(qualifiedClassName, Collections.singletonList(loader));
    }

    /**
     * 使用指定的 ClassLoader 去载入类.
     * 
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClassEx(final String qualifiedClassName, List<ClassLoader> loaders) throws ClassNotFoundException {
        Validate.notNull(qualifiedClassName, "qualifiedClassName must be not null");

        if (loaders == null || loaders.isEmpty()) {
            loaders = getDefaults();
        }

        // 尝试基本类型
        if (abbreviationMap.containsKey(qualifiedClassName)) {
            String className = '[' + abbreviationMap.get(qualifiedClassName);
            return doLoadClass(className, loaders).getComponentType();
        }

        // 尝试用 Class.forName()
        try {
            String className = getCanonicalClassName(qualifiedClassName);
            return doLoadClass(className, loaders);
        } catch (ClassNotFoundException e) {
        }

        // 尝试当做一个内部类去识别: java.util.Map.Entry --> java.util.Map$Entry
        if (qualifiedClassName.indexOf('$') == -1) {
            int ipos = qualifiedClassName.lastIndexOf('.');
            if (ipos > 0) {
                try {
                    String className = qualifiedClassName.substring(0, ipos) + '$' + qualifiedClassName.substring(ipos + 1);
                    className = getCanonicalClassName(className);
                    return doLoadClass(className, loaders);
                } catch (ClassNotFoundException e) {
                }
            }
        }

        throw new ClassNotFoundException(qualifiedClassName);
    }

    private static Class<?> doLoadClass(String className, List<ClassLoader> loaders) throws ClassNotFoundException {
        for (ClassLoader loader : loaders) {
            try {
                return Class.forName(className, false, loader);
            } catch (ClassNotFoundException e) {
            }
        }

        throw new ClassNotFoundException(className);
    }

    /**
     * 将 Java 类名转为 {@code Class.forName()} 可以载入的类名格式.
     * 
     * <pre>
     * getCanonicalClassName("int") == "int";
     * getCanonicalClassName("int[]") == "[I";
     * getCanonicalClassName("java.lang.String") == "java.lang.String";
     * getCanonicalClassName("java.lang.String[]") == "[Ljava.lang.String;";
     * </pre>
     */
    public static String getCanonicalClassName(String qualifiedClassName) {
        Validate.notNull(qualifiedClassName, "qualifiedClassName must be not null");

        String name = StringUtils.deleteWhitespace(qualifiedClassName);
        if (name.endsWith("[]")) {
            StringBuilder sb = new StringBuilder();

            while (name.endsWith("[]")) {
                name = name.substring(0, name.length() - 2);
                sb.append('[');
            }

            String abbreviation = abbreviationMap.get(name);
            if (abbreviation != null) {
                sb.append(abbreviation);
            } else {
                sb.append('L').append(name).append(';');
            }

            name = sb.toString();
        }
        return name;
    }

    /**
     * Finds the resource with the given name.
     * 
     * @param name - The resource name
     * @return A URL object for reading the resource, or null if the resource could
     *         not be found
     */
    public static URL getResource(String name) {
        return getResource(name, null);
    }

    /**
     * Finds the resource with the given name.
     * 
     * @param name - The resource name
     * @return A URL object for reading the resource, or null if the resource could
     *         not be found
     */
    public static URL getResource(String name, ClassLoader classLoader) {
        Validate.notNull(name, "resourceName must be not null");

        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (classLoader != null) {
            URL url = classLoader.getResource(name);
            if (url != null) {
                return url;
            }
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null && loader != classLoader) {
            URL url = loader.getResource(name);
            if (url != null) {
                return url;
            }
        }

        return ClassLoader.getSystemResource(name);
    }

    /**
     * Returns an input stream for reading the specified resource.
     */
    public static InputStream getResourceAsStream(String name) throws IOException {
        return getResourceAsStream(name, null);
    }

    /**
     * Returns an input stream for reading the specified resource.
     */
    public static InputStream getResourceAsStream(String name, ClassLoader classLoader) throws IOException {
        URL url = getResource(name, classLoader);
        if (url != null) {
            return url.openStream();
        }
        return null;
    }

    /**
     * Returns an input stream for reading the specified class.
     */
    public static InputStream getClassAsStream(Class<?> clazz) throws IOException {
        return getResourceAsStream(getClassFileName(clazz), clazz.getClassLoader());
    }

    /**
     * Returns an input stream for reading the specified class.
     */
    public static InputStream getClassAsStream(String qualifiedClassName) throws IOException {
        return getResourceAsStream(getClassFileName(qualifiedClassName));
    }

    /**
     * 获取一个 class 所代表的文件名
     */
    public static String getClassFileName(Class<?> clazz) {
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return getClassFileName(clazz.getName());
    }

    /**
     * 获取一个 class 所代表的文件名
     */
    public static String getClassFileName(String qualifiedClassName) {
        return qualifiedClassName.replace('.', '/') + ".class";
    }

    static {
        abbreviationMap = new HashMap<String, String>();
        abbreviationMap.put("boolean", "Z");
        abbreviationMap.put("byte", "B");
        abbreviationMap.put("short", "S");
        abbreviationMap.put("char", "C");
        abbreviationMap.put("int", "I");
        abbreviationMap.put("long", "J");
        abbreviationMap.put("float", "F");
        abbreviationMap.put("double", "D");
    }
}
