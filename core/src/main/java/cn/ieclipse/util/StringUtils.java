/*
 * Copyright (C) 2015-2017 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cn.ieclipse.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * StringUtils
 *
 * @author Jamling
 */
public class StringUtils {

    public static String join(CharSequence delimiter, String... arrays) {
        return TextUtils.join(delimiter, arrays);
    }

    public static String join(CharSequence delimiter, Object[] tokens) {
        return TextUtils.join(delimiter, tokens);
    }

    public static String join(CharSequence delimiter, List<?> list) {
        return TextUtils.join(delimiter, list.toArray());
    }

    public static boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static long getLong(String v, long dft) {
        if (StringUtils.isEmpty(v)) {
            return dft;
        }
        try {
            return Long.parseLong(v);
        } catch (Exception e) {
            return dft;
        }
    }

    public static int getInt(String v, int dft) {
        if (StringUtils.isEmpty(v)) {
            return dft;
        }
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return dft;
        }
    }

    public static boolean getBool(String v, boolean dft) {
        if (StringUtils.isEmpty(v)) {
            return dft;
        }
        try {
            return Boolean.parseBoolean(v);
        } catch (Exception e) {
            return dft;
        }
    }
    
    public static String toUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append("\\u");
            sb.append(getFixWidthString(Integer.toHexString(c), 4));
        }
        return sb.toString();
    }
    
    public static String getFixWidthString(String str, int width, char fill) {
        if (str.length() == width) {
            return str;
        }
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width - str.length(); i++) {
            sb.append(fill);
        }
        sb.append(str);
        return sb.toString();
    }
    
    public static String getFixWidthString(String str, int width) {
        return getFixWidthString(str, width, '0');
    }
    
    public static String getMatchRest(String str, String sub, int offset) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(sub);
        int pos = str.indexOf(sub);
        if (pos >= 0) {
            int start = sub.length() + 1 + offset;
            if (start < str.length()) {
                return str.substring(start);
            }
        }
        return "";
    }
}
