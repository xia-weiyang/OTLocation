package com.jiushig.location.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Guowang on 2016/12/17.
 * 所有Activity都应该继承此类
 */

public class BaseActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();

    private PermissionCallback permissionCallback;


    public interface PermissionCallback {
        void done(int requestCode, String[] permissions, int[] grantResults);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionCallback != null) {
            permissionCallback.done(requestCode, permissions, grantResults);
        }
    }

    public void setPermissionCallbackListener(PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
    }
}
