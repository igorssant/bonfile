package com.bonfile.controller.appendController;

import com.bonfile.model.bonfileObject.BonfileObject;
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
    private final Tokens TOKENS = new Tokens();

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

    public void writeObject(BonfileObject classObject) throws IOException {
        Object lastKey = classObject.getBonfileObject().entrySet().stream().reduce((first, second) -> second).get();
        writeVariable(classObject.getObjectName(), classObject.getObjectClass());

        this.file.writeBytes(
            Tokens.TOKENS.get("OPEN_CURLY_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        for(Map.Entry<String, Object> entry : classObject.getBonfileObject().entrySet()) {
            String key = entry.getKey();
            String value;

            for(int i = 0; i < append.getIndentationCounter(); i++) {
                this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
            }

            if(entry.getValue() instanceof Integer) {
                writeInteger(key, (Integer) entry.getValue());
            } else if(entry.getValue() instanceof Float) {
                writeFloat(key,(Float) entry.getValue());
            } else if(entry.getValue() instanceof Double) {
                writeDouble(key,(Double) entry.getValue());
            } else if(entry.getValue() instanceof Boolean) {
                writeBoolean(key,(Boolean) entry.getValue());
            } else if(entry.getValue() instanceof Character) {
                writeChar(key,(Character) entry.getValue());
            } else if(entry.getValue() instanceof String) {
                writeString(key,(String) entry.getValue());
            } else if (entry.getValue() instanceof BonfileObject) {
                writeObject((BonfileObject) entry.getValue());
            } else {
                if(entry.getValue().getClass() == HashMap.class) {
                    writeDict(key, (HashMap<String, String>) entry.getValue());
                } else if(entry.getValue() instanceof Integer[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(entry.getValue() instanceof Float[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(entry.getValue() instanceof Double[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        0
                    );
                } else if(entry.getValue() instanceof Boolean[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        1
                    );
                } else if(entry.getValue() instanceof Character[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        2
                    );
                } else if(entry.getValue() instanceof String[]) {
                    writeList(
                        key,
                        new LinkedList<>(List.of(entry.getValue())),
                        3
                    );
                } else if (entry.getValue() instanceof BonfileObject[]) {
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
        this.file.writeBytes(
            Tokens.TOKENS.get("CLOSE_CURLY_BRACKET")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        updateCaretPosition();
    }

    private void writeDictArray(String key, HashMap<String, String>[] value) throws IOException {
        writeVariable(key, 6);
        file.writeBytes(
            Tokens.TOKENS.get("OPEN_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        for(int i = 0; i < append.getIndentationCounter(); i++) {
            file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
        }

        for(HashMap<String, String> currentValue : value) {
            writeDict(currentValue);
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_BRACKET")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
    }

    public void writeList(LinkedList<Object> linkedList) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        file.writeBytes(
            Tokens.TOKENS.get("OPEN_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        if(linkedList.getFirst() instanceof Character) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(linkedList.getFirst() instanceof String) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(linkedList.getFirst() instanceof Boolean) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_BRACKET")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        updateCaretPosition();
    }

    public void writeList(String varName, LinkedList<Object> linkedList, Integer listType) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        writeVariable(varName, listType);
        file.writeBytes(
            Tokens.TOKENS.get("OPEN_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        if(linkedList.getFirst() instanceof Character) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(linkedList.getFirst() instanceof String) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(linkedList.getFirst() instanceof Boolean) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_BRACKET")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        updateCaretPosition();
    }

    private void writeListMember(Object listMember, Integer memberType, Boolean notLast) throws IOException {
        for(int i = 0; i < append.getIndentationCounter(); i++) {
            this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
        }

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

    public void writeDict(HashMap<String, String> dict) throws IOException {
        file.writeBytes(
            Tokens.TOKENS.get("OPEN_CURLY_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            for(int i = 0; i < append.getIndentationCounter(); i++) {
                this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
            }

            file.writeBytes(
                key
                + Tokens.TOKENS.get("SPACE")
                + Tokens.TOKENS.get("COLON")
                + Tokens.TOKENS.get("SPACE")
                + value
                + Tokens.TOKENS.get("NEW_LINE")
            );
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_CURLY_BRACKET")
            + Tokens.TOKENS.get("SEMICOLON")
            + Tokens.TOKENS.get("NEW_LINE")
        );

        updateCaretPosition();
    }

    public void writeDict(String varName, HashMap<String, String> dict) throws IOException {
        writeVariable(varName, 6);

        file.writeBytes(
            Tokens.TOKENS.get("OPEN_CURLY_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            for(int i = 0; i < append.getIndentationCounter(); i++) {
                this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
            }

            file.writeBytes(
                key
                + Tokens.TOKENS.get("SPACE")
                + Tokens.TOKENS.get("COLON")
                + Tokens.TOKENS.get("SPACE")
                + value
                + Tokens.TOKENS.get("NEW_LINE")
            );
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_CURLY_BRACKET")
                + Tokens.TOKENS.get("SEMICOLON")
                + Tokens.TOKENS.get("NEW_LINE")
        );

        updateCaretPosition();
    }

    public void writeDictArrayMember(HashMap<String, String> dict) throws IOException {
        file.writeBytes(
            Tokens.TOKENS.get("OPEN_CURLY_BRACKET")
            + Tokens.TOKENS.get("NEW_LINE")
        );
        append.increaseIndentation();

        for(Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            for(int i = 0; i < append.getIndentationCounter(); i++) {
                this.file.writeBytes(Tokens.TOKENS.get("INDENTATION"));
            }

            file.writeBytes(
                key
                + Tokens.TOKENS.get("SPACE")
                + Tokens.TOKENS.get("COLON")
                + Tokens.TOKENS.get("SPACE")
                + value
                + Tokens.TOKENS.get("NEW_LINE")
            );
        }

        append.decreaseIndentationCounter();
        file.writeBytes(
            Tokens.TOKENS.get("CLOSE_CURLY_BRACKET")
            + Tokens.TOKENS.get("COMMA")
            + Tokens.TOKENS.get("NEW_LINE")
        );
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
        if(variable instanceof Integer) {
            writeInteger((Integer) variable);
        } else if(variable instanceof Float) {
            writeFloat((Float) variable);
        } else if(variable instanceof Double) {
            writeDouble((Double) variable);
        } else if(variable instanceof Boolean) {
            writeBoolean((Boolean) variable);
        } else if(variable instanceof Character) {
            writeChar((Character) variable);
        } else if(variable instanceof String) {
            writeString((String) variable);
        }

        updateCaretPosition();
    }

    public void writePrimitive(String varName, Object variable) throws IOException {
        if(variable instanceof Integer) {
            writeInteger(varName, (Integer) variable);
        } else if(variable instanceof Float) {
            writeFloat(varName, (Float) variable);
        } else if(variable instanceof Double) {
            writeDouble(varName, (Double) variable);
        } else if(variable instanceof Boolean) {
            writeBoolean(varName, (Boolean) variable);
        } else if(variable instanceof Character) {
            writeChar(varName, (Character) variable);
        } else if(variable instanceof String) {
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
