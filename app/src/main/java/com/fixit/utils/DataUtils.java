package com.fixit.utils;

import android.location.Address;
import android.text.TextUtils;

import com.fixit.data.Profession;

import java.util.ArrayList;

/**
 * Created by konstantin on 3/29/2017.
 */

public class DataUtils {

    public static String[] toAutoCompleteList(Profession[] professions) {
        int professionCount = professions.length;
        String[] names = new String[professionCount];

        for(int i = 0; i < professionCount; i++) {
            Profession profession = professions[i];
            names[i] = profession.getName();
        }

        return names;
    }

    public static String combineAddressLines(Address address) {
        ArrayList<String> addressFragments = new ArrayList<>();
        for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }
        return TextUtils.join(System.getProperty("line.separator"), addressFragments);
    }

}
