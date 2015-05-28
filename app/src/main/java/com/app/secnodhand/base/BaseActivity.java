package com.app.secnodhand.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;

import com.app.secnodhand.Constants;
import com.app.secnodhand.R;
import com.app.secnodhand.imageutil.ImageCache;
import com.app.secnodhand.imageutil.ImageFetcher;
import com.app.secnodhand.util.AppUtil;

import java.lang.reflect.Field;


public abstract class BaseActivity extends FragmentActivity{
	protected Activity thisActivity;
	protected ProgressDialog progressDialog;
	protected Context mContext;
	protected LayoutInflater inflater;
	protected ImageFetcher mImageFetcher;
    protected Intent mIntent;

    protected abstract int getLayoutId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
		mContext=this;
		thisActivity=this;
		inflater = LayoutInflater.from(mContext);
        mIntent=getIntent();
        autoInjectAllField();
	}

    protected void autoInjectAllField(){
        try{
            Class<?> cls=this.getClass();
            Field[] fields=cls.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(ViewInject.class)){
                    ViewInject inject=field.getAnnotation(ViewInject.class);
                    int id=inject.value();
                    if(id>0){
                        field.setAccessible(true);
                        field.set(this,this.findViewById(id));
                    }
                }
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }

    }


	
	public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
	
	  public void initImageFetcher(int cacheHeight) {
	        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(thisActivity,
	                Constants.IMAGE_CACHE_DIR);

	        // cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25%
	        // of
	        // app memory
	        // The ImageFetcher takes care of loading images into our ImageView
	        // children asynchronously
	        mImageFetcher = new ImageFetcher(thisActivity, cacheHeight);
	        mImageFetcher.addImageCache(cacheParams);
	    }
	

	protected static void pushView(Context context,
			Class<? extends Activity> activityClass, Bundle bundle) {

		pushView(context, activityClass, bundle, false);

	}

	protected static void pushView(Context context,
			Class<? extends Activity> activityClass, Bundle bundle,
			Boolean clearTask) {

		Intent intent = new Intent(context, activityClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (clearTask) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);

	}
	

	public void pushView(Class<? extends Activity> activityClass,
			Bundle bundle, boolean isAnimator) {
		pushView(thisActivity, activityClass, bundle);
		if (isAnimator) {
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

	public void pushView(Class<? extends Activity> activityClass, Bundle bundle) {
		pushView(activityClass, bundle, true);
	}

    public void pushForResultView(Class<? extends Activity> activityClass,
                                  Bundle bundle, int requestCode) {
        Intent intent = new Intent(thisActivity, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

	public void popView() {
		thisActivity.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	protected void showDialog() {
		if (progressDialog == null) {

			progressDialog = AppUtil.showProgress(this);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
		} else {
			if (progressDialog.isShowing()) {
				return;
			}
			progressDialog.show();
		}

	}

	protected void hideDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

    protected boolean pushbackToHome() {

        return false;
    }

}
