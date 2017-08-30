package com.fixit.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public final static boolean isValidPhoneNumber(CharSequence phone) {
        return phone != null && !(phone.length() < 6 || phone.length() > 14) && Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isEmpty(List<?> l) {
        return l == null || l.size() == 0;
    }

    public static boolean notEmpty(List<?> l) {
        return l != null && l.size() > 0;
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

    public static <C, T extends C> C[] toArray(Class<C> componentType, List<T> list) {
        @SuppressWarnings("unchecked")
        C[] array = (C[]) Array.newInstance(componentType, list.size());
        return list.toArray(array);
    }

    public static Bundle toBundle(Map<String, String> map) {
        Bundle bundle = new Bundle();
        for(Map.Entry<String, String> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

}
