package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by konstantin on 4/2/2017.
 */

public class WorkingHours implements Parcelable {

    private double open;
    private double close;

    public WorkingHours() { }

    public WorkingHours(double open, double close) {
        this.open = open;
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "open=" + open +
                ", close=" + close +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingHours that = (WorkingHours) o;

        if (Double.compare(that.open, open) != 0) return false;
        return Double.compare(that.close, close) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(open);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(close);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.open);
        dest.writeDouble(this.close);
    }

    protected WorkingHours(Parcel in) {
        this.open = in.readDouble();
        this.close = in.readDouble();
    }

    public static final Creator<WorkingHours> CREATOR = new Creator<WorkingHours>() {
        @Override
        public WorkingHours createFromParcel(Parcel source) {
            return new WorkingHours(source);
        }

        @Override
        public WorkingHours[] newArray(int size) {
            return new WorkingHours[size];
        }
    };
}
