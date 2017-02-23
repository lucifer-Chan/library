package cn.ttsales.util;

import java.util.Date;

/**
 * Created by 露青 on 2016/10/20.
 */
public class DateUtil {
    public static String date(Date date){
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String time(Date date){
        return new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }
}
