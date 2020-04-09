package com.wzy.yuka.tools.screenshot;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.lzf.easyfloat.EasyFloat;
import com.wzy.yuka.R;

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
            TextView textView = EasyFloat.getAppFloatView("selectWindow").findViewById(R.id.translatedText);
            textView.setText("");
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bundle bundle = msg.getData();
                String error = bundle.getString("error");
                textView.setText(error);
                textView.setTextColor(Color.WHITE);
            }
            if (msg.what == 1) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textView.setText(ocrResult);
                    textView.setTextColor(Color.WHITE);
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
        screenshot.getScreenshot(this, true, () -> {
            EasyFloat.showAppFloat("selectWindow");
            TextView textView = EasyFloat.getAppFloatView("selectWindow").findViewById(R.id.translatedText);
            textView.setText("目标图片已发送，请等待...");
            textView.setTextColor(Color.WHITE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String mode = "ocr";
            String SBCS = "0";
            if (pref.getBoolean("trans_switch", false)) {
                //启用翻译
                if (pref.getBoolean("trans_select", false)) {
                    //启用百度翻译
                    mode = "baidu";
                    if (pref.getBoolean("trans_SBCS", false)) {
                        SBCS = "1";
                    }
                } else {
                    mode = "google";
                }
            }
            screenshot.UploadImage(this, mode, SBCS, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("ScreenshotUtils", "Failure");
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.toString());
                    Message message = Message.obtain();
                    message.what = 0;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseStr = response.body().string();
                    Log.d("ScreenshotUtils", responseStr);
                    Bundle bundle = new Bundle();
                    bundle.putString("response", responseStr);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
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