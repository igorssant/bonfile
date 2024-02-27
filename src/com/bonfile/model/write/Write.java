package com.bonfile.model.write;

import com.bonfile.model.file.File;

public class Write extends File {
    private Long currentLine;

    public Write() {
        super();
    }

    public Write(String fileName, String filePath, Long fileLength) {
        super(fileName, filePath, fileLength);
        this.currentLine = 0L;
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    public Long getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(Long currentLine) {
        this.currentLine = currentLine;
    }

    @Override
    public String getFilePath() {
        return super.getFilePath();
    }

    public void rewind() {
        setCurrentLine(0L);
    }

    public void increaseIndentation() {
        super.setIndentationCounter(super.getIndentationCounter() + 1);
    }

    public void decreaseIndentationCounter() {
        if(super.getIndentationCounter() > 0) {
            super.setIndentationCounter(super.getIndentationCounter() - 1);
        } else {
            throw new RuntimeException("Indentation counter cannot assume negative values.");
        }
    }

    @Override
    public Integer getIndentationCounter() {
        return super.getIndentationCounter();
    }
}
