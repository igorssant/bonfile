package com.bonfile.model.file;

public class File {
    private String fileName;
    private String filePath;
    private Long fileLength;
    private Integer indentationCounter;

    public File() {
        this.indentationCounter = 0;
    }

    public File(String fileName, String filePath, Long fileLength) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.indentationCounter = 0;
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getIndentationCounter() {
        return indentationCounter;
    }

    public void setIndentationCounter(Integer indentationCounter) {
        this.indentationCounter = indentationCounter;
    }

    public Long getFileLength() {
        return fileLength;
    }
}
