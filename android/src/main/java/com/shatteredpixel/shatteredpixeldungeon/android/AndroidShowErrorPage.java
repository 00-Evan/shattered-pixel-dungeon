package com.shatteredpixel.shatteredpixeldungeon.android;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class AndroidShowErrorPage extends Activity {
    public static String tag;
    public static String message;
    public static Throwable exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_err_page);

        String versionName;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "???";
        }

        int versionCode;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 0;
        }

        String installer;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                installer = getPackageManager().getInstallSourceInfo(getPackageName()).getInstallingPackageName();
            } else {
                installer = getPackageManager().getInstallerPackageName(getPackageName());
            }
        } catch (Exception e) {
            installer = "???";
        }

        TextView title = findViewById(R.id.titleTextView);

        String message = "Oops!Something went wrong!";
        message += "\n\nPackage: " + getPackageName();
        message += "\nVersion: " + versionName + " (" + versionCode + ")";
        message += "\nDevice: " + Build.MODEL;
        message += "\nInstaller: " + installer;

        title.setText(message);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pixel_font.ttf"));
        title.setPadding(10, 10, 10, 10);
        TextView errText = findViewById(R.id.errTextView);
        String errMessage =  AndroidShowErrorPage.message==null?"":AndroidShowErrorPage.message;
        errMessage += "\n\nError: " + (exception==null?"":(AndroidShowErrorPage.exception.getMessage()));
        errText.setText(errMessage);

    }
}
