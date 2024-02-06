package com.bonfile.util;

import java.util.regex.Pattern;

public class FileHelper {
    public static String capitalize(String className) {
        if (className == null || className.isEmpty()) return className;
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }

    public static boolean isObject(String character) {
        Pattern VOWELS_PATTERN = Pattern.compile("[AEIOU]", Pattern.CASE_INSENSITIVE);
        return VOWELS_PATTERN.matcher(character).matches();
    }

    public static String removeSpaces(String str) {
        return str.replaceAll("\\s+","");
    }
}
