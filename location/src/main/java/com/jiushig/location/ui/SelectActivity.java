package com.jiushig.location.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jiushig.location.R;
import com.jiushig.location.entity.Location;
import com.jiushig.location.location.LocationBuilder;
import com.jiushig.location.ui.adapter.SelectAdapter;
import com.jiushig.location.utils.Log;
import com.jiushig.location.utils.Permission;

import java.util.ArrayList;

/**
 * Created by zk on 2018/3/4.
 */

public class SelectActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    private static final String TAG = SelectActivity.class.getSimpleName();

    private AMap aMap;
    private MapView mapView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private Marker marker;

    private ArrayList<Location> locations = new ArrayList<>();

    public static final int REQUEST_CODE = 234;

    /**
     * 当不为空时 显示不选择位置item
     */
    private String unSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        unSelect = getIntent().getStringExtra("unSelect");

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SelectAdapter(this, locations));
    }

    public static void start(Activity activity, String unSelect) {
        Intent intent = new Intent(activity, SelectActivity.class);
        intent.putExtra("unSelect", unSelect);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void start(Activity activity) {
        start(activity, "");
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
//        if (aMap == null) {
        aMap = mapView.getMap();
        setUpMap();
//        }
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.gps_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(50, 135, 206, 150));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(50, 135, 206, 150));// 设置圆形的填充颜色
//         myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        //myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        aMap.getUiSettings().setZoomControlsEnabled(false);//取消右下角缩放按钮
        aMap.getUiSettings().setScaleControlsEnabled(true);//显示控制比例尺控件
        //设置希望展示的地图缩放级别   地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d(TAG, cameraPosition.toString());

                // 绘制标记点
                showMarker(new MarkerOptions().position(cameraPosition.target));
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Log.i(TAG, cameraPosition.toString());

                // 绘制标记点
                showMarker(new MarkerOptions().position(cameraPosition.target));

                // 查询地址详细信息
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                GeocodeSearch geocoderSearch = new GeocodeSearch(SelectActivity.this);
                geocoderSearch.setOnGeocodeSearchListener(SelectActivity.this);
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude), 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        });
    }

    /**
     * 展示高德地图标记点
     *
     * @param markerOptions
     */
    private void showMarker(MarkerOptions markerOptions) {
        if (marker == null) {
            marker = aMap.addMarker(markerOptions);
        } else {
            marker.setPosition(markerOptions.getPosition());
        }
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

        Permission.location(this, isSuccess -> {
            // 检测是否开启了定位
            LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (manager != null) {
                boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean network = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!gps && !network) {
                    new AlertDialog.Builder(this)
                            .setMessage("测到未开启定位服务，是否立即开启？")
                            .setPositiveButton("开启", (d, i) -> {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 8732);
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
            initMap();
        });
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


    @Override
    public void onMyLocationChange(android.location.Location location) {
        Log.i(TAG, location.toString());

//        // 查询地址详细信息
//        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
//        geocoderSearch.setOnGeocodeSearchListener(this);
//        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location.getLongitude()), 200, GeocodeSearch.AMAP);
//        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Log.i(TAG, regeocodeResult.toString());
        //解析result获取地址描述信息
        if (i == 1000) {
            // PIO检索
            PoiSearch.Query query = new PoiSearch.Query(LocationBuilder.POI, "", regeocodeResult.getRegeocodeAddress().getCityCode());
            query.setPageSize(50);
            query.setPageNum(0);
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude(),
                    regeocodeResult.getRegeocodeQuery().getPoint().getLongitude()), 1000));
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    if (i == 1000) {
                        //解析result获取POI信息
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        locations.clear();

                        if (!"".equals(unSelect)) {
                            Location info = new Location();
                            info.poiName = unSelect;
                            info.isSelect = false;
                            locations.add(info);
                        }

                        for (PoiItem poiItem : poiResult.getPois()) {
                            Log.i(TAG, poiItem.toString());
                            Location info = new Location();
                            info.country = regeocodeResult.getRegeocodeAddress().getCountry();
                            info.poiName = poiItem.getTitle();
                            info.details = poiItem.getSnippet();
                            info.latitude = poiItem.getLatLonPoint().getLatitude();
                            info.longitude = poiItem.getLatLonPoint().getLongitude();
                            info.province = poiItem.getProvinceName();
                            info.city = poiItem.getCityName();

                            locations.add(info);
                        }

                        progressBar.setVisibility(View.GONE);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        Toast.makeText(SelectActivity.this, "检索失败 " + i, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });
            poiSearch.searchPOIAsyn();
        } else {
            Log.e(TAG, "获取地址描述信息失败  " + i);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

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
