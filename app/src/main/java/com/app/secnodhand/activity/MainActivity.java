package com.app.secnodhand.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.secnodhand.BaiduMapManager;
import com.app.secnodhand.MyApplication;
import com.app.secnodhand.R;
import com.app.secnodhand.base.BaseActivity;
import com.app.secnodhand.base.ViewInject;
import com.app.secnodhand.entity.LocationInfo;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * Created by zxk on 15-5-26.
 */
public class MainActivity extends BaseActivity{

    @ViewInject(R.id.main_mapview)
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MyLocationData mLocData;


    @Override
    protected int getLayoutId() {
        return R.layout.main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        mBaiduMap=mMapView.getMap();
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.5f));
    }

    private void searchPoi(LatLng location){
        PoiSearch mPoiSearch=PoiSearch.newInstance();

        OnGetPoiSearchResultListener mPoiListener=new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                Log.d("TAG","poiResult="+poiResult.getTotalPoiNum());
                if(poiResult.getTotalPoiNum()>0){
                    for (PoiInfo poiInfo:poiResult.getAllPoi()) {
                        BitmapDescriptor sit=new BitmapDescriptorFactory().fromResource(R.drawable.location);
                        OverlayOptions overlayOptions = new MarkerOptions().position(poiInfo.location).icon(sit);
                        Marker marker=(Marker)(mBaiduMap.addOverlay(overlayOptions));
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        };

        mPoiSearch.setOnGetPoiSearchResultListener(mPoiListener);
        mPoiSearch.searchNearby(new PoiNearbySearchOption().pageCapacity(50).radius(5000).keyword("美食").location(location));
        mPoiSearch.searchNearby(new PoiNearbySearchOption().pageCapacity(50).radius(5000).keyword("KTV").location(location));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMapView!=null)
            mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMapView!=null)
            mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMapView!=null)
            mMapView.onResume();

        BaiduMapManager.getInstance().requestLocation(
                new BaiduMapManager.LocationChangedListener() {

                    @Override
                    public void onLocationChanged(BDLocation location) {
                        // TODO Auto-generated method stub

                        LocationInfo locInfo = LocationInfo
                                .fromBDLocationToLocationInfo(location);
                        MyApplication.sLocInfo = locInfo;
                        mLocData = new MyLocationData.Builder().accuracy(locInfo.getAccuracy())
                                .direction(100).latitude(locInfo.getLatitude())
                                .longitude(locInfo.getLongitude()).build();
                        Log.d("TAG","lat="+locInfo.getLatitude());
                        Log.d("TAG","lng="+locInfo.getLongitude());
                        if(mBaiduMap!=null){
                            mBaiduMap.setMyLocationEnabled(true);
                            mBaiduMap.setMyLocationData(mLocData);
                            LatLng ll = new LatLng(locInfo.getLatitude(), locInfo.getLongitude());
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                            mBaiduMap.animateMapStatus(u);
                            searchPoi(ll);
                        }
                    }
                });
    }

}
