package com.bonfile.util.fileHelper;

import java.util.regex.Pattern;

public class FileHelper {
    public static String capitalize(String className) {
        if (className == null || className.isEmpty()) return className;
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }

    public static boolean isObject(String character) {
        Pattern VOWELS_PATTERN = Pattern.compile("[A-Z]", Pattern.CASE_INSENSITIVE);
        return VOWELS_PATTERN.matcher(character).matches();
    }

    public static String removeSpaces(String str) {
        return str.replaceAll("\\s+","");
    }

    public static Boolean isNumericType(Object obj) {
        return isPrimitiveType(obj, "int") || isPrimitiveType(obj, "float") || isPrimitiveType(obj, "double");
    }

    public static Boolean isTextType(Object object) {
        return object instanceof Character || object instanceof String;
    }

    public static Boolean isPrimitiveType(Object subject, String typeName) {
        switch(typeName) {
            case "int":
                return subject instanceof Integer;

            case "float":
                return subject instanceof Float;

            case "double":
                return subject instanceof Double;

            case "bool":
                return subject instanceof Boolean;

            case "char":
                return subject instanceof Character;

            case "str":
                return subject instanceof String;

            default:
                return false;
        }
    }

    public static Boolean isArrayOfPrimitive(Object[] subject, String typeName) {
        switch(typeName) {
            case "int":
                return subject instanceof Integer[];

            case "float":
                return subject instanceof Float[];

            case "double":
                return subject instanceof Double[];

            case "bool":
                return subject instanceof Boolean[];

            case "char":
                return subject instanceof Character[];

            case "str":
                return subject instanceof String[];

            default:
                return false;
        }
    }
}
