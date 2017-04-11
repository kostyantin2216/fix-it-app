package com.fixit.core.data;

import java.util.Arrays;

/**
 * Created by konstantin on 4/2/2017.
 */

public class WorkingDay {

    private int dayOfWeek;
    private WorkingHours[] hours;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public WorkingHours[] getHours() {
        return hours;
    }

    public void setHours(WorkingHours[] hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "WorkingDay{" +
                "dayOfWeek=" + dayOfWeek +
                ", hours=" + Arrays.toString(hours) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingDay that = (WorkingDay) o;

        if (dayOfWeek != that.dayOfWeek) return false;

        return Arrays.equals(hours, that.hours);

    }

    @Override
    public int hashCode() {
        int result = dayOfWeek;
        result = 31 * result + Arrays.hashCode(hours);
        return result;
    }
}
