package com.iseeyou.jun.handlerutils.listener;

import com.iseeyou.jun.handlerutils.bean.CenterObj;

/**
 * 界面消息处理的回调接口
 * Created by ZhangLiJun on 2017/3/21 0021.
 */
public interface IHandlerMessage {
    public void handlerMessage(CenterObj obj);
    public void AsynHandlerMessage(CenterObj obj);
}