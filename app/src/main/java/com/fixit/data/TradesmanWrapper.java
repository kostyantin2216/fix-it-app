package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by konstantin on 5/16/2017.
 */

public class TradesmanWrapper implements Parcelable {

    public static Tradesman[] unwrap(List<TradesmanWrapper> tradesmanWrapperList) {
        Tradesman[] tradesmen = new Tradesman[tradesmanWrapperList.size()];
        for(int i = 0; i < tradesmen.length; i++) {
            tradesmen[i] = tradesmanWrapperList.get(i).tradesman;
        }
        return tradesmen;
    }

    public final Tradesman tradesman;
    public final int reviewCount;
    private boolean selected;

    public TradesmanWrapper(Tradesman tradesman, int reviewCount) {
        this.tradesman = tradesman;
        this.reviewCount = reviewCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.tradesman, flags);
        dest.writeInt(this.reviewCount);
    }

    protected TradesmanWrapper(Parcel in) {
        this.tradesman = in.readParcelable(Tradesman.class.getClassLoader());
        this.reviewCount = in.readInt();
    }

    public static final Creator<TradesmanWrapper> CREATOR = new Creator<TradesmanWrapper>() {
        @Override
        public TradesmanWrapper createFromParcel(Parcel source) {
            return new TradesmanWrapper(source);
        }

        @Override
        public TradesmanWrapper[] newArray(int size) {
            return new TradesmanWrapper[size];
        }
    };
}
