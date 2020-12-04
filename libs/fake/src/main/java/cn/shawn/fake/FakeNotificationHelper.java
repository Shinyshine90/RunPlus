package cn.shawn.fake;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class FakeNotificationHelper {

    private static final int ONGOING_NOTIFICATION_ID = 0x1024;

    public void startForeground(Service context) {
        Intent notificationIntent = new Intent("cn.shawn.runplus.MainActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, 0);
        String CHANNEL_ID = "fake_gps_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getResources().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(context.getResources().getText(R.string.notification_title))
                        .setContentText(context.getResources().getText(R.string.notification_message))
                        .setSmallIcon(R.drawable.ic_pin)
                        .setContentIntent(pendingIntent)
                        .setTicker(context.getResources().getText(R.string.ticker_text))
                        .build();
        context.startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

}
