package com.example.administrator.demotest.activity;

import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;

import java.time.LocalDate;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private LocationClient mLocationClient;
    BDLocationListener listener = new MyLocationListner();
    private LatLng latLng;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true; // 是否首次定位
    ToastUtil toastUtil = new ToastUtil(this);
    private MapView map;
    private MapView map1;
    private Button mBtnDw;

    private double mCurrentLat;
    private double mCurrentLon;
    private String address = "";
    private BitmapDescriptor bitmap;
    //    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
//        web = (WebView) findViewById(R.id.web);
//        WebSettings webSettings = web.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        web.loadUrl("http://test.h5.xianxiangle.com/#/purchase");
        initMap();
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map_red_pin);

    }

    private void initMap() {
        map = findViewById(R.id.map);
        mBtnDw = (Button) findViewById(R.id.dw_bt);
        mBaiduMap = map.getMap();
        //默认显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置定位可使用
        mBaiduMap.setMyLocationEnabled(true);

        // 不显示地图上比例尺
        map.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        map.showZoomControls(false);

        // 删除百度地图LoGo
        map.removeViewAt(1);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mBaiduMap.setTrafficEnabled(true);
//        mLocationClient.registerLocationListener(listener);
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 获取经纬度
                mCurrentLat = latLng.latitude;
                mCurrentLon = latLng.longitude;

                // 先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(mCurrentLat, mCurrentLon);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                // 实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                // 设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                // 发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(
                            ReverseGeoCodeResult arg0) {
                        // 获取点击的坐标地址
                        address = arg0.getAddress();
//                        city_tv.setText(arg0.getAddressDetail().city);
//                        address_tv.setText(address);


                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        });
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1);
        mLocationClient.setLocOption(option);
        mLocationClient.start();


//        initLocation();

        initClick();
    }

    private void initClick() {
        mBtnDw.setOnClickListener(this);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
//        int span = 1000;
//        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dw_bt:
                mBaiduMap.setMyLocationEnabled(true);
                mLocationClient.start();
                mLocationClient.requestLocation();
                break;
            default:
                break;
        }
    }

    public class MyLocationListner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            // map view 销毁后不在处理新接收的位置
            if (location == null || map == null) {
                return;

            }
            // 获取经纬度
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();

            // 先清除图层
            mBaiduMap.clear();
            // 定义Maker坐标点
            LatLng point = new LatLng(mCurrentLat, mCurrentLon);
            // 构建MarkerOption，用于在地图上添加Marker
            MarkerOptions options = new MarkerOptions().position(point).icon(
                    bitmap);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(options);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));

            // 实例化一个地理编码查询对象
            GeoCoder geoCoder = GeoCoder.newInstance();
            // 设置反地理编码位置坐标
            ReverseGeoCodeOption op = new ReverseGeoCodeOption();
            op.location(point);
            // 发起反地理编码请求(经纬度->地址信息)
            geoCoder.reverseGeoCode(op);
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                    // 获取点击的坐标地址
                    address = arg0.getAddress();
//                    city_tv.setText(arg0.getAddressDetail().city);
//                    address_tv.setText(address);
                    // 定位成功后就停止

                    mBaiduMap.setMyLocationEnabled(false);
                    if (mLocationClient.isStarted()) {
                        mLocationClient.stop();
                    }
                    ;
                }

                @Override
                public void onGetGeoCodeResult(GeoCodeResult arg0) {
                }
            });

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}
