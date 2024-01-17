package com.bonfile.util;

public class Capitalize {
    public static String capitalize(String className) {
        if (className == null || className.isEmpty()) return className;
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }
}
