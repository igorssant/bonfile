package com.bonfile.util.fileHelper;

import com.bonfile.util.tokens.Tokens;

import java.util.IllegalFormatConversionException;
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

    public static Object addSingleQuoteMark(Object value) {
        return Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
            + value
            + Tokens.TOKENS.get("SINGLE_QUOTE_MARK");
    }

    public static Object addDoubleQuoteMark(Object value) {
        return Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
            + value
            + Tokens.TOKENS.get("DOUBLE_QUOTE_MARK");
    }

    public static Object removeSingleQuoteMark(Object value) {
        return value.toString().replace(Tokens.TOKENS.get("SINGLE_QUOTE_MARK"), "");
    }

    public static Object removeDoubleQuoteMark(Object value) {
        return value.toString().replace(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"), "");
    }

    public static Object removeComma(Object value) {
        return value.toString().replace(Tokens.TOKENS.get("COMMA"), "");
    }

    public static Boolean isNumericType(Object obj) {
        return isPrimitiveType(obj, "int") || isPrimitiveType(obj, "float") || isPrimitiveType(obj, "double");
    }

    public static Boolean isTextType(Object object) {
        return object instanceof Character || object instanceof String;
    }

    public static Boolean isPrimitiveType(Object subject, String typeName) throws NumberFormatException {
        subject = removeDoubleQuoteMark(
            removeSingleQuoteMark(
                subject
                    .toString()
                    .substring(
                        subject.toString().indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 1,
                        subject.toString().indexOf(Tokens.TOKENS.get("SEMICOLON"))
                    )
            )
        );

        switch(typeName) {
            case "int":
                try {
                    Integer.parseInt(subject.toString());
                    return true;
                } catch(NumberFormatException e) {
                    return false;
                }

            case "float":
                try {
                    Float.parseFloat(subject.toString());
                    return true;
                } catch(NumberFormatException e) {
                    return false;
                }

            case "double":
                try {
                    Double.parseDouble(subject.toString());
                    return true;
                } catch(NumberFormatException e) {
                    return false;
                }

            case "bool":
                return subject instanceof Boolean;

            case "char":
                return subject instanceof Character && subject.toString().length() == 1;

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
