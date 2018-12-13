package com.example.wcedla.selltea;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wcedla.selltea.adapter.TeaBillShowAdapter;
import com.example.wcedla.selltea.tool.SystemTool;

import java.util.ArrayList;
import java.util.List;

public class TeaBIllActivity extends AppCompatActivity {

    TabLayout billTab;
    String[] tabTextArray=new String[]{"全部","未付款","待发货","待收货","已完成"};
    RecyclerView billRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_tea_bill);
        billTab=findViewById(R.id.me_bill_tab);
        for(int i=0;i<5;i++)
        {
            billTab.addTab(billTab.newTab(), i);
            TabLayout.Tab tab = billTab.getTabAt(i);
            tab.setText(tabTextArray[i]);
        }
        billRecycler=findViewById(R.id.me_bill_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        billRecycler.setLayoutManager(linearLayoutManager);
        List<String> dataList=new ArrayList<>();
        for(int i=0;i<8;i++)
        {
            dataList.add("i");
        }
        TeaBillShowAdapter teaBillShowAdapter=new TeaBillShowAdapter(TeaBIllActivity.this,dataList,linearLayoutManager);
        billRecycler.setAdapter(teaBillShowAdapter);

    }
}
