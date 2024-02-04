package com.bonfile.controller.readController;

import com.bonfile.model.read.Read;
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


}
