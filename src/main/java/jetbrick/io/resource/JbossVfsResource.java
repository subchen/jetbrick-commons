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
package jetbrick.io.resource;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;
import jetbrick.util.ExceptionUtils;

// jboss/wildfly vfs url
public final class JbossVfsResource extends AbstractResource {
    private final Object resource;

    public JbossVfsResource(URL url) {
        this.resource = vfsInvokeMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
    }

    private JbossVfsResource(Object resource) {
        this.resource = resource;
        setPath(getURL().toString());
    }

    @Override
    public InputStream openStream() {
        if (resource == null) {
            throw new ResourceNotFoundException();
        }
        
        InputStream is = (InputStream) vfsInvokeMethod(VIRTUAL_FILE_METHOD_OPEN_STREAM, resource, EMPTY_PARAMETERS);
        if (is == null) {
            throw new ResourceNotFoundException(getURL().toString());
        }
        return is;
    }

    @Override
    public File getFile() {
        return (File) vfsInvokeMethod(VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE, resource, EMPTY_PARAMETERS);
    }

    @Override
    public URI getURI() {
        return (URI) vfsInvokeMethod(VIRTUAL_FILE_METHOD_TO_URI, resource, EMPTY_PARAMETERS);
    }

    @Override
    public URL getURL() {
        return (URL) vfsInvokeMethod(VIRTUAL_FILE_METHOD_TO_URL, resource, EMPTY_PARAMETERS);
    }

    @Override
    public boolean isDirectory() {
        return (Boolean) vfsInvokeMethod(VIRTUAL_FILE_METHOD_IS_DIRECTORY, resource, EMPTY_PARAMETERS);
    }

    @Override
    public boolean isFile() {
        return (Boolean) vfsInvokeMethod(VIRTUAL_FILE_METHOD_IS_FILE, resource, EMPTY_PARAMETERS);
    }

    @Override
    public String getFileName() {
        return (String) vfsInvokeMethod(VIRTUAL_FILE_METHOD_GET_NAME, resource, EMPTY_PARAMETERS);
    }

    @Override
    public boolean exist() {
        return (Boolean) vfsInvokeMethod(VIRTUAL_FILE_METHOD_EXISTS, resource, EMPTY_PARAMETERS);
    }

    @Override
    public long length() {
        return (Long) vfsInvokeMethod(VIRTUAL_FILE_METHOD_GET_SIZE, resource, EMPTY_PARAMETERS);
    }

    @Override
    public long lastModified() {
        return (Long) vfsInvokeMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, resource, EMPTY_PARAMETERS);
    }

    public List<JbossVfsResource> getChildren() {
        @SuppressWarnings("unchecked")
        List<Object> resources = (List<Object>) vfsInvokeMethod(VIRTUAL_FILE_METHOD_GET_CHILDREN, resource, EMPTY_PARAMETERS);
        if (resources == null || resources.size() == 0) {
            return Collections.emptyList();
        }
        List<JbossVfsResource> children = new ArrayList<JbossVfsResource>(resources.size());
        for (Object resource : children) {
            children.add(new JbossVfsResource(resource));
        }
        return children;
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    //---------------------------------------------------------------------
    private static final Object[] EMPTY_PARAMETERS = new Object[0];
    private static final Method VFS_METHOD_GET_ROOT_URL;
    private static final Method VIRTUAL_FILE_METHOD_EXISTS;
    private static final Method VIRTUAL_FILE_METHOD_IS_DIRECTORY;
    private static final Method VIRTUAL_FILE_METHOD_IS_FILE;
    private static final Method VIRTUAL_FILE_METHOD_OPEN_STREAM;
    private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;
    private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
    private static final Method VIRTUAL_FILE_METHOD_TO_URL;
    private static final Method VIRTUAL_FILE_METHOD_TO_URI;
    private static final Method VIRTUAL_FILE_METHOD_GET_NAME;
    private static final Method VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE;
    private static final Method VIRTUAL_FILE_METHOD_GET_CHILDREN;

    static {
        ClassLoader loader = JbossVfsResource.class.getClassLoader();
        try {
            Class<?> vfsClass = loader.loadClass("org.jboss.vfs.VFS");
            VFS_METHOD_GET_ROOT_URL = vfsGetMethod(vfsClass, "getChild", new Class[] { URL.class });

            Class<?> virtualFileClass = loader.loadClass("org.jboss.vfs.VirtualFile");
            VIRTUAL_FILE_METHOD_EXISTS = vfsGetMethod(virtualFileClass, "exists", null);
            VIRTUAL_FILE_METHOD_IS_DIRECTORY = vfsGetMethod(virtualFileClass, "isDirectory", null);
            VIRTUAL_FILE_METHOD_IS_FILE = vfsGetMethod(virtualFileClass, "isFile", null);
            VIRTUAL_FILE_METHOD_OPEN_STREAM = vfsGetMethod(virtualFileClass, "openStream", null);
            VIRTUAL_FILE_METHOD_GET_SIZE = vfsGetMethod(virtualFileClass, "getSize", null);
            VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = vfsGetMethod(virtualFileClass, "getLastModified", null);
            VIRTUAL_FILE_METHOD_TO_URI = vfsGetMethod(virtualFileClass, "toURI", null);
            VIRTUAL_FILE_METHOD_TO_URL = vfsGetMethod(virtualFileClass, "toURL", null);
            VIRTUAL_FILE_METHOD_GET_NAME = vfsGetMethod(virtualFileClass, "getName", null);
            VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE = vfsGetMethod(virtualFileClass, "getPhysicalFile", null);
            VIRTUAL_FILE_METHOD_GET_CHILDREN = vfsGetMethod(virtualFileClass, "getChildren", null);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not detect JBoss VFS infrastructure", e);
        }
    }

    static Method vfsGetMethod(Class<?> clazz, String name, Class<?>[] parameterTypes) {
        Class<?> type = clazz;
        while (type != null) {
            for (Method method : type.getDeclaredMethods()) {
                if (name.equals(method.getName()) && (parameterTypes == null || Arrays.equals(parameterTypes, method.getParameterTypes()))) {
                    method.setAccessible(true);
                    return method;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    static Object vfsInvokeMethod(Method method, Object target, Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
}
