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
package jetbrick.io.file;

import java.io.File;
import java.io.IOException;

public final class FileUtils {
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long ONE_GB = ONE_MB * 1024L;

    public static long getFileSize(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(file + " does not exist.");
        }
        if (file.isDirectory()) {
            return getFolderSize(file);
        }
        return file.length();
    }

    public static long getFolderSize(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist.");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory.");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        }
        long size = 0L;

        for (File file : files) {
            try {
                if (!isSymbolLink(file)) {
                    size += getFileSize(file);
                    if (size < 0L) {
                        break;// 溢出了
                    }
                }
            } catch (IOException e) {
            }
        }
        return size;
    }

    /**
     * 判断文件是否是符号链接.
     * @throws IOException
     */
    public static boolean isSymbolLink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null.");
        }
        if (File.separatorChar == '\\') { // IS_WINDOWS
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }
        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        }
        return true;
    }
}
