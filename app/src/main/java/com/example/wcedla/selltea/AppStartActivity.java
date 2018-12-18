package com.example.wcedla.selltea;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class AppStartActivity extends AppCompatActivity {

    TextView timeText;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_app_start);
        timeText=findViewById(R.id.time_text);
        time=3;
        final Timer timer=new Timer();

        final TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time-=1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeText.setText("跳过:"+time+"s");
                                if(time==0)
                                {
                                    timer.cancel();
                                    Intent startIntent=new Intent(AppStartActivity.this,MainActivity.class);
                                    startActivity(startIntent);
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        };
        timer.schedule(timerTask,1000,1000);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent startIntent=new Intent(AppStartActivity.this,MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
