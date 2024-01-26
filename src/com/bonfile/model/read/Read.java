package com.bonfile.model.read;

import com.bonfile.model.file.File;

public class Read extends File {
    private Integer currentLine;

    public Read() {
        super();
    }

    public Read(String fileName, String filePath) {
        super(fileName, filePath);
        this.currentLine = 0;
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    public Integer getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(Integer currentLine) {
        this.currentLine = currentLine;
    }

    @Override
    public String getFilePath() {
        return super.getFilePath();
    }

    public void rewind() {
        setCurrentLine(0);
    }
}
