package com.bonfile.util;

public enum SpecialCharacters {
    SEMICOLON(";"),
    COLON(":"),
    POINT("."),
    COMMA(","),
    EQUALS("="),
    END_LINE("\n"),
    PIPE("|"),
    OP_CURLY_BRACKET("{"),
    ED_CURLY_BRACKET("}"),
    OP_BRACKET("["),
    ED_BRACKET("]"),
    OP_PARENTHESIS("("),
    ED_PARENTHESIS(")"),
    SINGLE_QUOTE("\'"),
    DOUBLE_QUOTES("\"");

    private final String symbol;

    SpecialCharacters(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
