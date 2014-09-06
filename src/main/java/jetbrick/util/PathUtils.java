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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.LinkedList;

public final class PathUtils {
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";
    public static final String ZIP_PROTOCOL = "zip";
    public static final String VFS_PROTOCOL = "vfs";
    public static final String FILE_PROTOCOL_PREFIX = "file:";
    public static final String JAR_PROTOCOL_PREFIX = "jar:";
    public static final String ZIP_PROTOCOL_PREFIX = "zip:";
    public static final String VFS_PROTOCOL_PREFIX = "vfs:";
    public static final String JAR_FILE_SEPARATOR = "!/";

    public static URL fileAsUrl(String file) {
        return fileAsUrl(new File(file));
    }

    public static URL fileAsUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static File urlAsFile(URL url) {
        if (url == null) return null;
        return new File(urlAsPath(url));
    }

    public static String urlAsPath(URL url) {
        if (url == null) return null;

        String protocol = url.getProtocol();
        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        if (FILE_PROTOCOL.equals(protocol)) {
            return file;
        } else if (JAR_PROTOCOL.equals(protocol) || ZIP_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            }
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            return file;
        } else if (VFS_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            } else if (file.endsWith("/")) {
                file = file.substring(0, file.length() - 1);
            }
            return file;
        }
        return file;
    }

    /**
     * Returns normalized <code>path</code> (or simply the <code>path</code> if
     * it is already in normalized form). Normalized path does not contain any
     * empty or "." segments or ".." segments preceded by other segment than
     * "..".
     *
     * @param path path to normalize
     * @return normalize path
     */
    public static String normalize(final String path) {
        if (path == null) {
            return null;
        }
        if (!path.contains("./")) {
            return path;
        }
        boolean absolute = path.startsWith("/");
        String[] elements = path.split("/");
        LinkedList<String> list = new LinkedList<String>();
        for (String e : elements) {
            if ("..".equals(e)) {
                if (list.isEmpty() || "..".equals(list.getLast())) {
                    list.add(e);
                } else {
                    list.removeLast();
                }
            } else if (!".".equals(e) && !e.isEmpty()) {
                list.add(e);
            }
        }
        StringBuilder sb = new StringBuilder(path.length());
        if (absolute) {
            sb.append("/");
        }
        int count = 0, last = list.size() - 1;
        for (String e : list) {
            sb.append(e);
            if (count++ < last) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    /**
     * 组合路径.
     */
    public static String concat(final String parent, final String child) {
        if (parent == null) {
            return normalize(child);
        }
        if (child == null) {
            return normalize(parent);
        }
        return normalize(parent + '/' + child);
    }

    /**
     * 计算相对路径.
     */
    public static String getRelativePath(final String path, final String relativePath) {
        if (relativePath.startsWith("/")) {
            return normalize(relativePath);
        }
        int separatorIndex = path.lastIndexOf('/');
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex + 1);
            return normalize(newPath + relativePath);
        } else {
            return normalize(relativePath);
        }
    }

    /**
     * 转为 Unix 样式的路径.
     */
    public static String separatorsToUnix(String path) {
        if (path == null || path.indexOf('\\') == -1) {
            return path;
        }
        return path.replace('\\', '/');
    }

    /**
     * 转为 Windows 样式的路径.
     */
    public static String separatorsToWindows(String path) {
        if (path == null || path.indexOf('/') == -1) {
            return path;
        }
        return path.replace('/', '\\');
    }

    /**
     * 转为系统默认样式的路径.
     */
    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        }
        if (File.separatorChar == '\\') {
            return separatorsToWindows(path);
        }
        return separatorsToUnix(path);
    }
}
