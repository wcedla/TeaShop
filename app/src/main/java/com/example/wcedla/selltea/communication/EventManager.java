package com.example.wcedla.selltea.communication;

import android.util.Log;

public class EventManager {

    //用于接收实现whattodo后的event
    private static Event mEvent;

    /**
     * 注册事件，把已经实现event接口的event传到类里
     * @param event
     */
    public static  void registerEvent(Event event){
        mEvent = event;
        Log.d("wcedla", "注册事件"+event);
    }

    /**
     * 通过本类，调用之前绑定进来的event的whattodo方法
     * @param eventBean
     */
    public static void callEvent(EventBean eventBean){
        mEvent.whatToDo(eventBean);
        Log.d("wcedla", "call事件"+eventBean.obj);
    }

}
