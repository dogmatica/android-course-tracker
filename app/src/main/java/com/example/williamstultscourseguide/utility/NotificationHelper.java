package com.example.williamstultscourseguide.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.core.app.NotificationCompat;

import com.example.williamstultscourseguide.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "Channel 1";
    private NotificationManager nManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager() {
        if (nManager == null) {
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return nManager;

    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID).setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_notification);
    }
}
