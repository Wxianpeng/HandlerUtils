package com.iseeyou.jun.handlerutils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iseeyou.jun.handlerutils.bean.CenterObj;
import com.iseeyou.jun.handlerutils.listener.IHandlerMessage;
import com.iseeyou.jun.handlerutils.utils.HandlerUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class MainActivity extends AppCompatActivity implements IHandlerMessage {
    public static final String TAG = "MainActivity";
    private TextView tvMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = (TextView) findViewById(R.id.tv_Main);
        HandlerUtils.getInstance().register(TAG, this);
    }

    @Override
    public void handlerMessage(CenterObj obj) {
        if (obj.key.equals(TAG)) {
            tvMain.setText(obj.content);
        }
    }

    @Override
    public void AsynHandlerMessage(CenterObj obj) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandlerUtils.getInstance().unRegister(this);
    }

    public void main(View v) {
        startActivity(new Intent(this, BActivity.class));
    }
}
