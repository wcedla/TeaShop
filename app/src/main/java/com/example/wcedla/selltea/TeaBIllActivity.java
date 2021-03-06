package com.example.wcedla.selltea;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wcedla.selltea.adapter.AllBillDetialBean;
import com.example.wcedla.selltea.adapter.AllBillShowBean;
import com.example.wcedla.selltea.adapter.ConfirmToBuyBean;
import com.example.wcedla.selltea.adapter.MyFrameAdapter;
import com.example.wcedla.selltea.adapter.TeaBillShowAdapter;
import com.example.wcedla.selltea.fragment.BuyCarFragment;
import com.example.wcedla.selltea.fragment.MeFragment;
import com.example.wcedla.selltea.gson.AllBillDetial;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TeaBIllActivity extends AppCompatActivity {

    TabLayout billTab;//查看订单的tab滑条布局
    String[] tabTextArray=new String[]{"全部","待付款","待发货","待收货","已完成"};
    RecyclerView billRecycler;//显示订单详情的recycler
    List<AllBillDetial> allBillDetialList=new ArrayList<>();//从订单表查询得到的各个订单的信息集合
    List<AllBillShowBean> allBillShowBeanList=new ArrayList<>();//从查询到的订单信息中提取需要的信息的封装后的bean的集合
    List<String> goodsIdList=new ArrayList<>();//获取每个订单详情中包括的商品id的字符串的集合
    List<String> goodsCountList=new ArrayList<>();//获取每个订单详情中包含的每个商品的数量
    String[] goodsIdArray;//分解每个订单中包含的货物id的数组，用~分割
    String[] goodsCountArray;//分解每个订单中包含货物数量的数组，用~分割
    List<SearchShowDetial> searchShowDetialList=new ArrayList<>();//订单中包含的每个货物的详情信息集合
    //用于存储各个订单中包含的所有货物的信息的bean信息集合，
    // 每次获取新订单时都需要重新new一遍，因为直接clear的话好像会覆盖掉之前的结果直接操作地址的
    List<ConfirmToBuyBean> AllBillGoodsInfoBeanList;
    int getGoodsIdIndex;//获取每个订单中包含的货物id集合的index
    int getSplitIdIndex;//每个订单中包含的货物id分解之后的id的集合，用~分割
    TeaBillShowAdapter teaBillShowAdapter;
    ProgressDialog progressDialog;
    int flag;
    LinearLayout noBilllayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_tea_bill);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            flag=bundle.getInt("flag");
        }
        billTab=findViewById(R.id.me_bill_tab);
        for(int i=0;i<5;i++)
        {

            billTab.addTab(billTab.newTab(), i);
            TabLayout.Tab tab = billTab.getTabAt(i);
            tab.setText(tabTextArray[i]);
            if(i==flag)
            {
                tab.select();
            }
        }
        billTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getOptionByFlag(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        billRecycler=findViewById(R.id.me_bill_recycler);
        noBilllayout=findViewById(R.id.bill_no_bill_layout);
        getOptionByFlag(flag);


    }

    private void getOptionByFlag(int flag)
    {
        allBillShowBeanList.clear();
        progressDialog = new ProgressDialog(TeaBIllActivity.this);
        progressDialog.setTitle("查找订单");
        progressDialog.setMessage("正在查找...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(flag==0)
        {
            getBillInfo();
        }
        else
        {
            getdFlagBillInfo(flag-1);
        }
    }

    /**
     * 获取所有订单信息
     */
    private void getBillInfo()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/AllBillServlet";
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        billRecycler.setVisibility(View.GONE);
                        noBilllayout.setVisibility(View.VISIBLE);
                        Toast.makeText(TeaBIllActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                allBillDetialList=JsonTool.getBillDetial(responseData);
                if(allBillDetialList.size()>0)
                {
                    goodsIdList.clear();//每次重新获取都需要清空一次
                    goodsCountList.clear();
                    //获取每个订单中包含的货物id集合，和数量集合
                    for (AllBillDetial allBillDetial : allBillDetialList) {
                        goodsIdList.add(allBillDetial.buyId);
                        goodsCountList.add(allBillDetial.buyCount);
                    }
                    getGoodsIdIndex = 0;//获取货物id集合的index置0
                    call.cancel();
                    getGoodsIdInfo();//根据货物id集合获取每个货物的信息
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            billRecycler.setVisibility(View.GONE);
                            noBilllayout.setVisibility(View.VISIBLE);
                            Toast.makeText(TeaBIllActivity.this,"获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getdFlagBillInfo(int tabFlag)
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/BillTypeServlet?billflag="+tabFlag;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        billRecycler.setVisibility(View.GONE);
                        noBilllayout.setVisibility(View.VISIBLE);
                        Toast.makeText(TeaBIllActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                allBillDetialList=JsonTool.getBillDetial(responseData);
                if(allBillDetialList.size()>0)
                {
                    goodsIdList.clear();//每次重新获取都需要清空一次
                    goodsCountList.clear();
                    //获取每个订单中包含的货物id集合，和数量集合
                    for (AllBillDetial allBillDetial : allBillDetialList) {
                        goodsIdList.add(allBillDetial.buyId);
                        goodsCountList.add(allBillDetial.buyCount);
                    }
                    getGoodsIdIndex = 0;//获取货物id集合的index置0
                    call.cancel();
                    getGoodsIdInfo();//根据货物id集合获取每个货物的信息
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            billRecycler.setVisibility(View.GONE);
                            noBilllayout.setVisibility(View.VISIBLE);
                            Toast.makeText(TeaBIllActivity.this,"没有订单！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 分解每个货物id集合的id并获取货物信息
     */
    private void getGoodsIdInfo()
    {
        AllBillGoodsInfoBeanList=new ArrayList<>();//每获取完一个订单的所有货物的信息之后需要重新实例化一个对象。
        goodsIdArray=goodsIdList.get(getGoodsIdIndex).split("~");//每个订单包含的货物id集合
        goodsCountArray=goodsIdList.get(getGoodsIdIndex).split("~");//每个订单包含的货物数量的集合
        getSplitIdIndex=0;//订单所有分解后的货物id索引
        getGoodsInfo();//根据id获取每个货物的信息

    }

    /**
     * 根据id获取每个货物的信息
     */
    private void getGoodsInfo()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id="+goodsIdArray[getSplitIdIndex];
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        billRecycler.setVisibility(View.GONE);
                        noBilllayout.setVisibility(View.VISIBLE);
                        Toast.makeText(TeaBIllActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
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
                        ConfirmToBuyBean allBillGoodsDetialBean=new ConfirmToBuyBean(
                                searchShowDetial.goodsId,
                                searchShowDetial.img,
                                searchShowDetial.title,
                                goodsCountArray[getSplitIdIndex],totalPrice);
                        AllBillGoodsInfoBeanList.add(allBillGoodsDetialBean);
                    }

                    getSplitIdIndex+=1;//订单货物id集合分解后的index+1；

                    if(getSplitIdIndex<goodsIdArray.length)//如果还有货物id没有获取信息
                    {

                        call.cancel();//取消当前call
                        getGoodsInfo();//继续重新执行本方法，重新货物货物信息
                    }
                    else//如果订单的所有货物id信息已经获取完毕
                    {
                        //显示在订单中的封装bean类型
                        AllBillShowBean allBillShowBean=new AllBillShowBean(allBillDetialList.get(getGoodsIdIndex).buyNo,
                                allBillDetialList.get(getGoodsIdIndex).buyFlag,AllBillGoodsInfoBeanList);
                        allBillShowBeanList.add(allBillShowBean);//订单显示的bean的集合
                        //Log.d("wcedla", "查看订单货物信息"+AllBillGoodsInfoBeanList.size()+","+AllBillGoodsInfoBeanList.get(0).getTitle());
                        //handler.sendEmptyMessage(1);
                        getGoodsIdIndex+=1;//订单中包含的货物集合的索引+1

                        if(getGoodsIdIndex<goodsIdList.size())//如果还有订单没处理完成
                        {
                            call.cancel();
                            //AllBillGoodsInfoBeanList.clear();
                            getGoodsIdInfo();//重新获取下一个订单的货物信息
                        }
                        else//如果所有订单处理完成了，就开始处理订单信息
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    setBillShowAdapter();
                                }
                            });
                        }

                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            billRecycler.setVisibility(View.GONE);
                            noBilllayout.setVisibility(View.VISIBLE);
                            Toast.makeText(TeaBIllActivity.this,"获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setBillShowAdapter()
    {
        billRecycler.setVisibility(View.VISIBLE);
        noBilllayout.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        billRecycler.setLayoutManager(linearLayoutManager);
        teaBillShowAdapter=new TeaBillShowAdapter(TeaBIllActivity.this,allBillShowBeanList,linearLayoutManager,billOptions);
        billRecycler.setAdapter(teaBillShowAdapter);

    }

    BillOptions billOptions=new BillOptions() {
        @Override
        public void cancel(final String billNo, final int position) {

            final AlertDialog.Builder alertDialog=new AlertDialog.Builder(TeaBIllActivity.this);
            alertDialog.setTitle("删除警告！");
            alertDialog.setMessage("确定删除吗？");
            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog = new ProgressDialog(TeaBIllActivity.this);
                    progressDialog.setTitle("取消订单");
                    progressDialog.setMessage("正在取消...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String sqlStr="delete from buybill where billno='"+billNo+"'";
                    String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                    Log.d("wcedla", "取消地址"+url);
                    HttpTool.doHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    Toast.makeText(TeaBIllActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
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
                                        progressDialog.cancel();
                                        allBillShowBeanList.remove(position);
                                        teaBillShowAdapter.setNewData(allBillShowBeanList);
                                        Toast.makeText(TeaBIllActivity.this,"操作成功！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.cancel();
                                        Toast.makeText(TeaBIllActivity.this,"操作失败！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
            alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();


        }

        @Override
        public void pay(String billNo,int position) {

        }
    };

    public interface BillOptions
    {
        public void cancel(String billNo,int position);

        public void pay(String billNo,int position);
    }

}
