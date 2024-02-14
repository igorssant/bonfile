package com.bonfile.controller.readController;

import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.model.read.Read;
import com.bonfile.util.fileHelper.FileHelper;
import com.bonfile.util.filePath.FilePath;
import com.bonfile.util.tokens.Tokens;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;

public class ReadController {
    private Read read;
    private RandomAccessFile file;

    public ReadController() {}

    public ReadController(String filePath) throws IOException, FileNotFoundException {
        String fileName = filePath.substring(filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1);
        this.file = new RandomAccessFile(FilePath.getFilePath(fileName), "r");
        this.read = new Read(fileName, filePath.substring(0, filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1), this.file.length());
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

    public Long getCurrentLine() {
        return this.read.getCurrentLine();
    }

    public void setCurrentLine(Long currentLine) {
        this.read.setCurrentLine(currentLine);
    }

    public String getFilePath() {
        return this.read.getFilePath();
    }

    public void rewind() {
        this.read.rewind();
    }

    private BonfileObject getObjectFromFile(String currLine, Integer indexOfObjectName, String objectName) throws IOException {
        BonfileObjectController bonfileObjectController = new BonfileObjectController();

        if(objectName.isEmpty()) {
            bonfileObjectController.setObjectName(currLine.substring(0, indexOfObjectName));
        } else {
            bonfileObjectController.setObjectName(objectName);
        }

        while(true) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());
            file.seek(this.file.getFilePointer() - 1);

            if(currLine.equals(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
                break;
            }

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
                                ),
                            ""
                        )
                    );
                }
            } else if(currLine.contains(Tokens.TOKENS.get("SINGLE_QUOTE_MARK") + Tokens.TOKENS.get("SEMICOLON"))) {
                bonfileObjectController.put(varName, readChar(varName));
            } else if(currLine.contains(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK") + Tokens.TOKENS.get("SEMICOLON"))) {
                bonfileObjectController.put(varName, readString(varName));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("BOOLEAN"))) {
                bonfileObjectController.put(varName, readBoolean(varName));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("INTEGER"))) {
                bonfileObjectController.put(varName, readInteger(varName));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("FLOAT"))) {
                bonfileObjectController.put(varName, readFloat(varName));
            } else {
                bonfileObjectController.put(varName, readDouble(varName));
            }
        }

        return bonfileObjectController.getBonfileObject();
    }

    public BonfileObject readObject() throws IOException {
        String lineContent = null;

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(lineContent.contains(Tokens.TOKENS.get("LET_SIGN"))) {
                Integer indexOfLetSign = lineContent.indexOf(Tokens.TOKENS.get("LET_SIGN"));

                if(FileHelper.isObject(lineContent.substring(indexOfLetSign + 2, indexOfLetSign + 3))) {
                    return getObjectFromFile(FileHelper.removeSpaces(lineContent), indexOfLetSign, "");
                }
            }
        }

        throw new IOException("Could not find any Objects in the Bonfile.");
    }

    public BonfileObject readObject(String objectName) throws IOException {
        String lineContent = null;

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(lineContent.contains(objectName)) {
                Integer indexOfLetSign = lineContent.indexOf(Tokens.TOKENS.get("LET_SIGN"));

                if(FileHelper.isObject(lineContent.substring(indexOfLetSign + 2, indexOfLetSign + 3))) {
                    return getObjectFromFile(FileHelper.removeSpaces(lineContent), indexOfLetSign, objectName);
                }
            }
        }

        throw new IOException("Invalid name.\nCould not find any Object with given name.");
    }

    public HashMap<String, String> readDict() throws IOException {
        String lineContent = "";

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(lineContent.contains(Tokens.TOKENS.get("LET_SIGN") + Tokens.TOKENS.get("DICTIONARY"))) {
                /* TODO */
            }
        }

        return null;
    }

    public HashMap<String, String> readDict(String dictName) {
        return null;
    }

    public LinkedList<Object> readList(){
        return null;
    }

    public LinkedList<Object> readList(String varName){
        return null;
    }

    public Integer readInteger() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(Tokens.TOKENS.get("INTEGER")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("INTEGER"))) {
                return Integer.parseInt(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not an Integer type.");
    }

    public Integer readInteger(String intName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(intName)) {
                return Integer.parseInt(
                    currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 2,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                    )
                );
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }

    public Float readFloat() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(Tokens.TOKENS.get("FLOAT")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("FLOAT"))) {
                return Float.parseFloat(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Float type.");
    }

    public Float readFloat(String floatName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(floatName)) {
                return Float.parseFloat(
                    currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 2,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                    )
                );
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }

    public Double readDouble() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(Tokens.TOKENS.get("DOUBLE")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("DOUBLE"))) {
                return Double.parseDouble(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Float type.");
    }

    public Double readDouble(String doubleName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(doubleName)) {
                return Double.parseDouble(
                    currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 2,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                    )
                );
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }

    public Boolean readBoolean() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(Tokens.TOKENS.get("BOOLEAN")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("BOOLEAN"))) {
                return Boolean.parseBoolean(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Boolean type.");
    }

    public Boolean readBoolean(String booleanName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(booleanName)) {
                return Boolean.parseBoolean(
                    currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 2,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                    )
                );
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }

    public Character readChar() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            } else if(currLine.length() > 4) {
                /*
                * EXPLANATION FOR ABOVE IF
                * INSIDE THE BONFILE, A CHARACTER
                * WOULD BE WRITTEN LIKE
                * 'c';
                * THAT MEANS THE LINE MUST HAVE
                * 4 CHARACTER IN TOTAL IN
                * ORDER TO BE A CHARACTER TYPE
                */
                continue;
            }

             return currLine.charAt(1);
        }

        throw new RuntimeException("The gathered data is not a Character type.");
    }

    public Character readChar(String charName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(charName)) {
                return currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("SINGLE_QUOTE_MARK")),
                    currLine.lastIndexOf(Tokens.TOKENS.get("SINGLE_QUOTE_MARK"))
                ).charAt(1);
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }

    public String readString() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK") + Tokens.TOKENS.get("SEMICOLON"))) {
                return currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK") + 1),
                    currLine.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"))
                );
            }
        }

        throw new RuntimeException("The gathered data is not a String type.");
    }

    public String readString(String stringName) throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine((long) this.file.getFilePointer());

            if(this.read.getCurrentLine() == (this.file.length() - 1)) {
                break;
            }

            if(currLine.contains(stringName)) {
                return currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK") + 1),
                    currLine.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"))
                );
            }
        }

        throw new RuntimeException("This variable does not exists.");
    }
}
