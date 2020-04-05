package com.wzy.yuka;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wzy.tools.Screenshot;

public class ScreenShotService extends Service {
    private static final String TAG = ScreenShotActivity.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Screenshot screenshot = (Screenshot) intent.getParcelableExtra("screenshot");
        Log.d(TAG,"暂未发送广播");
        screenshot.getScreenshot(this, true, () -> {
            Intent intent1 = new Intent("com.example.myapplication.MY_BROADCAST");
            intent1.putExtra("screenshot", screenshot);
            sendBroadcast(intent1);
            Log.d("sendBroadcast", "已经发送广播");
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String id = "channel_01";
        CharSequence name = "截屏";
        String description = "正在截屏";
        NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
        notificationChannel.setDescription(description);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);
        manager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this, id)
                .setContentTitle(name).setContentText(description).setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher_background))
                .setAutoCancel(true).build();
        startForeground(110, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}