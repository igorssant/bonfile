package com.bonfile.controller.readController;

import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.model.read.Read;
import com.bonfile.util.FileHelper;
import com.bonfile.util.filePath.FilePath;
import com.bonfile.util.tokens.Tokens;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadController {
    private Read read;
    private RandomAccessFile file;
    private final Tokens TOKENS = new Tokens();

    public ReadController() {}

    public ReadController(String filePath) throws IOException, FileNotFoundException {
        String fileName = filePath.substring(filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1);
        this.file = new RandomAccessFile(FilePath.getFilePath(fileName), "r");
        this.read = new Read(fileName, filePath.substring(0, filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1));
        file.seek(0);
    }

    private Read getRead() {
        return read;
    }

    public void setRead(Read read) {
        this.read = read;
    }

    public RandomAccessFile getFile() {
        return file;
    }

    public void setFile(RandomAccessFile file) {
        this.file = file;
    }

    public String getFileName() {
        return this.read.getFileName();
    }

    public Integer getCurrentLine() {
        return this.read.getCurrentLine();
    }

    public void setCurrentLine(Integer currentLine) {
        this.read.setCurrentLine(currentLine);
    }

    public String getFilePath() {
        return this.read.getFilePath();
    }

    public void rewind() {
        this.read.setCurrentLine(0);
    }

    private BonfileObject getObjectFromFile(String currLine, Integer indexOfObjectName) {
        BonfileObjectController bonfileObjectController = new BonfileObjectController();

        bonfileObjectController.setObjectName(currLine.substring(0, indexOfObjectName));
        return null;
    }

    public BonfileObject readObject() throws IOException {
        String lineContent = null;

        while((lineContent = file.readLine()) != null) {
            if(lineContent.contains(Tokens.TOKENS.get("LET_SIGN"))) {
                Integer indexOfLetSign = lineContent.indexOf(Tokens.TOKENS.get("LET_SIGN"));

                if(FileHelper.isObject(lineContent.substring(indexOfLetSign + 2, indexOfLetSign + 3))) {
                    return getObjectFromFile(FileHelper.removeSpaces(lineContent), indexOfLetSign);
                }
            }
        }

        throw new IOException("Could not find any Objects in the Bonfile.");
    }

    public BonfileObject readObject(Integer position) throws IOException {
        if(position == 0) {
            return readObject();
        }

        return null;
    }

    public BonfileObject readLastObject() {
        return null;
    }
}
