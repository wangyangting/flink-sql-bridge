package com.github.wangyangting.flink.sql.bridge.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyangting
 * @date 2024-07-25
 */
public class DateUtils {

    public static String formatString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

}
