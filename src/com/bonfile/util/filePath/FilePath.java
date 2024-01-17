package com.bonfile.util.filePath;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {
    public static String getAbsolutePath() {
        return getCurrentPath().toAbsolutePath().toString();
    }

    public static String getFilePath(String file) {
        return Paths.get(file).toAbsolutePath().toString();
    }

    public static Path getCurrentPath() {
        return Paths.get("");
    }
}
