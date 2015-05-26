package com.app.secnodhand;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;


import org.json.JSONException;
import org.json.JSONObject;


public class MyApplication extends Application{

	public static Context mContext;
	public static SharedPreferences config;



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext=this.getApplicationContext();
		config = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
