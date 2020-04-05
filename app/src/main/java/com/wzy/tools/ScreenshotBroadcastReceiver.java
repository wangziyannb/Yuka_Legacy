package com.wzy.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenshotBroadcastReceiver extends BroadcastReceiver {
    private Message message;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadcastReceiver", "获取到了广播");
        message.isSuccess((Screenshot) intent.getParcelableExtra("screenshot"));
    }

    public interface Message {
        void isSuccess(Screenshot screenshot);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
