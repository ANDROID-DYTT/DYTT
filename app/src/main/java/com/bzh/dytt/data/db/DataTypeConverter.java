package com.bzh.dytt.data.db;


import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import com.bzh.dytt.data.MovieCategory;

import java.util.ArrayList;
import java.util.List;


public class DataTypeConverter {

    private static final String SPLIT_CHAR = "$";

    @TypeConverter
    public static MovieCategory intToEnum(int category) {
        switch (category) {
            case 1:
                return MovieCategory.NEW_MOVIE;
            case 2:
                return MovieCategory.CHINA_MOVIE;
            case 3:
                return MovieCategory.OUMEI_MOVIE;
            case 4:
                return MovieCategory.RIHAN_MOVIE;
            case 5:
                return MovieCategory.HOME_LATEST_MOVIE;
            case 6:
                return MovieCategory.SEARCH_MOVIE;
        }
        return MovieCategory.NONE;
    }

    @TypeConverter
    public static int enumToInt(MovieCategory category) {
        return category.getId();
    }

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
        if (list == null) {
            return result.toString();
        }
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
