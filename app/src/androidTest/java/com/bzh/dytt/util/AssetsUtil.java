package com.bzh.dytt.util;

import android.support.test.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsUtil {
    public static String getAssest(String name) throws IOException {
        StringBuilder buf = new StringBuilder();
        InputStream is = InstrumentationRegistry.getContext().getAssets().open(name);
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String str;
        while ((str = in.readLine()) != null) {
            buf.append(str);
        }
        in.close();
        return buf.toString();
    }
}
