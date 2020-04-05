package com.wzy.yuka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.drawable.ColorDrawable;
import android.media.projection.MediaProjectionManager;
import android.view.Window;
import android.view.WindowManager;

import com.wzy.tools.Screenshot;

public class ScreenShotActivity extends Activity {
    private static final String TAG = "ScreenShotActivity";
    public static final int REQUEST_MEDIA_PROJECTION = 0x2893;
    private MediaProjectionManager mMediaProjectionManager;
    int[] locationA = new int[2];
    int[] locationB = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        locationA = getIntent().getIntArrayExtra("locationA");
        locationB = getIntent().getIntArrayExtra("locationB");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0f);
        requestScreenShot();
    }

    public void requestScreenShot() {
        Log.d(TAG, "requestScreenShot");
        mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

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
            Screenshot screenshot = new Screenshot(locationA, locationB, data);
            service.putExtra("screenshot", screenshot);
            startForegroundService(service);
        }
    }
}
