package com.wzy.yuka.tools.screenshot;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Screenshot implements Parcelable  {
    String filename;
    int[] locationA;
    int[] locationB;
    Intent data;


    protected Screenshot(Parcel in) {
        filename = in.readString();
        locationA = in.createIntArray();
        locationB = in.createIntArray();
        data = in.readParcelable(Intent.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeIntArray(locationA);
        dest.writeIntArray(locationB);
        dest.writeParcelable(data, flags);
    }

    public Screenshot(int[] locationA, int[] locationB, Intent data) {
        this.data = data;
        this.locationA = locationA;
        this.locationB = locationB;
        this.filename = "LU" + locationA[0] + "_" + locationA[1] +
                " " + "RU" + locationB[0] + "_" + locationB[1] + ".jpg";
    }

    public void getScreenshot(Context context, boolean isGrayscale, Shotter.OnShotListener onShotListener) {
        Shotter shotter = new Shotter(context, -1, data);
        shotter.startScreenShot(onShotListener, context.getExternalFilesDir("screenshot").getAbsoluteFile()
                + "/" + filename, locationA, locationB, isGrayscale);
    }

    public void UploadImage(Context context, String mode, String SBCS, okhttp3.Callback callback) {
        File image = new File(context.getExternalFilesDir("screenshot").getAbsoluteFile() + "/" + filename);
        MultipartBody body = new MultipartBody.Builder("Yuka2016203023^")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\";filename=\"1.jpg\""),
                        RequestBody.create(MediaType.parse("image/jpeg"), image)
                )
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"mode\""),
                        RequestBody.create(null, mode)
                )
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"SBCS\""),
                        RequestBody.create(null, SBCS)
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
        call.enqueue(callback);
    }

}

