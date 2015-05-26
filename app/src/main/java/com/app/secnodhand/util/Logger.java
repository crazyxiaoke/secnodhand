package com.app.secnodhand.util;

import android.util.Log;

import com.app.secnodhand.BuildConfig;


public class Logger {
	public static void sysoutMsg(String printStr) {
		if (BuildConfig.DEBUG)
			System.out.println(printStr);
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG)
			Log.e(tag, msg);
	}
}
