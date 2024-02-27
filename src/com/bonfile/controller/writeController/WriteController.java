package com.bonfile.controller.writeController;

import com.bonfile.controller.tupleController.*;
import com.bonfile.model.bonfileObject.BonfileObject;
import com.bonfile.model.write.Write;
import com.bonfile.util.fileHelper.FileHelper;
import com.bonfile.util.filePath.FilePath;
import com.bonfile.util.tokens.Tokens;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class WriteController implements AutoCloseable {
    private Write write;
    private RandomAccessFile file;

    public WriteController() {}

    public WriteController(String filePath) throws IOException, FileNotFoundException {
        String fileName = filePath.substring(filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1);
        this.file = new RandomAccessFile(FilePath.getFilePath(fileName), "w");
        this.write = new Write(fileName, filePath.substring(0, filePath.lastIndexOf(Tokens.TOKENS.get("SLASH_SIGN")) + 1), this.file.length());
        file.seek(0);
    }

    private Write getWrite() {
        return this.write;
    }

    public void setWrite() {
        this.write = write;
    }

    private void openParenthesis() throws IOException {
        this.file.writeBytes(Tokens.TOKENS.get("OPEN_PARENTHESIS"));
    }

    private void closeParenthesis() throws IOException {
        this.file.writeBytes(
                Tokens.TOKENS.get("CLOSE_PARENTHESIS")
                        + Tokens.TOKENS.get("SEMICOLON")
                        + Tokens.TOKENS.get("NEW_LINE")
        );
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
        for(int i = 0; i < write.getIndentationCounter(); i++) {
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
        write.increaseIndentation();

        for(Map.Entry<String, Object> entry : classObject.getBonfileObject().entrySet()) {
            String key = entry.getKey();
            String value;

            indentFile();

            if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("INTEGER"), true)) {
                writeInteger(key, (Integer) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("FLOAT"), true)) {
                writeFloat(key,(Float) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("DOUBLE"), true)) {
                writeDouble(key,(Double) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("BOOLEAN"), true)) {
                writeBoolean(key,(Boolean) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("CHAR"), true)) {
                writeChar(key,(Character) entry.getValue());
            } else if(FileHelper.isPrimitiveType(entry.getValue(), Tokens.TOKENS.get("STRING"), true)) {
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

        write.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    private void writeDictArray(String key, HashMap<String, String>[] value) throws IOException {
        writeVariable(key, 6);
        openBracket(false);
        write.increaseIndentation();
        indentFile();

        for(HashMap<String, String> currentValue : value) {
            writeDict(currentValue);
        }

        write.decreaseIndentationCounter();
        closeBracket(false, true);
    }

    public void writeList(LinkedList<Object> linkedList) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        openBracket(false);
        write.increaseIndentation();

        if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("CHAR"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("STRING"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("BOOLEAN"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else if(FileHelper.isNumericType(linkedList.getFirst())) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        write.decreaseIndentationCounter();
        closeBracket(false, true);
        updateCaretPosition();
    }

    public void writeList(String varName, LinkedList<Object> linkedList, Integer listType) throws IOException {
        ListIterator<Object> iterator = linkedList.listIterator();

        writeVariable(varName, listType);
        openBracket(false);
        write.increaseIndentation();

        if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("CHAR"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 2, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("STRING"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 3, iterator.hasNext());
            }
        } else if(FileHelper.isPrimitiveType(linkedList.getFirst(), Tokens.TOKENS.get("BOOLEAN"), true)) {
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 1, iterator.hasNext());
            }
        } else if(FileHelper.isNumericType(linkedList.getFirst())){
            while(iterator.hasNext()){
                writeListMember(iterator.next(), 0, iterator.hasNext());
            }
        }

        write.decreaseIndentationCounter();
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
        write.increaseIndentation();

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

        write.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    public void writeDict(String varName, HashMap<String, String> dict) throws IOException {
        String lastKey = (String) dict.keySet().toArray()[getDictSize(dict) - 1];
        writeVariable(varName, 6);
        openBracket(true);
        write.increaseIndentation();

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

        write.decreaseIndentationCounter();
        closeBracket(true, true);
        updateCaretPosition();
    }

    public void writeDictArrayMember(HashMap<String, String> dict) throws IOException {
        String lastKey = (String) dict.keySet().toArray()[getDictSize(dict) - 1];
        openBracket(true);
        write.increaseIndentation();

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

        write.decreaseIndentationCounter();
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

            case 7:
                file.writeBytes(Tokens.TOKENS.get("TUPLE"));
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
        if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("INTEGER"), true)) {
            writeInteger((Integer) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("FLOAT"), true)) {
            writeFloat((Float) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("DOUBLE"), true)) {
            writeDouble((Double) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("BOOLEAN"), true)) {
            writeBoolean((Boolean) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("CHAR"), true)) {
            writeChar((Character) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("STRING"), true)) {
            writeString((String) variable);
        }

        updateCaretPosition();
    }

    public void writePrimitive(String varName, Object variable) throws IOException {
        if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("INTEGER"), true)) {
            writeInteger(varName, (Integer) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("DOUBLE"), true)) {
            writeDouble(varName, (Double) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("FLOAT"), true)) {
            writeFloat(varName, (Float) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("BOOLEAN"), true)) {
            writeBoolean(varName, (Boolean) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("CHAR"), true)) {
            writeChar(varName, (Character) variable);
        } else if(FileHelper.isPrimitiveType(variable, Tokens.TOKENS.get("STRING"), true)) {
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
        this.write.setCurrentLine(this.file.length());
    }

    public void rewind() throws IOException {
        this.file.seek(0);
        write.rewind();
    }

    @Override
    public void close() throws IOException {
        file.close();
    }

    private void writeTupleElement(UnitController<Object> unitController) throws IOException {
        for(Object element : unitController.getTuple()) {
            this.file.writeBytes(element.toString());
        }

        updateCaretPosition();
    }
    private void writeTupleElement(Object[] array) throws IOException {
        Object lastElement = array[array.length - 1];

        for(Object element : array) {
            this.file.writeBytes(element.toString());

            if(!element.equals(lastElement)) {
                this.file.writeBytes(Tokens.TOKENS.get("COMMA") + Tokens.TOKENS.get("SPACE"));
            }
        }

        updateCaretPosition();
    }

    public void writeTuple(String varName, UnitController<Object> unitController) throws IOException {
        writeVariable(varName, 7);
        openParenthesis();

        if(FileHelper.isPrimitiveType(unitController.getItem(), Tokens.TOKENS.get("CHAR"), true)) {
            unitController.setItem(FileHelper.addSingleQuoteMark(unitController.getItem()));
        } else if(FileHelper.isPrimitiveType(unitController.getItem(), Tokens.TOKENS.get("STRING"), true)) {
            unitController.setItem(FileHelper.addDoubleQuoteMark(unitController.getItem()));
        }

        writeTupleElement(unitController);
        closeParenthesis();
        updateCaretPosition();
    }

    public void writeTuple(String varName, PairController<Object, Object> pairController) throws IOException {
        Object[] listAsArray = pairController.getTuple().toArray();

        writeVariable(varName, 7);
        openParenthesis();

        for(int i = 0; i < listAsArray.length; i++) {
            if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("CHAR"), true)) {
                listAsArray[i] = FileHelper.addSingleQuoteMark(listAsArray[i]);
            } else if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("STRING"), true)) {
                listAsArray[i] = FileHelper.addDoubleQuoteMark(listAsArray[i]);
            }
        }

        writeTupleElement(listAsArray);
        closeParenthesis();
        updateCaretPosition();
    }

    public void writeTuple(String varName, TripletController<Object, Object, Object> tripletController) throws IOException {
        Object[] listAsArray = tripletController.getTuple().toArray();

        writeVariable(varName, 7);
        openParenthesis();

        for(int i = 0; i < listAsArray.length; i++) {
            if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("CHAR"), true)) {
                listAsArray[i] = FileHelper.addSingleQuoteMark(listAsArray[i]);
            } else if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("STRING"), true)) {
                listAsArray[i] = FileHelper.addDoubleQuoteMark(listAsArray[i]);
            }
        }

        writeTupleElement(listAsArray);
        closeParenthesis();
        updateCaretPosition();
    }

    public void writeTuple(String varName, QuartetController<Object, Object, Object, Object> quartetController) throws IOException {
        Object[] listAsArray = quartetController.getTuple().toArray();

        writeVariable(varName, 7);
        openParenthesis();

        for(int i = 0; i < listAsArray.length; i++) {
            if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("CHAR"), true)) {
                listAsArray[i] = FileHelper.addSingleQuoteMark(listAsArray[i]);
            } else if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("STRING"), true)) {
                listAsArray[i] = FileHelper.addDoubleQuoteMark(listAsArray[i]);
            }
        }

        writeTupleElement(listAsArray);
        closeParenthesis();
        updateCaretPosition();
    }

    public void writeTuple(String varName, QuintupletController<Object, Object, Object, Object, Object> quintupletController) throws IOException {
        Object[] listAsArray = quintupletController.getTuple().toArray();

        writeVariable(varName, 7);
        openParenthesis();

        for(int i = 0; i < listAsArray.length; i++) {
            if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("CHAR"), true)) {
                listAsArray[i] = FileHelper.addSingleQuoteMark(listAsArray[i]);
            } else if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("STRING"), true)) {
                listAsArray[i] = FileHelper.addDoubleQuoteMark(listAsArray[i]);
            }
        }

        writeTupleElement(listAsArray);
        closeParenthesis();
        updateCaretPosition();
    }

    public void writeTuple(String varName, SextetController<Object, Object, Object, Object, Object, Object> sextetController) throws IOException {
        Object[] listAsArray = sextetController.getTuple().toArray();

        writeVariable(varName, 7);
        openParenthesis();

        for(int i = 0; i < listAsArray.length; i++) {
            if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("CHAR"), true)) {
                listAsArray[i] = FileHelper.addSingleQuoteMark(listAsArray[i]);
            } else if(FileHelper.isPrimitiveType(listAsArray[i], Tokens.TOKENS.get("STRING"), true)) {
                listAsArray[i] = FileHelper.addDoubleQuoteMark(listAsArray[i]);
            }
        }

        writeTupleElement(listAsArray);
        closeParenthesis();
        updateCaretPosition();
    }
}
