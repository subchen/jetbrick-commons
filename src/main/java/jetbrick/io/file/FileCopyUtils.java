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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import jetbrick.io.IoUtils;

public class FileCopyUtils {

    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new IllegalArgumentException("Source file must not be null.");
        }
        if (destFile == null) {
            throw new IllegalArgumentException("Destination file must not be null.");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source file '" + srcFile + "' does not exist.");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory.");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source file '" + srcFile + "' and destination file '" + destFile + "' are the same.");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null && (!parentFile.mkdirs()) && (!parentFile.isDirectory())) {
            throw new IOException("Destination '" + parentFile + "' directory cannot be created.");
        }

        if ((destFile.exists()) && (!destFile.canWrite())) {
            throw new IOException("Destination file '" + destFile + "' exists but is read-only.");
        }
        doCopyFile(srcFile, destFile, true);
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        if (destDir == null) {
            throw new IllegalArgumentException("Destination directory must not be null.");
        }
        if (destDir.exists() && (!destDir.isDirectory())) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory.");
        }
        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile);
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory.");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0L;
            long count = 0L;
            while (pos < size) {
                count = size - pos > FileUtils.ONE_MB ? FileUtils.ONE_MB : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IoUtils.closeQuietly(output);
            IoUtils.closeQuietly(fos);
            IoUtils.closeQuietly(input);
            IoUtils.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'.");
        }

        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, null);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null.");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null.");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist.");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory.");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same.");
        }

        List<String> exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
            if ((srcFiles != null) && (srcFiles.length > 0)) {
                exclusionList = new ArrayList<String>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, true, exclusionList);
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory.");
            }
        } else if ((!destDir.mkdirs()) && (!destDir.isDirectory())) {
            throw new IOException("Destination '" + destDir + "' directory cannot be created.");
        }

        if (!destDir.canWrite()) {
            throw new IOException("Destination '" + destDir + "' cannot be written to.");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if ((exclusionList == null) || (!exclusionList.contains(srcFile.getCanonicalPath()))) {
                if (srcFile.isDirectory())
                    doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
                else {
                    doCopyFile(srcFile, dstFile, preserveFileDate);
                }
            }
        }

        if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }
}
