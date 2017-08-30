package com.fixit.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.fixit.app.R;
import com.fixit.feedback.OrderFeedbackFlowManager;
import com.fixit.feedback.OrderFeedbackNotificationData;
import com.fixit.ui.activities.OrderFeedbackActivity;
import com.fixit.utils.Constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class OrderNotificationManager {

    public static void initiateOrderFeedback(Context context, String orderId) {
        Intent intent = new Intent();
        intent.setAction(OrderFeedbackNotificationReceiver.ACTION);

        OrderFeedbackNotificationData[] notificationData = OrderFeedbackFlowManager.createNotificationData(context, orderId);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(OrderFeedbackNotificationData data : notificationData) {
            intent.putExtra(Constants.ARG_NOTIFICATION_DATA, data);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(data.delayMin), alarmIntent);
        }
    }

    public static void registerOrderFeedbackNotification(Context context, String orderId, boolean sendImmediately, int forFlow) {
        Intent intent = new Intent();
        intent.setAction(OrderFeedbackNotificationReceiver.ACTION);

        OrderFeedbackNotificationData[] notificationData = OrderFeedbackFlowManager.createNotificationData(context, orderId, forFlow);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(OrderFeedbackNotificationData data : notificationData) {
            intent.putExtra(Constants.ARG_NOTIFICATION_DATA, data);
            if(sendImmediately) {
                context.sendBroadcast(intent);
            } else {
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                alarmMgr.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(data.delayMin), alarmIntent);
            }
        }
    }

    static Notification createOrderFeedbackNotification(Context context, OrderFeedbackNotificationData data) {
        Notification notification = new NotificationCompat.Builder(context)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_xl_yellow_logo_white_border))
                .setSmallIcon(R.drawable.ic_large_yellow_logo_white_border)
                .setContentTitle(data.title)
                .setContentText(data.message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.message))
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent(context, 1, data.orderId, data.flowCode, false, false))
                .addAction(createAction(context, 2, data.orderId, data.flowCode, true))
                .addAction(createAction(context, 3, data.orderId, data.flowCode, false))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[] {1, 1, 1})
                .build();

        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;

        return notification;
    }

    private static NotificationCompat.Action createAction(Context context, int requestCode, String orderId, int flowCode, boolean yesAction) {
        int drawableId;
        int textId;
        if(yesAction) {
            drawableId = R.drawable.ic_check_white_24dp;
            textId = R.string.yes;
        } else {
            drawableId = R.drawable.ic_close_white_24dp;
            textId = R.string.no;
        }
        return new NotificationCompat.Action.Builder(
                drawableId,
                context.getString(textId),
                createPendingIntent(context, requestCode, orderId, flowCode, true, yesAction)
        ).build();
    }

    private static PendingIntent createPendingIntent(Context context, int requestCode, String orderId, int flowCode, boolean fromAction, boolean yesAction) {
        Intent intent;
        if(!fromAction) {
            intent = createDefaultIntent(context, orderId, flowCode);
        } else {
            intent = createActionIntent(context, orderId, flowCode, yesAction);
        }

        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static Intent createActionIntent(Context context, String orderId, int flowCode, boolean yesAction) {
        Intent intent = createIntent(context, orderId, flowCode);
        intent.putExtra(Constants.ARG_FROM_ACTION, true);
        intent.putExtra(Constants.ARG_SELECTED_YES, yesAction);
        return intent;
    }

    private static Intent createDefaultIntent(Context context, String orderId, int flowCode) {
        Intent intent = createIntent(context, orderId, flowCode);
        intent.putExtra(Constants.ARG_FROM_ACTION, false);
        return intent;
    }

    private static Intent createIntent(Context context, String orderId, int flowCode) {
        Intent intent = new Intent(context, OrderFeedbackActivity.class);
        intent.putExtra(Constants.ARG_ORDER_ID, orderId);
        intent.putExtra(Constants.ARG_FLOW_CODE, flowCode);
        intent.putExtra(Constants.ARG_FROM_NOTIFICATION, true);
        return intent;
    }

}
