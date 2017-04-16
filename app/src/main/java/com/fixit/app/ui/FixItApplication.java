package com.fixit.app.ui;

import com.fixit.core.BaseApplication;

/**
 * Created by konstantin on 3/29/2017.
 */

public class FixItApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        /*ProfessionDAO professionDAO = getDaoFactory().createProfessionDao();
        if(professionDAO.count() == 0) {
            List<Profession> professions = new ArrayList<>();
            professions.add(new Profession(1, "Plumber", "All you plumbing needs", "", true, new Date()));
            professions.add(new Profession(2, "Mechanic", "All you plumbing needs", "", true, new Date()));
            professions.add(new Profession(3, "Locksmith", "All you plumbing needs", "", true, new Date()));
            professions.add(new Profession(4, "Electrician", "All you plumbing needs", "", true, new Date()));
            professions.add(new Profession(5, "Air Con Repair Man", "All you plumbing needs", "", true, new Date()));

            professionDAO.insert(professions);
        }*/
    }
}
