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
package jetbrick.util;

public final class FilenameUtils {

    /**
     * 得到文件名.
     */
    public static String getFilename(final String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = getFileSeparatorIndex(path);
        return separatorIndex == -1 ? path.substring(separatorIndex + 1) : path;
    }

    /**
     * 得到文件扩展名.
     */
    public static String getFileExtension(final String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf('.');
        if (extIndex == -1) {
            return null;
        }
        int folderIndex = getFileSeparatorIndex(path);
        if (folderIndex > extIndex) {
            return null;
        }
        return path.substring(extIndex + 1);
    }

    /**
     * 得到文件 basename (去掉路径，去掉扩展名).
     */
    public static String getFileBasename(final String filename) {
        return removeFileExtension(getFilename(filename));
    }

    /**
     * 删除文件扩展名.
     */
    public static String removeFileExtension(final String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf('.');
        if (extIndex == -1) {
            return path;
        }
        int folderIndex = getFileSeparatorIndex(path);
        if (folderIndex > extIndex) {
            return path;
        }
        return path.substring(0, extIndex);
    }

    private static int getFileSeparatorIndex(String path) {
        for (int i = path.length() - 1; i >= 0; i--) {
            char c = path.charAt(i);
            if (c == '/' || c == '\\') {
                return i;
            }
        }
        return -1;
    }
}
