package com.dhivakar.filetransferhelper.utils;

import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static final List<String> list = Arrays.asList("jpg", "png", "mp4");

    private FileUtil() {
    }

    public static boolean validateExtension(String input) {
        return list.stream().anyMatch(input::contains);
    }
}
