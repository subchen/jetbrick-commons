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
package jetbrick.web.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import jetbrick.util.*;
import jetbrick.util.codec.Base64Utils;

public final class RequestUtils {
    /**
     * 获取相对 ContextPath 的 requestURI
     */
    public static String getPathInfo(HttpServletRequest request) {
        String uri = (String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
        if (uri != null) {
            String pathInfo = (String) request.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
            if (pathInfo != null) {
                uri += pathInfo;
            }
        } else {
            uri = request.getServletPath();
            String pathInfo = request.getPathInfo();
            if (pathInfo != null) {
                uri += pathInfo;
            }
        }
        if (uri == null || uri.length() == 0) {
            uri = "/";
        }
        return uri;
    }

    public static String getParametersAsJSON(HttpServletRequest request) {
        Map<String, Object> json = new HashMap<String, Object>();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            String[] value = request.getParameterValues(name);
            if (value == null || value.length == 0) {
                continue;
            }
            if (value.length > 1) {
                json.put(name, value);
            } else {
                json.put(name, value[0]);
            }
        }
        return JSONUtils.toJSONString(json);
    }

    public static String getUrlWithParameters(HttpServletRequest request, String excludeNames) {
        String[] excludeNamesArray = ArrayUtils.EMPTY_STRING_ARRAY;
        if (excludeNames != null) {
            excludeNamesArray = StringUtils.split(excludeNames, ',');
        }

        StringBuffer sb = new StringBuffer();
        String encoding = request.getCharacterEncoding();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            if (ArrayUtils.contains(excludeNamesArray, name)) continue;

            String[] value = request.getParameterValues(name);
            if (value == null) continue;
            for (int i = 0; i < value.length; i++) {
                try {
                    if (sb.length() > 0) sb.append('&');
                    sb.append(name).append('=').append(URLEncoder.encode(value[i], encoding));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        if (sb.length() > 0) sb.insert(0, '?');
        sb.insert(0, request.getContextPath() + getPathInfo(request));

        return sb.toString();
    }

    public static String getClientIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static int getParameterAsInteger(HttpServletRequest request, String key, int defaultValue) {
        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getParameterAsLong(HttpServletRequest request, String key, long defaultValue) {
        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getParameterAsBoolean(HttpServletRequest request, String key, boolean defaultValue) {
        String value = request.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return (value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("y") || value.equals("on"));
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + Base64Utils.encodeToString(encode.getBytes());
    }

    public static String getAuthUsername(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }
        String encoded = header.substring(header.indexOf(' ') + 1);
        String decoded = Base64Utils.decodeToString(encoded);
        return decoded.substring(0, decoded.indexOf(':'));
    }

    public static String getAuthPassword(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }
        String encoded = header.substring(header.indexOf(' ') + 1);
        String decoded = Base64Utils.decodeToString(encoded);
        return decoded.substring(decoded.indexOf(':') + 1);
    }

    // 是否是Ajax请求数据
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || request.getHeader("accept").contains("application/json");
    }

    // 是否是Pjax请求数据: https://github.com/defunkt/jquery-pjax
    public static boolean isPjaxRequest(HttpServletRequest request) {
        return StringUtils.isNotEmpty(request.getHeader("X-PJAX"));
    }

    // 是否是Flash请求数据
    public static boolean isFlashRequest(HttpServletRequest request) {
        return "Shockwave Flash".equals(request.getHeader("User-Agent")) || StringUtils.isNotEmpty(request.getHeader("x-flash-version"));
    }

    // 是否是文件上传
    public static boolean isMultipartRequest(HttpServletRequest request) {
        String type = request.getHeader("Content-Type");
        return (type != null) && (type.startsWith("multipart/form-data"));
    }

    public static boolean isGzipSupported(HttpServletRequest request) {
        String browserEncodings = request.getHeader("Accept-Encoding");
        return (browserEncodings != null) && (browserEncodings.contains("gzip"));
    }

    // 判断是否为搜索引擎
    public static boolean isRobot(HttpServletRequest request) {
        String ua = request.getHeader("user-agent");
        if (StringUtils.isBlank(ua)) return false;
        //@formatter:off
        return (ua != null && (ua.contains("Baiduspider")
                            || ua.contains("Googlebot")
                            || ua.contains("sogou")
                            || ua.contains("sina")
                            || ua.contains("iaskspider")
                            || ua.contains("ia_archiver")
                            || ua.contains("Sosospider")
                            || ua.contains("YoudaoBot")
                            || ua.contains("yahoo")
                            || ua.contains("yodao")
                            || ua.contains("MSNBot")
                            || ua.contains("spider")
                            || ua.contains("Twiceler")
                            || ua.contains("Sosoimagespider")
                            || ua.contains("naver.com/robots")
                            || ua.contains("Nutch")
                            || ua.contains("spider")));
        //@formatter:on
    }

}
