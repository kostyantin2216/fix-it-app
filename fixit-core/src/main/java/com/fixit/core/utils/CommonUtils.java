package com.fixit.core.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Kostyantin on 12/23/2016.
 */

public class CommonUtils {

    public static String toPrettyString(Enum<?> _enum) {
        String[] split = _enum.name().split("_");
        String result;

        if(split.length > 1) {
            result = "";
            Iterator<String> itr = Arrays.asList(split).iterator();
            while(itr.hasNext()) {
                result += capitalize(itr.next());
                if(itr.hasNext()) {
                    result += " ";
                }
            }
        } else {
            result = capitalize(split[0]);
        }

        return result;
    }

    public static <K, V> String toString(Map<K, V> map) {
        StringBuilder sb = new StringBuilder();

        Iterator<Map.Entry<K, V>> itr = map.entrySet().iterator();
        boolean hasNext = itr.hasNext();
        while(hasNext) {
            Map.Entry<K, V> entry = itr.next();
            String value = entry.getValue().toString();
            sb.append(entry.getKey()).append("=").append(value);

            hasNext = itr.hasNext();
            if(hasNext) {
                sb.append("&");
            }
        }

        return sb.toString();
    }

    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }

    public static String capitalizeAllWords(String string) {
        if(!TextUtils.isEmpty(string)) {
            String[] arr = string.split(" ");
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < arr.length; i++) {
                String word = arr[i];
                if(!TextUtils.isEmpty(word)) {
                    sb.append(Character.toUpperCase(arr[i].charAt(0)));
                    if(word.length() > 1) {
                        sb.append(arr[i].substring(1).toLowerCase()).append(" ");
                    }
                }
            }

            return sb.toString().trim();
        }
        return "";
    }

    public static boolean isNumber(String string) {
        if(string != null) {
            String regexStr = "^[0-9]*$";
            String trimmed = string.trim();
            return !trimmed.isEmpty() && trimmed.matches(regexStr);
        }
        return false;
    }

    public static boolean isDecimal(String string) {
        if(string != null) {
            String regexStr = "([0-9]*)\\.([0-9]*)";
            String trimmed = string.trim();
            return !trimmed.isEmpty() && trimmed.matches(regexStr);
        }
        return false;
    }

    public static boolean isBoolean(String string) {
        return string != null && (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false"));
    }

    public static int getPercentage(int value, int percentage) {
        return (int)(value * (percentage / 100.0f));
    }

    public static String hundredthsToTimeDisplay(double hundredths) {
        if(hundredths > 0) {
            int hours = (int) Math.floor(hundredths);
            int minutes = (int) ((hundredths - (long) hundredths) * 60);

            return String.format("%02d:%02d", hours, minutes);
        }
        return "";
    }

}
