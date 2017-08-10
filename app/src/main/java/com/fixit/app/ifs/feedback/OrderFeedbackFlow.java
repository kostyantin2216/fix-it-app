package com.fixit.app.ifs.feedback;

/**
 * Created by konstantin on 8/9/2017.
 */

public class OrderFeedbackFlow {

    private OrderFeedbackFlowType type;
    private String notificationTitle;
    private String notificationBody;
    private long delayMinutes;

    public OrderFeedbackFlow(OrderFeedbackFlowType type, String notificationTitle, String notificationBody, long delayMinutes) {
        this.type = type;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.delayMinutes = delayMinutes;
    }

    public OrderFeedbackFlowType getType() {
        return type;
    }

    public void setType(OrderFeedbackFlowType type) {
        this.type = type;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public long getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(long delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    @Override
    public String toString() {
        return "OrderFeedbackFlow{" +
                "type=" + type +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationBody='" + notificationBody + '\'' +
                ", delayMinutes=" + delayMinutes +
                '}';
    }
}
