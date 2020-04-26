package com.wzy.yuka.tools.floatwindow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.wzy.yuka.R;
import com.wzy.yuka.tools.screenshot.ScreenShotService;

import org.jetbrains.annotations.NotNull;

public class FloatWindow {
    static final String TAG = "FloatWindow";
    //悬浮窗坐标及索引。应当注意的是static线程不安全，应当减少类外调用
    public static int[][] location;
    private static String[] tags;
    public static int NumOfFloatWindows = 0;

    //初始化控制面板
    public static void initFloatWindow(Activity activity) {
        Intent service = new Intent(activity, ScreenShotService.class);
        NumOfFloatWindows += 1;
        EasyFloat.with(activity).setTag("startBtn")
                .setLayout(R.layout.start, view -> {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                    view.findViewById(R.id.button1).setOnClickListener(v -> {
                        if ((((Button) v).getText()).equals("关闭")) {
                            ((Button) v).setText("控制");
                        } else {
                            ((Button) v).setText("关闭");
                        }
                        EasyFloat.dismissAppFloat("control");
                        EasyFloat.with(activity)
                                .setTag("control")
                                .setLayout(R.layout.control_floatwindow, v1 -> {
                                    v1.findViewById(R.id.button3).setOnClickListener(v2 -> {
                                        if ((((Button) v2).getText().equals("启用"))) {
                                            if (pref.getBoolean("settings_window_multiple", false)) {
                                                location = new int[pref.getInt("settings_window_number", 1)][4];
                                                tags = new String[pref.getInt("settings_window_number", 1)];
                                                if (NumOfFloatWindows == 1) {
                                                    NumOfFloatWindows += pref.getInt("settings_window_number", 1);
                                                }
                                                multiFloatWindow(activity, pref.getInt("settings_window_number", 1));
                                            } else {
                                                location = new int[1][4];
                                                tags = new String[1];
                                                if (NumOfFloatWindows == 1) {
                                                    NumOfFloatWindows += 1;
                                                }
                                                multiFloatWindow(activity, 1);
                                            }
                                            showAllFloatWindow(false);
                                            ((Button) v2).setText("隐藏");
                                        } else if ((((Button) v2).getText().equals("隐藏"))) {
                                            hideAllFloatWindow();
                                            ((Button) v2).setText("启用");
                                        }
                                    });
                                    v1.findViewById(R.id.button4).setOnClickListener(v2 -> {
                                        activity.stopService(service);
                                        activity.finishAffinity();
                                        System.exit(0);
                                    });
                                    v1.findViewById(R.id.button7).setOnClickListener(v2 -> {
                                        dismissAllFloatWindow(true);
                                        activity.stopService(service);
                                        ((Button) v1.findViewById(R.id.button3)).setText("启用");
                                        v1.findViewById(R.id.button3).performClick();
                                    });
                                    v1.findViewById(R.id.button8).setOnClickListener(v2 -> {
                                        EasyFloat.with(activity)
                                                .setTag("settings_panel")
                                                .setLayout(R.layout.settings_floatwindow, v3 -> {
                                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    v3.findViewById(R.id.settings_hide).setOnClickListener(v4 -> {
                                                        EasyFloat.dismissAppFloat("settings_panel");
                                                    });
                                                    v3.findViewById(R.id.settings_trans_translator).setOnClickListener(v4 -> {
                                                        if ((((Button) v4).getText()).equals("关闭")) {
                                                            ((Button) v4).setText("进阶设置");
                                                        } else {
                                                            ((Button) v4).setText("关闭");
                                                        }
                                                        EasyFloat.dismissAppFloat("settings_more");
                                                        EasyFloat.with(activity)
                                                                .setTag("settings_more")
                                                                .setLayout(R.layout.empty, v5 -> {
                                                                    LinearLayout linearLayout = v5.findViewById(R.id.empty_linearLayout);
                                                                    String[] translator_name = activity.getResources().getStringArray(R.array.translator_name);
                                                                    String[] translator = activity.getResources().getStringArray(R.array.translator);
                                                                    Button[] button = new Button[translator_name.length];
                                                                    for (int i = 0; i < translator_name.length; i++) {
                                                                        button[i] = new Button(activity);
                                                                        button[i].setText(translator_name[i]);
                                                                        int finalI = i;
                                                                        button[i].setOnClickListener(vx -> {
                                                                            editor.putString("settings_trans_translator", translator[finalI]);
                                                                            editor.commit();
                                                                            EasyFloat.dismissAppFloat("settings_more");
                                                                        });
                                                                        linearLayout.addView(button[i]);
                                                                    }

                                                                })
                                                                .setLocation(700, 0)
                                                                .setDragEnable(false)
                                                                .setShowPattern(ShowPattern.ALL_TIME)
                                                                .show();
                                                    });
                                                    v3.findViewById(R.id.settings_advance).setOnClickListener(v4 -> {
                                                        if ((((Button) v4).getText()).equals("关闭")) {
                                                            ((Button) v4).setText("进阶设置");
                                                        } else {
                                                            ((Button) v4).setText("关闭");
                                                        }
                                                        EasyFloat.dismissAppFloat("settings_more");
                                                        EasyFloat.with(activity)
                                                                .setTag("settings_more")
                                                                .setLayout(R.layout.empty, v5 -> {
                                                                    LinearLayout linearLayout = v5.findViewById(R.id.empty_linearLayout);
                                                                    Switch switch_fastMode = new Switch(activity);
                                                                    switch_fastMode.setText("快速模式：");
                                                                    switch_fastMode.setChecked(preferences.getBoolean("settings_fastMode", false));
                                                                    switch_fastMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                                        if (isChecked) {
                                                                            editor.putBoolean("settings_fastMode", true);
                                                                            editor.commit();
                                                                        } else {
                                                                            editor.putBoolean("settings_fastMode", false);
                                                                            editor.commit();
                                                                        }
                                                                    });
                                                                    linearLayout.addView(switch_fastMode);
                                                                    Switch switch_continuousMode = new Switch(activity);
                                                                    switch_continuousMode.setText("半自动模式：");
                                                                    switch_continuousMode.setChecked(preferences.getBoolean("settings_continuousMode", false));
                                                                    switch_continuousMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                                        if (isChecked) {
                                                                            EasyFloat.getAppFloatView("startBtn")
                                                                                    .findViewById(R.id.button6).setVisibility(View.VISIBLE);
                                                                            editor.putBoolean("settings_continuousMode", true);
                                                                            editor.commit();
                                                                        } else {
                                                                            EasyFloat.getAppFloatView("startBtn")
                                                                                    .findViewById(R.id.button6).setVisibility(View.GONE);
                                                                            editor.putBoolean("settings_continuousMode", false);
                                                                            editor.commit();
                                                                        }
                                                                    });
                                                                    linearLayout.addView(switch_continuousMode);
                                                                })
                                                                .setLocation(700, 0)
                                                                .setDragEnable(false)
                                                                .setShowPattern(ShowPattern.ALL_TIME)
                                                                .show();
                                                    });
                                                })
                                                .setShowPattern(ShowPattern.ALL_TIME)
                                                .setLocation(300, 0)
                                                .setDragEnable(false)
                                                .show();

                                    });
                                })
                                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                                .setShowPattern(ShowPattern.ALL_TIME)
                                .setLocation(0, 500)
                                .setDragEnable(true)
                                .show();
                    });
                    view.findViewById(R.id.button5).setOnClickListener(v -> {
                        if (NumOfFloatWindows > 1) {
                            hideAllFloatWindow();
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                                activity.startService(service);
                            } else {
                                activity.startForegroundService(service);
                            }
                        } else {
                            Toast.makeText(activity, "还没有悬浮窗初始化呢，请从控制中启用悬浮窗", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (pref.getBoolean("settings_continuousMode", false)) {
                        Button button6 = view.findViewById(R.id.button6);
                        button6.setVisibility(View.VISIBLE);
                        button6.setOnClickListener((v2) -> {
                            try {
                                activity.stopService(service);
                            } catch (Exception e) {
                            }
                        });
                    }
                })
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setLocation(100, 100).show();
    }

    //复数选词悬浮窗初始化，根据参数多次调用singleFloatWindow实现
    private static void multiFloatWindow(Activity activity, int num) {
        for (int i = 0; i < num; i++) {
            tags[i] = "selectWindow" + i;
            singleFloatWindow(activity, i);
        }
    }

    //单选词悬浮窗初始化
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
                        //locationA[0]左上角对左边框，locationA[1]左上角对上边框
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
                        //locationA[0]左上角对左边框，locationA[1]左上角对上边框
                        getLocation(view, index);
                    }

                    @Override
                    public void hide(@NotNull View view) {
                        //locationA[0]左上角对左边框，locationA[1]左上角对上边框
                        getLocation(view, index);
                    }

                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void touchEvent(@NotNull View view, @NotNull MotionEvent motionEvent) {
                        //locationA[0]左上角对左边框，locationA[1]左上角对上边框
                        getLocation(view, index);
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

    //获取并处理所有选词悬浮窗的坐标位置以便截取所选位置图像
    private static void getLocation(View view, int index) {
        view.getLocationOnScreen(location[index]);
        location[index][2] = location[index][0] + view.getRight();
        location[index][3] = location[index][1] + view.getBottom();
    }

    //获取并处理所有选词悬浮窗内的TextView控件以分别展示翻译内容
    public static TextView[] getAllTextViews() {
        TextView[] textViews = new TextView[tags.length];
        for (int i = 0; i < tags.length; i++) {
            textViews[i] = EasyFloat.getAppFloatView(tags[i]).findViewById(R.id.translatedText);
        }
        return textViews;
    }

    //隐藏以截图，抑或是用户希望隐藏时由选词悬浮窗调用
    public static void hideAllFloatWindow() {
        if (NumOfFloatWindows > 1) {
            for (String tag : tags) {
                EasyFloat.hideAppFloat(tag);
            }
        }
    }

    //截图完成后显示悬浮窗，抑或是用户希望显示隐藏的选词窗时调用
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

    //删除所有选词窗，使用于重置时
    public static void dismissAllFloatWindow(boolean except) {
        if (NumOfFloatWindows > 1) {
            for (String tag : tags) {
                EasyFloat.dismissAppFloat(tag);
                NumOfFloatWindows -= 1;
            }
            if (!except) {
                EasyFloat.dismissAppFloat("startBtn");
                NumOfFloatWindows = 0;
            }
        } else if (NumOfFloatWindows == 1) {
            if (!except) {
                EasyFloat.dismissAppFloat("startBtn");
                NumOfFloatWindows = 0;
            }
        }
    }

}


