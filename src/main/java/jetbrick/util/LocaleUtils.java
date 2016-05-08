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
package jetbrick.util;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleUtils {
    private static Map<String, LocaleInfo> locales = new HashMap<String, LocaleInfo>();

    /**
     * Returns Locale from cache.
     */
    public static Locale getLocale(final String language, final String country, final String variant) {
        LocaleInfo info = lookupLocaleInfo(resolveLocaleCode(language, country, variant));
        return info.locale;
    }

    /**
     * Returns Locale from cache.
     */
    public static Locale getLocale(final String language, final String country) {
        return getLocale(language, country, null);
    }

    /**
     * Returns Locale from cache where Locale may be specified also using language code.
     * Converts a locale string like "en", "en_US" or "en_US_win" to <b>new</b> Java locale object.
     */
    public static Locale getLocale(final String languageCode) {
        LocaleInfo info = lookupLocaleInfo(languageCode);
        return info.locale;
    }

    /**
     * Transforms locale data to locale code. <code>null</code> values are allowed.
     */
    public static String resolveLocaleCode(final String lang, final String country, final String variant) {
        StringBuilder code = new StringBuilder(lang);
        if (country != null && country.length() > 0) {
            code.append('_').append(country);
            if (variant != null && variant.length() > 0) {
                code.append('_').append(variant);
            }
        }
        return code.toString();
    }

    /**
     * Resolves locale code from locale.
     */
    public static String resolveLocaleCode(final Locale locale) {
        return resolveLocaleCode(locale.getLanguage(), locale.getCountry(), locale.getVariant());
    }

    /**
     * Decodes locale code in string array that can be used for <code>Locale</code> constructor.
     */
    public static String[] decodeLocaleCode(final String localeCode) {
        String[] data = StringUtils.split(localeCode, '_');
        String[] result = new String[] { data[0], "", "" };
        if (data.length >= 2) {
            result[1] = data[1];
            if (data.length >= 3) {
                result[2] = data[2];
            }
        }
        return result;
    }

    /**
     * Returns cached <code>DateFormatSymbols</code> instance for specified locale.
     */
    public static DateFormatSymbols getDateFormatSymbols(Locale locale) {
        LocaleInfo info = lookupLocaleInfo(locale);
        DateFormatSymbols dfs = info.dateFormatSymbols;
        if (dfs == null) {
            dfs = new DateFormatSymbols(locale);
            info.dateFormatSymbols = dfs;
        }
        return dfs;
    }

    /**
     * Returns cached <code>NumberFormat</code> instance for specified locale.
     */
    public static NumberFormat getNumberFormat(Locale locale) {
        LocaleInfo info = lookupLocaleInfo(locale);
        NumberFormat nf = info.numberFormat;
        if (nf == null) {
            nf = NumberFormat.getInstance(locale);
            info.numberFormat = nf;
        }
        return nf;
    }

    /**
     * Lookups for locale info and creates new if it doesn't exist.
     */
    protected static LocaleInfo lookupLocaleInfo(final String code) {
        LocaleInfo info = locales.get(code);
        if (info == null) {
            String[] data = decodeLocaleCode(code);
            info = new LocaleInfo(new Locale(data[0], data[1], data[2]));
            locales.put(code, info);
        }
        return info;
    }

    protected static LocaleInfo lookupLocaleInfo(final Locale locale) {
        return lookupLocaleInfo(resolveLocaleCode(locale));
    }

    /**
     * Holds all per-Locale data.
     */
    private static class LocaleInfo {
        final Locale locale;
        DateFormatSymbols dateFormatSymbols;
        NumberFormat numberFormat;

        LocaleInfo(Locale locale) {
            this.locale = locale;
        }
    }
}
