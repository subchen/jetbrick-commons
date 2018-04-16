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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class StringUtils {

    // Empty checks
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     */
    public static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is not empty ("") and not null.
     * </p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     */
    public static boolean isNotEmpty(final String s) {
        return s != null && s.length() > 0;
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     */
    public static boolean isBlank(final String s) {
        int len;
        if (s == null || (len = s.length()) == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (Character.isWhitespace(s.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if a String is not empty (""), not null and not whitespace
     * only.
     * </p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     */
    public static boolean isNotBlank(final String s) {
        return !isBlank(s);
    }

    public static byte[] getBytes(String s, String charsetName) {
        if (s == null) {
            return null;
        }
        try {
            return s.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getUtf8Bytes(String s) {
        return s.getBytes(CharsetUtils.UTF_8);
    }

    // Trim
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String,
     * handling {@code null} by returning {@code null}.
     * </p>
     *
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32.
     * </p>
     *
     * <pre>
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * </pre>
     */
    public static String trim(final String s) {
        return s == null ? null : s.trim();
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String
     * returning {@code null} if the String is empty ("") after the trim or if
     * it is {@code null}.
     *
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32.
     * </p>
     *
     * <pre>
     * StringUtils.trimToNull(null)          = null
     * StringUtils.trimToNull("")            = null
     * StringUtils.trimToNull("     ")       = null
     * StringUtils.trimToNull("abc")         = "abc"
     * StringUtils.trimToNull("    abc    ") = "abc"
     * </pre>
     */
    public static String trimToNull(final String s) {
        final String ts = trim(s);
        return ts == null || ts.length() == 0 ? null : ts;
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String
     * returning an empty String ("") if the String is empty ("") after the trim
     * or if it is {@code null}.
     *
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32.
     * </p>
     *
     * <pre>
     * StringUtils.trimToEmpty(null)          = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     */
    public static String trimToEmpty(final String s) {
        return s == null ? "" : s.trim();
    }

    public static String[] trim(String[] ss) {
        if (ss != null) {
            for (int i = 0; i < ss.length; i++) {
                String string = ss[i];
                if (string != null) {
                    ss[i] = string.trim();
                }
            }
        }
        return ss;
    }

    // Defaults
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Returns either the passed in String, or if the String is {@code null},
     * the value of {@code defaultStr}.
     * </p>
     *
     * <pre>
     * StringUtils.defaultIfNull(null, "NULL")  = "NULL"
     * StringUtils.defaultIfNull("", "NULL")    = ""
     * StringUtils.defaultIfNull("bat", "NULL") = "bat"
     * </pre>
     */
    public static String defaultIfNull(final String s, final String defaultStr) {
        return s == null ? defaultStr : s;
    }

    /**
     * <p>
     * Returns either the passed in String, or if the String is
     * empty or {@code null}, the value of {@code defaultStr}.
     * </p>
     *
     * <pre>
     * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
     * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
     * StringUtils.defaultIfEmpty(" ", "NULL")   = " "
     * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
     * StringUtils.defaultIfEmpty("", null)      = null
     * </pre>
     */
    public static String defaultIfEmpty(final String s, final String defaultStr) {
        return isEmpty(s) ? defaultStr : s;
    }

    /**
     * <p>
     * Returns either the passed in String, or if the String is
     * whitespace, empty ("") or {@code null}, the value of {@code defaultStr}.
     * </p>
     *
     * <pre>
     * StringUtils.defaultIfBlank(null, "NULL")  = "NULL"
     * StringUtils.defaultIfBlank("", "NULL")    = "NULL"
     * StringUtils.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringUtils.defaultIfBlank("bat", "NULL") = "bat"
     * StringUtils.defaultIfBlank("", null)      = null
     * </pre>
     */
    public static String defaultIfBlank(final String s, final String defaultStr) {
        return isBlank(s) ? defaultStr : s;
    }

    // Equals
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered to be equal. The comparison is case sensitive.
     * </p>
     *
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     */
    public static boolean equals(final String s1, final String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * <p>
     * Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered equal. Comparison is case insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        if (s1 == null || s2 == null) {
            return s1 == s2;
        } else if (s1 == s2) {
            return true;
        } else if (s1.length() != s2.length()) {
            return false;
        } else {
            return s1.equals(s2);
        }
    }

    // startsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String starts with a specified prefix.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered to be equal. The comparison is case sensitive.
     * </p>
     *
     * <pre>
     * StringUtils.startsWith(null, null)      = true
     * StringUtils.startsWith(null, "abc")     = false
     * StringUtils.startsWith("abcdef", null)  = false
     * StringUtils.startsWith("abcdef", "abc") = true
     * StringUtils.startsWith("ABCDEF", "abc") = false
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param s
     *            the String to check, may be null
     * @param prefix
     *            the prefix to find, may be null
     * @return {@code true} if the String starts with the prefix, case
     *         sensitive, or both {@code null}
     */
    public static boolean startsWith(final String s, final String prefix) {
        return startsWith(s, prefix, false);
    }

    /**
     * <p>
     * Case insensitive check if a String starts with a specified prefix.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered to be equal. The comparison is case insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abc")     = false
     * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
     * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     */
    public static boolean startsWithIgnoreCase(final String s, final String prefix) {
        return startsWith(s, prefix, true);
    }

    /**
     * <p>
     * Check if a String starts with a specified prefix (optionally case
     * insensitive).
     * </p>
     *
     * @see java.lang.String#startsWith(String)
     */
    private static boolean startsWith(final String s, final String prefix, final boolean ignoreCase) {
        if (s == null || prefix == null) {
            return s == null && prefix == null;
        }
        if (prefix.length() > s.length()) {
            return false;
        }
        return s.toString().regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());
    }

    public static int startsWithOne(String s, String... dest) {
        for (int i = 0; i < dest.length; i++) {
            String m = dest[i];
            if (m != null) {
                if (s.startsWith(m)) return i;
            }
        }
        return -1;
    }

    public static int startsWithOneIgnoreCase(String s, String... dest) {
        for (int i = 0; i < dest.length; i++) {
            String m = dest[i];
            if (m != null) {
                if (startsWithIgnoreCase(s, m)) return i;
            }
        }
        return -1;
    }

    public static boolean startsWithChar(final String s, final char c) {
        if (s == null || s.length() == 0) {
            return false;
        }
        return s.charAt(0) == c;
    }

    // endsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String ends with a specified suffix.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered to be equal. The comparison is case sensitive.
     * </p>
     *
     * <pre>
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "def")     = false
     * StringUtils.endsWith("abcdef", null)  = false
     * StringUtils.endsWith("abcdef", "def") = true
     * StringUtils.endsWith("ABCDEF", "def") = false
     * StringUtils.endsWith("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     */
    public static boolean endsWith(final String s, final String suffix) {
        return endsWith(s, suffix, false);
    }

    /**
     * <p>
     * Case insensitive check if a String ends with a specified suffix.
     * </p>
     *
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references
     * are considered to be equal. The comparison is case insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "def")     = false
     * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     */
    public static boolean endsWithIgnoreCase(final String s, final String suffix) {
        return endsWith(s, suffix, true);
    }

    /**
     * <p>
     * Check if a String ends with a specified suffix (optionally case
     * insensitive).
     * </p>
     *
     * @see java.lang.String#endsWith(String)
     */
    private static boolean endsWith(final String s, final String suffix, final boolean ignoreCase) {
        if (s == null || suffix == null) {
            return s == null && suffix == null;
        }
        if (suffix.length() > s.length()) {
            return false;
        }
        final int strOffset = s.length() - suffix.length();
        return s.toString().regionMatches(ignoreCase, strOffset, suffix.toString(), 0, suffix.length());
    }

    public static int endsWithOne(String src, String... dest) {
        for (int i = 0; i < dest.length; i++) {
            String m = dest[i];
            if (m != null) {
                if (src.endsWith(m)) return i;
            }
        }
        return -1;
    }

    public static int endsWithOneIgnoreCase(String s, String... dest) {
        for (int i = 0; i < dest.length; i++) {
            String m = dest[i];
            if (m != null) {
                if (endsWithIgnoreCase(s, m)) return i;
            }
        }
        return -1;
    }

    public static boolean endsWithChar(final String s, final char c) {
        if (s == null || s.length() == 0) {
            return false;
        }
        return s.charAt(s.length() - 1) == c;
    }

    // ---------------------------------------------------------------- indexof and ignore cases

    /**
     * Finds first occurrence of a substring in the given source but within limited range [start, end).
     * It is fastest possible code, but still original <code>String.indexOf(String, int)</code>
     * is much faster (since it uses char[] value directly) and should be used when no range is needed.
     *
     * @param s                source string for examination
     * @param substr                substring to find
     * @param startIndex        starting index
     * @param endIndex                ending index
     * @return index of founded substring or -1 if substring not found
     */
    public static int indexOf(String s, String substr, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = s.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }
        int sublen = substr.length();
        if (sublen == 0) {
            return startIndex > srclen ? srclen : startIndex;
        }

        int total = endIndex - sublen + 1;
        char c = substr.charAt(0);
        mainloop: for (int i = startIndex; i < total; i++) {
            if (s.charAt(i) != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (substr.charAt(j) != s.charAt(k)) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence of a character in the given source but within limited range (start, end].
     */
    public static int indexOf(String s, char c, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = s.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }
        for (int i = startIndex; i < endIndex; i++) {
            if (s.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of a character in the given source but within limited range (start, end].
     */
    public static int indexOfIgnoreCase(String s, char c, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = s.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }
        c = Character.toLowerCase(c);
        for (int i = startIndex; i < endIndex; i++) {
            if (Character.toLowerCase(s.charAt(i)) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds first index of a substring in the given source string with ignored case.
     *
     * @param s    source string for examination
     * @param substr   substring to find
     *
     * @return index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int indexOfIgnoreCase(String s, String substr) {
        return indexOfIgnoreCase(s, substr, 0, s.length());
    }

    /**
     * Finds first index of a substring in the given source string with ignored
     * case. This seems to be the fastest way doing this, with common string
     * length and content (of course, with no use of Boyer-Mayer type of
     * algorithms). Other implementations are slower: getting char array first,
     * lower casing the source string, using String.regionMatch etc.
     *
     * @param s        source string for examination
     * @param substr       substring to find
     * @param startIndex starting index from where search begins
     *
     * @return index of founded substring or -1 if substring is not found
     */
    public static int indexOfIgnoreCase(String s, String substr, int startIndex) {
        return indexOfIgnoreCase(s, substr, startIndex, s.length());
    }

    /**
     * Finds first index of a substring in the given source string and range with
     * ignored case.
     *
     * @param s                source string for examination
     * @param substr                substring to find
     * @param startIndex        starting index from where search begins
     * @param endIndex                endint index
     * @return index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int indexOfIgnoreCase(String s, String substr, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = s.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }

        int sublen = substr.length();
        if (sublen == 0) {
            return startIndex > srclen ? srclen : startIndex;
        }
        substr = substr.toLowerCase();
        int total = endIndex - sublen + 1;
        char c = substr.charAt(0);
        mainloop: for (int i = startIndex; i < total; i++) {
            if (Character.toLowerCase(s.charAt(i)) != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                char source = Character.toLowerCase(s.charAt(k));
                if (substr.charAt(j) != source) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the very first index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code> if
     * noting found.
     *
     * @param s      source string
     * @param arr    string array
     */
    public static int[] indexOf(String s, String arr[]) {
        return indexOf(s, arr, 0);
    }

    /**
     * Finds the very first index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param arr    string array
     * @param start  starting position
     */
    public static int[] indexOf(String s, String arr[], int start) {
        int arrLen = arr.length;
        int index = Integer.MAX_VALUE;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = s.indexOf(arr[j], start);
            if (i != -1) {
                if (i < index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    /**
     * Finds the very first index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code> if
     * noting found.
     *
     * @param s      source string
     * @param c      char array
     */
    public static int[] indexOf(String s, char c[]) {
        return indexOf(s, c, 0);
    }

    /**
     * Finds the very first index of a char from the specified array. It
     * returns an int[2] where int[0] represents the char index and int[1]
     * represents position where char was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param c      char array
     * @param start  starting position
     */
    public static int[] indexOf(String s, char c[], int start) {
        int arrLen = c.length;
        int index = Integer.MAX_VALUE;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = s.indexOf(c[j], start);
            if (i != -1) {
                if (i < index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    /**
     * Finds the very first index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param arr    string array
     */
    public static int[] indexOfIgnoreCase(String s, String arr[]) {
        return indexOfIgnoreCase(s, arr, 0);
    }

    /**
     * Finds the very first index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param arr    string array
     * @param start  starting position
     */
    public static int[] indexOfIgnoreCase(String s, String arr[], int start) {
        int arrLen = arr.length;
        int index = Integer.MAX_VALUE;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = indexOfIgnoreCase(s, arr[j], start);
            if (i != -1) {
                if (i < index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    /**
     * Finds last index of a substring in the given source string with ignored
     * case.
     *
     * @param s      source string
     * @param substr   substring to find
     *
     * @return last index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     * @see #lastIndexOfIgnoreCase(String, String, int)
     */
    public static int lastIndexOfIgnoreCase(String s, String substr) {
        return lastIndexOfIgnoreCase(s, substr, s.length(), 0);
    }

    /**
     * Finds last index of a substring in the given source string with ignored
     * case.
     *
     * @param s        source string for examination
     * @param substr       substring to find
     * @param startIndex starting index from where search begins
     *
     * @return last index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int lastIndexOfIgnoreCase(String s, String substr, int startIndex) {
        return lastIndexOfIgnoreCase(s, substr, startIndex, 0);
    }

    /**
     * Finds last index of a substring in the given source string with ignored
     * case in specified range.
     *
     * @param s                source to examine
     * @param sub                substring to find
     * @param startIndex        starting index
     * @param endIndex                end index
     * @return last index of founded substring or -1 if substring is not found
     */
    public static int lastIndexOfIgnoreCase(String s, String sub, int startIndex, int endIndex) {
        int sublen = sub.length();
        int srclen = s.length();
        if (sublen == 0) {
            return startIndex > srclen ? srclen : (startIndex < -1 ? -1 : startIndex);
        }
        sub = sub.toLowerCase();
        int total = srclen - sublen;
        if (total < 0) {
            return -1;
        }
        if (startIndex >= total) {
            startIndex = total;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        char c = sub.charAt(0);
        mainloop: for (int i = startIndex; i >= endIndex; i--) {
            if (Character.toLowerCase(s.charAt(i)) != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                char source = Character.toLowerCase(s.charAt(k));
                if (sub.charAt(j) != source) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds last index of a substring in the given source string in specified range [end, start]
     * See {@link #indexOf(String, String, int, int)}  for details about the speed.
     *
     * @param s                source to examine
     * @param sub                substring to find
     * @param startIndex        starting index
     * @param endIndex                end index
     * @return last index of founded substring or -1 if substring is not found
     */
    public static int lastIndexOf(String s, String sub, int startIndex, int endIndex) {
        int sublen = sub.length();
        int srclen = s.length();
        if (sublen == 0) {
            return startIndex > srclen ? srclen : (startIndex < -1 ? -1 : startIndex);
        }
        int total = srclen - sublen;
        if (total < 0) {
            return -1;
        }
        if (startIndex >= total) {
            startIndex = total;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        char c = sub.charAt(0);
        mainloop: for (int i = startIndex; i >= endIndex; i--) {
            if (s.charAt(i) != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub.charAt(j) != s.charAt(k)) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds last index of a character in the given source string in specified range [end, start]
     */
    public static int lastIndexOf(String s, char c, int startIndex, int endIndex) {
        int total = s.length() - 1;
        if (total < 0) {
            return -1;
        }
        if (startIndex >= total) {
            startIndex = total;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        for (int i = startIndex; i >= endIndex; i--) {
            if (s.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds last index of a character in the given source string in specified range [end, start]
     */
    public static int lastIndexOfIgnoreCase(String s, char c, int startIndex, int endIndex) {
        int total = s.length() - 1;
        if (total < 0) {
            return -1;
        }
        if (startIndex >= total) {
            startIndex = total;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        c = Character.toLowerCase(c);
        for (int i = startIndex; i >= endIndex; i--) {
            if (Character.toLowerCase(s.charAt(i)) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param arr    string array
     */
    public static int[] lastIndexOf(String s, String arr[]) {
        return lastIndexOf(s, arr, s.length());
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s         source string
     * @param arr       string array
     * @param fromIndex starting position
     */
    public static int[] lastIndexOf(String s, String arr[], int fromIndex) {
        int arrLen = arr.length;
        int index = -1;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = s.lastIndexOf(arr[j], fromIndex);
            if (i != -1) {
                if (i > index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param c      char array
     */
    public static int[] lastIndexOf(String s, char c[]) {
        return lastIndexOf(s, c, s.length());
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s         source string
     * @param c         char array
     * @param fromIndex starting position
     */
    public static int[] lastIndexOf(String s, char c[], int fromIndex) {
        int arrLen = c.length;
        int index = -1;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = s.lastIndexOf(c[j], fromIndex);
            if (i != -1) {
                if (i > index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s      source string
     * @param arr    string array
     *
     * @return int[2]
     */
    public static int[] lastIndexOfIgnoreCase(String s, String arr[]) {
        return lastIndexOfIgnoreCase(s, arr, s.length());
    }

    /**
     * Finds the very last index of a substring from the specified array. It
     * returns an int[2] where int[0] represents the substring index and int[1]
     * represents position where substring was found. Returns <code>null</code>
     * if noting found.
     *
     * @param s         source string
     * @param arr       string array
     * @param fromIndex starting position
     */
    public static int[] lastIndexOfIgnoreCase(String s, String arr[], int fromIndex) {
        int arrLen = arr.length;
        int index = -1;
        int last = -1;
        for (int j = 0; j < arrLen; j++) {
            int i = lastIndexOfIgnoreCase(s, arr[j], fromIndex);
            if (i != -1) {
                if (i > index) {
                    index = i;
                    last = j;
                }
            }
        }
        return last == -1 ? null : new int[] { last, index };
    }

    // Substring
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets a substring from the specified String avoiding exceptions.
     * </p>
     *
     * <p>
     * A negative start position can be used to start {@code n} characters from
     * the end of the String.
     * </p>
     *
     * <p>
     * A {@code null} String will return {@code null}. An empty ("") String will
     * return "".
     * </p>
     *
     * <pre>
     * StringUtils.substring(null, *)   = null
     * StringUtils.substring("", *)     = ""
     * StringUtils.substring("abc", 0)  = "abc"
     * StringUtils.substring("abc", 2)  = "c"
     * StringUtils.substring("abc", 4)  = ""
     * StringUtils.substring("abc", -2) = "bc"
     * StringUtils.substring("abc", -4) = "abc"
     * </pre>
     */
    public static String substring(final String s, int start) {
        if (s == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = s.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > s.length()) {
            return "";
        }

        return s.substring(start);
    }

    /**
     * <p>
     * Gets a substring from the specified String avoiding exceptions.
     * </p>
     *
     * <p>
     * A negative start position can be used to start/end {@code n} characters
     * from the end of the String.
     * </p>
     *
     * <p>
     * The returned substring starts with the character in the {@code start}
     * position and ends before the {@code end} position. All position counting
     * is zero-based -- i.e., to start at the beginning of the string use
     * {@code start = 0}. Negative start and end positions can be used to
     * specify offsets relative to the end of the String.
     * </p>
     *
     * <p>
     * If {@code start} is not strictly to the left of {@code end}, "" is
     * returned.
     * </p>
     *
     * <pre>
     * StringUtils.substring(null, *, *)    = null
     * StringUtils.substring("", * ,  *)    = "";
     * StringUtils.substring("abc", 0, 2)   = "ab"
     * StringUtils.substring("abc", 2, 0)   = ""
     * StringUtils.substring("abc", 2, 4)   = "c"
     * StringUtils.substring("abc", 4, 6)   = ""
     * StringUtils.substring("abc", 2, 2)   = ""
     * StringUtils.substring("abc", -2, -1) = "b"
     * StringUtils.substring("abc", -4, 2)  = "ab"
     * </pre>
     */
    public static String substring(final String s, int start, int end) {
        if (s == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = s.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = s.length() + start; // remember start is negative
        }

        // check length next
        if (end > s.length()) {
            end = s.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return s.substring(start, end);
    }

    // SubStringAfter/SubStringBefore
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the substring before the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBefore(null, *)      = null
     * StringUtils.substringBefore("", *)        = ""
     * StringUtils.substringBefore("abc", "a")   = ""
     * StringUtils.substringBefore("abcba", "b") = "a"
     * StringUtils.substringBefore("abc", "c")   = "ab"
     * StringUtils.substringBefore("abc", "d")   = "abc"
     * StringUtils.substringBefore("abc", "")    = ""
     * StringUtils.substringBefore("abc", null)  = "abc"
     * </pre>
     */
    public static String substringBefore(final String s, final String separator) {
        if (isEmpty(s) || separator == null) {
            return s;
        }
        if (separator.isEmpty()) {
            return "";
        }
        final int pos = s.indexOf(separator);
        if (pos < 0) {
            return s;
        }
        return s.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the empty string if the input string is not {@code null}.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     */
    public static String substringAfter(final String s, final String separator) {
        if (isEmpty(s)) {
            return s;
        }
        if (separator == null) {
            return "";
        }
        final int pos = s.indexOf(separator);
        if (pos < 0) {
            return "";
        }
        return s.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the substring before the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBeforeLast(null, *)      = null
     * StringUtils.substringBeforeLast("", *)        = ""
     * StringUtils.substringBeforeLast("abcba", "b") = "abc"
     * StringUtils.substringBeforeLast("abc", "c")   = "ab"
     * StringUtils.substringBeforeLast("a", "a")     = ""
     * StringUtils.substringBeforeLast("a", "z")     = "a"
     * StringUtils.substringBeforeLast("a", null)    = "a"
     * StringUtils.substringBeforeLast("a", "")      = "a"
     * </pre>
     */
    public static String substringBeforeLast(final String s, final String separator) {
        if (isEmpty(s) || isEmpty(separator)) {
            return s;
        }
        final int pos = s.lastIndexOf(separator);
        if (pos < 0) {
            return s;
        }
        return s.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the empty string if the input string is not
     * {@code null}.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     */
    public static String substringAfterLast(final String s, final String separator) {
        if (isEmpty(s)) {
            return s;
        }
        if (isEmpty(separator)) {
            return "";
        }
        final int pos = s.lastIndexOf(separator);
        if (pos < 0 || pos == s.length() - separator.length()) {
            return "";
        }
        return s.substring(pos + separator.length());
    }

    // Substring between
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the String that is nested in between two instances of the same
     * String.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null} tag
     * returns {@code null}.
     * </p>
     *
     * <pre>
     * StringUtils.substringBetween(null, *)            = null
     * StringUtils.substringBetween("", "")             = ""
     * StringUtils.substringBetween("", "tag")          = null
     * StringUtils.substringBetween("tagabctag", null)  = null
     * StringUtils.substringBetween("tagabctag", "")    = ""
     * StringUtils.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     */
    public static String substringBetween(final String s, final String tag) {
        return substringBetween(s, tag, tag);
    }

    /**
     * <p>
     * Gets the String that is nested in between two Strings. Only the first
     * match is returned.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null}
     * open/close returns {@code null} (no match). An empty ("") open and close
     * returns an empty string.
     * </p>
     *
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     */
    public static String substringBetween(final String s, final String open, final String close) {
        if (s == null || open == null || close == null) {
            return null;
        }
        final int start = s.indexOf(open);
        if (start >= 0) {
            final int end = s.indexOf(close, start + open.length());
            if (end >= 0) {
                return s.substring(start + open.length(), end);
            }
        }
        return null;
    }

    // Left/Right/Mid
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the leftmost {@code len} characters of a String.
     * </p>
     *
     * <p>
     * If {@code len} characters are not available, or the String is
     * {@code null}, the String will be returned without an exception. An empty
     * String is returned if len is negative.
     * </p>
     *
     * <pre>
     * StringUtils.left(null, *)    = null
     * StringUtils.left(*, -ve)     = ""
     * StringUtils.left("", *)      = ""
     * StringUtils.left("abc", 0)   = ""
     * StringUtils.left("abc", 2)   = "ab"
     * StringUtils.left("abc", 4)   = "abc"
     * </pre>
     */
    public static String left(final String s, final int len) {
        if (s == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (s.length() <= len) {
            return s;
        }
        return s.substring(0, len);
    }

    /**
     * <p>
     * Gets the rightmost {@code len} characters of a String.
     * </p>
     *
     * <p>
     * If {@code len} characters are not available, or the String is
     * {@code null}, the String will be returned without an an exception. An
     * empty String is returned if len is negative.
     * </p>
     *
     * <pre>
     * StringUtils.right(null, *)    = null
     * StringUtils.right(*, -ve)     = ""
     * StringUtils.right("", *)      = ""
     * StringUtils.right("abc", 0)   = ""
     * StringUtils.right("abc", 2)   = "bc"
     * StringUtils.right("abc", 4)   = "abc"
     * </pre>
     */
    public static String right(final String s, final int len) {
        if (s == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (s.length() <= len) {
            return s;
        }
        return s.substring(s.length() - len);
    }

    /**
     * <p>
     * Gets {@code len} characters from the middle of a String.
     * </p>
     *
     * <p>
     * If {@code len} characters are not available, the remainder of the String
     * will be returned without an exception. If the String is {@code null},
     * {@code null} will be returned. An empty String is returned if len is
     * negative or exceeds the length of {@code str}.
     * </p>
     *
     * <pre>
     * StringUtils.mid(null, *, *)    = null
     * StringUtils.mid(*, *, -ve)     = ""
     * StringUtils.mid("", 0, *)      = ""
     * StringUtils.mid("abc", 0, 2)   = "ab"
     * StringUtils.mid("abc", 0, 4)   = "abc"
     * StringUtils.mid("abc", 2, 4)   = "c"
     * StringUtils.mid("abc", 4, 2)   = ""
     * StringUtils.mid("abc", -2, 2)  = "ab"
     * </pre>
     */
    public static String mid(final String s, int pos, final int len) {
        if (s == null) {
            return null;
        }
        if (len < 0 || pos > s.length()) {
            return "";
        }
        if (pos < 0) {
            pos = 0;
        }
        if (s.length() <= pos + len) {
            return s.substring(pos);
        }
        return s.substring(pos, pos + len);
    }

    // Padding
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Repeat a String {@code repeat} times to form a new String.
     * </p>
     *
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * </pre>
     */
    public static String repeat(final String s, final int repeat) {
        if (s == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        final int inputLength = s.length();
        if (repeat == 1 || inputLength == 0) {
            return s;
        }

        final int outputLength = inputLength * repeat;
        switch (inputLength) {
        case 1:
            return repeat(s.charAt(0), repeat);
        case 2:
            final char ch0 = s.charAt(0);
            final char ch1 = s.charAt(1);
            final char[] output2 = new char[outputLength];
            for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                output2[i] = ch0;
                output2[i + 1] = ch1;
            }
            return new String(output2);
        default:
            final StringBuilder buf = new StringBuilder(outputLength);
            for (int i = 0; i < repeat; i++) {
                buf.append(s);
            }
            return buf.toString();
        }
    }

    /**
     * <p>
     * Repeat a String {@code repeat} times to form a new String, with a String
     * separator injected each time.
     * </p>
     *
     * <pre>
     * StringUtils.repeat(null, null, 2) = null
     * StringUtils.repeat(null, "x", 2)  = null
     * StringUtils.repeat("", null, 0)   = ""
     * StringUtils.repeat("", "", 2)     = ""
     * StringUtils.repeat("", "x", 3)    = "xxx"
     * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
     * </pre>
     */
    public static String repeat(final String s, final String separator, final int repeat) {
        if (s == null || separator == null) {
            return repeat(s, repeat);
        }
        // given that repeat(String, int) is quite optimized, better to rely on
        // it than try and splice this into it
        final String result = repeat(s + separator, repeat);
        return removeEnd(result, separator);
    }

    /**
     * <p>
     * Returns padding using the specified delimiter repeated to a given length.
     * </p>
     *
     * <pre>
     * StringUtils.repeat('e', 0)  = ""
     * StringUtils.repeat('e', 3)  = "eee"
     * StringUtils.repeat('e', -2) = ""
     * </pre>
     *
     * <p>
     * Note: this method doesn't not support padding with <a
     * href="http://www.unicode.org/glossary/#supplementary_character">Unicode
     * Supplementary Characters</a> as they require a pair of {@code char}s to
     * be represented. If you are needing to support full I18N of your
     * applications consider using {@link #repeat(String, int)} instead.
     * </p>
     * @see #repeat(String, int)
     */
    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if ((padLen == 1) && (pads <= 8192)) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        }
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }
        return new String(padding).concat(str);
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if ((padLen == 1) && (pads <= 8192)) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        }
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }
        return str.concat(new String(padding));
    }

    public static String center(String str, int size) {
        return center(str, size, ' ');
    }

    public static String center(String str, int size, char padChar) {
        if ((str == null) || (size <= 0)) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    public static String reverse(String s) {
        StringBuilder result = new StringBuilder(s.length());
        for (int i = s.length() - 1; i >= 0; i--) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    // Delete
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <pre>
     * StringUtils.deleteWhitespace(null)         = null
     * StringUtils.deleteWhitespace("")           = ""
     * StringUtils.deleteWhitespace("abc")        = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param s
     *            the String to delete whitespace from, may be null
     * @return the String without whitespaces, {@code null} if null String input
     */
    public static String deleteWhitespace(final String s) {
        if (isEmpty(s)) {
            return s;
        }
        final int sz = s.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                chs[count++] = s.charAt(i);
            }
        }
        if (count == sz) {
            return s;
        }
        return new String(chs, 0, count);
    }

    // Remove
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Removes a substring only if it is at the beginning of a source string,
     * otherwise returns the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} search string
     * will return the source string.
     * </p>
     *
     * <pre>
     * StringUtils.removeStart(null, *)      = null
     * StringUtils.removeStart("", *)        = ""
     * StringUtils.removeStart(*, null)      = *
     * StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
     * StringUtils.removeStart("domain.com", "www.")       = "domain.com"
     * StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeStart("abc", "")    = "abc"
     * </pre>
     */
    public static String removeStart(final String s, final String remove) {
        if (isEmpty(s) || isEmpty(remove)) {
            return s;
        }
        if (s.startsWith(remove)) {
            return s.substring(remove.length());
        }
        return s;
    }

    /**
     * <p>
     * Case insensitive removal of a substring if it is at the beginning of a
     * source string, otherwise returns the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} search string
     * will return the source string.
     * </p>
     *
     * <pre>
     * StringUtils.removeStartIgnoreCase(null, *)      = null
     * StringUtils.removeStartIgnoreCase("", *)        = ""
     * StringUtils.removeStartIgnoreCase(*, null)      = *
     * StringUtils.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
     * StringUtils.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
     * StringUtils.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
     * StringUtils.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeStartIgnoreCase("abc", "")    = "abc"
     * </pre>
     */
    public static String removeStartIgnoreCase(final String s, final String remove) {
        if (isEmpty(s) || isEmpty(remove)) {
            return s;
        }
        if (startsWithIgnoreCase(s, remove)) {
            return s.substring(remove.length());
        }
        return s;
    }

    /**
     * <p>
     * Removes a substring only if it is at the end of a source string,
     * otherwise returns the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} search string
     * will return the source string.
     * </p>
     *
     * <pre>
     * StringUtils.removeEnd(null, *)      = null
     * StringUtils.removeEnd("", *)        = ""
     * StringUtils.removeEnd(*, null)      = *
     * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeEnd("abc", "")    = "abc"
     * </pre>
     */
    public static String removeEnd(final String s, final String remove) {
        if (isEmpty(s) || isEmpty(remove)) {
            return s;
        }
        if (s.endsWith(remove)) {
            return s.substring(0, s.length() - remove.length());
        }
        return s;
    }

    /**
     * <p>
     * Case insensitive removal of a substring if it is at the end of a source
     * string, otherwise returns the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} search string
     * will return the source string.
     * </p>
     *
     * <pre>
     * StringUtils.removeEndIgnoreCase(null, *)      = null
     * StringUtils.removeEndIgnoreCase("", *)        = ""
     * StringUtils.removeEndIgnoreCase(*, null)      = *
     * StringUtils.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
     * StringUtils.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
     * StringUtils.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     * StringUtils.removeEndIgnoreCase("abc", "")    = "abc"
     * StringUtils.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
     * StringUtils.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
     * </pre>
     */
    public static String removeEndIgnoreCase(final String s, final String remove) {
        if (isEmpty(s) || isEmpty(remove)) {
            return s;
        }
        if (endsWithIgnoreCase(s, remove)) {
            return s.substring(0, s.length() - remove.length());
        }
        return s;
    }

    /**
     * <p>
     * Removes all occurrences of a substring from within the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string. A {@code null} remove string
     * will return the source string. An empty ("") remove string will return
     * the source string.
     * </p>
     *
     * <pre>
     * StringUtils.remove(null, *)        = null
     * StringUtils.remove("", *)          = ""
     * StringUtils.remove(*, null)        = *
     * StringUtils.remove(*, "")          = *
     * StringUtils.remove("queued", "ue") = "qd"
     * StringUtils.remove("queued", "zz") = "queued"
     * </pre>
     */
    public static String remove(final String s, final String remove) {
        if (isEmpty(s) || isEmpty(remove)) {
            return s;
        }
        return replace(s, remove, "", -1);
    }

    /**
     * <p>
     * Removes all occurrences of a character from within the source string.
     * </p>
     *
     * <p>
     * A {@code null} source string will return {@code null}. An empty ("")
     * source string will return the empty string.
     * </p>
     *
     * <pre>
     * StringUtils.remove(null, *)       = null
     * StringUtils.remove("", *)         = ""
     * StringUtils.remove("queued", 'u') = "qeed"
     * StringUtils.remove("queued", 'z') = "queued"
     * </pre>
     */
    public static String remove(final String s, final char remove) {
        if (isEmpty(s) || s.indexOf(remove) < 0) {
            return s;
        }
        final char[] chars = s.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * Removes all characters contained in provided string.
     *
     * @param s    source string
     * @param chars  string containing characters to remove
     */
    public static String removeChars(String s, String chars) {
        int i = s.length();
        StringBuilder sb = new StringBuilder(i);
        for (int j = 0; j < i; j++) {
            char c = s.charAt(j);
            if (chars.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Removes set of characters from string.
     *
     * @param s    string
     * @param chars  characters to remove
     */
    public static String removeChars(String s, char... chars) {
        int i = s.length();
        StringBuilder sb = new StringBuilder(i);
        mainloop: for (int j = 0; j < i; j++) {
            char c = s.charAt(j);
            for (char aChar : chars) {
                if (c == aChar) {
                    continue mainloop;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String prefix(String s, String prefix) {
        if (!s.startsWith(prefix)) {
            s = prefix + s;
        }
        return s;
    }

    public static String suffix(String s, String suffix) {
        if (!s.endsWith(suffix)) {
            s = s + suffix;
        }
        return s;
    }

    // Replacing
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Replaces a String with another String inside a larger String, once.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     * </p>
     *
     * <pre>
     * StringUtils.replaceOnce(null, *, *)        = null
     * StringUtils.replaceOnce("", *, *)          = ""
     * StringUtils.replaceOnce("any", null, *)    = "any"
     * StringUtils.replaceOnce("any", *, null)    = "any"
     * StringUtils.replaceOnce("any", "", *)      = "any"
     * StringUtils.replaceOnce("aba", "a", null)  = "aba"
     * StringUtils.replaceOnce("aba", "a", "")    = "ba"
     * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int
     *      max)
     */
    public static String replaceOnce(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * Replaces each substring of the source String that matches the given
     * regular expression with the given replacement using the
     * {@link Pattern#DOTALL} option. DOTALL is also know as single-line mode in
     * Perl. This call is also equivalent to:
     * <ul>
     * <li>{@code source.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
     * <li>
     * {@code Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement)}
     * </li>
     * </ul>
     *
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String replacePattern(final String source, final String regex, final String replacement) {
        return Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement);
    }

    /**
     * <p>
     * Replaces all occurrences of a String within another String.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     * </p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, for the
     * first {@code max} values of the search String.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     * </p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     */
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end < 0) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end >= 0) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     *
     * <pre>
     *  StringUtils.replaceEach(null, *, *)        = null
     *  StringUtils.replaceEach("", *, *)          = ""
     *  StringUtils.replaceEach("aba", null, null) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0]) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null)  = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     *  (example of how it does not repeat)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     */
    public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     *
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalStateException
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
     * </pre>
     */
    public static String replaceEachRepeatedly(final String text, final String[] searchList, final String[] replacementList) {
        // timeToLive should be 0 if not used or nothing to replace, else it's
        // the length of the replace array
        final int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     *
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalStateException
     * </pre>
     */
    private static String replaceEach(final String text, final String[] searchList, final String[] replacementList, final boolean repeat, final int timeToLive) {
        if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - " + "output of one loop is the input of another");
        }

        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
        }

        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].isEmpty() || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have
        // to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their
        // corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            final int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        final StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].isEmpty() || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        final int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        final String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    // Replace, character based
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Replaces all occurrences of a character in a String with another. This is
     * a null-safe version of {@link String#replace(char, char)}.
     * </p>
     *
     * <p>
     * A {@code null} string input returns {@code null}. An empty ("") string
     * input returns an empty string.
     * </p>
     *
     * <pre>
     * StringUtils.replaceChars(null, *, *)        = null
     * StringUtils.replaceChars("", *, *)          = ""
     * StringUtils.replaceChars("abcba", 'b', 'y') = "aycya"
     * StringUtils.replaceChars("abcba", 'z', 'y') = "abcba"
     * </pre>
     */
    public static String replaceChars(final String str, final char searchChar, final char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    /**
     * <p>
     * Replaces multiple characters in a String in one go. This method can also
     * be used to delete characters.
     * </p>
     *
     * <p>
     * For example:<br>
     * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>
     * .
     * </p>
     *
     * <p>
     * A {@code null} string input returns {@code null}. An empty ("") string
     * input returns an empty string. A null or empty set of search characters
     * returns the input string.
     * </p>
     *
     * <p>
     * The length of the search characters should normally equal the length of
     * the replace characters. If the search characters is longer, then the
     * extra search characters are deleted. If the search characters is shorter,
     * then the extra replace characters are ignored.
     * </p>
     *
     * <pre>
     * StringUtils.replaceChars(null, *, *)           = null
     * StringUtils.replaceChars("", *, *)             = ""
     * StringUtils.replaceChars("abc", null, *)       = "abc"
     * StringUtils.replaceChars("abc", "", *)         = "abc"
     * StringUtils.replaceChars("abc", "b", null)     = "ac"
     * StringUtils.replaceChars("abc", "b", "")       = "ac"
     * StringUtils.replaceChars("abcba", "bc", "yz")  = "ayzya"
     * StringUtils.replaceChars("abcba", "bc", "y")   = "ayya"
     * StringUtils.replaceChars("abcba", "bc", "yzx") = "ayzya"
     * </pre>
     */
    public static String replaceChars(final String str, final String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        boolean modified = false;
        final int replaceCharsLength = replaceChars.length();
        final int strLength = str.length();
        final StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            final char ch = str.charAt(i);
            final int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    public static String[] split(String str, String delimiter) {
        if (str == null) return null;
        List<String> results = new ArrayList<String>();

        int ipos = 0, lastpos = 0;
        while ((ipos = str.indexOf(delimiter, lastpos)) != -1) {
            results.add(str.substring(lastpos, ipos));
            lastpos = ipos + delimiter.length();
        }
        results.add(str.substring(lastpos));
        return results.toArray(new String[results.size()]);
    }

    public static String[] split(String str, char delimiter) {
        if (str == null) return null;
        List<String> results = new ArrayList<String>();

        int ipos = 0, lastpos = 0;
        while ((ipos = str.indexOf(delimiter, lastpos)) != -1) {
            results.add(str.substring(lastpos, ipos));
            lastpos = ipos + 1;
        }
        results.add(str.substring(lastpos));
        return results.toArray(new String[results.size()]);
    }

    public static String[] splitChars(String str, String delimiters) {
        if (str == null) return null;
        List<String> results = new ArrayList<String>();

        int lastpos = 0;
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (delimiters.indexOf(c) != -1) {
                results.add(str.substring(lastpos, i));
                lastpos = i + 1;
            }
        }
        results.add(str.substring(lastpos));
        return results.toArray(new String[results.size()]);
    }

    public static String[] splitChars(String str, char... delimiters) {
        if (str == null) return null;
        List<String> results = new ArrayList<String>();

        int lastpos = 0;
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (ArrayUtils.contains(delimiters, c)) {
                results.add(str.substring(lastpos, i));
                lastpos = i + 1;
            }
        }
        results.add(str.substring(lastpos));
        return results.toArray(new String[results.size()]);
    }

    /**
     * 按逗号分隔，两边加引号的不分割（CSV 规则）.
     */
    public static String[] splitCSV(String str) {
        if (str == null) return null;
        String[] parts = StringUtils.split(str, ',');

        List<String> results = new ArrayList<String>();
        for (int i = 0; i < parts.length; i++) {
            String s = parts[i].trim();
            if (s.length() == 0) {
                results.add(s);
            } else {
                char c = s.charAt(0);
                if (c == '"' || c == '\'' || c == '`') {
                    StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    while (i + 1 < parts.length) {
                        if (sb.length() > 1 && s.length() > 0 && s.charAt(s.length() - 1) == c) {
                            break;
                        }
                        s = parts[++i];
                        sb.append(',').append(s);
                    }
                    s = sb.toString().trim();
                    if (s.charAt(s.length() - 1) == c) {
                        s = s.substring(1, s.length() - 1);
                    }
                    results.add(s);
                } else {
                    results.add(s);
                }
            }
        }
        return results.toArray(new String[results.size()]);
    }

    public static String join(String... parts) {
        StringBuilder sb = new StringBuilder(parts.length);
        for (String part : parts) {
            sb.append(part);
        }
        return sb.toString();
    }

    public static String join(Iterable<?> elements, String separator) {
        if (elements == null) {
            return "";
        }
        return join(elements.iterator(), separator);
    }

    public static String join(Iterator<?> elements, String separator) {
        if (elements == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        while (elements.hasNext()) {
            Object o = elements.next();
            if (sb.length() > 0 && separator != null) {
                sb.append(separator);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    public static String join(Object[] elements, String separator) {
        if (elements == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : elements) {
            if (sb.length() > 0 && separator != null) {
                sb.append(separator);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    public static int count(String source, String substr) {
        return count(source, substr, 0);
    }

    public static int count(String source, String substr, int start) {
        if (source == null || source.length() == 0) {
            return 0;
        }

        int count = 0;
        int j = start;
        int sublen = substr.length();
        if (sublen == 0) return 0;
        while (true) {
            int i = source.indexOf(substr, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + sublen;
        }
        return count;
    }

    public static int count(String source, char c) {
        return count(source, c, 0);
    }

    public static int count(String source, char c, int start) {
        if (source == null || source.length() == 0) {
            return 0;
        }

        int count = 0;
        int j = start;
        while (true) {
            int i = source.indexOf(c, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + 1;
        }
        return count;
    }
}
