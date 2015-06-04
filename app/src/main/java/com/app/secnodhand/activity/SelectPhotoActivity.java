package com.app.secnodhand.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.app.secnodhand.R;
import com.app.secnodhand.adapter.SelectPhotoAdapter;
import com.app.secnodhand.base.BaseActivity;
import com.app.secnodhand.base.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxk on 15-6-4.
 */
public class SelectPhotoActivity extends BaseActivity{
    @ViewInject(R.id.selectphoto_gridview)
    private GridView mGridView;
    private SelectPhotoAdapter mAdapter;

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            mGridView.setAdapter(mAdapter);
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.selectphoto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        initImageFetcher(180);
        getImages();
    }

    private void getImages(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(mContext,"未发现SD卡",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage=null;
                List<Map<String,String>> mList=new ArrayList<Map<String,String>>();
                Uri mImageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver=mContext.getContentResolver();
                Cursor mCursor=mContentResolver.query(mImageUri,null,MediaStore.Images.Media.MIME_TYPE+"=? or "+MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/jpeg","image/png"},MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG","mCursor.getCount()="+mCursor.getCount()+"");
                while(mCursor.moveToNext()){
                    String path=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("path",path);
                    mList.add(map);
                    Log.e("TAG","path="+path);
                    if(firstImage==null){
                        firstImage=path;
                    }
                    File parentFile=new File(path).getParentFile();
                    if(parentFile==null)
                        continue;
                    String dirPath=parentFile.getAbsolutePath();
                    Log.e("TAG","dirPath="+dirPath);
                }
                Log.e("TAG","mList.size()="+mList.size());
                mAdapter=new SelectPhotoAdapter(mImageFetcher,mList);
//                mGridView.setAdapter(mAdapter);
                Log.e("TAG","mAdapter="+mAdapter.getCount());
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }
}
