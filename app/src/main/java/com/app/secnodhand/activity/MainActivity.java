package com.app.secnodhand.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.secnodhand.BaiduMapManager;
import com.app.secnodhand.MyApplication;
import com.app.secnodhand.R;
import com.app.secnodhand.base.BaseActivity;
import com.app.secnodhand.base.ViewInject;
import com.app.secnodhand.entity.LocationInfo;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by zxk on 15-5-26.
 */
public class MainActivity extends BaseActivity{

    @ViewInject(R.id.main_mapview)
    private MapView mMapView;
    @ViewInject(R.id.main_menu)
    private TextView menu;
    private BaiduMap mBaiduMap;
    private MyLocationData mLocData;
    private TextView text;


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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=new TextView(mContext);
//                text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                text.setBackgroundColor(0xffff0000);
                text.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.map_bz_anim));
                addContentView(text,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            }
        });
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
                        if(mBaiduMap!=null){
                            mBaiduMap.setMyLocationEnabled(true);
                            mBaiduMap.setMyLocationData(mLocData);
                            LatLng ll = new LatLng(locInfo.getLatitude(), locInfo.getLongitude());
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                            mBaiduMap.animateMapStatus(u);
                        }
                    }
                });
    }
}
