package com.app.secnodhand.base;

import android.os.AsyncTask;
import android.util.Log;

import com.app.secnodhand.Constants;
import com.app.secnodhand.MyApplication;
import com.app.secnodhand.entity.ErrorType;
import com.app.secnodhand.util.AppUtil;


public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected UiChange mUiChange;
	protected ErrorType errorType;
	private String requestTag;



    public interface UiChange {
		void preUiChange(String requestTag);

		void lateUiChange(Object result, Boolean isSuccess, String requestTag);
	}

	protected BaseTask(UiChange uiChange) {
		this(uiChange, null);
	}

	protected BaseTask(UiChange uiChange, String requestTag) {
		mUiChange = uiChange;
		this.requestTag = requestTag;
	}

	@Override
	protected void onPreExecute() {
		if (!AppUtil.isNetwork(MyApplication.mContext)) {
			errorType = ErrorType.getErrorType(ErrorType.NETWORK_ERROR);
			return;
		}
		if (mUiChange != null)
			mUiChange.preUiChange(requestTag);
	}

	@Override
	protected Result doInBackground(Params... para) {
		return null;
	}

	@Override
	protected void onPostExecute(Result result) {
		if (errorType != null) {
            Log.d(Constants.LOGTAG, "errorType=" + errorType.getErrorBody());
			AppUtil.showShortMessage(MyApplication.mContext, errorType.getErrorBody());
		}
		if (mUiChange != null)
			mUiChange.lateUiChange(result, errorType == null || errorType.getErrorCode() == ErrorType.SUCCESS, requestTag);
	}
}
