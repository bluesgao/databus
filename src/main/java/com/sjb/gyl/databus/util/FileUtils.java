package com.sjb.gyl.databus.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class FileUtils {
    public static String md5(String path) {
        String md5Hex = null;
        try {
            md5Hex = DigestUtils.md5Hex(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5Hex;
    }

    public static String read(File file) {
        Scanner scanner = null;

        StringBuilder buffer = null;
        try {
            buffer = new StringBuilder();
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            log.error("读取文件错误：e:", e);

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }


    public static List<String> readAll(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (File temp : tempList) {
            if (temp.isFile()) {
                files.add(temp.toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            } else {
                //递归
                files.addAll(readAll(temp.getPath()));
            }
        }
        return files;
    }
}
