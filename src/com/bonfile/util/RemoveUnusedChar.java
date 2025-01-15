package com.bonfile.util;

public class RemoveUnusedChar {
    public static String removeSemicolon(String phrase) {
        int lastSemicolonIndex = phrase.lastIndexOf(SpecialCharacters.SEMICOLON.getSymbol()),
            endOfLineIndex = phrase.indexOf(SpecialCharacters.END_LINE.getSymbol());

        if(lastSemicolonIndex > 0 && lastSemicolonIndex == (endOfLineIndex - 1)) {
            phrase = phrase.substring(0, lastSemicolonIndex);
        }

        return phrase;
    }

    private static String concatMultipartString(String firstPart, String actualData, String secondPart) {
        firstPart = firstPart.concat(actualData);
        firstPart = firstPart.concat(secondPart);
        return firstPart;
    }

    public static String removeWhiteSpaces(String phrase) {
        if(phrase.contains(SpecialCharacters.DOUBLE_QUOTES.getSymbol())) {
            int firstDoubleQuotesIndex = phrase.indexOf(SpecialCharacters.DOUBLE_QUOTES.getSymbol()),
                lastDoubleQuotesIndex = phrase.lastIndexOf(SpecialCharacters.DOUBLE_QUOTES.getSymbol());
            String firstPart = phrase.substring(0, firstDoubleQuotesIndex),
                secondPart = phrase.substring(lastDoubleQuotesIndex + 1),
                actualData = phrase.substring(firstDoubleQuotesIndex, lastDoubleQuotesIndex + 1);

            firstPart = removeWhiteSpaces(firstPart);
            secondPart = removeWhiteSpaces(secondPart);
            phrase = firstPart.concat(actualData);
            phrase = phrase.concat(secondPart);
        } else if(phrase.contains(SpecialCharacters.SINGLE_QUOTE.getSymbol())) {
            int firstSingleQuoteIndex = phrase.indexOf(SpecialCharacters.SINGLE_QUOTE.getSymbol()),
                    lastSingleQuoteIndex = phrase.lastIndexOf(SpecialCharacters.SINGLE_QUOTE.getSymbol());
            String firstPart = phrase.substring(0, firstSingleQuoteIndex),
                    secondPart = phrase.substring(lastSingleQuoteIndex + 1),
                    actualData = phrase.substring(firstSingleQuoteIndex, lastSingleQuoteIndex + 1);

            firstPart = removeWhiteSpaces(firstPart);
            secondPart = removeWhiteSpaces(secondPart);
            phrase = firstPart.concat(actualData);
            phrase = phrase.concat(secondPart);
        } else {
            phrase = phrase.replace("\\s+", "");
        }

        return phrase;
    }
}
