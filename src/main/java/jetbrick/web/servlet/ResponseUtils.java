/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.web.servlet;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletResponse;

public final class ResponseUtils {

    public static void setBufferOff(HttpServletResponse response) {
        // Http 1.0 header
        response.setHeader("Buffer", "false");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        // Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String contentType) {
        if (contentType == null) contentType = "application/x-download";
        response.setContentType(contentType);

        // 中文文件名支持
        try {
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedfileName);
        } catch (UnsupportedEncodingException e) {
        }
    }
}
