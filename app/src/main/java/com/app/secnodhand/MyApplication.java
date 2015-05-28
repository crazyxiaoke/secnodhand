package com.app.secnodhand;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.app.secnodhand.entity.LocationInfo;
import com.baidu.mapapi.SDKInitializer;



public class MyApplication extends Application{

	public static Context mContext;
	public static SharedPreferences config;
    public static LocationInfo sLocInfo;



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        Log.d("TAG","MyApplication启动");
		mContext=getApplicationContext();
        Log.d("TAG","mContext="+mContext.getPackageName());
		config = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SDKInitializer.initialize(getApplicationContext());
	}


    //	public static BMapManager getBMapManager(){
//
//		return mBMapManager;
//
//	}

    public static String getImeiCode(){
        TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        if (szImei==null) {
            szImei="";
        }
        return szImei;
    }

    public static String getTempImageFile() {
        return getSDPath() + "/tempImage.jpg";
    }

    public static String getSDPath() {
        if (isSDPresent()) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    public static boolean isSDPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
