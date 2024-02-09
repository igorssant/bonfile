package com.bonfile.controller.appendController;

import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.util.fileHelper.FileHelper;
import com.bonfile.util.tokens.Tokens;
import com.bonfile.model.append.Append;
import com.bonfile.util.filePath.FilePath;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class AppendController {
    private Append append;
    private RandomAccessFile file;

    public AppendController() {}

    public AppendController(String filePath) throws IOException, FileNotFoundException {
        String fileName = filePath.substring(filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1);
        this.file = new RandomAccessFile(FilePath.getFilePath(fileName), "rw");
        this.append = new Append(fileName, filePath.substring(0, filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1), (int) file.length());
        file.seek(file.length());
    }

    private Append getAppend() {
        return append;
    }

    public void setAppend(Append append) {
        this.append = append;
    }

    public RandomAccessFile getFile() {
        return file;
    }

    private void setFile(RandomAccessFile file) {
        this.file = file;
    }

    private void openBracket(Boolean isCurly) throws IOException {
        if(isCurly) {
            this.file.writeBytes(Tokens.TOKENS.get("OPEN_CURLY_BRACKET"));
        } else {
            this.file.writeBytes(Tokens.TOKENS.get("OPEN_BRACKET"));
        }

        this.file.writeBytes(Tokens.TOKENS.get("NEW_LINE"));
    }

    private void indentFile() throws IOException {
        for(int i = 0; i < append.getIndentationCounter(); i++) {
            this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
        }
    }

    private void closeBracket(Boolean isCurly, Boolean endOfInstance) throws IOException {
        if(isCurly) {
            this.file.writeBytes(Tokens.TOKENS.get("CLOSE_CURLY_BRACKET"));
        } else {
            this.file.writeBytes(Tokens.TOKENS.get("CLOSE_BRACKET"));
        }

        if(endOfInstance) {
            this.file.writeBytes(Tokens.TOKENS.get("SEMICOLON"));
        }

        this.file.writeBytes(Tokens.TOKENS.get("NEW_LINE"));
    }

    public void writeObject(BonfileObject classObject) throws IOException {
        Object lastKey = classObject.getBonfileObject().entrySet().stream().reduce((first, second) -> second).get();
        writeVariable(classObject.getObjectName(), classObject.getObjectClass());

        openBracket(true);
        append.increaseIndentation();

        for(Map.Entry<String, Object> entry : classObject.getBonfileObject().entrySet()) {
            String key = entry.getKey();
            String value;

            indentFile();

            if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("INTEGER"))) {
                writeInteger(key, (Integer) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("FLOAT"))) {
                writeFloat(key,(Float) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("DOUBLE"))) {
                writeDouble(key,(Double) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("BOOLEAN"))) {
                writeBoolean(key,(Boolean) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("CHAR"))) {
                writeChar(key,(Character) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("STRING"))) {
                writeString(key,(String) entry.getValue());
            } else if(entry.getValue() instanceof BonfileObject) {
                writeObject((BonfileObject) entry.getValue());
            } else {
                if(entry.getValue().getClass() == HashMap.class) {
                    writeDict(key, (HashMap<String, String>) entry.getValue());
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("INTEGER"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("FLOAT"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("DOUBLE"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("BOOLEAN"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        1
                    );
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("CHAR"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        2
                    );
                } else if(FileHelper.isArrayOfPrimitive((Object[]) entry.getValue(), Tokens.TOKENS.get("STRING"))) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        3
                    );
                } else if(entry.getValue() instanceof BonfileObject[]) {
                    BonfileObject[] array = (BonfileObject[]) entry.getValue();

                    for(BonfileObject obj : array) {
                        writeObject(obj);
                    }
                } else {
                    if(entry.getValue().getClass() == HashMap[].class) {
                        writeDictArray(key, (HashMap<String, String>[]) entry.getValue());
                    } else {
                        LinkedList<BonfileObject> list = (LinkedList <BonfileObject>) entry.getValue();

                        for(BonfileObject obj : list){
                            writeObject(obj);
                        }
                    }
                }
            }
        }

        append.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    private void writeDictArray(String key, HashMap<String, String>[] value) throws IOException {
        writeVariable(key, 6);
        openBracket(false);
        append.increaseIndentation();

        indentFile();

        for(HashMap<String, String> currentValue : value) {
            writeDict(currentValue);
        }

        append.decreaseIndentationCounter();
        closeBracket(false, true);
    }

    public void writeList(LinkedList<Object> linkedList) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        openBracket(false);
        append.increaseIndentation();

        if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("CHAR"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("STRING"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("BOOLEAN"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else if(FileHelper.isNumericType(linkedList.getFirst())) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        append.decreaseIndentationCounter();
        closeBracket(false, true);
        updateCaretPosition();
    }

    public void writeList(String varName, LinkedList<Object> linkedList, Integer listType) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        writeVariable(varName, listType);
        openBracket(false);
        append.increaseIndentation();

        if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("CHAR"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("STRING"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("BOOLEAN"))) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else if(FileHelper.isNumericType(linkedList.getFirst())){
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        append.decreaseIndentationCounter();
        closeBracket(false, true);
        updateCaretPosition();
    }

    private void writeListMember(Object listMember, Integer memberType, Boolean notLast) throws IOException {
        indentFile();

        switch(memberType) {
            case 0:
                file.writeBytes(listMember.toString());
                break;

            case 1:
                file.writeBytes(String.valueOf(listMember));
                break;

            case 2:
                file.writeBytes(
                    Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
                    + listMember
                    + Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
                );
                break;

            case 3:
                file.writeBytes(
                    Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
                    + listMember
                    + Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
                );
                break;

            default:
                System.err.println("An unexpected error has occurred!");
                System.exit(-1);
        }

        if(notLast) {
            file.writeBytes(Tokens.TOKENS.get("COMMA"));
        }

        file.writeBytes(Tokens.TOKENS.get("NEW_LINE"));
    }

    public static Integer getDictSize(HashMap<String, String> hashMap) {
        return hashMap.size();
    }

    public void writeDict(HashMap<String, String> dict) throws IOException {
        String lastKey = (String) dict.keySet().toArray()[getDictSize(dict) - 1];
        openBracket(true);
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            indentFile();
            writeDictLine(
                key,
                value,
                lastKey
            );
        }

        append.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    public void writeDict(String varName, HashMap<String, String> dict) throws IOException {
        String lastKey = (String) dict.keySet().toArray()[getDictSize(dict) - 1];
        writeVariable(varName, 6);
        openBracket(true);
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            indentFile();
            writeDictLine(
                key,
                value,
                lastKey
            );
        }

        append.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    public void writeDictArrayMember(HashMap<String, String> dict) throws IOException {
        String lastKey = (String) dict.keySet().toArray()[getDictSize(dict) - 1];
        openBracket(true);
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            indentFile();
            writeDictLine(
                key,
                value,
                lastKey
            );
        }

        append.decreaseIndentationCounter();
        closeBracket(true, true);
    }

    private void writeDictLine(String key, String value, String lastKey) throws IOException {
        file.writeBytes(
            key
            + Tokens.TOKENS.get("SPACE")
            + Tokens.TOKENS.get("COLON")
            + Tokens.TOKENS.get("SPACE")
            + value
        );

        if(!key.equals(lastKey)) {
            file.writeBytes(Tokens.TOKENS.get("COMMA"));
        }

        file.writeBytes(Tokens.TOKENS.get("NEW_LINE"));
    }

    private void writeVariable(String varName, Integer objectType) throws IOException {
        file.writeBytes(
            varName
            + Tokens.TOKENS.get("LET_SIGN")
        );

        switch(objectType) {
            case 0:
                file.writeBytes(Tokens.TOKENS.get("INTEGER"));
                break;

            case 1:
                file.writeBytes(Tokens.TOKENS.get("FLOAT"));
                break;

            case 2:
                file.writeBytes(Tokens.TOKENS.get("DOUBLE"));
                break;

            case 3:
                file.writeBytes(Tokens.TOKENS.get("BOOLEAN"));
                break;

            case 4:
                file.writeBytes(Tokens.TOKENS.get("CHAR"));
                break;

            case 5:
                file.writeBytes(Tokens.TOKENS.get("STRING"));
                break;

            case 6:
                file.writeBytes(Tokens.TOKENS.get("DICTIONARY"));
                break;

            default:
                System.err.println("WRONG ARGUMENT ***objectType***\nPlease note that the types range from 0 to 6.\n");
                System.exit(-1);
        }

        file.writeBytes(
            Tokens.TOKENS.get("SPACE")
            + Tokens.TOKENS.get("EQUALS_SIGN")
            + Tokens.TOKENS.get("SPACE")
        );
    }

    private void writeVariable(String varName, String className) throws IOException {
        file.writeBytes(
            varName
            + Tokens.TOKENS.get("LET_SIGN")
            + className
            + Tokens.TOKENS.get("SPACE")
            + Tokens.TOKENS.get("EQUALS_SIGN")
            + Tokens.TOKENS.get("SPACE")
        );
    }

    public void writePrimitive(Object variable) throws IOException {
        if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("INTEGER"))) {
            writeInteger((Integer) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("FLOAT"))) {
            writeFloat((Float) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("DOUBLE"))) {
            writeDouble((Double) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("BOOLEAN"))) {
            writeBoolean((Boolean) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("CHAR"))) {
            writeChar((Character) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("STRING"))) {
            writeString((String) variable);
        }

        updateCaretPosition();
    }

    public void writePrimitive(String varName, Object variable) throws IOException {
        if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("INTEGER"))) {
            writeInteger(varName, (Integer) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("FLOAT"))) {
            writeFloat(varName, (Float) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("DOUBLE"))) {
            writeDouble(varName, (Double) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("BOOLEAN"))) {
            writeBoolean(varName, (Boolean) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("CHAR"))) {
            writeChar(varName, (Character) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("STRING"))) {
            writeString(varName, (String) variable);
        }

        updateCaretPosition();
    }

    private void writeInteger(Integer variable) throws IOException {
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeInteger(String varName, Integer variable) throws IOException {
        writeVariable(varName, 0);
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeFloat(Float variable) throws IOException {
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeFloat(String varName, Float variable) throws IOException {
        writeVariable(varName, 1);
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeDouble(Double variable) throws IOException {
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeDouble(String varName, Double variable) throws IOException {
        writeVariable(varName, 2);
        file.writeBytes(
            variable
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeBoolean(Boolean variable) throws IOException {
        file.writeBytes(
            String.valueOf(variable)
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeBoolean(String varName, Boolean variable) throws IOException {
        writeVariable(varName, 3);
        file.writeBytes(
            String.valueOf(variable)
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeChar(Character variable) throws IOException {
        file.writeBytes(
            Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
            + variable
            + Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeChar(String varName, Character variable) throws IOException {
        writeVariable(varName, 4);
        file.writeBytes(
            Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
            + variable
            + Tokens.TOKENS.get("SINGLE_QUOTE_MARK")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeString(String variable) throws IOException {
        file.writeBytes(
            Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
            + variable
            + Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void writeString(String varName, String variable) throws IOException {
        writeVariable(varName, 5);
        file.writeBytes(
            Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
            + variable
            + Tokens.TOKENS.get("DOUBLE_QUOTE_MARK")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    private void updateCaretPosition() throws IOException {
        this.append.setCurrentLine((int) this.file.length());
    }

    public void rewind() throws IOException {
        append.rewind();
    }

    public void close() throws IOException {
        file.close();
    }
}
