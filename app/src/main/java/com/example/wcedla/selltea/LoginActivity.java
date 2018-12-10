package com.example.wcedla.selltea;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.wcedla.selltea.tool.SystemTool;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_login);


    }

    public static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<LoginActivity> mOuter;

        public MyHandler(LoginActivity activity) {
            mOuter = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final LoginActivity outer = mOuter.get();
            if (outer != null) {
                switch (msg.what)
                {
                    case 1:
                        Log.d("wcedla", "登录活动收到消息"+(MainActivity) MainActivity.context);
                        MainActivity.MyHandler handler = new MainActivity.MyHandler((MainActivity) MainActivity.context);
                        handler.sendEmptyMessage(1);
                        outer.finish();
                        break;
                }
            }
        }
    }
}
