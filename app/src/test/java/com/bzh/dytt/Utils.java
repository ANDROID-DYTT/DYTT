package com.bzh.dytt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String getResource(Class clazz, String file) throws IOException {
        ClassLoader classLoader = clazz.getClassLoader();
        File indexFile = new File(classLoader.getResource(file).getFile());
        byte[] bytes = new byte[(int) indexFile.length()];
        FileInputStream fileInputStream = new FileInputStream(indexFile);
        fileInputStream.read(bytes);
        return new String(bytes);
    }

    public static String getMD5(String content) throws NoSuchAlgorithmException {
        return convertByteToHex(MessageDigest.getInstance("MD5").digest(content.getBytes()));
    }

    public static String convertByteToHex(byte[] byteData) {
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
