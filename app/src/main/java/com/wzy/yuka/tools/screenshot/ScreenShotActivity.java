package com.wzy.yuka.tools.screenshot;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ScreenShotActivity extends Activity {
    private static final String TAG = "ScreenShotActivity";
    public static final int REQUEST_MEDIA_PROJECTION = 0x2893;
    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0f);
        requestScreenShot();
    }

    //unknown wrong
    @SuppressLint("WrongConstant")
    public void requestScreenShot() {
        Log.d(TAG, "requestScreenShot");
        mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e(TAG, "User cancel");
        } else {
            try {
                WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
            } catch (Exception e) {
                Log.e(TAG, "MediaProjection error");
            }
            Intent service = new Intent(this, ScreenShotService.class);
            //于此初始化screenshot对象
            Screenshot screenshot = new Screenshot(data);
            service.putExtra("screenshot", screenshot);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(service);
            } else {
                startForegroundService(service);
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            finish();
        }, 900);
    }

}
