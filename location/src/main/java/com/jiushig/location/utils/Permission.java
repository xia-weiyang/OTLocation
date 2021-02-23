package com.jiushig.location.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.jiushig.location.ui.BaseActivity;

/**
 * Created by zk on 2017/6/16.
 * 权限请求管理
 */

public class Permission {

    public static final int REQUEST_EXTERNAL_LOCATION = 102;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

    /**
     * 权限申请回调
     */
    public interface Callback {
        void done(boolean isSuccess);
    }


    /**
     * 定位权限
     *
     * @param activity
     * @param callback
     */
    public static void location(BaseActivity activity, Callback callback) {
        request(activity, callback, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_LOCATION, REQUEST_EXTERNAL_LOCATION);
    }

    /**
     * 定位权限
     *
     * @param activity
     */
    public static boolean location(Activity activity) {
       return request(activity, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_LOCATION, REQUEST_EXTERNAL_LOCATION);
    }


    /**
     * 权限申请
     *
     * @param activity
     * @param permission
     * @param requestPermission
     * @param requestCode
     * @return
     */
    private static boolean request(Activity activity, String permission, String[] requestPermission, int requestCode) {
        if (!(ActivityCompat.checkSelfPermission(activity,
                permission) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, requestPermission,
                    requestCode);
            return false;
        }
        return true;
    }


    /**
     * 权限申请  回调
     *
     * @param activity
     * @param callback
     * @param permission
     * @param requestPermission
     * @param myRequestCode
     */
    private static void request(BaseActivity activity, Callback callback, String permission, String[] requestPermission, int myRequestCode) {
        if (request(activity, permission, requestPermission, myRequestCode)) {
            if (callback != null) {
                callback.done(true);
            }
        } else {
            activity.setPermissionCallbackListener(((requestCode, permissions, grantResults) -> {
                if (requestCode == myRequestCode) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (callback != null) {
                            callback.done(true);
                        }
                    } else {
                        if (callback != null) {
                            callback.done(false);
                        }
                        Toast.makeText(activity, "当前权限已被禁止，可前往应用设置中手动开启。", Toast.LENGTH_LONG).show();
                    }
                }
            }));
        }
    }
}
