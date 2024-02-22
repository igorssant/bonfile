package com.bonfile.util.variableFromLine;

import com.bonfile.util.tokens.Tokens;
import java.io.RandomAccessFile;

public class VariableFromLine {
    public static Character charFromLine(String line) {
        return line.substring(
            line.indexOf(Tokens.TOKENS.get("SINGLE_QUOTE_MARK")),
            line.lastIndexOf(Tokens.TOKENS.get("SINGLE_QUOTE_MARK"))
        ).charAt(1);
    }

    public static String stringFromLine(String line) {
        return line.substring(
            line.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")) + 1,
            line.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"))
        );
    }

    public static Integer intFromLine(String line) {
        return Integer.parseInt(getSubString(line));
    }

    public static Float floatFromLine(String line) {
        return Float.parseFloat(getSubString(line));
    }

    public static Double doubleFromLine(String line) {
        return Double.parseDouble(getSubString(line));
    }

    public static Boolean boolFromLine(String line) {
        return Boolean.parseBoolean(getSubString(line));
    }

    private static String getSubString(String line) {
        return line.substring(
            line.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) +  1,
            line.indexOf(Tokens.TOKENS.get("SEMICOLON"))
        );
    }
}
