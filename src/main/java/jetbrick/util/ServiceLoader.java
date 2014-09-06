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
package jetbrick.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * A simple service-provider loading facility.
 *
 * <pre><code>
 * JetServiceLoader.load(LoggerFactory.class,
 *      "jetbrick.log.support.Log4jLoggerFactory, org.apache.log4j.Logger",
 *      "jetbrick.log.support.Jdk14LoggerFactory"
 * );
 * </code></pre>
 *
 * Load Service Sequence:
 * <ul>
 * <li>System.getProperty()</li>
 * <li>load from classpath:/jetbrick-services.properties</li>
 * <li>java.util.ServiceLoader.load()</li>
 * <li>defaultService if all classnames are found.</li>
 * </ul>
 */
public abstract class ServiceLoader {
    private static final String DEFAULT_SERVICES_FILE = "jetbrick-services.properties";
    private static final Properties props = getServiceProperties();

    /**
     * @param clazz - The interface or abstract class representing the service
     * @param defaultServiceClass - A service factory class name.
     * @return A found service object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, String... defaultServiceClass) {
        String serviceClass = System.getProperty(clazz.getName());
        if (StringUtils.isBlank(serviceClass)) {
            serviceClass = props.getProperty(clazz.getName());
        }

        if (StringUtils.isBlank(serviceClass)) {
            java.util.ServiceLoader<T> jdkLoader = java.util.ServiceLoader.load(clazz);
            Iterator<T> jdkLoadServices = jdkLoader.iterator();
            if (jdkLoadServices.hasNext()) {
                return jdkLoadServices.next();
            }
        }

        ClassLoader loader = ClassLoaderUtils.getDefault();

        if (StringUtils.isBlank(serviceClass)) {
            if (defaultServiceClass.length == 0) {
                return null;
            }

            for (String className : defaultServiceClass) {
                String[] names = StringUtils.split(className, ',');
                if (names.length == 0) continue;

                boolean found = true;
                for (String name : names) {
                    try {
                        loader.loadClass(name.trim());
                    } catch (ClassNotFoundException e) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    serviceClass = names[0].trim();
                    break;
                }
            }
        }

        try {
            return (T) loader.loadClass(serviceClass).newInstance();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    private static Properties getServiceProperties() {
        Properties props = new Properties();
        ClassLoader loader = ClassLoaderUtils.getDefault();
        try {
            Enumeration<URL> urls = loader.getResources(DEFAULT_SERVICES_FILE);
            while (urls.hasMoreElements()) {
                InputStream is = null;
                try {
                    is = urls.nextElement().openStream();
                    props.load(is);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return props;
    }
}
