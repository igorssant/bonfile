package com.bonfile.model.write;

import com.bonfile.model.file.File;

public class Write extends File {
    private Integer currentLine;

    public Write() {
        super();
    }

    public Write(String fileName, String filePath) {
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

    public void increaseIndentation() {
        super.setIndentationCounter(1);
    }

    public void decreaseIndentationCounter() {
        if(super.getIndentationCounter() > 0) {
            super.setIndentationCounter(-1);
        } else {
            throw new RuntimeException("Indentation counter cannot assume negative values.");
        }
    }

    @Override
    public Integer getIndentationCounter() {
        return super.getIndentationCounter();
    }
}
