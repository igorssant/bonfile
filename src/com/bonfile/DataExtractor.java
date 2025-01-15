package com.bonfile;

import com.bonfile.util.RemoveUnusedChar;
import com.bonfile.util.SpecialCharacters;

public class DataExtractor {
    public static Integer extractInteger(String phrase) {
        int indexOfEqualsSign;

        // Removing every whitespace from the original string
        phrase = RemoveUnusedChar.removeWhiteSpaces(phrase);
        indexOfEqualsSign = phrase.indexOf(SpecialCharacters.EQUALS.getSymbol());
        return Integer.valueOf(phrase.substring(indexOfEqualsSign + 1));
    }

    public static Integer extractInteger(String phrase, String name) {
        int indexOfEqualsSign;

        // Removing every whitespace from the original string
        phrase = RemoveUnusedChar.removeWhiteSpaces(phrase);
        indexOfEqualsSign = phrase.indexOf(SpecialCharacters.EQUALS.getSymbol());

        // Verifying if name of the variable matches
        if(name.equals(phrase.substring(0, indexOfEqualsSign))) {
            return Integer.valueOf(phrase.substring(indexOfEqualsSign + 1));
        }

        return null;
    }
}
