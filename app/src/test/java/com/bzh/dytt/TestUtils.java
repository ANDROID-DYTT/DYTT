package com.bzh.dytt;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestUtils {

    public static final LifecycleOwner TEST_OBSERVER = new LifecycleOwner() {

        private LifecycleRegistry mRegistry = init();

        // Creates a LifecycleRegistry in RESUMED state.
        private LifecycleRegistry init() {
            LifecycleRegistry registry = new LifecycleRegistry(this);
            registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            return registry;
        }

        @Override
        public Lifecycle getLifecycle() {
            return mRegistry;
        }
    };

    public static String getResource(Class clazz, String file) throws IOException {
        return getResource(clazz, file, null);
    }

    public static String getResource(Class clazz, String file, String encoding) throws IOException {
        ClassLoader classLoader = clazz.getClassLoader();
        File indexFile = new File(classLoader.getResource(file).getFile());
        byte[] bytes = new byte[(int) indexFile.length()];
        FileInputStream fileInputStream = new FileInputStream(indexFile);
        fileInputStream.read(bytes);
        if (encoding != null) {
            return new String(bytes, encoding);
        }
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
