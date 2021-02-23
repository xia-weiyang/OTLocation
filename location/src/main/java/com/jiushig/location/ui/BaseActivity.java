package com.jiushig.location.ui;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }
}
