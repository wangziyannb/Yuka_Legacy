package com.wzy.yuka.tools.params;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.wzy.yuka.R;

public class GetParams {

    public static String[] getParamsForReq(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //0:mode 1:model 2:translator 3:SBCS
        String[] params = new String[4];
        Resources resources = context.getResources();
        //默认为 ocr - google -google - 0
        params[0] = resources.getStringArray(R.array.mode)[0];
        params[1] = resources.getStringArray(R.array.model)[0];
        params[2] = "google";
        params[3] = resources.getString(R.string.False);

        if (preferences.getBoolean("settings_trans_switch", false)) {
            //启用翻译
            //translate-
            params[0] = resources.getStringArray(R.array.mode)[1];
            if (preferences.getBoolean("settings_trans_select", false)) {
                //启用百度翻译
                //translate - google - baidu -0
                params[2] = "baidu";
                if (preferences.getBoolean("settings_trans_SBCS", false)) {
                    //日韩文字启用全角
                    //translate - google - baidu -1
                    params[3] = resources.getString(R.string.True);
                }
            } else {
                //启用默认谷歌翻译
                params[0] = resources.getStringArray(R.array.mode)[1];
            }
        } else {
            //未启用翻译
            //ocr -
            if (preferences.getString("settings_detect_language", resources.getStringArray(R.array.model)[0])
                    .equals(resources.getStringArray(R.array.model)[1])) {
                //启用中文识别
                params[1] = resources.getStringArray(R.array.model)[1];
            } else if (preferences.getString("settings_detect_language", resources.getStringArray(R.array.model)[0])
                    .equals(resources.getStringArray(R.array.model)[2])) {
                //启用英文识别
                params[1] = resources.getStringArray(R.array.model)[2];
            }
        }
        for (String str : params) {
            Log.d("Screenshot", str);
        }
        return params;
    }

}
