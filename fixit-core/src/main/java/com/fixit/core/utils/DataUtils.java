package com.fixit.core.utils;

import com.fixit.core.data.Profession;

import java.util.List;

/**
 * Created by konstantin on 3/29/2017.
 */

public class DataUtils {

    public static String[] toAutoCompleteList(List<Profession> professions) {
        String[] names = new String[professions.size()];

        for(int i = 0; i < professions.size(); i++) {
            Profession profession = professions.get(i);
            names[i] = profession.getName();
        }

        return names;
    }

}
