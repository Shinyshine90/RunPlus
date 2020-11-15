package cn.shawn.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String FORMAT_YY_MM_DD = "yyyy-MM-dd HH:mm:ss";

    public static String format(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YY_MM_DD);
        return format.format(new Date(timeStamp));
    }
}
