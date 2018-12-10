package com.example.wcedla.selltea;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.adapter.MyFrameAdapter;
import com.example.wcedla.selltea.fragment.BuyCarFragment;
import com.example.wcedla.selltea.fragment.GoodsFragment;
import com.example.wcedla.selltea.fragment.LoginFragment;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wcedla";
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] tabTitle = new String[]{"首页", "商品分类", "购物车", "我的"};
    int[] tab_drawable = new int[]{R.drawable.tab_home_selecter, R.drawable.tab_type_selecter, R.drawable.tab_car_selecter, R.drawable.tab_me_selecter};
    MyHandler handler = new MyHandler(this);
    MyFrameAdapter myFrameAdapter;
    public static Context context;
    boolean isLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBannerData();
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        final Toolbar toolbar = findViewById(R.id.login_bar);
        final TextView barTitle = findViewById(R.id.bar_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsRelative(toolbar.getContentInsetStartWithNavigation(), toolbar.getContentInsetStartWithNavigation());
        viewPager = findViewById(R.id.goods_viewpager);
        tabLayout = findViewById(R.id.goods_tab);
        SharedPreferences loginPreference=getSharedPreferences("login",MODE_PRIVATE);
        isLogin=loginPreference.getBoolean("isLogin",false);
        if(isLogin)
        {
            for (int i = 0; i < 4; i++) {
                GoodsFragment goodsFragment = GoodsFragment.newInstance();
                if (i == 2) {
                    fragmentList.add(BuyCarFragment.newInstance());
                } else
                    fragmentList.add(goodsFragment);
            }
        }
        else
        {
            for (int i = 0; i < 4; i++) {
                GoodsFragment goodsFragment = GoodsFragment.newInstance();
                if (i == 2||i==3) {
                    fragmentList.add(LoginFragment.newInstance(5));
                } else
                    fragmentList.add(goodsFragment);
            }
        }

        myFrameAdapter = new MyFrameAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myFrameAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        for (int i = 0; i < tab_drawable.length; i++) {
            tabLayout.addTab(tabLayout.newTab(), i);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(R.layout.tab_item_layout);
            //这里是初始化时，默认item0被选中，setSelected（true）是为了给图片和文字设置选中效果
            if (i == 0) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_img)).setSelected(true);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setSelected(true);
            }
            ImageView imageView = tab.getCustomView().findViewById(R.id.tab_img);
            TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
            imageView.setImageResource(tab_drawable[i]);
            textView.setText(tabTitle[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                tab.getCustomView().findViewById(R.id.tab_img).setSelected(true);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_img).setSelected(false);
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.select();
                if (i == 2) {
                    toolbar.setVisibility(View.VISIBLE);
                    barTitle.setText("购物车");
                } else if (i == 3) {
                    barTitle.setText("我的");
                } else {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                        toolbar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }


    private void initBannerData() {
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/HotServlet";
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                int result=JsonTool.getHotImage(responseData);
                if(result<1)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "数据获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<MainActivity> mOuter;

        public MyHandler(MainActivity activity) {
            mOuter = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final MainActivity outer = mOuter.get();
            if (outer != null) {
                switch (msg.what) {
                    case 1:
                        outer.fragmentList.remove(2);
                        outer.fragmentList.add(2, BuyCarFragment.newInstance());
                        outer.myFrameAdapter = new MyFrameAdapter(outer.getSupportFragmentManager(), outer.fragmentList);
                        outer.viewPager.setAdapter(outer.myFrameAdapter);
                        outer.viewPager.setCurrentItem(0);
                        break;

                }
            }
        }
    }
}
