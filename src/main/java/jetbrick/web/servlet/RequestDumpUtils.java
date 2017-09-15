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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import jetbrick.util.StringUtils;

public final class RequestDumpUtils {

    public static String dump(HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);

        final String FORMAT = "%20s: %s%n";
        final char PADDING_CHAR = '=';
        final int PADDING_SIZE = 60;

        out.println(StringUtils.center(" Request Basic ", PADDING_SIZE, PADDING_CHAR));
        out.printf(FORMAT, "Request Date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(new Date()));
        out.printf(FORMAT, "Request URL", request.getRequestURL());
        out.printf(FORMAT, "QueryString", request.getQueryString());
        out.printf(FORMAT, "Method", request.getMethod());
        out.println();

        out.printf(FORMAT, "CharacterEncoding", request.getCharacterEncoding());
        out.printf(FORMAT, "ContentType", request.getContentType());
        out.printf(FORMAT, "ContentLength", request.getContentLength());
        out.printf(FORMAT, "Locale", request.getLocale());
        out.printf(FORMAT, "RemoteAddr", request.getRemoteAddr());
        out.println();

        out.println(StringUtils.center(" Request Headers ", PADDING_SIZE, PADDING_CHAR));
        Enumeration<String> header = request.getHeaderNames();
        while (header.hasMoreElements()) {
            String name = header.nextElement();
            String value = request.getHeader(name);
            out.printf(FORMAT, name, value);
        }
        out.println();

        out.println(StringUtils.center(" Request Parameters ", PADDING_SIZE, PADDING_CHAR));
        Enumeration<String> param = request.getParameterNames();
        while (param.hasMoreElements()) {
            String name = param.nextElement();
            String value[] = request.getParameterValues(name);
            out.printf(FORMAT, name, StringUtils.join(value, ", "));
        }
        out.println();

        out.println(StringUtils.center(" Request Cookies ", PADDING_SIZE, PADDING_CHAR));
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                out.printf(FORMAT, cookie.getName(), cookie.getValue());
            }
        }
        out.println();

        out.println(StringUtils.repeat(PADDING_CHAR, PADDING_SIZE));
        out.flush();

        return sw.toString();
    }
}
