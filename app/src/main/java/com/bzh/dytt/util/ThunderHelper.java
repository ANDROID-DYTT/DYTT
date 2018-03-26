package com.bzh.dytt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.bzh.dytt.R;

/**
 * ==========================================================<br>
 * <b>版权</b>：　　　别志华 版权所有(c)2016<br>
 * <b>作者</b>：　　  biezhihua@163.com<br>
 * <b>创建日期</b>：　16-4-8<br>
 * <b>描述</b>：　　　<br>
 * <b>版本</b>：　    V1.0<br>
 * <b>修订历史</b>：　<br>
 * ==========================================================<br>
 */
public class ThunderHelper {

    public static final String XUNLEI_PACKAGENAME = "com.xunlei.downloadprovider";
    private static ThunderHelper instance;
    private Activity baseActivity;

    private ThunderHelper(Activity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static ThunderHelper getInstance(Activity baseActivity) {
        if (instance == null) {
            instance = new ThunderHelper(baseActivity);
        }
        return instance;
    }

    public void onClickDownload(String ftpUrl) {
        if (TextUtils.isEmpty(ftpUrl)) {
            return;
        }

        if (checkIsInstall(baseActivity, XUNLEI_PACKAGENAME)) {
            // 唤醒迅雷
            baseActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getThunderEncode(ftpUrl))));
        }
    }

    private boolean checkIsInstall(Context paramContext, String paramString) {
        if ((paramString == null) || ("".equals(paramString)))
            return false;
        try {
            paramContext.getPackageManager().getApplicationInfo(paramString, 0);
            return true;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
        }
        return false;
    }

    private String getThunderEncode(String ftpUrl) {
        return "thunder://" + XunLeiBase64.base64encode(("AA" + ftpUrl + "ZZ").getBytes());
    }
}
