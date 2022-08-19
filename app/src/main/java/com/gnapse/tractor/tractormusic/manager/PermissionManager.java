package com.gnapse.tractor.tractormusic.manager;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;

public class PermissionManager {
    private static final String TAG = PermissionManager.class.getSimpleName();

    private static PermissionManager sInstance;
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Activity mActivity;

    private PermissionManager(Activity activity) {
        this.mActivity = activity;
    }

    public static PermissionManager getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (PermissionManager.class) {
                if (sInstance == null) {
                    sInstance = new PermissionManager(activity);
                }
            }
        }
        return sInstance;
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.e(TAG, "Require permissions for a USB connecting");
                }

                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return;
            }
        }
    }
}
