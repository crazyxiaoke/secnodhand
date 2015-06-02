package com.app.secnodhand.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

import com.app.secnodhand.R;

/**
 * Created by zxk on 15-6-2.
 */
public class MainMenuPopWindow {
    private static PopupWindow mPopupWindow;
    private Boolean mFocusable=true;
    private Boolean mOutsideTouchable=true;
    private static MainMenuPopWindow mMainMenuPopWindow;


    public static MainMenuPopWindow getInterface(View view,int width,int height){
        mMainMenuPopWindow=new MainMenuPopWindow();
        mPopupWindow=new PopupWindow(view,width,height);
        return mMainMenuPopWindow;
    }

    private void init(){
        mPopupWindow.setAnimationStyle(R.anim.map_bz_anim);
        mPopupWindow.setFocusable(mFocusable);
        mPopupWindow.setOutsideTouchable(mOutsideTouchable);
        mPopupWindow.update();
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void show(View parent,int gravity,int x,int y){
        if(mPopupWindow!=null) {
            if (!mPopupWindow.isShowing()) {
                init();
                mPopupWindow.showAtLocation(parent, gravity, x, y);
            } else {
                mPopupWindow.dismiss();
            }
        }
    }

    public void dismiss(){
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }


}
