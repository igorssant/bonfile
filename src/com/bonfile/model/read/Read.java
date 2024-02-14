package com.bonfile.model.read;

import com.bonfile.model.file.File;

public class Read extends File {
    private Long currentLine;

    public Read() {
        super();
    }

    public Read(String fileName, String filePath, Long fileLength) {
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
            super.setIndentationCounter(-1);
        } else {
            throw new RuntimeException("Indentation counter cannot assume negative values.");
        }
    }

    public Boolean isEOF() {
        return super.getFileLength().equals(this.currentLine);
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
