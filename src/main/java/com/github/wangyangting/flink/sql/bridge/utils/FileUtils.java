package com.github.wangyangting.flink.sql.bridge.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wangyangting
 * @date 2024-07-25
 */
public class FileUtils {

    public static String readFileToString(String path) throws IOException {
        String str = null;

        File file = new File(path);
        if (file.exists() && file.canRead()) {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            str = new String(bytes, StandardCharsets.UTF_8);
        } else {
            System.out.println("file not found: " + path);
        }

        return str;
    }

}
