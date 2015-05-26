package com.app.secnodhand.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.app.secnodhand.MyApplication;
import com.app.secnodhand.R;
import com.app.secnodhand.util.AppUtil;


@SuppressWarnings("rawtypes")
public class HttpHelper {
    private Context mContext;

    public HttpHelper(Context context) {
        mContext = context;
    }

    public String mPost(String httpUrl, List<NameValuePair> params) {
        String httpResult = MyHttpClient.post(mContext, httpUrl, params);
        return httpResult;
    }

    public String myPost(String httpUrl, List<NameValuePair> params) {
        String httpResult = MyHttpClient.mypost(mContext, httpUrl, params);
        return httpResult;
    }

    public String mPut(String httpUrl, List<NameValuePair> params) {
        String httpResult = MyHttpClient.put(mContext, httpUrl, params);
        return httpResult;
    }

    public String mPostWithFile(String httpUrl, List<NameValuePair> params) {
        String httpResult = MyHttpClient.postWithFile(mContext, httpUrl, params);
        return httpResult;
    }

    public String mGet(String httpUrl) {
        String httpResult = MyHttpClient.get(mContext, httpUrl);
        return httpResult;
    }

    
    public String getStrFromParamsList(List<NameValuePair> params) {
        Iterator iter = params.iterator();
        Map<String, String> map = new HashMap<String, String>();
        String paramsStr = "";

        try {
            while (iter.hasNext()) {
                BasicNameValuePair item = (BasicNameValuePair) iter.next();
                map.put(item.getName(), item.getValue());
            }
            JSONObject json = new JSONObject(map);
            try {
                paramsStr = URLEncoder.encode(json.toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return "?data=" + paramsStr;
    }

    /**
     * 组装参数
     *
     * @param params
     */
    public List<NameValuePair> constructParams(List<NameValuePair> params) {
        if (params == null)
            return null;
        // 添加基本参数
        params.add(new BasicNameValuePair("system", "android"));
        params.add(new BasicNameValuePair("version", MyApplication.mContext.getString(R.string.app_version)));
        System.out.println("qudao"+ AppUtil.getChannelStr());
        params.add(new BasicNameValuePair("ttid", AppUtil.getChannelStr()));
        // 手机型号
        params.add(new BasicNameValuePair("device", android.os.Build.MODEL));
        params.add(new BasicNameValuePair("deviceId", MyApplication.getImeiCode()));

        String sec = getSecuValue(params);
        if (AppUtil.isNotEmpty(sec)) {
            params.add(new BasicNameValuePair("secu", sec));
        }
        return params;
    }
    

    /**
     * secu生成规则
     *
     * @param params
     * @return
     */
    private String getSecuValue(List<NameValuePair> params) {
        String sec = getValuesStr(params) + "xrok3l~^=zcpy";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 加密后的字符串
            sec = byte2hex(md5.digest(sec.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sec;
    }

    /**
     * List<NameValuePair>转成一个String
     *
     * @param params
     * @return
     */
    private String getValuesStr(List<NameValuePair> params) {
        StringBuffer value = new StringBuffer();
//		Iterator iter = params.iterator();
        synchronized (params) {
            Iterator iter = params.iterator();
            while (iter.hasNext()) {
                BasicNameValuePair item = (BasicNameValuePair) iter.next();
                value.append(item.getValue());
            }
            return value.toString();

        }
    }

    private String byte2hex(byte[] b) {

        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}