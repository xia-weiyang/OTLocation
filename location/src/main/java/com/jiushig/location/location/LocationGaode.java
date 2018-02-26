package com.jiushig.location.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.jiushig.location.utils.Log;

/**
 * Created by zk on 2018/2/25.
 * 高的定位
 */

public class LocationGaode extends LocationBase {

    private static final String TAG = LocationGaode.class.getSimpleName();

    //声明AMapLocationClient类对象
    private AMapLocationClient locationClient = null;

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (aMapLocation.getErrorCode() == 0) {
                    Log.i(TAG, "高德定位：" + aMapLocation.toStr());

                    LocationInfo info = new LocationInfo();
                    info.latitude = aMapLocation.getLatitude();
                    info.longitude = aMapLocation.getLongitude();
                    info.country = aMapLocation.getCountry();
                    info.province = aMapLocation.getProvince();
                    info.city = aMapLocation.getCity();
                    info.aoiname = aMapLocation.getAoiName();
                    info.details = aMapLocation.getAddress();
                    locationSuccess(info);

                } else {
                    //定位失败
                    Log.w(TAG, "高德定位：" + aMapLocation.getErrorCode());
                    Log.w(TAG, "高德定位：" + aMapLocation.getErrorInfo());
                    Log.w(TAG, "高德定位：" + aMapLocation.getLocationDetail());
                    locationFail("定位失败：" + aMapLocation.getErrorCode());
                }

            } else {
                Log.e(TAG, "高德定位：location is null");
                locationFail("定位失败");
            }
        }
    };

    public LocationGaode(Context context) {
        super(context);
        locationClient = new AMapLocationClient(context);
    }

    @Override
    public void stop() {
        locationClient.unRegisterLocationListener(locationListener);
        locationClient.stopLocation();
    }

    @Override
    public void start() {
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

}
