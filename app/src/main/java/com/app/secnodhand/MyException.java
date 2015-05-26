package com.app.secnodhand;

/**
 * An exception class that will be thrown when WeiboAPI calls are failed.<br>
 * In case the Weibo server returned HTTP error code, you can get the HTTP
 * status code using getStatusCode() method.
 */
@SuppressWarnings("serial")
public class MyException extends Exception {
	private int statusCode = -1;

	public MyException(String msg) {
		super(msg);
	}

	public MyException(Exception cause) {
		super(cause);
	}

	public MyException(int statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
	}

	public MyException(String msg, Exception cause) {
		super(msg, cause);
	}

	public MyException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
