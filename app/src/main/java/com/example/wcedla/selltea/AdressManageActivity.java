package com.example.wcedla.selltea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.adapter.AdressBean;
import com.example.wcedla.selltea.adapter.AdressItemAdapter;
import com.example.wcedla.selltea.communication.ActivityEventManger;
import com.example.wcedla.selltea.communication.Event;
import com.example.wcedla.selltea.communication.EventBean;
import com.example.wcedla.selltea.communication.EventManager;
import com.example.wcedla.selltea.gson.AdressDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdressManageActivity extends AppCompatActivity {

    LinearLayout adressNone;
    NestedScrollView adressShow;
    RecyclerView adressRecycler;
    String userName;
    TextView addAdress;
   // List<String> imgList = new ArrayList<>();
    List<AdressDetial> adressDetialList=new ArrayList<>();
    List<AdressBean> adressBeanList=new ArrayList<>();
    public static ActivityEventManger activityEventManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_adress_manage);
        SharedPreferences loginPreference=getSharedPreferences("login",MODE_PRIVATE);
        userName=loginPreference.getString("username","");
        adressNone=findViewById(R.id.adress_none);
        adressShow=findViewById(R.id.adress_manage_show);
        adressRecycler = findViewById(R.id.address_recycler);
        addAdress=findViewById(R.id.adress_manage_add);
        addAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAdressIntent=new Intent(AdressManageActivity.this,AdressEditActivity.class);
                startActivity(addAdressIntent);
            }
        });
        getAdressData();
        //EventManager.registerEvent(event);
        activityEventManger=ActivityEventManger.newEventManger(event);
    }

    private void getAdressData()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/AdressServlet?username="+userName;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adressShow.setVisibility(View.GONE);
                        adressNone.setVisibility(View.VISIBLE);
                        Toast.makeText(AdressManageActivity.this,"数据获取失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                adressDetialList.clear();
                adressDetialList=JsonTool.getAdressInfo(responseData);
                //Log.d("wcela", "回调查看"+adressDetialList.get(0).adressName);
                if(adressDetialList.size()<1)
                {
                    Log.d("wcedla", "地址信息为空");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adressShow.setVisibility(View.GONE);
                            adressNone.setVisibility(View.VISIBLE);
                        }
                    });

                }
                else
                {
                    adressBeanList.clear();
                    for(AdressDetial adressDetial:adressDetialList)
                    {
                        AdressBean adressBean=new AdressBean(adressDetial.id,adressDetial.adressName,
                                adressDetial.adressPhone,
                                adressDetial.adressText);
                        adressBeanList.add(adressBean);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adressShow.setVisibility(View.VISIBLE);
                            adressNone.setVisibility(View.GONE);
                            setAdapterData();
                        }
                    });

                }
            }
        });
    }

    private void setAdapterData()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AdressItemAdapter adressItemAdapter = new AdressItemAdapter(this, adressBeanList, linearLayoutManager);
        adressRecycler.setLayoutManager(linearLayoutManager);
        adressRecycler.setAdapter(adressItemAdapter);
    }

    Event event=new Event() {
        @Override
        public void whatToDo(EventBean eventBean) {
            switch (eventBean.what)
            {
                case 1:
                    //Log.d("wcedla", "地址编辑call保存"+((List<String>)eventBean.obj).get(2));
                    getAdressData();
                    break;
            }

        }
    };


}
