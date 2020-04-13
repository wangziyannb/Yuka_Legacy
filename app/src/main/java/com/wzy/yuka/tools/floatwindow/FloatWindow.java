package com.wzy.yuka.tools.floatwindow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.wzy.yuka.R;
import com.wzy.yuka.tools.screenshot.ScreenShotActivity;

import org.jetbrains.annotations.NotNull;

public class FloatWindow {
    static final String TAG = "FloatWindow";
    //location 0 1 2 3 = lA 0 1 + lB 0 1
    public static int[][] location;
    private static String[] tags;
    public static int NumOfFloatWindows = 0;

    public static void initFloatWindow(Activity activity) {
        NumOfFloatWindows += 1;
        EasyFloat.with(activity)
                .setTag("startBtn")
                .setLayout(R.layout.start, view -> {
                    view.findViewById(R.id.button3).setOnClickListener(v -> {
                        if ((((Button) v).getText().equals("显示"))) {
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                            if (pref.getBoolean("settings_window_multiple", false)) {
                                location = new int[pref.getInt("settings_window_number", 1)][4];
                                tags = new String[pref.getInt("settings_window_number", 1)];
                                NumOfFloatWindows += pref.getInt("settings_window_number", 1);
                                multiFloatWindow(activity, pref.getInt("settings_window_number", 1));
                            } else {
                                location = new int[1][4];
                                tags = new String[1];
                                NumOfFloatWindows += 1;
                                multiFloatWindow(activity, 1);
                            }
                            showAllFloatWindow(false);
                            ((Button) v).setText("隐藏");
                        } else if ((((Button) v).getText().equals("隐藏"))) {
                            hideAllFloatWindow();
                            ((Button) v).setText("显示");
                        }
                    });
                    view.findViewById(R.id.button4).setOnClickListener(v -> {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    });
                    view.findViewById(R.id.button5).setOnClickListener(v -> {
                        if (NumOfFloatWindows > 1) {
                            hideAllFloatWindow();
                            Intent intent = new Intent(activity, ScreenShotActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "还没有悬浮窗初始化呢", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setLocation(100, 100).show();
    }

    private static void multiFloatWindow(Activity activity, int num) {
        for (int i = 0; i < num; i++) {
            singleFloatWindow(activity, i);
            tags[i] = "selectWindow" + i;
        }
    }

    private static void singleFloatWindow(Activity activity, int index) {
        EasyFloat.with(activity)
                .setTag("selectWindow" + index)
                .setLayout(R.layout.select_window, view1 -> {
                    RelativeLayout rl = view1.findViewById(R.id.testFloatScale);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rl.getLayoutParams();
                    ScaleImageView si = view1.findViewById(R.id.sw_scale);
                    si.setOnScaledListener((x, y, event) -> {
                        params.width += (int) x;
                        params.height += (int) y;
                        rl.setLayoutParams(params);
                    });
                    view1.findViewById(R.id.sw_close).setOnClickListener(v1 -> {
                        EasyFloat.hideAppFloat("selectWindow" + index);
                    });
                })
                .setShowPattern(ShowPattern.ALL_TIME)
                .setLocation(100 + index * 40, 100 + index * 40)
                .setAppFloatAnimator(null)
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean b, @org.jetbrains.annotations.Nullable String s, @org.jetbrains.annotations.Nullable View view) {
                        if (b) {
                            getLocation(view, index);
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
                        getLocation(view, index);
                    }
                }).show();
    }

    private static void getLocation(View view, int index) {
        view.getLocationOnScreen(location[index]);
        location[index][2] = location[index][0] + view.getRight();
        location[index][3] = location[index][1] + view.getBottom();
    }

    public static TextView[] getAllTextViews() {
        TextView[] textViews = new TextView[tags.length];
        for (int i = 0; i < tags.length; i++) {
            textViews[i] = EasyFloat.getAppFloatView(tags[i]).findViewById(R.id.translatedText);
        }
        return textViews;
    }

    //准备隐藏以截图
    private static void hideAllFloatWindow() {
        if (NumOfFloatWindows > 1) {
            for (String tag : tags) {
                EasyFloat.hideAppFloat(tag);
            }
        }
    }

    //获得数据后显示
    public static void showAllFloatWindow(boolean after) {
        if (NumOfFloatWindows > 1) {
            for (String tag : tags) {
                EasyFloat.showAppFloat(tag);
                if (after) {
                    TextView textView = EasyFloat.getAppFloatView(tag).findViewById(R.id.translatedText);
                    textView.setText("目标图片已发送，请等待...");
                    textView.setTextColor(Color.WHITE);
                }
            }
        }
    }

    public static void dismissAllFloatWindow(boolean changeOrientation) {
        if (NumOfFloatWindows > 1) {
            for (String tag : tags) {
                EasyFloat.dismissAppFloat(tag);
                NumOfFloatWindows -= 1;
            }
            if (!changeOrientation) {
                EasyFloat.dismissAppFloat("startBtn");
                NumOfFloatWindows = 0;
            }
        } else if (NumOfFloatWindows == 1) {
            if (!changeOrientation) {
                EasyFloat.dismissAppFloat("startBtn");
                NumOfFloatWindows = 0;
            }
        }
    }
}


