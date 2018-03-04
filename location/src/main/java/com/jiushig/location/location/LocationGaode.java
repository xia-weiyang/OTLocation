package com.jiushig.location.location;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jiushig.location.entity.Location;
import com.jiushig.location.ui.SelectActivity;
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

                    // PIO检索
                    PoiSearch.Query query = new PoiSearch.Query(LocationBuilder.POI, "", aMapLocation.getCityCode());
                    query.setPageSize(1);
                    query.setPageNum(0);
                    PoiSearch poiSearch = new PoiSearch(context, query);
                    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(),
                            aMapLocation.getLongitude()), 1000));
                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            //解析result获取POI信息
                            if (i == 1000) {
                                Location info = new Location();
                                for (PoiItem poiItem : poiResult.getPois()) {
                                    Log.i(TAG, poiItem.toString());
                                    info.country = aMapLocation.getCountry();
                                    info.poiName = poiItem.getTitle();
                                    info.details = poiItem.getSnippet();
                                    info.latitude = poiItem.getLatLonPoint().getLatitude();
                                    info.longitude = poiItem.getLatLonPoint().getLongitude();
                                    info.province = poiItem.getProvinceName();
                                    info.city = poiItem.getCityName();
                                }

                                locationSuccess(info);
                            } else {
                                Log.e(TAG, "POI 检索失败 " + i);
                                locationFail("POI 检索失败 " + i);
                            }
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {

                        }
                    });
                    poiSearch.searchPOIAsyn();

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
