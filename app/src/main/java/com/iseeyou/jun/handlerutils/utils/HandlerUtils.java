package com.iseeyou.jun.handlerutils.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.iseeyou.jun.handlerutils.bean.CenterObj;
import com.iseeyou.jun.handlerutils.listener.IHandlerMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 单列模式
 * 观察者模式
 * 回调模式
 * Created by ZhangLiJun on 2017/3/21 0021.
 */

public class HandlerUtils {
    //在主线程创建我们Handler的对象
    private Handler mUiHandler;
    //单列模式
    public static HandlerUtils instance;
    //装载注册的对象的集合
    private Map<String, IHandlerMessage> mUiMap;
    //装载注册的对象的集合
    private Map<String, IHandlerMessage> mWorkMap;
    //WorkThread
    private HandlerThread mWorkThread;
    //在工作线程创建我们的Handler对象
    private Handler mWorkHandler;

    private HandlerUtils() {
        //初始化主线程的Handler对象
        mUiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                CenterObj obj = (CenterObj) msg.obj;
                //处理所有注册界面的消息处理
                for (Map.Entry<String, IHandlerMessage> entry : mUiMap.entrySet()) {
                    //回调注册了IHandlerMessage接口的回调方法
                    entry.getValue().handlerMessage(obj);
                }
            }
        };
        //初始化我们的工作线程
        mWorkThread = new HandlerThread("WorkThread");
        mWorkThread.start();
        //初始化工作线程的Handler
        mWorkHandler = new Handler(mWorkThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                CenterObj obj = (CenterObj) msg.obj;
                //处理所有注册界面的消息处理
                for (Map.Entry<String, IHandlerMessage> entry : mWorkMap.entrySet()) {
                    //回调注册了IHandlerMessage接口的回调方法
                    entry.getValue().AsynHandlerMessage(obj);
                }
            }
        };
        //初始化Map对象
        mUiMap = new HashMap<>();
        mWorkMap = new HashMap<>();

    }

    /**
     * 对外公布一个获取HandlerUtils实例的静态方法
     *
     * @return
     */
    public static HandlerUtils getInstance() {
        if (instance == null) {
            synchronized (HandlerUtils.class) {
                if (instance == null) {
                    instance = new HandlerUtils();
                }
            }
        }
        return instance;
    }

    /***
     * 注册是实现了IHandlerMessage接口的对象
     * @param key
     * @param activity
     */
    public void register(String key, IHandlerMessage activity) {
        mUiMap.put(key, activity);
        mWorkMap.put(key, activity);
    }

    /**
     * 取消注册的界面
     *
     * @param activity
     */
    public void unRegister(IHandlerMessage activity) {
        mUiMap.remove(activity);
        mWorkMap.remove(activity);
    }

    /**
     * 传递的对象obj
     * 在我们的主线程执行
     *
     * @param obj
     */
    public void post(CenterObj obj) {
        //创建Message对象包装相关的参数
        Message msg = new Message();
        //给注册了界面发送传递的消息
        for (Map.Entry<String, IHandlerMessage> entry : mUiMap.entrySet()) {
            obj.key = entry.getKey();
            msg.obj = obj;
            msg.what = obj.code;
            mUiHandler.sendMessage(msg);
        }
    }

    /**
     * 异步的
     * 传递的对象obj
     * 在我们的工作线程执行
     *
     * @param obj
     */
    public void postAsyn(CenterObj obj) {
        //创建Message对象包装相关的参数
        Message msg = new Message();
        //给注册了界面发送传递的消息
        for (Map.Entry<String, IHandlerMessage> entry : mWorkMap.entrySet()) {
            obj.key = entry.getKey();
            msg.obj = obj;
            msg.what = obj.code;
            mWorkHandler.sendMessage(msg);
        }
    }


    /***
     * 运用反射调用对象中的方法
     * @param obj 注册的对象
     * @param  centerObj 传递的消息对象
     */
    private void invokeHandlerMessage(Object obj, CenterObj centerObj) {
        try {
            //获取Class类对象的三种方法
            //1.通过类名获取当前类的对象f
            //2.Class.forName的静态方法
            //3.通过对象的getClass()的方法获取类对象
            //得到obj的Class类对象
            Class clazz = obj.getClass();
            //暴力得到指定方法名的方法
            Method m = clazz.getDeclaredMethod("handlerMessage", CenterObj.class);
            //设置可行
            m.setAccessible(true);
            //调用次对象中的方法
            m.invoke(obj, centerObj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    //================>
    //接口的回调
    //反射的使用
    //消息处理具体的处理的两个方式
    //================>
}
