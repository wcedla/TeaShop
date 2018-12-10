package com.example.wcedla.selltea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.adapter.AdressBean;
import com.example.wcedla.selltea.adapter.ConfirmToBuyBean;
import com.example.wcedla.selltea.communication.ActivityEventManger;
import com.example.wcedla.selltea.communication.Event;
import com.example.wcedla.selltea.communication.EventBean;
import com.example.wcedla.selltea.communication.EventManager;
import com.example.wcedla.selltea.gson.AdressDetial;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ConfirmToBuyActivity extends AppCompatActivity {

    int count=0;
    float price=0;
    String idStr="";
    LinearLayout adressEmptylayout;
    LinearLayout adressShowLayout;
    TextView buyAdressName;
    TextView buyAdressPhone;
    TextView buyAdressText;
    LinearLayout layoutForAdd;
    TextView submitTotalCount;
    TextView getSubmitTotalPrice;
    TextView billTotalCount;
    TextView billTotalPrice;
    TextView submitToBuy;
    EditText buyMessage;
    public static ActivityEventManger activityEventManger;
    String userName;
    List<AdressDetial> adressDetialList=new ArrayList<>();
    List<AdressBean> adressBeanList=new ArrayList<>();
    List<String> adressInfoList=new ArrayList<>();
    List<String> buyIdList=new ArrayList<>();
    List<String> buyCountList=new ArrayList<>();
    int getIndex=0;
    List<SearchShowDetial> searchShowDetialList=new ArrayList<>();
    List<ConfirmToBuyBean> confirmToBuyBeanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_confirm_to_buy);
        SharedPreferences loginPreference=getSharedPreferences("login",MODE_PRIVATE);
        userName=loginPreference.getString("username","");
        adressEmptylayout=findViewById(R.id.buy_adress_empty);
        adressShowLayout=findViewById(R.id.buy_adress_root);
        buyAdressName=findViewById(R.id.buy_adress_name);
        buyAdressPhone=findViewById(R.id.buy_adress_phone);
        buyAdressText=findViewById(R.id.buy_adress_text);
        layoutForAdd=findViewById(R.id.bill_add_root);
        submitTotalCount=findViewById(R.id.confirm_total_count);
        getSubmitTotalPrice=findViewById(R.id.bill_submit_price);
        billTotalCount=findViewById(R.id.bill_count);
        billTotalPrice=findViewById(R.id.bill_price);
        submitToBuy=findViewById(R.id.bill_submit);
        buyMessage=findViewById(R.id.buy_message);
        getAdressInfoFromServer();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            buyIdList=Arrays.asList(bundle.get("id").toString().split("~"));
            buyCountList=Arrays.asList(bundle.get("count").toString().split("~"));
        }
        getBuyInfo();


        final TextView addAdress=findViewById(R.id.bill_add_adress);
        addAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAdressIntent=new Intent(ConfirmToBuyActivity.this,AdressManageActivity.class);
                startActivity(addAdressIntent);
            }
        });
        submitToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adressInfoList.size()>0&&confirmToBuyBeanList.size()>0)
                {
                    insetNewItem();
                    //String sql = "insert into buybill values('"++"','','','','','','','')";
                }
            }
        });
        activityEventManger = ActivityEventManger.newEventManger(event);

    }

    private void getAdressInfoFromServer()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/AdressServlet?username="+userName;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adressEmptylayout.setVisibility(View.VISIBLE);
                        adressShowLayout.setVisibility(View.GONE);
                        Toast.makeText(ConfirmToBuyActivity.this,"连接服务器异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                adressDetialList.clear();
                adressDetialList=JsonTool.getAdressInfo(responseData);
                if(adressDetialList.size()<1)
                {
                    adressEmptylayout.setVisibility(View.VISIBLE);
                    adressShowLayout.setVisibility(View.GONE);
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
                    adressInfoList.clear();
                    adressInfoList.add(adressBeanList.get(0).getAdressName());
                    adressInfoList.add(adressBeanList.get(0).getAdressPhone());
                    adressInfoList.add(adressBeanList.get(0).getAdressText());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdressInfo(adressInfoList);
                        }
                    });

                }
            }
        });
    }

     Event event=new Event() {
        @Override
        public void whatToDo(EventBean eventBean) {
            switch(eventBean.what)
            {
                case 1:
                    setAdressInfo(eventBean.obj);
                    break;
            }
        }
    };

    private void setAdressInfo(Object adressInfo)
    {
        List<String> adressInfoList=(List<String>) adressInfo;

        adressEmptylayout.setVisibility(View.GONE);
        adressShowLayout.setVisibility(View.VISIBLE);
        buyAdressName.setText(adressInfoList.get(0));
        buyAdressPhone.setText(adressInfoList.get(1));
        buyAdressText.setText(adressInfoList.get(2));
    }

    private void getBuyInfo()
    {

        //Log.d("wcedla", "查看"+buyCountList.size()+","+buyIdList.size());
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id="+buyIdList.get(getIndex);
        getBuyInfoFromServer(url);

    }

    private void getBuyInfoFromServer(String url)
    {
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConfirmToBuyActivity.this,"获取购物信息错误！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                searchShowDetialList=JsonTool.getShowResult(responseData);
                //Log.d("wcedla", "获取正确"+searchShowDetialList.get(0).title);
                if(searchShowDetialList.size()<1)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConfirmToBuyActivity.this,"数据获取异常！",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    for(SearchShowDetial searchShowDetial : searchShowDetialList)
                    {
                        DecimalFormat decimalFormat=new DecimalFormat(".00");
                        String totalPrice=decimalFormat.format(Float.valueOf(buyCountList.get(getIndex))*Float.valueOf(searchShowDetial.nowPrice));
                        ConfirmToBuyBean confirmToBuyBean=new ConfirmToBuyBean(
                                searchShowDetial.goodsId,
                                searchShowDetial.img,
                                searchShowDetial.title,
                                buyCountList.get(getIndex),totalPrice);
                        confirmToBuyBeanList.add(confirmToBuyBean);
                    }
                    getIndex+=1;
                    if(getIndex<buyIdList.size())
                    {
                        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id="+buyIdList.get(getIndex);
                        call.cancel();
                        getBuyInfoFromServer(url);
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBuyLayout();
                            }
                        });

                    }
                }
            }
        });
    }

    private void setBuyLayout()
    {

        for (ConfirmToBuyBean confirmToBuyBean : confirmToBuyBeanList)
        {
            LinearLayout viewForAdd = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bill_item_for_add, layoutForAdd, false);
            ImageView imageView=viewForAdd.findViewById(R.id.confim_to_buy_img);
            TextView title=viewForAdd.findViewById(R.id.confim_to_buy_title);
            TextView buyCount=viewForAdd.findViewById(R.id.confim_to_buy_count);
            TextView totalPrice=viewForAdd.findViewById(R.id.confirm_to_buy_total_price);
            RequestOptions options = new RequestOptions()
                    .override((int)getResources().getDisplayMetrics().density*80, (int)getResources().getDisplayMetrics().density*80)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop();
            Glide.with(this).load(confirmToBuyBean.getImg()).apply(options).into(imageView);
            title.setText(confirmToBuyBean.getTitle());
            buyCount.setText("x"+confirmToBuyBean.getBuyCount());
            count+=Integer.valueOf(confirmToBuyBean.getBuyCount());
            totalPrice.setText("¥"+confirmToBuyBean.getTotalPrice());
            price+=Float.valueOf(confirmToBuyBean.getTotalPrice());
            layoutForAdd.addView(viewForAdd);
            idStr+=confirmToBuyBean.getId()+"~";
        }
        billTotalPrice.setText("¥"+String.valueOf(price));
        billTotalCount.setText("共"+String.valueOf(count)+"件商品，小计:");
        submitTotalCount.setText("共"+String.valueOf(count)+"件商品，合计:");
        getSubmitTotalPrice.setText("¥"+String.valueOf(price));
        Log.d("wcedla", "啥"+idStr);
    }

    private void insetNewItem() {
        String adressNameForAdd=buyAdressName.getText().toString()+"~"+
                buyAdressPhone.getText().toString()+"~"+
                buyAdressText.getText().toString();
        String buyMessageForAdd="未留言";
        if(buyMessage.getText().toString().length()>0)
        {
            buyMessageForAdd=buyMessage.getText().toString();
        }
        String billNoForAdd=String.valueOf(System.currentTimeMillis()*85+2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        String buildTimeForAdd=format.format(currentDate);
        String sqlStr="insert into buybill values('"+adressNameForAdd+"','"+idStr+"','0.0','"+buyMessageForAdd+"','"+billNoForAdd+"','"+buildTimeForAdd+"','0')";
        Log.d("wcedla", "获得:"+sqlStr);
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConfirmToBuyActivity.this,"提交失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                boolean result = JsonTool.getStatus(responseData);
                if(result)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConfirmToBuyActivity.this,"提交成功!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConfirmToBuyActivity.this,"提交失败!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        submitToBuy.performClick();
        super.onDestroy();
    }
}
