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

/**
 * Checks whether a path matches a given wildcard pattern.
 * Possible patterns allow to match single characters ('?') or any count of
 * characters ('*'). Wildcard characters can be escaped (by an '\').
 * When matching path, deep tree wildcard also can be used ('**').
 * <p>
 * This method uses recursive matching, as in linux or windows. regexp works the same.
 * This method is very fast, comparing to similar implementations.
 */
public class WildcharPathUtils {
    protected static final String PATH_MATCH = "**";
    protected static final String PATH_SEPARATORS = "/\\";

    /**
     * Matches path against pattern using *, ? and ** wildcards.
     * Both path and the pattern are tokenized on path separators (both \ and /).
     * '**' represents deep tree wildcard, as in Ant.
     */
    public static boolean matchPath(String path, String pattern) {
        String[] pathElements = StringUtils.splitChars(path, PATH_SEPARATORS);
        String[] patternElements = StringUtils.splitChars(pattern, PATH_SEPARATORS);
        return matchTokens(pathElements, patternElements);
    }

    /**
     * Matches path to at least one pattern.
     * Returns index of matched pattern or <code>-1</code> otherwise.
     * @see #matchPath(String, String)
     */
    public static int matchPathOne(String path, String[] patterns) {
        for (int i = 0; i < patterns.length; i++) {
            if (matchPath(path, patterns[i]) == true) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Match tokenized string and pattern.
     */
    protected static boolean matchTokens(String[] tokens, String[] patterns) {
        int patNdxStart = 0;
        int patNdxEnd = patterns.length - 1;
        int tokNdxStart = 0;
        int tokNdxEnd = tokens.length - 1;

        while (patNdxStart <= patNdxEnd && tokNdxStart <= tokNdxEnd) { // find first **
            String patDir = patterns[patNdxStart];
            if (patDir.equals(PATH_MATCH)) {
                break;
            }
            if (!WildcharUtils.match(tokens[tokNdxStart], patDir)) {
                return false;
            }
            patNdxStart++;
            tokNdxStart++;
        }
        if (tokNdxStart > tokNdxEnd) {
            for (int i = patNdxStart; i <= patNdxEnd; i++) { // string is finished
                if (!patterns[i].equals(PATH_MATCH)) {
                    return false;
                }
            }
            return true;
        }
        if (patNdxStart > patNdxEnd) {
            return false; // string is not finished, but pattern is
        }

        while (patNdxStart <= patNdxEnd && tokNdxStart <= tokNdxEnd) { // to the last **
            String patDir = patterns[patNdxEnd];
            if (patDir.equals(PATH_MATCH)) {
                break;
            }
            if (!WildcharUtils.match(tokens[tokNdxEnd], patDir)) {
                return false;
            }
            patNdxEnd--;
            tokNdxEnd--;
        }
        if (tokNdxStart > tokNdxEnd) {
            for (int i = patNdxStart; i <= patNdxEnd; i++) { // string is finished
                if (!patterns[i].equals(PATH_MATCH)) {
                    return false;
                }
            }
            return true;
        }

        while ((patNdxStart != patNdxEnd) && (tokNdxStart <= tokNdxEnd)) {
            int patIdxTmp = -1;
            for (int i = patNdxStart + 1; i <= patNdxEnd; i++) {
                if (patterns[i].equals(PATH_MATCH)) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patNdxStart + 1) {
                patNdxStart++; // skip **/** situation
                continue;
            }
            // find the pattern between padIdxStart & padIdxTmp in str between strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patNdxStart - 1);
            int strLength = (tokNdxEnd - tokNdxStart + 1);
            int ndx = -1;
            strLoop: for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = patterns[patNdxStart + j + 1];
                    String subStr = tokens[tokNdxStart + i + j];
                    if (!WildcharUtils.match(subStr, subPat)) {
                        continue strLoop;
                    }
                }

                ndx = tokNdxStart + i;
                break;
            }

            if (ndx == -1) {
                return false;
            }

            patNdxStart = patIdxTmp;
            tokNdxStart = ndx + patLength;
        }

        for (int i = patNdxStart; i <= patNdxEnd; i++) {
            if (!patterns[i].equals(PATH_MATCH)) {
                return false;
            }
        }

        return true;
    }
}
