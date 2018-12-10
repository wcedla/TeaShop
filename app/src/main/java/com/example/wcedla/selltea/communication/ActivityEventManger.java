package com.example.wcedla.selltea.communication;

import android.util.Log;

public class ActivityEventManger {

    public static ActivityEventManger newEventManger(Event event)
    {
        ActivityEventManger activityEventManger=new ActivityEventManger(event);
        return activityEventManger;
    }

    private ActivityEventManger(Event event)
    {
        this.mEvent=event;
    }

    //用于接收实现whattodo后的event
    private Event mEvent;


    /**
     * 通过本类，调用之前绑定进来的event的whattodo方法
     * @param eventBean
     */
    public void callEvent(EventBean eventBean){
        mEvent.whatToDo(eventBean);
       // Log.d("wcedla", "call事件"+eventBean.obj);
    }
}


