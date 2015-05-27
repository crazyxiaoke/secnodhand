package com.app.secnodhand.activity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.secnodhand.R;
import com.app.secnodhand.base.BaseActivity;
import com.app.secnodhand.base.ViewInject;

/**
 * Created by zxk on 15-5-26.
 */
public class MainActivity extends BaseActivity{

    @ViewInject(R.id.btn1)
    private Button btn1;
    @ViewInject(R.id.text)
    private TextView text;

    @Override
    protected int getLayoutId() {
        return R.layout.main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context mContext=this;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TelephonyManager tm=(TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
                text.setText(tm.getLine1Number());
            }
        });
    }
}
