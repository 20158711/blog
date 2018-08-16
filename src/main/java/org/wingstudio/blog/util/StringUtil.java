package org.wingstudio.blog.util;

public class StringUtil {
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }
    public static boolean equals(String str,String s){
        if (isBlank(str) || isBlank(str)){
            return false;
        }
        return str.equals(s);
    }
}
