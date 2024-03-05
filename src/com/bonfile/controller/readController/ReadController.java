package com.bonfile.controller.readController;

import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.controller.tupleController.*;
import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.model.read.Read;
import com.bonfile.util.fileHelper.FileHelper;
import com.bonfile.util.filePath.FilePath;
import com.bonfile.util.tokens.Tokens;
import com.bonfile.util.variableFromLine.VariableFromLine;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ReadController implements AutoCloseable {
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

    public void rewind() throws IOException {
        this.file.seek(0);
        this.read.rewind();
    }

    @Override
    public void close() throws IOException {
        this.file.close();
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

    private void ignoreObjectBody() throws IOException {
        Integer curlyBracketCounter = 1;

        while(curlyBracketCounter > 0) {
            String currLine = this.file.readLine();

            if(currLine.contains(Tokens.TOKENS.get("OPEN_CURLY_BRACKET"))) {
                curlyBracketCounter++;
            } else if (currLine.equals(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
                curlyBracketCounter--;
            }
        }
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

            if(currLine.equals(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
                break;
            }

            String varName = currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("LET_SIGN")));
            Object varValue = currLine.substring(
                currLine.indexOf(Tokens.TOKENS.get("LET_SIGN")) + 2,
                currLine.indexOf(Tokens.TOKENS.get("SEMICOLON")) + 1
            );

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
            } else if(currLine.contains(Tokens.TOKENS.get("SINGLE_QUOTE_MARK") + Tokens.TOKENS.get("SEMICOLON"))) {
                bonfileObjectController.put(varName, VariableFromLine.charFromLine(varValue.toString()));
            } else if(currLine.contains(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK") + Tokens.TOKENS.get("SEMICOLON"))) {
                bonfileObjectController.put(varName, VariableFromLine.stringFromLine(varValue.toString()));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("BOOLEAN"), false)) {
                bonfileObjectController.put(varName, VariableFromLine.boolFromLine(varValue.toString()));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("INTEGER"), false)) {
                bonfileObjectController.put(varName, VariableFromLine.intFromLine(varValue.toString()));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("FLOAT"), false)) {
                bonfileObjectController.put(varName, VariableFromLine.floatFromLine(varValue.toString()));
            } else if(FileHelper.isPrimitiveType(varValue, Tokens.TOKENS.get("DOUBLE"), false)) {
                bonfileObjectController.put(varName, VariableFromLine.doubleFromLine(varValue.toString()));
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

    private static String removeWhiteSpacesFromDictLine(String line) {
        return line.substring(line.lastIndexOf("\t") + 1);
    }

    private HashMap<String, String> readDictLine() throws IOException {
        HashMap<String, String> hashMap = new HashMap<>();

        while(!this.read.isEOF()) {
            String currLine = removeWhiteSpacesFromDictLine(this.file.readLine());

            if(currLine.equals(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
                break;
            }

            hashMap.put(
                currLine.substring(
                    0,
                    currLine.indexOf(Tokens.TOKENS.get("COLON")) - 1
                ),
                FileHelper.removeComma(
                    currLine.substring(currLine.indexOf(Tokens.TOKENS.get("COLON")) + 2)
                ).toString()
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

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());

            if(currLine.equals(Tokens.TOKENS.get("CLOSE_BRACKET") + Tokens.TOKENS.get("SEMICOLON"))) {
                break;
            }

            currLine = FileHelper.removeComma(
                FileHelper.removeSingleQuoteMark(
                    FileHelper.removeDoubleQuoteMark(currLine)
                )
            ).toString();

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

    private ArrayList<Object> readTuple(String currLine) {
        ArrayList<Object> arrayList = new ArrayList<>();
        Integer startIndex = currLine.indexOf(Tokens.TOKENS.get("OPEN_PARENTHESIS")) + 1,
            jump = 0;

        currLine = FileHelper.removeDoubleQuoteMark(
            FileHelper.removeSingleQuoteMark(
                currLine.substring(startIndex)
            )
        ).toString();

        while(!currLine.equals(Tokens.TOKENS.get("CLOSE_PARENTHESIS") + Tokens.TOKENS.get("SEMICOLON"))) {
            if(currLine.contains(Tokens.TOKENS.get("COMMA"))) {
                arrayList.add(currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("COMMA"))));
                jump = currLine.indexOf(Tokens.TOKENS.get("COMMA")) + 1;
                currLine = currLine.substring(jump);
            } else {
                arrayList.add(currLine.substring(0, currLine.indexOf(Tokens.TOKENS.get("CLOSE_PARENTHESIS"))));
                currLine = currLine.substring(currLine.indexOf(Tokens.TOKENS.get("CLOSE_PARENTHESIS")));
            }
        }

        return arrayList;
    }

    public UnitController<Object> readUnit(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new UnitController<>(readTuple(currLine).get(0));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public PairController<Object, Object> readPair(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new PairController<>(readTuple(currLine));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public TripletController<Object, Object, Object> readTriplet(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new TripletController<>(readTuple(currLine));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public QuartetController<Object, Object, Object, Object> readQuartet(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new QuartetController<>(readTuple(currLine));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public QuintupletController<Object, Object, Object, Object, Object> readQuintuplet(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new QuintupletController<>(readTuple(currLine));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public SextetController<Object, Object, Object, Object, Object, Object> readSextet(String varName) throws IOException {
        String currLine = "";

        while(!this.read.isEOF()) {
            currLine = FileHelper.removeSpaces(this.file.readLine());
            this.read.setCurrentLine(this.file.getFilePointer());

            if(currLine.contains(varName)) {
                return new SextetController<>(readTuple(currLine));
            }
        }

        throw new RuntimeException("No Tuple was found with given name.");
    }

    public Integer readInteger() throws IOException {
        while(!this.read.isEOF()) {
            String currLine = FileHelper.removeSpaces(this.file.readLine()),
                possibleValue;

            this.read.setCurrentLine(this.file.getFilePointer());

            if (currLine.contains(Tokens.TOKENS.get("EQUALS_SIGN")) && currLine.contains(Tokens.TOKENS.get("SEMICOLON"))) {
                possibleValue = currLine.substring(
                    currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 1,
                    currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );

                if(currLine.contains(Tokens.TOKENS.get("INTEGER")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("INTEGER"), false)) {
                    return Integer.parseInt(possibleValue);
                }
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
                    possibleValue;

            this.read.setCurrentLine(this.file.getFilePointer());

            if (currLine.contains(Tokens.TOKENS.get("EQUALS_SIGN")) && currLine.contains(Tokens.TOKENS.get("SEMICOLON"))) {
                possibleValue = currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 1,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );

                if(currLine.contains(Tokens.TOKENS.get("FLOAT")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("FLOAT"), false)) {
                    return Float.parseFloat(possibleValue);
                }
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
                possibleValue;

            this.read.setCurrentLine(this.file.getFilePointer());

            if (currLine.contains(Tokens.TOKENS.get("EQUALS_SIGN")) && currLine.contains(Tokens.TOKENS.get("SEMICOLON"))) {
                possibleValue = currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 1,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );

                if(currLine.contains(Tokens.TOKENS.get("DOUBLE")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("DOUBLE"), false)) {
                    return Double.parseDouble(possibleValue);
                }
            }
        }

        throw new RuntimeException("The gathered data is not a Double type.");
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
                possibleValue;

            this.read.setCurrentLine(this.file.getFilePointer());

            if (currLine.contains(Tokens.TOKENS.get("EQUALS_SIGN")) && currLine.contains(Tokens.TOKENS.get("SEMICOLON"))) {
                possibleValue = currLine.substring(
                        currLine.indexOf(Tokens.TOKENS.get("EQUALS_SIGN")) + 1,
                        currLine.indexOf(Tokens.TOKENS.get("SEMICOLON"))
                );

                if(currLine.contains(Tokens.TOKENS.get("BOOLEAN")) || FileHelper.isPrimitiveType(possibleValue, Tokens.TOKENS.get("BOOLEAN"), false)) {
                    return Boolean.parseBoolean(possibleValue);
                }
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
                * EXPLANATION FOR ABOVE IF:
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
                    currLine.indexOf(Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")) + 1,
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
