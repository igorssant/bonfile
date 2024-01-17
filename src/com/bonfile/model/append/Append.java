package com.bonfile.model.append;

import com.bonfile.model.file.File;

public class Append extends File {
    private Integer currentLine;

    public Append() {
        super();
    }

    public Append(String fileName, String filePath, Integer currentLine) {
        super(fileName, filePath);
        this.currentLine = currentLine;
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
