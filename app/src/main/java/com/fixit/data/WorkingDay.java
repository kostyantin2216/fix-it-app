package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by konstantin on 4/2/2017.
 *
 * dayOfWeek starts from 1 same as {@link Calendar}.
 *  1 = sunday
 *  2 = monday
 *  3 = tuesday
 *  etc..
 *
 */
public class WorkingDay implements Parcelable {

    private int dayOfWeek;
    private WorkingHours[] hours;

    public WorkingDay() { }

    public WorkingDay(int dayOfWeek, WorkingHours[] hours) {
        this.dayOfWeek = dayOfWeek;
        this.hours = hours;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dayOfWeek);
        dest.writeTypedArray(this.hours, flags);
    }

    protected WorkingDay(Parcel in) {
        this.dayOfWeek = in.readInt();
        this.hours = in.createTypedArray(WorkingHours.CREATOR);
    }

    public static final Creator<WorkingDay> CREATOR = new Creator<WorkingDay>() {
        @Override
        public WorkingDay createFromParcel(Parcel source) {
            return new WorkingDay(source);
        }

        @Override
        public WorkingDay[] newArray(int size) {
            return new WorkingDay[size];
        }
    };
}
