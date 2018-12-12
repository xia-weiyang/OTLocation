package com.jiushig.location.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jiushig.location.R;
import com.jiushig.location.utils.Log;


/**
 * Created by zk on 2018/2/26.
 */

public class MapActivity extends BaseActivity {

    private static final String TAG = MapActivity.class.getSimpleName();

    private MapView mapView;
    private AMap aMap;

    private double lat, lon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);

        initView();

        mapView.onCreate(savedInstanceState);

        aMap = mapView.getMap();
        getAddress();

        if (getSupportActionBar() != null) {
            int color = getIntent().getIntExtra("barColor", getResources().getColor(R.color.colorPrimary));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            setStatusBarColor(color);
        }
    }

    private void initView() {
        mapView = findViewById(R.id.map);
    }


    /**
     * 启动此Activity
     *
     * @param context
     * @param lat     纬度
     * @param lon     经度
     */
    public static void start(Context context, double lat, double lon, int barColor) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("barColor", barColor);
        context.startActivity(intent);
    }

    public static void start(Context context, double lat, double lon) {
        start(context, lat, lon, context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    //解析指定坐标的地址
    public void getAddress() {
        Log.i(TAG, "获取位置中");
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);//地址查询器
        //设置查询参数,
        //三个参数依次为坐标，范围多少米，坐标系
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(lat, lon), 50, GeocodeSearch.AMAP);

        //设置查询结果监听
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            //根据坐标获取地址信息调用
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                String s = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                Log.i(TAG, s);
                makePoint(s);
            }

            //根据地址获取坐标信息是调用
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });

        geocodeSearch.getFromLocationAsyn(regeocodeQuery);//发起异步查询请求
    }

    //根据地址绘制需要显示的点
    public void makePoint(String s) {
        //北纬39.22，东经116.39，为负则表示相反方向
        LatLng latLng = new LatLng(lat, lon);
        //使用默认点标记
        Marker maker = aMap.addMarker(new MarkerOptions().position(latLng).title("位置").snippet(s));

        //改变可视区域为指定位置
        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 17, 0, 30));
        aMap.moveCamera(cameraUpdate);//地图移向指定区域
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
