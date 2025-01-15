package com.bonfile.BonfileDocument;

import java.io.*;

public class BonfileObject {
    private Boolean onlyRead;
    private BufferedReader reader;
    private BufferedWriter writer;

    public BonfileObject(String filePath) throws FileNotFoundException {
        this.onlyRead = true;
        this.reader = new BufferedReader(new FileReader(filePath));
    }

    public BonfileObject(String filePath, Boolean readOnly) throws IOException {
        this.onlyRead = readOnly;
        this.reader = new BufferedReader(new FileReader(filePath));

        if(!onlyRead) {
            this.writer = new BufferedWriter(new FileWriter(filePath));
        }
    }

    public void close() throws IOException {
        this.reader.close();

        if(!onlyRead) {
            this.writer.close();
        }
    }

    public Boolean isReadOnly() {
        return this.onlyRead;
    }

    public Integer readInteger() {
        return 1;
    }

    public Integer readInteger(String variableName) {
        return 1;
    }

    public void writeInteger(Integer value) {
        System.out.println(value);
    }

    public void writeInteger(String name, Integer value) {
        System.out.println(value);
    }
}
