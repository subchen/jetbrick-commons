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
package jetbrick.io.file;

import java.io.*;

public class FileMoveUtils {

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source file must not be null.");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist.");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory.");
        }
        if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists.");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory.");
        }
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            FileCopyUtils.copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                FileDeleteUtils.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'.");
            }
        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source file must not be null.");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null.");
        }
        if ((!destDir.exists()) && (createDestDir)) {
            destDir.mkdirs();
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "].");
        }

        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory.");
        }
        moveFile(srcFile, new File(destDir, srcFile.getName()));
    }

    public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null.");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null.");
        }
        if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist.");
        }
        if (src.isDirectory()) {
            moveDirectoryToDirectory(src, destDir, createDestDir);
        } else {
            moveFileToDirectory(src, destDir, createDestDir);
        }
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source file must not be null.");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist.");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' is not a directory.");
        }
        if (destDir.exists()) {
            throw new IOException("Destination '" + destDir + "' already exists.");
        }
        boolean rename = srcDir.renameTo(destDir);
        if (!rename) {
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
            }
            FileCopyUtils.copyDirectory(srcDir, destDir);
            FileDeleteUtils.deleteDirectory(srcDir);
            if (srcDir.exists()) throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'.");
        }
    }

    public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source file must not be null.");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null.");
        }
        if ((!destDir.exists()) && (createDestDir)) {
            destDir.mkdirs();
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "].");
        }

        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory.");
        }
        moveDirectory(src, new File(destDir, src.getName()));
    }
}
