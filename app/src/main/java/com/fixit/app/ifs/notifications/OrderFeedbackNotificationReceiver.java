package com.fixit.app.ifs.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.fixit.core.receivers.BaseBroadcastReceiver;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;

/**
 * Created by konstantin on 7/19/2017.
 */

public class OrderFeedbackNotificationReceiver extends BaseBroadcastReceiver {

    public final static String ACTION = "com.fixit.app.order.feedback.notifier";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        long orderId = intent.getLongExtra(Constants.ARG_ORDER_ID, -1);

        if(orderId > -1) {
            Notification notification = OrderNotificationManager.createOrderFeedbackNotification(context, orderId);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, notification);
        } else {
            FILog.w(OrderFeedbackNotificationReceiver.class.getName(), "Cannot create notification without order id", context);
        }
    }

}
