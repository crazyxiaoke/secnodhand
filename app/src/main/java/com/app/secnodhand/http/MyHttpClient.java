package com.app.secnodhand.http;

import java.io.IOException;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.util.Log;

public class MyHttpClient {
	private static HttpClient myHttpClient;

	private static final int GET = 0;
	private static final int POST = 1;
	private static final int POST_WITH_FILE = 4;
	private static final int MYPOST = 5;
	private static final int PUT = 2;
	private static final int DELETE = 3;

	public static synchronized HttpClient getHttpClient() {
		if (null == myHttpClient) {
			try {
				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(params, false);
				params.setParameter("Proxy-Connection", "Keep-Alive");

				ConnManagerParams.setTimeout(params, 20 * 1000);
				HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
				HttpConnectionParams.setSoTimeout(params, 20 * 1000);
				HttpConnectionParams.setSocketBufferSize(params, 8 * 1024);

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));
				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
						params, registry);
				myHttpClient = new DefaultHttpClient(conMgr, params);
			} catch (Exception e) {
				return new DefaultHttpClient();
			}
		}
		BasicCookieStore cookie = new BasicCookieStore();
		((DefaultHttpClient) myHttpClient).setCookieStore(cookie);
		return myHttpClient;
	}

	public static void clearCookie() {
		BasicCookieStore cookie = new BasicCookieStore();
		((DefaultHttpClient) getHttpClient()).setCookieStore(cookie);
	}

	public static boolean checkAndSetWap(Context mContext, HttpClient client) {
		NetworkType networkType = NetworkControl.getNetworkType(mContext);
		if (networkType != null
				&& networkType.getType() == NetworkType.NET_TYPE_MOBILE_WAP) {
			String proxyName = networkType.getProxy();
			int proxyPort = networkType.getPort();

			Uri uri = Uri.parse("content://telephony/carriers/preferapn");
			Cursor mCursor = mContext.getContentResolver().query(uri, null,
					null, null, null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToNext();
				String proxyStr = mCursor.getString(mCursor
						.getColumnIndex("proxy"));
				if (proxyStr != null && proxyStr.trim().length() > 0) {
					proxyName = proxyStr;
					proxyPort = 80;
				}
			}
			HttpHost proxy = new HttpHost(proxyName, proxyPort);
			client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					proxy);
			return true;
		}
		return false;
	}

	public static String put(Context mContext, String httpUrl,
			List<NameValuePair> params) {
		return getHttpResult(mContext, PUT, httpUrl, params);
	}

	public static String post(Context mContext, String httpUrl,
			List<NameValuePair> params) {
		return getHttpResult(mContext, POST, httpUrl, params);
	}
	
	public static String mypost(Context mContext,String httpUrl,List<NameValuePair> params){
		return getHttpResult(mContext, MYPOST, httpUrl, params);
	}

	public static String postWithFile(Context mContext, String httpUrl,
			List<NameValuePair> params) {
		return getHttpResult(mContext, POST_WITH_FILE, httpUrl, params);
	}

	public static String get(Context mContext, String httpUrl) {
		return getHttpResult(mContext, GET, httpUrl, null);
	}

	public static String delete(Context mContext, String httpUrl) {
		return getHttpResult(mContext, DELETE, httpUrl, null);
	}

	public static String getHttpResult(Context mContext, int method,
			String httpUrl, List<NameValuePair> params) {
		HttpResponse httpResponse = getHttpResponse(mContext, method, httpUrl,
				params);
		try {
			if (httpResponse != null) {
				HttpEntity resEntity = httpResponse.getEntity();
				if (resEntity != null) {
					String result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					Log.d("result", "result:"+result);
					if (result != null) {
						return result;
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		return "";
	}

	public static HttpResponse getHttpResponse(Context mContext, int method,
			String httpUrl, List<NameValuePair> params) {
		try {
			HttpClient client = getHttpClient();
			HttpRequestBase request = null;
			switch (method) {
			case GET:
				request = new HttpGet(httpUrl);
				break;
			case POST:
				request = new HttpPost(httpUrl);
				if (params != null) {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(
							params, HTTP.UTF_8));
				}
				break;
			case MYPOST:
		           request=new HttpPost(httpUrl);
		           request.setHeader("Content-type", "application/json");
		           Log.d("result", params.get(0).getValue());
		           StringEntity sn=new StringEntity(params.get(0).getValue(),HTTP.UTF_8);
		           sn.setContentType("text/html");
		           ((HttpPost)request).setEntity(sn);
		        break;
			case PUT:
				request = new HttpPut(httpUrl);
				if (params != null) {
					((HttpPut) request).setEntity(new UrlEncodedFormEntity(
							params, HTTP.UTF_8));
				}
				break;
			case DELETE:
				request = new HttpDelete(httpUrl);
				break;
			case POST_WITH_FILE:
				request = new HttpPost(httpUrl);
				// 这个应用中使用了又拍云，所以这里使用的httpmime中的MultipartEntity不需要了
				// if (params != null) {
				// MultipartEntity entity = new MultipartEntity();
				// for (NameValuePair param : params) {
				// if (param.getName().equals("file")) {
				// File file = new File(param.getValue());
				// if (file.exists()) {
				// entity.addPart(param.getName(), new FileBody(file));
				// }
				// } else {
				// entity.addPart(param.getName(), new
				// StringBody(param.getValue(), Charset.forName("UTF-8")));
				// }
				// }
				// ((HttpPost) request).setEntity(entity);
				// }
				break;
			}
			Log.d("result", "request:"+request.getParams());
			HttpResponse httpResponse = client.execute(request);
			Log.d("result", "httpResponse:"+httpResponse);
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				boolean isWapProxy = checkAndSetWap(mContext, client);
				if (isWapProxy) {
					httpResponse = client.execute(request);
				}
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return httpResponse;
			} else {
			}
		} catch (Exception e) {
		}
		return null;
	}
}
