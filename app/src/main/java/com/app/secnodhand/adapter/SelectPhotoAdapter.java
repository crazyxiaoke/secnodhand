package com.app.secnodhand.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.secnodhand.R;
import com.app.secnodhand.base.MyBaseAdapter;
import com.app.secnodhand.imageutil.ImageCache;
import com.app.secnodhand.imageutil.ImageFetcher;
import com.app.secnodhand.util.MyBitmapUtil;

import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

/**
 * Created by zxk on 15-6-4.
 */
public class SelectPhotoAdapter extends MyBaseAdapter<Map<String,String>>{
    private ImageFetcher mImageFetcher;
    public SelectPhotoAdapter(ImageFetcher mImageFetcher,List<Map<String,String>> dataList){
        super(dataList);
        this.mImageFetcher=mImageFetcher;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.selectphoto_item,null);
            mViewHolder=new ViewHolder();
            mViewHolder.mImageView=(ImageView)convertView.findViewById(R.id.selectphoto_item_img);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        String path=dataList.get(position).get("path");
        try{
            FileInputStream fis=new FileInputStream(path);
            Bitmap mBitmap=  BitmapFactory.decodeByteArray(MyBitmapUtil.decodeBitmap(path), 0, MyBitmapUtil.decodeBitmap(path).length);
//            imageCache.put(path, new SoftReference<Bitmap>(mBitmap));
            mViewHolder.mImageView.setImageBitmap(mBitmap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        ImageView mImageView;
    }
}
