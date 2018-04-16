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
package jetbrick.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {
    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINITE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINITE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

    public static final String STD_DATE_PATTERN = "yyyy-MM-dd";
    public static final String STD_TIME_PATTERN = "HH:mm:ss";
    public static final String STD_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String RFC822_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
    public static final String W3C_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    //@formatter:off
	private static final String[] RFC822_PATTENRS = {
		"EEE, dd MMM yy HH:mm:ss z",
		"EEE, dd MMM yy HH:mm z",
		"dd MMM yy HH:mm:ss z",
		"dd MMM yy HH:mm z",
	};

	private static final String[] W3CDATETIME_PATTERNS = {
		"yyyy-MM-dd'T'HH:mm:ss.SSSz",
		"yyyy-MM-dd't'HH:mm:ss.SSSz",
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
		"yyyy-MM-dd't'HH:mm:ss.SSS'z'",
		"yyyy-MM-dd'T'HH:mm:ssz",
		"yyyy-MM-dd't'HH:mm:ssz",
		"yyyy-MM-dd'T'HH:mm:ss'Z'",
		"yyyy-MM-dd't'HH:mm:ss'z'",
		"yyyy-MM-dd'T'HH:mmz",
		"yyyy-MM'T'HH:mmz",
		"yyyy'T'HH:mmz",
		"yyyy-MM-dd't'HH:mmz",
		"yyyy-MM-dd'T'HH:mm'Z'",
		"yyyy-MM-dd't'HH:mm'z'",
		"yyyy-MM-dd",
		"yyyy-MM",
		"yyyy",
	};

	private static final String[] STD_PATTERNS = {
		"yyyy-MM-dd HH:mm:ss,SSS",
		"yyyy-MM-dd HH:mm:ss.SSS",
		"yyyy-MM-dd HH:mm:ss",
		"yyyy-MM-dd HH:mm",
		"yyyy-MM-dd",
		"yyyy/MM/dd HH:mm:ss,SSS",
		"yyyy/MM/dd HH:mm:ss.SSS",
		"yyyy/MM/dd HH:mm:ss",
		"yyyy/MM/dd HH:mm",
		"yyyy/MM/dd",
		"yyyyMMddHHmmss",
		"yyyyMMdd",
		"hh:mm:ss,SSS",
		"hh:mm:ss.SSS",
		"hh:mm:ss",
	};
	//@formatter:on

    /**
     * 用指定的格式格式化当前时间.
     */
    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 用指定的格式格式化指定时间.
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 用尝试多种格式解析日期时间.
     *
     * @param date  时间字符串
     * @return      如果无法解析，那么返回 {@code null}
     */
    public static Date parse(String date) {
        Date d = parse(date, STD_PATTERNS);
        if (d == null) {
            d = parseRFC822Date(date);
        }
        if (d == null) {
            d = parseW3CDateTime(date);
        }
        if (d == null) {
            try {
                d = DateFormat.getInstance().parse(date);
            } catch (ParseException e) {
                d = null;
            }
        }
        return d;
    }

    /**
     * 用指定的格式解析日期时间.
     *
     * @param date      时间字符串
     * @param pattern   see {@link java.text.SimpleDateFormat}
     * @return          如果无法解析，那么返回 {@code null}
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);

        try {
            ParsePosition pp = new ParsePosition(0);
            Date d = df.parse(date, pp);
            if (d != null && pp.getIndex() == date.length()) {
                return d;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 用指定的格式解析日期时间.
     *
     * @param date      时间字符串
     * @param patterns  多个模式，see {@link java.text.SimpleDateFormat}
     * @return          如果无法解析，那么返回 {@code null}
     */
    public static Date parse(String date, String[] patterns) {
        if (date == null || date.length() == 0) {
            return null;
        }

        date = date.trim();
        for (String pattern : patterns) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            df.setLenient(false);
            try {
                ParsePosition pp = new ParsePosition(0);
                Date d = df.parse(date, pp);
                if (d != null && pp.getIndex() == date.length()) {
                    return d;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseRFC822Date(String date) {
        int ipos = date.indexOf(" UT");
        if (ipos > -1) {
            String pre = date.substring(0, ipos);
            String post = date.substring(ipos + 3);
            date = pre + " GMT" + post;
        }
        return parse(date, RFC822_PATTENRS);
    }

    public static Date parseW3CDateTime(String date) {
        // if sDate has time on it, it injects 'GTM' before de TZ displacement
        // to allow the SimpleDateFormat parser to parse it properly
        int tIndex = date.indexOf("T");
        if (tIndex > -1) {
            if (date.endsWith("Z")) {
                date = date.substring(0, date.length() - 1) + "+00:00";
            }
            int tzdIndex = date.indexOf("+", tIndex);
            if (tzdIndex == -1) {
                tzdIndex = date.indexOf("-", tIndex);
            }
            if (tzdIndex > -1) {
                String pre = date.substring(0, tzdIndex);
                int secFraction = pre.indexOf(",");
                if (secFraction > -1) {
                    pre = pre.substring(0, secFraction);
                }
                String post = date.substring(tzdIndex);
                date = pre + "GMT" + post;
            }
        } else {
            date += "T00:00GMT";
        }
        return parse(date, W3CDATETIME_PATTERNS);
    }

    public static String formatRFC822(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(RFC822_DATETIME_PATTERN);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    public static String formatW3CDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(W3C_DATETIME_PATTERN);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }
}
