package com.mahesh.vnotifications.beta;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mahesh.vnotifications.beta.utils.Config;
import com.mahesh.vnotifications.beta.utils.DBAdapter;
import com.mahesh.vnotifications.beta.utils.GcmBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Mahesh on 3/8/14.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                //for (int i=0; i<5; i++) {
                //   Log.i(Config.TAG, "Working... " + (i + 1)
                //          + "/5 @ " + SystemClock.elapsedRealtime());
                // }
                //Log.i(Config.TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                addToDemoDataBase(extras.getString("title"),extras.getString("message"),extras.getString("level"),extras.getString("tag"),extras.getString("postedby"));
                sendNotification("New Message " + extras.getString("title"));
                Log.i(Config.TAG, "Received: " + extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void addToDemoDataBase(String Title, String Message, String Level,String Tag, String PostedBy) {
        DBAdapter da = new DBAdapter(this);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        da.open();
        Message=Message.replace("\n","<br>");
        for (int i = 1; i <= 10; i++) {
            try {
                da.insertRow(i, Title, Message,Tag, mydate, Level, PostedBy);
                break;
            } catch (SQLiteConstraintException e) {

            }
        }
        da.close();
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginMainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("New Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}