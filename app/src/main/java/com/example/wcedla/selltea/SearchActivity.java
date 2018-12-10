package com.example.wcedla.selltea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.adapter.GoodSShowAdapter;
import com.example.wcedla.selltea.adapter.GoodsShowBean;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    List<SearchShowDetial> searchShowDetialList=new ArrayList<>();
    String teaName;
    RecyclerView goodsRecyclerView;
    LinearLayout noneLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_search);
        Bundle dataBundle=getIntent().getExtras();
        teaName=(String)dataBundle.get("name");
        //Toast.makeText(SearchActivity.this,teaName,Toast.LENGTH_SHORT).show();
        TextView searchText=findViewById(R.id.goods_search);
        searchText.setText(teaName);
        goodsRecyclerView=findViewById(R.id.goods_recycler_view);
        noneLinearLayout=findViewById(R.id.goods_result_none);
        getSearchResult();
        TextView searchTextView=findViewById(R.id.goods_search);
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent=new Intent(SearchActivity.this,ShowSearchWindowActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    private void setRecyclerData()
    {
        List<GoodsShowBean> goodsShowBeanList=new ArrayList<>();
        for(SearchShowDetial searchShowDetial : searchShowDetialList)
        {
            GoodsShowBean goodsShowBean=new GoodsShowBean(
                    searchShowDetial.goodsId,
                    searchShowDetial.img,
                    searchShowDetial.title,
                    searchShowDetial.nowPrice,
                    searchShowDetial.originCost);
            goodsShowBeanList.add(goodsShowBean);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        goodsRecyclerView.setLayoutManager(gridLayoutManager);
        GoodSShowAdapter adapter=new GoodSShowAdapter(this,goodsShowBeanList,gridLayoutManager);
        goodsRecyclerView.setAdapter(adapter);
    }

    private void getSearchResult()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/SearchShowServlet?name="+teaName;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"服务器连接失败!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                searchShowDetialList=JsonTool.getShowResult(responseData);
                if(searchShowDetialList.size()<1)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noneLinearLayout.setVisibility(View.VISIBLE);
                            goodsRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(SearchActivity.this,"没有搜索到商品",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noneLinearLayout.setVisibility(View.GONE);
                            goodsRecyclerView.setVisibility(View.VISIBLE);
                            setRecyclerData();
                        }
                    });
                }

            }
        });
    }
}
