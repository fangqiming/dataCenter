package com.gougu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTime(Long millis) {
        Date date = new Date(millis);
        return simpleDateFormat.format(date);
    }
}
