package com.fixit.core.utils;

import com.fixit.core.data.Tradesman;
import com.fixit.core.data.WorkingDay;
import com.fixit.core.data.WorkingHours;

/**
 * Created by konstantin on 4/27/2017.
 */

public class ObjectGenerator {

    public static Tradesman createTradesman() {
        Tradesman tradesman = new Tradesman();
        tradesman.setWorkingDays(createWorkingDays());
        tradesman.setRating(3.5f);
        return tradesman;
    }

    public static WorkingDay[] createWorkingDays() {
        WorkingDay[] workingDays = new WorkingDay[5];
        workingDays[0] = new WorkingDay(1, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[1] = new WorkingDay(2, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[2] = new WorkingDay(3, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[3] = new WorkingDay(4, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[4] = new WorkingDay(5, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        return workingDays;
    }
}
