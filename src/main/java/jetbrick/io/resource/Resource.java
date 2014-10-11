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

import java.io.*;
import java.nio.charset.Charset;
import java.net.URI;
import java.net.URL;

public interface Resource {
    public static final String URL_PREFIX_CLASSPATH = "classpath:";
    public static final String URL_PREFIX_FILE = "file:";
    public static final String URL_PREFIX_JAR = "jar:";
    public static final String URL_PREFIX_ZIP = "zip:";
    public static final String URL_PROTOCOL_FILE = "file";
    public static final String URL_PROTOCOL_JAR = "jar";
    public static final String URL_PROTOCOL_ZIP = "zip";
    public static final String URL_PROTOCOL_VFS = "vfs";
    public static final String URL_SEPARATOR_JAR = "!/";

    /**
     * 代表 Resource 名称，默认是 url/file (包含路径)
     */
    public String getPath();

    /**
     * 打开文件输入流.
     */
    public InputStream openStream() throws ResourceNotFoundException;

    /**
     * 获取文件内容.
     */
    public byte[] toByteArray() throws ResourceNotFoundException;

    /**
     * 获取文件内容.
     */
    public char[] toCharArray(Charset charset) throws ResourceNotFoundException;

    /**
     * 获取文件内容.
     */
    public String toString(Charset charset) throws ResourceNotFoundException;

    /**
     * 文件对象.
     */
    public File getFile() throws UnsupportedOperationException;

    /**
     * URI 对象.
     */
    public URI getURI() throws UnsupportedOperationException;

    /**
     * URL 对象.
     */
    public URL getURL() throws UnsupportedOperationException;

    /**
     * 文件名(不包含路径)
     */
    public String getFileName();

    /**
     * 是否存在
     */
    public boolean exist();

    /**
     * 是否是一个目录
     */
    public boolean isDirectory();

    /**
     * 是否是一个文件
     */
    public boolean isFile();

    /**
     * 文件大小 (byte 长度).
     *
     * @return 如果文件不存在，返回 -1.
     */
    public long length();

    /**
     * 最后修改时间.
     *
     * @return 如果文件不存在，返回 0.
     */
    public long lastModified();
}
