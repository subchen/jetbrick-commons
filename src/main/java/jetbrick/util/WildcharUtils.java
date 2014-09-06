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
package jetbrick.util;

/**
 * Checks whether a string matches a given wildcard pattern.
 * Possible patterns allow to match single characters ('?') or any count of
 * characters ('*'). Wildcard characters can be escaped (by an '\').
 * <p>
 * This method uses recursive matching, as in linux or windows. regexp works the same.
 * This method is very fast, comparing to similar implementations.
 */
public class WildcharUtils {
    /**
     * Checks whether a string matches a given wildcard pattern.
     *
     * @param string    input string
     * @param pattern   pattern to match
     * @return          <code>true</code> if string matches the pattern, otherwise <code>false</code>
     */
    public static boolean match(String string, String pattern) {
        if (string.equals(pattern)) { // speed-up
            return true;
        }
        return match(string, pattern, 0, 0);
    }

    /**
     * Matches string to at least one pattern.
     * Returns index of matched pattern, or <code>-1</code> otherwise.
     * @see #match(String, String)
     */
    public static int matchOne(String src, String[] patterns) {
        for (int i = 0; i < patterns.length; i++) {
            if (match(src, patterns[i]) == true) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Internal matching recursive function.
     */
    private static boolean match(String string, String pattern, int sNdx, int pNdx) {
        int pLen = pattern.length();
        if (pLen == 1) {
            if (pattern.charAt(0) == '*') { // speed-up
                return true;
            }
        }
        int sLen = string.length();
        boolean nextIsNotWildcard = false;

        while (true) {

            // check if end of string and/or pattern occurred
            if ((sNdx >= sLen) == true) { // end of string still may have pending '*' in pattern
                while (pNdx < pLen && pattern.charAt(pNdx) == '*') {
                    pNdx++;
                }
                return pNdx >= pLen;
            }
            if (pNdx >= pLen) { // end of pattern, but not end of the string
                return false;
            }
            char p = pattern.charAt(pNdx); // pattern char

            // perform logic
            if (nextIsNotWildcard == false) {

                if (p == '\\') {
                    pNdx++;
                    nextIsNotWildcard = true;
                    continue;
                }
                if (p == '?') {
                    sNdx++;
                    pNdx++;
                    continue;
                }
                if (p == '*') {
                    char pNext = 0; // next pattern char
                    if (pNdx + 1 < pLen) {
                        pNext = pattern.charAt(pNdx + 1);
                    }
                    if (pNext == '*') { // double '*' have the same effect as one '*'
                        pNdx++;
                        continue;
                    }
                    int i;
                    pNdx++;

                    // find recursively if there is any substring from the end of the
                    // line that matches the rest of the pattern !!!
                    for (i = string.length(); i >= sNdx; i--) {
                        if (match(string, pattern, i, pNdx) == true) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                nextIsNotWildcard = false;
            }

            // check if pattern char and string char are equals
            if (p != string.charAt(sNdx)) {
                return false;
            }

            // everything matches for now, continue
            sNdx++;
            pNdx++;
        }
    }

}
