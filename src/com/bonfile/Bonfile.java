package com.bonfile;

import java.util.Optional;

public class Bonfile {
    public static Integer readInteger(Optional<String> variableName) {
        if(variableName.isPresent()) {
            return variableName.get();
        }

        return null;
    }

    public static void main(String [] args) {

    }
}
