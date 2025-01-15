package com.bonfile;

import com.bonfile.BonfileDocument.BonfileObject;

import java.io.IOException;

public class Bonfile {
    public static void main(String [] args) {
        String path = "./scratch.bon";
        try {
            BonfileObject objectData = new BonfileObject(path);
            System.out.println("file exists!");
            objectData.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
