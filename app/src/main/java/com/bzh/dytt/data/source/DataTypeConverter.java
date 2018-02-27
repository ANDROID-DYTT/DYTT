package com.bzh.dytt.data.source;


import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class DataTypeConverter {

    private static final String SPLIT_CHAR = "$";

    @TypeConverter
    public static List<String> strToList(String s) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            return list;
        }
        String[] strList = s.split(SPLIT_CHAR);
        for (String str : strList) {
            if (TextUtils.isEmpty(str)) {
                continue;
            }
            list.add(str);
        }
        return list;
    }

    @TypeConverter
    public static String strToList(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            result.append(s);
            result.append(SPLIT_CHAR);
        }
        return result.toString();
    }
}
