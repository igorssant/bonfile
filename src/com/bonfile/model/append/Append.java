package com.bonfile.model.append;

import com.bonfile.model.file.File;

public class Append extends File {
    private Long currentLine;

    public Append() {
        super();
    }

    public Append(String fileName, String filePath, Long currentLine) {
        super(fileName, filePath, currentLine);
        this.currentLine = currentLine;
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
            super.setIndentationCounter(-1);
        } else {
            throw new RuntimeException("Indentation counter cannot assume negative values.");
        }
    }

    @Override
    public Integer getIndentationCounter() {
        return super.getIndentationCounter();
    }

    @Override
    public Long getFileLength() {
        return super.getFileLength();
    }
}
