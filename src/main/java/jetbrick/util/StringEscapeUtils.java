/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class StringEscapeUtils {

    public static String unescapeJava(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        StringBuilder buf = null;
        int len = value.length();
        int len1 = len - 1;
        for (int i = 0; i < len; i++) {
            char ch = value.charAt(i);
            if (ch == '\\' && i < len1) {
                int j = i;
                i++;
                ch = value.charAt(i);
                switch (ch) {
                case '\\':
                    ch = '\\';
                    break;
                case '\"':
                    ch = '\"';
                    break;
                case '\'':
                    ch = '\'';
                    break;
                case 't':
                    ch = '\t';
                    break;
                case 'n':
                    ch = '\n';
                    break;
                case 'r':
                    ch = '\r';
                    break;
                case 'b':
                    ch = '\b';
                    break;
                case 'f':
                    ch = '\f';
                    break;
                case 'u':
                    ch = (char) Integer.parseInt(value.substring(i + 1, i + 5), 16);
                    i = i + 4;
                    break;
                default:
                    j--;
                }
                if (buf == null) {
                    buf = new StringBuilder(len);
                    if (j > 0) {
                        buf.append(value.substring(0, j));
                    }
                }
                buf.append(ch);
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return value;
    }

    public static String escapeJava(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        int len = value.length();
        StringBuilder buf = null;
        for (int i = 0; i < len; i++) {
            char ch = value.charAt(i);
            String rep;
            switch (ch) {
            case '\\':
                rep = "\\\\";
                break;
            case '\"':
                rep = "\\\"";
                break;
            case '\'':
                rep = "\\\'";
                break;
            case '\t':
                rep = "\\t";
                break;
            case '\n':
                rep = "\\n";
                break;
            case '\r':
                rep = "\\r";
                break;
            case '\b':
                rep = "\\b";
                break;
            case '\f':
                rep = "\\f";
                break;
            default:
                rep = null;
                break;
            }
            if (rep != null) {
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append(rep);
            } else {
                if (buf != null) {
                    buf.append(ch);
                }
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return value;
    }

    public static String escapeJavaRegexPattern(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        int len = value.length();
        StringBuilder buf = null;
        for (int i = 0; i < len; i++) {
            char ch = value.charAt(i);
            switch (ch) {
            case '\\':
            case '$':
            case '^':
            case '[':
            case ']':
            case '(':
            case ')':
            case '{':
            case '}':
            case '<':
            case '>':
            case '|':
            case '+':
            case '*':
            case '?':
            case '.':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append('\\').append(ch);
                break;
            default:
                if (buf != null) {
                    buf.append(ch);
                }
            }
        }

        if (buf != null) {
            return buf.toString();
        }
        return value;
    }

    public static String unescapeXml(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        StringBuilder buf = null;
        int len = value.length();
        int len3 = len - 3;
        for (int i = 0; i < len; i++) {
            char ch = value.charAt(i);
            if (ch == '&' && i < len3) {
                int j = i;
                char ch1 = value.charAt(i + 1);
                switch (ch1) {
                case 'l':
                    if (value.charAt(i + 2) == 't' && value.charAt(i + 3) == ';') {
                        i += 3;
                        if (buf == null) {
                            buf = new StringBuilder(len3);
                            if (j > 0) {
                                buf.append(value.substring(0, j));
                            }
                        }
                        buf.append('<');
                    } else if (buf != null) {
                        buf.append('&');
                    }
                    break;
                case 'g':
                    if (value.charAt(i + 2) == 't' && value.charAt(i + 3) == ';') {
                        i += 3;
                        if (buf == null) {
                            buf = new StringBuilder(len3);
                            if (j > 0) {
                                buf.append(value.substring(0, j));
                            }
                        }
                        buf.append('>');
                    } else if (buf != null) {
                        buf.append('&');
                    }
                    break;
                case 'a':
                    int len4 = len - 4;
                    int len5 = len - 5;
                    if (i < len4 && value.charAt(i + 2) == 'm' && value.charAt(i + 3) == 'p' && value.charAt(i + 4) == ';') {
                        i += 4;
                        if (buf == null) {
                            buf = new StringBuilder(len4);
                            if (j > 0) {
                                buf.append(value.substring(0, j));
                            }
                        }
                        buf.append('&');
                    } else if (i < len5 && value.charAt(i + 2) == 'p' && value.charAt(i + 3) == 'o' && value.charAt(i + 4) == 's' && value.charAt(i + 5) == ';') {
                        i += 5;
                        if (buf == null) {
                            buf = new StringBuilder(len5);
                            if (j > 0) {
                                buf.append(value.substring(0, j));
                            }
                        }
                        buf.append('\'');
                    } else if (buf != null) {
                        buf.append('&');
                    }
                    break;
                case 'q':
                    int len_5 = len - 5;
                    if (i < len_5 && value.charAt(i + 2) == 'u' && value.charAt(i + 3) == 'o' && value.charAt(i + 4) == 't' && value.charAt(i + 5) == ';') {
                        i += 5;
                        if (buf == null) {
                            buf = new StringBuilder(len_5);
                            if (j > 0) {
                                buf.append(value.substring(0, j));
                            }
                        }
                        buf.append('\"');
                    } else if (buf != null) {
                        buf.append('&');
                    }
                    break;
                default:
                    if (buf != null) {
                        buf.append('&');
                    }
                    break;
                }
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return value;
    }

    public static String escapeXml(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        int len = value.length();
        StringBuilder buf = null;
        for (int i = 0; i < len; i++) {
            char ch = value.charAt(i);
            switch (ch) {
            case '<':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append("&lt;");
                break;
            case '>':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append("&gt;");
                break;
            case '\"':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append("&quot;");
                break;
            case '\'':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append("&apos;");
                break;
            case '&':
                if (buf == null) {
                    buf = new StringBuilder(len * 2);
                    if (i > 0) {
                        buf.append(value.substring(0, i));
                    }
                }
                buf.append("&amp;");
                break;
            default:
                if (buf != null) {
                    buf.append(ch);
                }
                break;
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return value;
    }

    public static String escapeJavaScript(String value) {
        return escapeJava(value);
    }

    public static String unescapeJavaScript(String value) {
        return unescapeJava(value);
    }

    public static String escapeUrl(String value) {
        return escapeUrl(value, "UTF-8");
    }

    public static String escapeUrl(String value, String encoding) {
        try {
            return value == null ? null : URLEncoder.encode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static String unescapeUrl(String value) {
        return unescapeUrl(value, "UTF-8");
    }

    public static String unescapeUrl(String value, String encoding) {
        try {
            return value == null ? null : URLDecoder.decode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
