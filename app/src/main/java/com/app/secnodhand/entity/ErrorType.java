package com.app.secnodhand.entity;

import java.util.HashMap;

import android.annotation.SuppressLint;

public class ErrorType {

	public static Integer NETWORK_ERROR = 1000;
	public static Integer NETWORK_REQUEST_ERROR = -1;
	public static Integer SUCCESS = 49;

	/** all error types */
	@SuppressLint("UseSparseArrays")
	static HashMap<Integer, ErrorType> allErrorTypes = new HashMap<Integer, ErrorType>();
	static {
		allErrorTypes.put(NETWORK_REQUEST_ERROR, new ErrorType(NETWORK_REQUEST_ERROR, "网络连接不给力，请重试"));
		allErrorTypes.put(SUCCESS, new ErrorType(SUCCESS, "操作成功"));
		allErrorTypes.put(NETWORK_ERROR, new ErrorType(NETWORK_ERROR, "网络连接失败，请检查网络配置"));
	}

	private Integer errorCode;
	private String errorBody;

	public ErrorType(int errorCode, String errorBody) {
		this.setErrorCode(errorCode);
		this.setErrorBody(errorBody);
	}

	public ErrorType(int errorCode) {
		this.setErrorCode(errorCode);
		this.setErrorBody(getErrorBody(errorCode));
	}

	public static ErrorType getErrorType(int errorCode) {
		ErrorType error = allErrorTypes.get(errorCode);
		if (error == null) {
			error = allErrorTypes.get(-1);
		}
		return error;
	}

	public static String getErrorBody(int error) {
		ErrorType type = allErrorTypes.get(error);
		if (type != null) {
			return type.getErrorBody();
		}
		return "";
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorBody() {
		return errorBody;
	}

	public void setErrorBody(String errorBody) {
		this.errorBody = errorBody;
	}

}