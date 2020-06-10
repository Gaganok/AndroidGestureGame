package app.gestureproject.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import app.gestureproject.R;
import app.gestureproject.view.MainActivity;

public class BackgroundService extends Service {

    private final int THREAD_SLEEP_TIME = 1000;
    public static boolean running = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                createNotificationChannel();
                for(int i = 0; i < 1; i++) {
                    try {
                        Thread.sleep(THREAD_SLEEP_TIME);
                        throwNotification();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return Service.START_NOT_STICKY;
    }

    private void throwNotification(){
        final int NOTIFICATION_ID = 1234321;

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "id1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notification")
                .setContentText("You haven't played the game for a long time!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You haven't played the game for a long time!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "testChannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("id1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void setRunning(boolean b){
        running = b;
    }
}
