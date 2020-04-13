package com.wzy.yuka.tools.screenshot;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class Screenshot implements Parcelable {
    private Intent data;
    private String[] fileName;

    Screenshot(Intent data) {
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

    void getScreenshot(Context context, int[][] location, boolean isGrayscale, Shotter.OnShotListener onShotListener) {
        fileName = new String[location.length];
        for (int i = 0; i < location.length; i++) {
            fileName[i] = context.getExternalFilesDir("screenshot").getAbsoluteFile()
                    + "/" + "LU" + location[i][0] + "_" + location[i][1] +
                    " " + "RU" + location[i][2] + "_" + location[i][3] + ".jpg";
        }
        Shotter shotter = new Shotter(context, -1, data);
        shotter.startScreenShot(onShotListener, fileName, location, isGrayscale, true);
    }

    String[] getFileNames() {
        return fileName;
    }

    public void cleanImage() {
        for (String str : fileName) {
            File image = new File(str);
            image.delete();
        }
    }

}

