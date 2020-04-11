package com.wzy.yuka.tools.screenshot;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wzy.yuka.R;
import com.wzy.yuka.tools.floatwindow.FloatWindow;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScreenShotService extends Service {
    private static final String TAG = ScreenShotActivity.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TextView[] textViews = FloatWindow.getAllTextViews();
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bundle bundle = msg.getData();
                String error = bundle.getString("error");
                for (TextView textview : textViews) {
                    textview.setText(error);
                    textview.setTextColor(getResources().getColor(R.color.colorError));
                }
            }
            if (msg.what == 1) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[0].setText(ocrResult);
                    textViews[0].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[1].setText(ocrResult);
                    textViews[1].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 3) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[2].setText(ocrResult);
                    textViews[2].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 4) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[3].setText(ocrResult);
                    textViews[3].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Screenshot screenshot = intent.getParcelableExtra("screenshot");
        screenshot.getScreenshot(this, FloatWindow.location, true, () -> {
            FloatWindow.showAllFloatWindow(true);
            Callback[] callbacks = new Callback[FloatWindow.location.length];
            for (int i = 0; i < FloatWindow.location.length; i++) {
                int a = i;
                callbacks[i] = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("ScreenshotUtils", "Failure in" + a);
                        Bundle bundle = new Bundle();
                        bundle.putString("error", e.toString());
                        Message message = Message.obtain();
                        message.what = 0;
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response.body().string());
                        Message message = Message.obtain();
                        message.what = a + 1;
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                };
            }
            screenshot.UploadImage(this, callbacks);
        });
        return super.onStartCommand(intent, flags, startId);
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String id = "channel_01";
        CharSequence name = "Yuka";
        String description = "服务已启动";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, id)
                    .setContentTitle(name).setContentText(description).setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_foreground).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_launcher_background)).setAutoCancel(true).build();
            startForeground(110, notification);
        } else {
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}