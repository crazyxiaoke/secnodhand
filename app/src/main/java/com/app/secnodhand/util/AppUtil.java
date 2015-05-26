package com.app.secnodhand.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.secnodhand.Constants;
import com.app.secnodhand.MyApplication;
import com.app.secnodhand.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AppUtil {
    private static Toast btoast;

    /**
     * 判断网络是否连接
     */
    public static boolean isNetwork(Context context) {
        Log.d(Constants.LOGTAG,"isNetWork");
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            Log.d(Constants.LOGTAG,"true");
            return true;
        }
        Log.d(Constants.LOGTAG,"false");
        return false;
    }

    /**
     * 显示toast提示框
     */
    public static void showShortMessage(Context mContext, CharSequence text) {
        if (text != null && text.length() > 0) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongMessage(Context mContext, CharSequence text) {
        if (text != null && text.length() > 0) {
            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 判断字符串为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0
                || str.trim().equals("null");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 关闭task
     */
    public static void cancelTask(AsyncTask task) {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
            task = null;
        }
    }

    public static ProgressDialog showProgress(Activity activity, String hintText) {
        Activity mActivity = null;
        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        ProgressDialog window = ProgressDialog
                .show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);

        window.setCancelable(false);
        return window;
    }

    public static ProgressDialog showProgress(Activity activity) {
        ProgressDialog progressDialog = showProgress(activity, "加载中，请稍候...");
        setDialogText(progressDialog.getWindow().getDecorView());
        return progressDialog;
    }

    public static void setDialogText(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) v;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                setDialogText(child);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTextSize(12);
        }
    }

    public static int convertPxToDp(float px) {
        WindowManager wm = (WindowManager) MyApplication.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        float dp = px / logicalDensity;
        return (int) dp;
    }

    public static int convertDpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MyApplication.mContext.getResources().getDisplayMetrics());
    }

    public static int convertSpToPx(float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,
                MyApplication.mContext.getResources().getDisplayMetrics());
    }




    /**json操作*/
    public static String[] getJsonObjectStringArray(JSONArray jsonArray,
                                                    String key) {
        String res[];
        try {
            res = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));
                res[i] = getJsonStringValue(jsonObject2, key);
            }
        } catch (Exception e) {
            return new String[] {};
        }
        return res;
    }

    public static int[] getJsonObjectIntegerArray(JSONArray jsonArray,
                                                  String key) {
        int res[];
        try {
            res = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));
                res[i] = getJsonIntegerValue(jsonObject2, key);
            }
        } catch (Exception e) {
            return new int[] {};
        }
        return res;
    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
        try {
            if (jsonArray != null && index >= 0 && index < jsonArray.length()) {
                return jsonArray.getJSONObject(index);
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    public static String getArrayValue(String[] array, int index) {
        if (array != null && index >= 0 && index < array.length) {
            return array[index];
        }
        return "";
    }

    public static int getArrayValue(int[] array, int index) {
        if (array != null && index >= 0 && index < array.length) {
            return array[index];
        }
        return 0;
    }


    public static String getJsonStringValue(JSONObject jsonObject, String key) {
        return getJsonStringValue(jsonObject, key, "");
    }

    public static String getJsonStringValue(JSONObject jsonObject, String key,
                                            String defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                String value = jsonObject.getString(key).trim();
                if (value.equals("null")) {
                    value = "";
                }
                return value;
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static int getJsonIntegerValue(JSONObject json, String key) {
        return getJsonIntegerValue(json, key, 0);
    }

    public static int getJsonIntegerValue(JSONObject jsonObject, String key,
                                          int defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static Long getJsonLongValue(JSONObject json, String key) {
        return getJsonLongValue(json, key, 0L);
    }

    public static Long getJsonLongValue(JSONObject jsonObject, String key,
                                        Long defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getLong(key);
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static float getJsonFloatValue(JSONObject jsonObject, String key,
                                          float defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return Float.valueOf(jsonObject.getString(key));
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static boolean getJsonBooleanValue(JSONObject jsonObject,
                                              String key, boolean defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getBoolean(key);
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String key) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getJSONObject(key);
            }
        } catch (Exception e) {
            return new JSONObject();
        }
        return new JSONObject();
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String key) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getJSONArray(key);
            }
        } catch (Exception e) {
            return new JSONArray();
        }
        return new JSONArray();
    }

    public static List<JSONObject> JsonArrayToList(JSONArray array){
        List<JSONObject> list=new ArrayList<JSONObject>();
        try{
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    list.add(array.getJSONObject(i));
                }
            }
        }catch (Exception e){}
        return list;
    }

    public static List<Map<String,String>> JsonArrayToListMap(JSONArray array){
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        try{
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    list.add(toMap(array.getJSONObject(i)));
                }
            }
        }catch (Exception e){}
        return list;
    }

    public static Map<String, String> toMap(JSONObject object)throws JSONException {

        Map<String,String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, AppUtil.getJsonStringValue(object,key));
        }
        return map;
    }


    /**MD5加密*/
    public static String getMD5(String str) {
        Log.d("result", "AppUtilMd5:" + str);
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("result", "AppUtil.Md5:" + re_md5);
        return re_md5;
    }

    /**获取渠道*/
    public static String getChannelStr() {
        String qudao = "";
        ApplicationInfo appInfo = null;
        try {
            appInfo = MyApplication.mContext.getPackageManager()
                    .getApplicationInfo(
                            MyApplication.mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            qudao = appInfo.metaData.getString("UMENG_CHANNEL");
            if (qudao == null) {
                qudao = String.valueOf(appInfo.metaData.getInt("UMENG_CHANNEL"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.d("caronline", "appInfo.metaData.getInt(\"UMENG_CHANNEL\")=" + appInfo.metaData.getInt("UMENG_CHANNEL"));
                qudao = String
                        .valueOf(appInfo.metaData.getInt("UMENG_CHANNEL"));
                Log.d("caronline", "qudao1=" + qudao);
            } catch (Exception e2) {
                Log.d("caronline", "error");
                e2.printStackTrace();
            }
        }
        Log.d("caronline", "qudao2=" + qudao);
        return qudao;
    }

    /**获取屏幕宽度*/
    public static int getScreenWidth() {
        return MyApplication.mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static boolean isAfterHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**日期转换格式*/
    public static String formatDate(String datestr,String f) {
        try {
            if(datestr.length()>0&&f.length()>0) {
                SimpleDateFormat sdf = new SimpleDateFormat(f);
                Date date = new Date((Long.valueOf(datestr) * 1000l));
                return sdf.format(date);
            }
        }catch(Exception e){
            return "";
        }
        return "";
    }

    /**月份加减*/
    public static long getMonth(long time,String type){
        try {
            if (time > 0) {
                Calendar ca = Calendar.getInstance();
                ca.setTimeInMillis(time);
                if (type.equals("add")) {
                    ca.add(Calendar.MONTH, 1);
                } else if (type.equals("subtract")) {
                    ca.add(Calendar.MONTH, -1);
                }
                return ca.getTimeInMillis();
            }
        }catch (Exception e){
            return 0;
        }
        return 0;
    }

    public static void dialPhone(Activity activity, String telephone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telephone));
        activity.startActivity(callIntent);
    }

}
