package com.fixit.core.data;

/**
 * Created by konstantin on 4/2/2017.
 */

public class WorkingHours {

    private double open;
    private double close;

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
}
