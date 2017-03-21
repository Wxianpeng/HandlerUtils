package com.iseeyou.jun.handlerutils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iseeyou.jun.handlerutils.bean.CenterObj;
import com.iseeyou.jun.handlerutils.utils.HandlerUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class BActivity extends AppCompatActivity {
    public static final String TAG = "BActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }

    public void post(View v) {
        CenterObj obj = new CenterObj();
        obj.content = "正在下载...";
        obj.key = TAG;
//        HandlerUtils.getInstance().post(obj);
        HandlerUtils.getInstance().postAsyn(obj);
    }
}
