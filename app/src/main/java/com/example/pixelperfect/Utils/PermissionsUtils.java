package com.example.pixelperfect.Utils;

import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    public static final String[] PERMISSIONS_EXTERNAL_WRITE = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    public static boolean checkWriteStoragePermission(Activity activity) {

        if(Build.VERSION.SDK_INT>=29)return true;

        boolean z = ContextCompat.checkSelfPermission(activity.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return z;
    }
}
