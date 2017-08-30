package com.fixit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Kostyantin on 4/2/2017.
 */

public class DateUtils {

    public final static String FORMAT_RFC_2822 = "EEE, dd MMM yyyy HH:mm:ss Z";
    public final static String FORMAT_REST_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public final static String FORMAT_DMY = "dd/MM/yyyy";
    public final static String FORMAT_DMY_AT_HM = "dd/MM/yyy 'at' hh:mm a";

    public static Date stringToDate(String format, String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringToDate(String format, String dateString, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(tz);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if(date != null) {
            return sdf.format(date);
        }
        return "";
    }

    public static int getCurrentDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

}
