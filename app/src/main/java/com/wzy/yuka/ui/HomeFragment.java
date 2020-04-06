package com.wzy.yuka.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.wzy.yuka.R;
import com.wzy.yuka.prepare.ScreenShotActivity;
import com.wzy.yuka.tools.ScaleImageView;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "MainActivity";
    int[] locationA = new int[2];
    int[] locationB = new int[2];

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_legacy, container, false);
        root.findViewById(R.id.startBtn).setOnClickListener(this);
        root.findViewById(R.id.closeBtn).setOnClickListener(this);
        root.findViewById(R.id.settingBtn).setOnClickListener(this);
        return root;
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
//                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void initFloat(String tag) {
        switch (tag) {
            case "startBtn":
                EasyFloat.with(getActivity())
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
                EasyFloat.with(getActivity())
                        .setTag(tag)
                        .setLayout(R.layout.selectwindow, view -> {
                            RelativeLayout rl = view.findViewById(R.id.testFloatScale);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rl.getLayoutParams();
                            ScaleImageView si = view.findViewById(R.id.sw_scale);
                            si.setOnScaledListener((x, y, event) -> {
                                params.width += (int) x;
                                params.height += (int) y;
                                rl.setLayoutParams(params);
                            });
                            view.findViewById(R.id.sw_close).setOnClickListener(v -> {
                                hideFloat(tag);
                            });
                            view.findViewById(R.id.sw_ok).setOnClickListener(v -> {
                                hideFloat("selectPanel");
                                Intent intent = new Intent(getActivity(), ScreenShotActivity.class);
                                intent.putExtra("locationA", locationA);
                                intent.putExtra("locationB", locationB);
                                startActivity(intent);
                            });
                        })
                        .setShowPattern(ShowPattern.ALL_TIME)
                        .setLocation(100, 100)
                        .setAppFloatAnimator(null)
                        .registerCallbacks(new OnFloatCallbacks() {
                            @Override
                            public void createdResult(boolean b, @org.jetbrains.annotations.Nullable String s, @org.jetbrains.annotations.Nullable View view) {
                                if (b) {
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

}
