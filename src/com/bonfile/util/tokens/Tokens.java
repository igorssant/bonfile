package com.bonfile.util.tokens;

import java.util.HashMap;

public class Tokens {
    public static final HashMap<String, String> TOKENS = new HashMap<>();

    public Tokens(){
        /* GENERAL USE TOKENS */
        TOKENS.put("SLASH_SIGN", "/");
        TOKENS.put("DASH_SIGN", "-");
        TOKENS.put("SEMICOLON", ";");
        TOKENS.put("SPACE", " ");
        TOKENS.put("COMMA", ",");
        TOKENS.put("DOT", ".");
        TOKENS.put("INDENTATION", "\t");
        TOKENS.put("NEW_LINE", "\n");

        /* LIST TOKENS */
        TOKENS.put("OPEN_BRACKET", "[");
        TOKENS.put("CLOSE_BRACKET", "]");

        /* DICTIONARY AND CLASS TOKENS */
        TOKENS.put("OPEN_CURLY_BRACKET", "{");
        TOKENS.put("CLOSE_CURLY_BRACKET", "}");

        /* VARIABLE ASSIGNMENT TOKENS */
        TOKENS.put("EQUALS_SIGN", "=");
        TOKENS.put("COLON", ":");
        TOKENS.put("LET_SIGN", "::");

        /* PRIMITIVE TYPE TOKENS */
        TOKENS.put("INTEGER", "int");
        TOKENS.put("FLOAT", "float");
        TOKENS.put("DOUBLE", "double");
        TOKENS.put("BOOLEAN", "bool");
        TOKENS.put("CHAR", "char");
        TOKENS.put("STRING", "str");

        /* THE DICTONARY TYPE */
        TOKENS.put("DICTIONARY", "dict");

        /* QUOTATION MARK TOKENS */
        TOKENS.put("SINGLE_QUOTE_MARK", "\'");
        TOKENS.put("DOUBLE_QUOTE_MARK", "\"");
    }
}
