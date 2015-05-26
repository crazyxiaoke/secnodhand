package com.app.secnodhand.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.app.secnodhand.Constants;
import com.app.secnodhand.MyApplication;
import com.app.secnodhand.http.HttpHelper;
import com.app.secnodhand.util.AppUtil;
import com.app.secnodhand.util.Logger;


public class BaseApi {
	protected HttpHelper httpHelper;
	protected List<NameValuePair> params;

	public BaseApi() {
		params = new ArrayList<NameValuePair>();
		this.httpHelper = new HttpHelper(MyApplication.mContext);
	}

	public Object getJsonResponseByGet(String httpUrl) {
		try {
			String response = httpHelper.mGet(httpUrl);
			if (AppUtil.isNotEmpty(response)) {
				try {

					return new JSONObject(response);
				} catch (Exception e) {
					// TODO: handle exception
				}
				return new JSONArray(response);
			}
		} catch (JSONException e) {
		}
		return new JSONObject();
	}

	public JSONObject getJsonResponseByPost(String httpUrl,
			List<NameValuePair> params) {
		return getJsonResponseByPost(httpUrl, params, false);
	}

	public JSONObject getJsonResponseByMyPost(String httpUrl,
			List<NameValuePair> params) {
		String response = httpHelper.myPost(httpUrl, params);

		try {
			if (AppUtil.isNotEmpty(response)) {
				response = "{\"ret\":\"ok\",\"data\":" + response
						+ ",\"errorcode\":0}";
				return new JSONObject(response);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new JSONObject();
	}

	public JSONObject getJsonResponseByPost(String httpUrl,
			List<NameValuePair> params, boolean isWithFile) {
		try {
			String response;
			if (isWithFile)
				response = httpHelper.mPostWithFile(httpUrl, params);
			else
				response = httpHelper.mPost(httpUrl, params);
			if (AppUtil.isNotEmpty(response)) {
				return new JSONObject(response);
			}
		} catch (JSONException e) {
		}
		return new JSONObject();
	}

	public JSONObject getJsonResponseByPut(String httpUrl,
			List<NameValuePair> params) {
		String response = httpHelper.mPut(httpUrl, params);
		try {
			if (AppUtil.isNotEmpty(response)) {
				Log.d("result", "response:" + response.toString());
				return new JSONObject(response);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return new JSONObject();
	}

	public JSONObject getJsonResponseByGet(String httpUrl, String varName) {
		String response = httpHelper.mGet(httpUrl);
		try {
			varName = varName + "=";
			if (response.indexOf(varName) != -1) {
				int beginIndex = response.indexOf(varName) + varName.length();
				int endIndex = response.lastIndexOf("};") + 1;
				response = response.substring(beginIndex, endIndex);
			}
			return new JSONObject(response);
		} catch (JSONException e) {
		}
		return new JSONObject();
	}

	public Object getJson(String httpUrl) {
		Object json;
		if (Constants.IS_HTTP_GET_METHOD) {
			Logger.sysoutMsg(httpUrl
                    + (params.size() > 0 ? httpHelper
                    .getStrFromParamsList(params) : ""));
			json = getJsonResponseByGet(httpUrl
					+ (params.size() > 0 ? httpHelper
							.getStrFromParamsList(params) : ""));
		} else {
			json = getJsonResponseByPost(httpUrl, params);
		}
		return json;
	}

	public JSONObject getJsonObject(String httpUrl) {

		return (JSONObject) getJson(httpUrl);
	}

    public void addAllParams(Map<String,String> map){
        Set<String> set=map.keySet();
        Iterator<String> it=set.iterator();
        while(it.hasNext()){
            String key=it.next();
            params.add(new BasicNameValuePair(key,map.get(key)));
        }
    }
}