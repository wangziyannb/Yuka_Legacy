package com.wzy.yuka.tools.screenshot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.wzy.yuka.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Screenshot implements Parcelable {
    private Intent data;
    private String[] fileName = new String[4];

    public Screenshot(Intent data) {
        this.data = data;
    }

    protected Screenshot(Parcel in) {
        data = in.readParcelable(Intent.class.getClassLoader());
        fileName = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeStringArray(fileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Screenshot> CREATOR = new Creator<Screenshot>() {
        @Override
        public Screenshot createFromParcel(Parcel in) {
            return new Screenshot(in);
        }

        @Override
        public Screenshot[] newArray(int size) {
            return new Screenshot[size];
        }
    };

    public void getScreenshot(Context context, int[][] location, boolean isGrayscale, Shotter.OnShotListener onShotListener) {
        for (int i = 0; i < location.length; i++) {
            fileName[i] = context.getExternalFilesDir("screenshot").getAbsoluteFile()
                    + "/" + "LU" + location[i][0] + "_" + location[i][1] +
                    " " + "RU" + location[i][2] + "_" + location[i][3] + ".jpg";
        }
        Shotter shotter = new Shotter(context, -1, data);
        shotter.startScreenShot(onShotListener, fileName, location, isGrayscale, true);
    }

    public void UploadImage(Context context, okhttp3.Callback[] callbacks) {
        String[] params = checkReferences(context);
        for (int i = 0; i < callbacks.length; i++) {
            File image = new File(fileName[i]);
            MultipartBody body = new MultipartBody.Builder("Yuka2016203023^")
                    .setType(MultipartBody.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"image\";filename=\"1.jpg\""),
                            RequestBody.create(MediaType.parse("image/jpeg"), image)
                    )
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"mode\""),
                            RequestBody.create(null, params[0])
                    )
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"ENG\""),
                            RequestBody.create(null, params[1])
                    )
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"SBCS\""),
                            RequestBody.create(null, params[2])
                    )
                    .build();
            Request request = new Request.Builder()
                    .removeHeader("User-Agent")
                    .addHeader("User_Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0")
                    .url("https://wangclimxnb.xyz/detect")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callbacks[i]);
        }
    }

    public String[] checkReferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        //0:mode 1:ENG 2:SBCS
        String[] params = new String[3];
        Resources resources = context.getResources();
        params[0] = resources.getStringArray(R.array.mode)[0];
        params[1] = resources.getString(R.string.False);
        params[2] = resources.getString(R.string.False);
        if (pref.getBoolean("settings_trans_switch", false)) {
            //启用翻译
            if (pref.getBoolean("settings_trans_select", false)) {
                //启用百度翻译
                params[0] = resources.getStringArray(R.array.mode)[2];
                if (pref.getBoolean("settings_trans_SBCS", false)) {
                    //日韩文字启用全角
                    params[2] = resources.getString(R.string.True);
                }
            } else {
                //启用默认谷歌翻译
                params[0] = resources.getStringArray(R.array.mode)[1];
            }
        } else {
            //未启用翻译
            if (pref.getString("settings_detect_language", resources.getStringArray(R.array.detect_languages)[0])
                    .equals(resources.getStringArray(R.array.detect_languages)[1])) {
                //启用英文识别
                params[1] = resources.getString(R.string.True);
            }
        }
        for (String str : params) {
            Log.d("Screenshot", str);
        }
        return params;
    }

}

