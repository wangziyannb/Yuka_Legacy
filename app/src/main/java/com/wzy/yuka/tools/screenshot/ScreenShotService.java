package com.wzy.yuka.tools.screenshot;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.wzy.yuka.R;
import com.wzy.yuka.tools.floatwindow.FloatWindow;
import com.wzy.yuka.tools.handler.GlobalHandler;
import com.wzy.yuka.tools.network.HttpRequest;
import com.wzy.yuka.tools.params.GetParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScreenShotService extends Service implements GlobalHandler.HandleMsgListener {
    private static final String TAG = ScreenShotActivity.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private GlobalHandler globalHandler;

    @Override
    public void handleMsg(Message msg) {
        TextView[] textViews = FloatWindow.getAllTextViews();
        Bundle bundle;
        String error;
        String response;
        switch (msg.what) {
            case 0:
                bundle = msg.getData();
                error = bundle.getString("error");
                textViews[0].setText(error);
                textViews[0].setTextColor(getResources().getColor(R.color.colorError));
                break;
            case 1:
                bundle = msg.getData();
                error = bundle.getString("error");
                textViews[1].setText(error);
                textViews[1].setTextColor(getResources().getColor(R.color.colorError));
                break;
            case 2:
                bundle = msg.getData();
                error = bundle.getString("error");
                textViews[2].setText(error);
                textViews[2].setTextColor(getResources().getColor(R.color.colorError));
                break;
            case 3:
                bundle = msg.getData();
                error = bundle.getString("error");
                textViews[3].setText(error);
                textViews[3].setTextColor(getResources().getColor(R.color.colorError));
                break;
            case 4:
                bundle = msg.getData();
                response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[0].setText(ocrResult);
                    textViews[0].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                bundle = msg.getData();
                response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[1].setText(ocrResult);
                    textViews[1].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                bundle = msg.getData();
                response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[2].setText(ocrResult);
                    textViews[2].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                bundle = msg.getData();
                response = bundle.getString("response");
                Log.d(TAG, response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    String ocrResult = resultJson.getString("results");
                    textViews[3].setText(ocrResult);
                    textViews[3].setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

//    @SuppressLint("HandlerLeak")
//    final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            TextView[] textViews = FloatWindow.getAllTextViews();
//            super.handleMessage(msg);
//            if (msg.what == 0) {
//                Bundle bundle = msg.getData();
//                String error = bundle.getString("error");
//                textViews[0].setText(error);
//                textViews[0].setTextColor(getResources().getColor(R.color.colorError));
//            }
//            if (msg.what == 1) {
//                Bundle bundle = msg.getData();
//                String error = bundle.getString("error");
//                textViews[1].setText(error);
//                textViews[1].setTextColor(getResources().getColor(R.color.colorError));
//            }
//            if (msg.what == 2) {
//                Bundle bundle = msg.getData();
//                String error = bundle.getString("error");
//                textViews[2].setText(error);
//                textViews[2].setTextColor(getResources().getColor(R.color.colorError));
//            }
//            if (msg.what == 3) {
//                Bundle bundle = msg.getData();
//                String error = bundle.getString("error");
//                textViews[3].setText(error);
//                textViews[3].setTextColor(getResources().getColor(R.color.colorError));
//            }
//            if (msg.what == 4) {
//                Bundle bundle = msg.getData();
//                String response = bundle.getString("response");
//                Log.d(TAG, response);
//                try {
//                    JSONObject resultJson = new JSONObject(response);
//                    String ocrResult = resultJson.getString("results");
//                    textViews[0].setText(ocrResult);
//                    textViews[0].setTextColor(Color.WHITE);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (msg.what == 5) {
//                Bundle bundle = msg.getData();
//                String response = bundle.getString("response");
//                Log.d(TAG, response);
//                try {
//                    JSONObject resultJson = new JSONObject(response);
//                    String ocrResult = resultJson.getString("results");
//                    textViews[1].setText(ocrResult);
//                    textViews[1].setTextColor(Color.WHITE);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (msg.what == 6) {
//                Bundle bundle = msg.getData();
//                String response = bundle.getString("response");
//                Log.d(TAG, response);
//                try {
//                    JSONObject resultJson = new JSONObject(response);
//                    String ocrResult = resultJson.getString("results");
//                    textViews[2].setText(ocrResult);
//                    textViews[2].setTextColor(Color.WHITE);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (msg.what == 7) {
//                Bundle bundle = msg.getData();
//                String response = bundle.getString("response");
//                Log.d(TAG, response);
//                try {
//                    JSONObject resultJson = new JSONObject(response);
//                    String ocrResult = resultJson.getString("results");
//                    textViews[3].setText(ocrResult);
//                    textViews[3].setTextColor(Color.WHITE);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        globalHandler = GlobalHandler.getInstance();
        globalHandler.setHandleMsgListener(this);
        final Screenshot screenshot = intent.getParcelableExtra("screenshot");
        screenshot.getScreenshot(this, FloatWindow.location, true, () -> {
            FloatWindow.showAllFloatWindow(true);
            Callback[] callbacks = new Callback[FloatWindow.NumOfFloatWindows - 1];
            for (int i = 0; i < (FloatWindow.NumOfFloatWindows - 1); i++) {
                int a = i;
                callbacks[i] = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("ScreenshotUtils", "Failure in" + a);
                        Bundle bundle = new Bundle();
                        bundle.putString("error", e.toString());
                        Message message = Message.obtain();
                        message.what = a;
                        message.setData(bundle);
                        globalHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response.body().string());
                        Message message = Message.obtain();
                        message.what = a + 4;
                        message.setData(bundle);
                        globalHandler.sendMessage(message);
                    }
                };
            }
            HttpRequest.requestTowardsYukaServer(GetParams.getParamsForReq(this), screenshot.getFileNames(), callbacks);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (!sharedPreferences.getBoolean("settings_debug_savePic", true)) {
                globalHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenshot.cleanImage();
                    }
                }, 6000);
            }
        });
        //screenshot该如何回收呢？ service结束后自己释放吧
        return super.onStartCommand(intent, flags, startId);
    }

    // TODO: 2020/4/12 通知去不掉了（可能是系统要求？） 
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalHandler.removeCallbacks(null);
    }
}