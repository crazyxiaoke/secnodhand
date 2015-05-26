package com.app.secnodhand.base;




import com.app.secnodhand.MyException;
import com.app.secnodhand.entity.ErrorType;
import com.app.secnodhand.util.AppUtil;

import org.json.JSONObject;

import java.io.Serializable;

public class BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static boolean isConnectedSuccess(JSONObject json) throws MyException {
        System.out.println("json="+json);
		if (json.has("ret")) {
			if ("ok".equals(AppUtil.getJsonStringValue(json, "ret"))) {
				return true;
			} else {
				throw new MyException(AppUtil.getJsonIntegerValue(json, "errorcode"), AppUtil.getJsonStringValue(json, "errordesc"));
			}
		} else {
			ErrorType errorType = ErrorType.getErrorType(ErrorType.NETWORK_REQUEST_ERROR);
			throw new MyException(errorType.getErrorCode(), errorType.getErrorBody());
		}
	}

	public static void showResponseResult(JSONObject json) throws MyException {
		if (json.has("ret")) {
			if ("ok".equals(AppUtil.getJsonStringValue(json, "ret"))) {
				ErrorType errorType = ErrorType.getErrorType(ErrorType.SUCCESS);
				throw new MyException(errorType.getErrorCode(), errorType.getErrorBody());
			} else {
				throw new MyException(AppUtil.getJsonIntegerValue(json, "errorcode"), AppUtil.getJsonStringValue(json, "errordesc"));
			}
		} else {
			ErrorType errorType = ErrorType.getErrorType(ErrorType.NETWORK_REQUEST_ERROR);
			throw new MyException(errorType.getErrorCode(), errorType.getErrorBody());
		}
	}
}
