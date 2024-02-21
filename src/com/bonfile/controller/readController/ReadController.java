package com.bonfile.controller.readController;

import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.controller.tupleController.*;
import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.model.read.Read;
import com.bonfile.util.fileHelper.FileHelper;
import com.bonfile.util.filePath.FilePath;
import com.bonfile.util.tokens.Tokens;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
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

    private Boolean hasCommentary(String currLine) {
        Boolean isInsideString = false;

        if(currLine.contains(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK"))) {
            isInsideString = (
                currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")) < currLine.indexOf(Tokens.TOKENS.get("SINGLE_LINE_COMMENTARY"))
                &&
                currLine.indexOf(Tokens.TOKENS.get("SINGLE_LINE_COMMENTARY")) < currLine.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")));
        } else if(currLine.contains(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY"))) {
            isInsideString = (
                currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")) < currLine.indexOf(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY"))
                &&
                currLine.indexOf(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY")) < currLine.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")));
        } else if(currLine.contains(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY"))) {
            isInsideString = (
                currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")) < currLine.indexOf(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY"))
                &&
                currLine.indexOf(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY")) < currLine.lastIndexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")));
        }

        return !isInsideString;
    }

    private String removeSingleLineCommentary() throws IOException {
        this.file.seek(this.read.getCurrentLine() - 1);
        this.read.setCurrentLine(this.read.getCurrentLine() - 1);
        String currLine = FileHelper.removeSpaces(this.file.readLine());
        return currLine.substring(0, currLine.indexOf("SINGLE_LINE_COMMENTARY"));
    }

    private String removeMultiLineCommentary() throws IOException {
        this.file.seek(this.read.getCurrentLine() - 1);
        this.read.setCurrentLine(this.read.getCurrentLine() - 1);
        String currLine = FileHelper.removeSpaces(this.file.readLine());

        if(currLine.contains(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY"))) {
            if(currLine.contains(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY"))) {
                return currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY")))
                    + currLine.substring(currLine.indexOf(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY")) + 2);
            }

            return currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("OPEN_MULTILINE_COMMENTARY")));
        }

        return currLine.substring(currLine.indexOf(Tokens.TOKENS.get("CLOSE_MULTILINE_COMMENTARY")) + 2);
    }

    private String iterateThroughCommentary() throws IOException {
        String currLine = FileHelper.removeSpaces(this.file.readLine());

        while(!hasCommentary(currLine)) {
            currLine= FileHelper.removeSpaces(this.file.readLine());
        }

        return removeMultiLineCommentary();
    }

    private Short verifyListType(String line) {
        Integer indexOfLetSign = line.indexOf(Tokens.TOKENS.get("LET_SIGN"));

        if(line.contains(Tokens.TOKENS.get("INTEGER"))) {
            return 0;
        } else if (line.contains(Tokens.TOKENS.get("BOOLEAN"))) {
            return 1;
        } else if (line.contains(Tokens.TOKENS.get("FLOAT"))) {
            return 2;
        } else if(line.contains(Tokens.TOKENS.get("DOUBLE"))) {
            return 3;
        } else if(line.contains(Tokens.TOKENS.get("CHAR"))) {
            return 4;
        } else if(line.contains(Tokens.TOKENS.get("STRING"))) {
            return 5;
        } else if (line.contains(Tokens.TOKENS.get("DICTIONARY"))) {
            return 6;
        } else if(line.contains(Tokens.TOKENS.get("TUPLE"))) {
            return 7;
        } else if(FileHelper.isObject(line.substring(indexOfLetSign + 2, indexOfLetSign + 3))){
            return 8;
        }

        throw new RuntimeException("This was caused by a commentary line or a blank line or EOF reached.");
    }

    private BonfileObject getObjectFromFile(String currLine, Integer indexOfObjectName, String objectName) throws IOException {
        BonfileObjectController bonfileObjectController = new BonfileObjectController();

        if(objectName.isEmpty()) {
            bonfileObjectController.setObjectName(currLine.substring(0, indexOfObjectName));
        } else {
            bonfileObjectController.setObjectName(objectName);
        }

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());
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
                    bonfileObjectController.put(varName, readDict(varName));
                } else if(currLine.contains(Tokens.TOKENS.get("OPEN_BRACKET"))) {
                    LinkedList<Object> linkedList = readList(varName);

                    switch(verifyListType(currLine)) {
                        case 0:
                            bonfileObjectController.putIntList(varName, (LinkedList<Integer>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 1:
                            bonfileObjectController.putBooleanList(varName, (LinkedList<Boolean>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 2:
                            bonfileObjectController.putFloatList(varName, (LinkedList<Float>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 3:
                            bonfileObjectController.putDoubleList(varName, (LinkedList<Double>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 4:
                            bonfileObjectController.putCharList(varName, (LinkedList<Character>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 5:
                            bonfileObjectController.putStringList(varName, (LinkedList<String>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        case 6:
                            bonfileObjectController.putDictList(varName, (LinkedList<HashMap<String, String>>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        /*case 7:
                            bonfileObjectController.put(varName, linkedList.toArray());
                            break;*/
                        case 8:
                            bonfileObjectController.putBonfileObjectList(varName, (LinkedList<BonfileObject>) (LinkedList) Arrays.asList(linkedList));
                            break;

                        default:
                            throw new RuntimeException("Unhandled type was parsed.");
                    }
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
        String lineContent = "";

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine(this.file.getFilePointer());

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
        String lineContent = "";

        while(!this.read.isEOF()) {
            lineContent = this.file.readLine();
            this.read.setCurrentLine(this.file.getFilePointer());

            if(lineContent.contains(objectName)) {
                Integer indexOfLetSign = lineContent.indexOf(Tokens.TOKENS.get("LET_SIGN"));

                if(FileHelper.isObject(lineContent.substring(indexOfLetSign + 2, indexOfLetSign + 3))) {
                    return getObjectFromFile(FileHelper.removeSpaces(lineContent), indexOfLetSign, objectName);
                }
            }
        }

        throw new IOException("Invalid name.\nCould not find any Object with given name.");
    }

    private HashMap<String, String> readDictLine() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();
        String currLine = "";

        while(!currLine.equals(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            hashMap.put(
                currLine.substring(
                    0,
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                ),
                currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("COMMA"))
                )
            );
        }

        return hashMap;
    }

    public HashMap<String, String> readDict() throws IOException {
        String lineContent = "";

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine(this.file.getFilePointer());

            if(lineContent.contains(Tokens.TOKENS.get("LET_SIGN") + Tokens.TOKENS.get("DICTIONARY"))) {
                return readDictLine();
            }
        }

        throw new RuntimeException("No Dictionary-like structure was found.");
    }

    public HashMap<String, String> readDict(String dictName) throws IOException {
        String lineContent = "";

        while(!this.read.isEOF()) {
            lineContent = file.readLine();
            this.read.setCurrentLine(this.file.getFilePointer());

            if(lineContent.contains(dictName)) {
                return readDictLine();
            }
        }

        throw new RuntimeException("No Dictionary was found with given name.");
    }

    private LinkedList<Object> readListMember() throws IOException {
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!currLine.equals(Tokens.TOKENS.get("CLOSE_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            currLine = currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("COMMA")));
            linkedList.add(currLine);
        }

        return linkedList;
    }

    public LinkedList<Object> readList() throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(Tokens.TOKENS.get("LET_SIGN"))
            &&
            currLine.contains(Tokens.TOKENS.get("EQUALS_SIGN") + Tokens.TOKENS.get("OPEN_BRACKET"))) {
                return readListMember();
            }
        }

        throw new RuntimeException("No List nor Array-like structure was found.");
    }

    public LinkedList<Object> readList(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return readListMember();
            }
        }

        throw new RuntimeException("No List nor Array-like structure was found with given name.");
    }

    private LinkedList<Object> readTuple(String currLine) {
        LinkedList<Object> linkedList = new LinkedList<>();
        Integer startIndex = currLine.indexOf(Tokens.TOKENS.get("OPEN_PARENTHESIS")) + 1,
            jump = 0;

        currLine = currLine.substring(startIndex);

        while(currLine.equals(Tokens.TOKENS.get("CLOSE_PARENTHESIS") + Tokens.TOKENS.get("SEMICOLON"))) {
            if(currLine.contains(Tokens.TOKENS.get("COMMA"))) {
                linkedList.add(currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("COMMA"))));
                jump = currLine.indexOf(Tokens.TOKENS.get("COMMA")) + 1;
                currLine = currLine.substring(jump);
            } else {
                linkedList.add(currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("CLOSE_PARENTHESIS"))));
                currLine = currLine.substring(currLine.indexOf(Tokens.TOKENS.get("CLOSE_PARENTHESIS")));
            }
        }

        return linkedList;
    }

    public UnitController<Object> readUnit(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new UnitController<>(readTuple(currLine).pop());
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public PairController<Object, Object> readPair(String varName) throws IOException {
        PairController<Object, Object> pairController = new PairController<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                linkedList = readTuple(currLine);
                pairController.setItem1(linkedList.get(0));
                pairController.setItem2(linkedList.get(1));
                return pairController;
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public TripletController<Object, Object, Object> readTriplet(String varName) throws IOException {
        TripletController<Object, Object, Object> tripletController = new TripletController<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                linkedList = readTuple(currLine);
                tripletController.setItem1(linkedList.get(0));
                tripletController.setItem2(linkedList.get(1));
                tripletController.setItem3(linkedList.get(2));
                return tripletController;
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public QuartetController<Object, Object, Object, Object> readQuartet(String varName) throws IOException {
        QuartetController<Object, Object, Object, Object> quartetController = new QuartetController<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                linkedList = readTuple(currLine);
                quartetController.setItem1(linkedList.get(0));
                quartetController.setItem2(linkedList.get(1));
                quartetController.setItem3(linkedList.get(2));
                quartetController.setItem4(linkedList.get(3));
                return quartetController;
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public QuintupletController<Object, Object, Object, Object, Object> readQuintuplet(String varName) throws IOException {
        QuintupletController<Object, Object, Object, Object, Object> quintupletController = new QuintupletController<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                linkedList = readTuple(currLine);
                quintupletController.setItem1(linkedList.get(0));
                quintupletController.setItem2(linkedList.get(1));
                quintupletController.setItem3(linkedList.get(2));
                quintupletController.setItem4(linkedList.get(3));
                quintupletController.setItem5(linkedList.get(4));
                return quintupletController;
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public SextetController<Object, Object, Object, Object, Object, Object> readSextet(String varName) throws IOException {
        SextetController<Object, Object, Object, Object, Object, Object> sextetController = new SextetController<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                linkedList = readTuple(currLine);
                sextetController.setItem1(linkedList.get(0));
                sextetController.setItem2(linkedList.get(1));
                sextetController.setItem3(linkedList.get(2));
                sextetController.setItem4(linkedList.get(3));
                sextetController.setItem5(linkedList.get(4));
                sextetController.setItem5(linkedList.get(5));
                return sextetController;
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public Integer readInteger() throws IOException {
        while(true) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(Tokens.TOKENS.get("FLOAT")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("FLOAT"))) {
                return Float.parseFloat(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Float type.");
    }

    public Float readFloat(String floatName) throws IOException {
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(Tokens.TOKENS.get("DOUBLE")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("DOUBLE"))) {
                return Double.parseDouble(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Float type.");
    }

    public Double readDouble(String doubleName) throws IOException {
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN") + 1),
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(Tokens.TOKENS.get("BOOLEAN")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("BOOLEAN"))) {
                return Boolean.parseBoolean(possibleValue);
            }
        }

        throw new RuntimeException("The gathered data is not a Boolean type.");
    }

    public Boolean readBoolean(String booleanName) throws IOException {
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.length() > 4) {
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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

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
