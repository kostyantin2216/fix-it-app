package com.fixit.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import com.fixit.feedback.OrderFeedbackNotificationData;
import com.fixit.receivers.BaseBroadcastReceiver;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;

/**
 * Created by konstantin on 7/19/2017.
 */

public class OrderFeedbackNotificationReceiver extends BaseBroadcastReceiver {

    public final static String ACTION = "com.fixit.app.order.feedback.notifier";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        OrderFeedbackNotificationData data = intent.getParcelableExtra(Constants.ARG_NOTIFICATION_DATA);

        if(data != null) {
            int requestCode = data.orderId.hashCode();
            Notification notification = OrderNotificationManager.createOrderFeedbackNotification(context, data, requestCode);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(requestCode, notification);
        } else {
            FILog.w(OrderFeedbackNotificationReceiver.class.getName(), "Cannot create notification without notification data", context);
        }
    }

}
