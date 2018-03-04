package com.jiushig.location.location;

import android.content.Context;

import com.jiushig.location.entity.Location;
import com.jiushig.location.utils.Helper;

/**
 * Created by zk on 2018/2/25.
 */

public abstract class LocationBase {

    protected Context context;

    private CallbackFail callbackFail;
    private CallbackSuccess callbackSuccess;

    /**
     * 最大定位错误次数
     */
    private int maxErrorNum = 5;
    private int currentNum = 0;

    public LocationBase(Context context) {
        this.context = context;
    }

    public interface CallbackSuccess {
        void done(Location location);
    }

    public interface CallbackFail {
        void done(String msg);
    }

    public LocationBase setCallbackFail(CallbackFail callbackFail) {
        this.callbackFail = callbackFail;
        return this;
    }

    public LocationBase setCallbackSuccess(CallbackSuccess callbackSuccess) {
        this.callbackSuccess = callbackSuccess;
        return this;
    }

    public LocationBase setMaxErrorNum(int maxErrorNum) {
        this.maxErrorNum = maxErrorNum;
        return this;
    }

    /**
     * 定位失败
     *
     * @param msg
     */
    protected void locationFail(String msg) {
        currentNum++;
        if (currentNum > maxErrorNum) {
            stop();
            if (callbackFail != null) {
                callbackFail.done(msg);
            }
        }
    }

    /**
     * 定位成功
     *
     * @param location
     */
    protected void locationSuccess(Location location) {
        if (location == null
                || location.longitude == 0
                || location.latitude == 0
                || (Helper.isEmptyString(location.details)
                && Helper.isEmptyString(location.city)
                && Helper.isEmptyString(location.country))) {
            locationFail("定位失败");
            return;
        }
        stop();
        if (callbackSuccess != null) {
            callbackSuccess.done(location);
        }
    }

    /**
     * 停止定位
     */
    public abstract void stop();

    /**
     * 开始定位
     */
    public abstract void start();
}
