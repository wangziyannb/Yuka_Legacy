package com.wzy.yuka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;

import com.wzy.tools.ScaleImageView;
import com.wzy.tools.ScreenshotBroadcastReceiver;
import com.wzy.tools.Screenshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ScreenshotBroadcastReceiver.Message {
    static final String TAG = "MainActivity";
    int[] locationA = new int[2];
    int[] locationB = new int[2];
    ScreenshotBroadcastReceiver SSReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        Button start = (Button) findViewById(R.id.startBtn);
        Button close = (Button) findViewById(R.id.closeBtn);
        Button setting = (Button) findViewById(R.id.settingBtn);
        start.setOnClickListener(this);
        close.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
                initFloat("startBtn");
                break;
            case R.id.closeBtn:
                dismissFloat("startBtn");
                dismissFloat("selectPanel");
                break;
            case R.id.settingBtn:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void initFloat(String tag) {
        switch (tag) {
            case "startBtn":
                EasyFloat.with(this)
                        .setLayout(R.layout.start, view -> {
                            view.findViewById(R.id.button3).setOnClickListener(v -> {
                                initFloat("selectPanel");
                            });
                        })
                        .setTag(tag)
                        .setShowPattern(ShowPattern.ALL_TIME)
                        .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                        .setLocation(100, 100)
                        .show();
                break;
            case "selectPanel":
                EasyFloat.with(this)
                        .setTag(tag)
                        .setLayout(R.layout.selectwindow, view -> {
                            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.testFloatScale);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rl.getLayoutParams();
                            ScaleImageView si = (ScaleImageView) view.findViewById(R.id.ivScale);
                            si.setOnScaledListener((x, y, event) -> {
                                params.width += (int) x;
                                params.height += (int) y;
                                rl.setLayoutParams(params);
                            });
                            view.findViewById(R.id.ivClose).setOnClickListener(v -> {
                                hideFloat(tag);
                            });
                            view.findViewById(R.id.ivButton).setOnClickListener(v -> {
                                SSReceiver = new ScreenshotBroadcastReceiver();
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction("com.example.myapplication.MY_BROADCAST");
                                getApplicationContext().registerReceiver(SSReceiver, intentFilter);
                                SSReceiver.setMessage(this);
                                hideFloat("selectPanel");
                                Intent intent = new Intent(this, ScreenShotActivity.class);
                                intent.putExtra("locationA", locationA);
                                intent.putExtra("locationB", locationB);
                                startActivity(intent);
                                finish();
                            });
                        })
                        .setShowPattern(ShowPattern.ALL_TIME)
                        .setLocation(100, 100)
                        .setAppFloatAnimator(null)
                        .registerCallbacks(new OnFloatCallbacks() {
                            @Override
                            public void createdResult(boolean b, @org.jetbrains.annotations.Nullable String s, @org.jetbrains.annotations.Nullable View view) {
                                if(b){
                                    view.getLocationOnScreen(locationA);
                                    locationB[0] = locationA[0] + view.getRight();
                                    locationB[1] = locationA[1] + view.getBottom();
                                }
                            }

                            @Override
                            public void show(@NotNull View view) {

                            }

                            @Override
                            public void hide(@NotNull View view) {

                            }

                            @Override
                            public void dismiss() {

                            }

                            @Override
                            public void touchEvent(@NotNull View view, @NotNull MotionEvent motionEvent) {

                            }

                            @Override
                            public void drag(@NotNull View view, @NotNull MotionEvent motionEvent) {

                            }

                            @Override
                            public void dragEnd(@NotNull View view) {
                                //locationA[0]左上角对左边框，locationA[1]左上角对上边框
                                view.getLocationOnScreen(locationA);
                                locationB[0] = locationA[0] + view.getRight();
                                locationB[1] = locationA[1] + view.getBottom();
                            }
                        })
                        .show();
                showFloat("selectPanel");
                break;
            default:
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            TextView textView = EasyFloat.getAppFloatView("selectPanel").findViewById(R.id.translatedText);
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
                    int timeResult = (int) resultJson.getDouble("time");
                    Toast.makeText(getApplicationContext(), "于" + timeResult + "秒成功", Toast.LENGTH_SHORT).show();
                    textView.setText(ocrResult);
                    textView.setTextColor(Color.WHITE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static void showFloat(String a) {
        EasyFloat.showAppFloat(a);
        Log.d(TAG, "显示浮窗");
    }

    public static void hideFloat(String a) {
        EasyFloat.hideAppFloat(a);
        Log.d(TAG, "隐藏浮窗");
    }

    public static void dismissFloat(String a) {
        EasyFloat.dismissAppFloat(a);
        Log.d(TAG, "关闭浮窗");
    }

    @Override
    public void isSuccess(Screenshot screenshot) {
        getApplicationContext().unregisterReceiver(SSReceiver);
        //收到了截图成功的广播,开始裁剪图片并发送
        showFloat("selectPanel");
        TextView textView = EasyFloat.getAppFloatView("selectPanel").findViewById(R.id.translatedText);
        textView.setText("目标图片已发送，请等待...");
        textView.setTextColor(Color.WHITE);
        textView.setVisibility(View.VISIBLE);
        EasyFloat.getAppFloatView("selectPanel").findViewById(R.id.ivButton).setVisibility(View.VISIBLE);
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
        String a = null;
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
                Log.d("ScreenshotUtils", "response get");
                Bundle bundle = new Bundle();
                bundle.putString("response", response.body().string());
                Message message = Message.obtain();
                message.what = 1;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}