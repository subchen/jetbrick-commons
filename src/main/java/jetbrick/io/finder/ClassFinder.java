/**
 * Copyright 2013-2015 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.io.finder;

import java.lang.annotation.Annotation;
import java.util.*;
import jetbrick.util.ClassLoaderUtils;
import jetbrick.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClassFinder {
    private static final Logger log = LoggerFactory.getLogger(ClassFinder.class);

    public static Set<Class<?>> getClasses(Collection<String> packageNames, boolean recursive, Collection<Class<? extends Annotation>> annotations, final boolean skiperrors) {
        final AnnotationClassReader reader = new AnnotationClassReader();
        for (Class<? extends Annotation> annotation : annotations) {
            reader.addAnnotation(annotation);
        }

        final ClassLoader loader = ClassLoaderUtils.getDefault();
        final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

        FileFinder finder = new FileFinder() {
            @Override
            public void visitFile(ResourceEntry file) {
                if (file.isJavaClass()) {
                    if (reader.isAnnotationed(file.openStream())) {
                        addClass(file.getQualifiedJavaName());
                    }
                }
            }

            private void addClass(String qualifiedClassName) {
                try {
                    Class<?> klass = loader.loadClass(qualifiedClassName);
                    classes.add(klass);
                } catch (ClassNotFoundException e) {
                } catch (Exception e) {
                    if (skiperrors) {
                        log.warn("Class load error.", e);
                    } else {
                        throw ExceptionUtils.unchecked(e);
                    }
                }
            }
        };

        finder.lookupClasspath(packageNames, recursive);

        return classes;
    }
}
