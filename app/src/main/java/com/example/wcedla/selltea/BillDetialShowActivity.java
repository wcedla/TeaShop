package com.example.wcedla.selltea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.adapter.AllBillDetialBean;
import com.example.wcedla.selltea.adapter.AllBillShowBean;
import com.example.wcedla.selltea.adapter.ConfirmToBuyBean;
import com.example.wcedla.selltea.gson.AllBillDetial;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BillDetialShowActivity extends AppCompatActivity {

    private static final String TAG = "wcedla" ;
    String billNoData;
    LinearLayout billDetialLayoutForAdd;
    List<AllBillDetial> allBillDetialList=new ArrayList<>();//从订单表查询得到的各个订单的信息集合
    String[] goodsIdArray;//分解每个订单中包含的货物id的数组，用~分割
    String[] goodsCountArray;//分解每个订单中包含货物数量的数组，用~分割
    int getSplitIdIndex;
    List<SearchShowDetial> searchShowDetialList=new ArrayList<>();//订单中包含的每个货物的详情信息集合
    List<ConfirmToBuyBean> billAllGoodsInfoList=new ArrayList<>();//存储货物信息集合
    List<AllBillDetialBean> allBillDetialBeanList=new ArrayList<>();//封装好的数据的集合

    LinearLayout billMailLayout;
    TextView billTotalPrice;
    TextView billStatus;
    TextView billMailstatus;
    TextView billAdressName;
    TextView billAdressPhone;
    TextView billAdressText;
    TextView billInfoMessage;
    TextView billInfoBillNo;
    TextView getBillInfoBuyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_bill_detial_show);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            billNoData=bundle.getString("billNo");
            Log.d(TAG, "onCreate: "+billNoData);
        }
        billDetialLayoutForAdd=findViewById(R.id.bill_detial_show_for_add);
        billTotalPrice=findViewById(R.id.bill_show_total_price);
        billStatus=findViewById(R.id.bill_show_bill_status);
        billMailstatus=findViewById(R.id.bill_show_mail_status);
        billMailLayout=findViewById(R.id.bill_mail_layout);
        billAdressName=findViewById(R.id.bill_show_name);
        billAdressPhone=findViewById(R.id.bill_show_adress_phone);
        billAdressText=findViewById(R.id.bill_show_adress_text);
        billInfoMessage=findViewById(R.id.bill_info_message);
        billInfoBillNo=findViewById(R.id.bill_info_bill_no);
        getBillInfoBuyTime=findViewById(R.id.bill_info_buy_time);
        getBillDetial();
    }


    private void getBillDetial()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/SingleBillServlet?billno="+billNoData;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BillDetialShowActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                allBillDetialList=JsonTool.getBillDetial(responseData);
                if(allBillDetialList.size()>0)
                {
                    goodsIdArray=allBillDetialList.get(0).buyId.split("~");
                    goodsCountArray=allBillDetialList.get(0).buyCount.split("~");
                    call.cancel();
                    getSplitIdIndex=0;//订单所有分解后的货物id索引
                    billAllGoodsInfoList.clear();
                    getGoodsInfo();//根据id获取每个货物的信息
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BillDetialShowActivity.this,"获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getGoodsInfo()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id="+goodsIdArray[getSplitIdIndex];
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BillDetialShowActivity.this,"服务器连接失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                searchShowDetialList=JsonTool.getShowResult(responseData);
                if(searchShowDetialList.size()>0)
                {
                    for(SearchShowDetial searchShowDetial : searchShowDetialList)
                    {
                        DecimalFormat decimalFormat=new DecimalFormat(".00");
                        String totalPrice=decimalFormat.format(Float.valueOf(goodsCountArray[getSplitIdIndex])*Float.valueOf(searchShowDetial.nowPrice));
                        ConfirmToBuyBean goodsDetialBean=new ConfirmToBuyBean(
                                searchShowDetial.goodsId,
                                searchShowDetial.img,
                                searchShowDetial.title,
                                goodsCountArray[getSplitIdIndex],totalPrice);
                        billAllGoodsInfoList.add(goodsDetialBean);
                    }

                    getSplitIdIndex+=1;//订单货物id集合分解后的index+1；

                    if(getSplitIdIndex<goodsIdArray.length)//如果还有货物id没有获取信息
                    {

                        call.cancel();//取消当前call
                        getGoodsInfo();//继续重新执行本方法，重新货物货物信息
                    }
                    else//如果订单的所有货物id信息已经获取完毕
                    {
                        AllBillDetialBean allBillDetialBean=new AllBillDetialBean(
                                allBillDetialList.get(0).billId,
                                allBillDetialList.get(0).billAdress,
                                allBillDetialList.get(0).buyId,
                                allBillDetialList.get(0).buyCount,
                                allBillDetialList.get(0).mailPrice,
                                allBillDetialList.get(0).buyMessage,
                                allBillDetialList.get(0).buyNo,
                                allBillDetialList.get(0).buyDate,
                                allBillDetialList.get(0).buyFlag,
                                billAllGoodsInfoList);
                        allBillDetialBeanList.add(allBillDetialBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BillDetialShowActivity.this,"获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setData()
    {
        float price=0;
        List<ConfirmToBuyBean> dataBeanList=allBillDetialBeanList.get(0).getGoodsBeanList();
        AllBillDetialBean detialBean=allBillDetialBeanList.get(0);
        for(int i=0;i<dataBeanList.size();i++)
        {
            View viewForAdd=LayoutInflater.from(this).inflate(R.layout.bill_item_for_add,billDetialLayoutForAdd,false);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewForAdd.getLayoutParams();
            layoutParams.topMargin = (int)getResources().getDisplayMetrics().density * 10;
            viewForAdd.setLayoutParams(layoutParams);

            ImageView imageView=viewForAdd.findViewById(R.id.confim_to_buy_img);
            TextView title=viewForAdd.findViewById(R.id.confim_to_buy_title);
            TextView buyCount=viewForAdd.findViewById(R.id.confim_to_buy_count);
            TextView totalPrice=viewForAdd.findViewById(R.id.confirm_to_buy_total_price);
            RequestOptions options = new RequestOptions()
                    .override((int)getResources().getDisplayMetrics().density*80, (int)getResources().getDisplayMetrics().density*80)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop();
            Glide.with(this).load(dataBeanList.get(i).getImg()).apply(options).into(imageView);
            title.setText(dataBeanList.get(i).getTitle());
            buyCount.setText("x"+dataBeanList.get(i).getBuyCount());
            totalPrice.setText("¥"+dataBeanList.get(i).getTotalPrice());
            price+=Float.valueOf(dataBeanList.get(i).getTotalPrice());
            billDetialLayoutForAdd.addView(viewForAdd);
        }
        billTotalPrice.setText("¥"+price);
        billStatus.setText("订单状态："+getBuyFlagText(detialBean.getBuyFlag()));
        if(getBuyFlagText(detialBean.getBuyFlag()).equals("待付款"))
        {
            billMailLayout.setVisibility(View.GONE);
            billMailstatus.setText("暂未付款");
        }
        else
        {
            billMailLayout.setVisibility(View.VISIBLE);
            billMailstatus.setText("等待商家上传物流信息");
        }
        String[] adressInfoArray=detialBean.getBillAdress().split("~");
        billAdressName.setText(adressInfoArray[0]);
        billAdressPhone.setText(adressInfoArray[1]);
        billAdressText.setText(adressInfoArray[2]);
        billInfoMessage.setText(detialBean.getBuyMessage());
        billInfoBillNo.setText(detialBean.getBuyNo());
        getBillInfoBuyTime.setText(detialBean.getBuyDate());


    }

    private String getBuyFlagText(String status) {
        if (status.equals("0")) {
            return "待付款";
        } else if (status.equals("1")) {
            return "待发货";
        } else if (status.equals("2")) {
            return "待收货";
        } else if (status.equals("3")) {
            return "已完成";
        }
        return "未知状态";

    }
}
