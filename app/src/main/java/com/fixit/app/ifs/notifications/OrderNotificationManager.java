package com.fixit.app.ifs.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.fixit.app.R;
import com.fixit.app.ui.activities.OrderFeedbackActivity;
import com.fixit.app.ui.activities.SearchActivity;
import com.fixit.core.data.Order;
import com.fixit.core.utils.Constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class OrderNotificationManager {

    public static void registerOrderFeedbackNotification(Context context, Order order) {
        Intent intent = new Intent();
        intent.setAction(OrderFeedbackNotificationReceiver.ACTION);
        intent.putExtra(Constants.ARG_ORDER_ID, order.getId());

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(2), alarmIntent);
    }

    static Notification createOrderFeedbackNotification(Context context, long orderId) {
        String title = context.getString(R.string.order_notification_title);
        String message = context.getString(R.string.order_notification_message);
        Notification notification = new NotificationCompat.Builder(context)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_xl_yellow_logo_white_border))
                .setSmallIcon(R.drawable.ic_large_yellow_logo_white_border)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent(context, orderId, false, false))
                .addAction(createAction(context, orderId, true))
                .addAction(createAction(context, orderId, false))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[] {1, 1, 1})
                .build();

        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;

        return notification;
    }

    private static NotificationCompat.Action createAction(Context context, long orderId, boolean yesAction) {
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
                createPendingIntent(context, orderId, true, yesAction)
        ).build();
    }

    private static PendingIntent createPendingIntent(Context context, long orderId, boolean fromAction, boolean yesAction) {
        Intent intent;
        if(!fromAction) {
            intent = createDefaultIntent(context, orderId);
        } else {
            intent = createActionIntent(context, orderId, yesAction);
        }

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static Intent createActionIntent(Context context, long orderId, boolean yesAction) {
        Intent intent = createIntent(context, orderId);
        intent.putExtra(Constants.ARG_FROM_ACTION, true);
        intent.putExtra(Constants.ARG_SELECTED_YES, yesAction);
        return intent;
    }

    private static Intent createDefaultIntent(Context context, long orderId) {
        Intent intent = createIntent(context, orderId);
        intent.putExtra(Constants.ARG_FROM_ACTION, false);
        return intent;
    }

    private static Intent createIntent(Context context, long orderId) {
        Intent intent = new Intent(context, OrderFeedbackActivity.class);
        intent.putExtra(Constants.ARG_ORDER_ID, orderId);
        return intent;
    }

}
