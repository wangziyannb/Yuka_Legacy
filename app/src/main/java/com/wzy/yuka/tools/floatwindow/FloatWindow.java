package com.wzy.yuka.tools.floatwindow;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.wzy.yuka.R;
import com.wzy.yuka.tools.screenshot.ScreenShotActivity;

import org.jetbrains.annotations.NotNull;

public class FloatWindow {
    static final String TAG = "FloatWindow";
    Activity mActivity;
    int[] locationA = new int[2];
    int[] locationB = new int[2];

    public FloatWindow(Activity activity) {
        this.mActivity = activity;
    }

    public void initFloatWindow() {
        EasyFloat.with(mActivity)
                .setTag("startBtn")
                .setLayout(R.layout.start, view -> {
                    view.findViewById(R.id.button3).setOnClickListener(v -> {
                        EasyFloat.with(mActivity)
                                .setTag("selectWindow")
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
                                        EasyFloat.hideAppFloat("selectWindow");
                                    });
                                    view1.findViewById(R.id.sw_ok).setOnClickListener(v1 -> {
                                        EasyFloat.hideAppFloat("selectWindow");
                                        Intent intent = new Intent(mActivity, ScreenShotActivity.class);
                                        intent.putExtra("locationA", locationA);
                                        intent.putExtra("locationB", locationB);
                                        mActivity.startActivity(intent);
                                        mActivity.finish();
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
                                }).show();
                        EasyFloat.showAppFloat("selectWindow");
                    });
                })
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setLocation(100, 100).show();
    }
}
