package com.app.secnodhand;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduMapManager {
	private static BaiduMapManager instance;

	// 定位相关
	private LocationClient mLocClient;
	private MyLocationListener locationListener = new MyLocationListener();
	private LocationChangedListener locationChangedListener;
	private int scanSpan=0;

	public interface LocationChangedListener {
		void onLocationChanged(BDLocation location);
	}

	public static BaiduMapManager getInstance() {
		if (instance == null)
			instance = new BaiduMapManager();
		return instance;
	}

	public void requestLocation(LocationChangedListener locationChangedListener) {
		mLocClient = new LocationClient(MyApplication.mContext);
		this.locationChangedListener = locationChangedListener;

		mLocClient = new LocationClient(MyApplication.mContext);
		mLocClient.registerLocationListener(locationListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setAddrType("all"); // 返回的定位结果包含地址信息
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	public void setScanSpan(int scanSpan){
		this.scanSpan=scanSpan;
	}

	public void updateLocation() {
		mLocClient.requestLocation();
	}

	public void destroyLocation() {
		if (mLocClient != null)
			mLocClient.stop();
	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if (locationChangedListener != null) {
				locationChangedListener.onLocationChanged(location);
			}
		}




	}
}
