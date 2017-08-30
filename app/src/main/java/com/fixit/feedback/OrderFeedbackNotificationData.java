package com.fixit.feedback;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public class OrderFeedbackNotificationData implements Parcelable {

    public final String orderId;
    public final String title;
    public final String message;
    public final int flowCode;
    public final long delayMin;

    public OrderFeedbackNotificationData(String orderId, String title, String message, int flowCode, long delayMin) {
        this.orderId = orderId;
        this.title = title;
        this.message = message;
        this.flowCode = flowCode;
        this.delayMin = delayMin;
    }

    @Override
    public String toString() {
        return "OrderFeedbackNotificationData{" +
                "orderId=" + orderId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", flowCode=" + flowCode +
                ", delayMin=" + delayMin +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeInt(this.flowCode);
        dest.writeLong(this.delayMin);
    }

    protected OrderFeedbackNotificationData(Parcel in) {
        this.orderId = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.flowCode = in.readInt();
        this.delayMin = in.readLong();
    }

    public static final Creator<OrderFeedbackNotificationData> CREATOR = new Creator<OrderFeedbackNotificationData>() {
        @Override
        public OrderFeedbackNotificationData createFromParcel(Parcel source) {
            return new OrderFeedbackNotificationData(source);
        }

        @Override
        public OrderFeedbackNotificationData[] newArray(int size) {
            return new OrderFeedbackNotificationData[size];
        }
    };
}
