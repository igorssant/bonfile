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
import java.util.HashMap;

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

    private BonfileObject getObjectFromFile(String currLine, Integer indexOfObjectName) throws IOException {
        BonfileObjectController bonfileObjectController = new BonfileObjectController();
        Integer curlyBracketCounter = 1;

        bonfileObjectController.setObjectName(currLine.substring(0, indexOfObjectName));
        while(true) {
            if(curlyBracketCounter == 0) {
                break;
            }

            currLine = FileHelper.removeSpaces(this.file.readLine());
            String varName = currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("LET_SIGN")));
            Object varValue = currLine.substring(
                currLine.indexOf(Tokens.TOKENS.get("LET_SIGN") + 2),
                currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
            );

            if(currLine.contains(Tokens.TOKENS.get("LET_SIGN"))) {
                if(currLine.contains(Tokens.TOKENS.get("DICTIONARY"))) {
                    /* GO TO THE readDict method */
                } else if(currLine.contains(Tokens.TOKENS.get("OPEN_BRACKET"))) {
                    /* GO TO THE readList method */
                } else {
                    bonfileObjectController.put(
                        varName,
                        getObjectFromFile(
                            currLine,
                            currLine
                                .indexOf(
                                    Tokens.TOKENS.get("LET_SIGN")
                                )
                        )
                    );
                }
            } else if(currLine.contains(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"))) {
                /* GO TO THE readChar method */
            } else if(currLine.contains(Tokens.TOKENS.get("SINGLE_QUOTE_MARK"))) {
                /* GO TO THE readString method */
            } else if(varValue instanceof Boolean) {
                /* GO TO THE readBool method */
            } else if(varValue instanceof Integer) {
                /* GO TO THE readInt method */
            } else if(varValue instanceof Float) {
                /* GO TO THE readFloat method */
            } else {
                /* GO TO THE readDouble method */
            }
        }

        return bonfileObjectController.getBonfileObject();
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

    public BonfileObject readObject(String objectName) throws IOException {
        return null;
    }

    public HashMap<String, String> readDict() {
        return null;
    }

    public HashMap<String, String> readDict(String dictName) {
        return null;
    }

    public Integer readInteger() {
        return null;
    }

    public Integer readInteger(String intName) {
        return null;
    }

    public Float readFloat() {
        return null;
    }

    public Float readFloat(String floatName) {
        return null;
    }

    public Double readDouble() {
        return null;
    }

    public Double readDouble(String doubleName) {
        return null;
    }

    public Boolean readBoolean() {
        return null;
    }

    public Boolean readBoolean(String booleanName) {
        return null;
    }

    public Character readChar() {
        return null;
    }

    public Character readChar(String charName) {
        return null;
    }

    public String readString() {
        return null;
    }

    public String readString(String floatName) {
        return null;
    }
}
