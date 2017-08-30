package com.fixit.utils;

import com.fixit.data.Profession;

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

}
