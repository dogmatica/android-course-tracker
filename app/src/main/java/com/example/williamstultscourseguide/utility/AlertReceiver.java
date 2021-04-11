package com.example.williamstultscourseguide.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    //public static int numAlert = 0;
    String title;
    String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, message);
        notificationHelper.getManager().notify(1, nb.build());
    }

    /*public static int getNumAlert() {
        return numAlert;
    }

    public static void setNumAlert(int numAlert) {
        AlertReceiver.numAlert = numAlert;
    }*/
}
