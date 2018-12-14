package com.example.wcedla.selltea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.adapter.GoodsDetialImageLoader;
import com.example.wcedla.selltea.gson.GoodsDetialGson;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class GoodsDetialActivity extends AppCompatActivity {

    Banner goodsDetialBanner;
    String goodsId;
    GoodsDetialGson goodsDetialGson;
    List<String> imageList = new ArrayList<>();
    LinearLayout goodsDetialShowRoot;
    LinearLayout goodsDetialHideRoot;
    LinearLayout longImageRoot;
    TextView goodsName;
    TextView nowPrice;
    TextView originCost;
    TextView mailPrice;
    TextView sellCount;
    Button add;
    Button sub;
    EditText buyNumber;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_goods_detial);
        goodsId = getIntent().getExtras().getString("id");
        SharedPreferences loginPreference = getSharedPreferences("login", MODE_PRIVATE);
        userName = loginPreference.getString("username", "");
        getGoodsDetial();
        Toast.makeText(this, "当前货物id" + goodsId, Toast.LENGTH_SHORT).show();
        goodsDetialShowRoot=findViewById(R.id.goods_detial_show_root);
        goodsDetialHideRoot=findViewById(R.id.goods_dedtial_hide_root);
        goodsDetialBanner = findViewById(R.id.goods_detial_banner);
        longImageRoot= findViewById(R.id.long_image_root);
        goodsName=findViewById(R.id.goods_detial_name);
        nowPrice=findViewById(R.id.goods_detial_now_price);
        originCost=findViewById(R.id.good_detial_original_cost);
        mailPrice=findViewById(R.id.goods_detial_mail_price);
        sellCount=findViewById(R.id.goods_detial_sell_count);
        add = findViewById(R.id.goods_number_add);
        sub = findViewById(R.id.goods_number_sub);
        buyNumber= findViewById(R.id.goods_buy_number);
        buyNumberJob();
        NestedScrollView scrollView = findViewById(R.id.goods_detial_scroll);
        TextView buyNow = findViewById(R.id.buy_now);
        TextView addToCar=findViewById(R.id.add_car);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginPreference = getSharedPreferences("login", MODE_PRIVATE);
                boolean isLogin = loginPreference.getBoolean("isLogin", false);
                if (!isLogin) {
                    Intent loginIntent = new Intent(GoodsDetialActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent confireToBuyIntent = new Intent(GoodsDetialActivity.this, ConfirmToBuyActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString("id",goodsId);
//                    bundle.putString("count",buyNumber.getText().toString());
                    bundle.putString("id","1~4");
                    bundle.putString("count","5~3");
                    confireToBuyIntent.putExtras(bundle);
                    startActivity(confireToBuyIntent);
                }
            }
        });
        addToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCarJob();
            }
        });


    }

    public void showBanner(List<String> imagePath) {

        //实例化图片加载器
        GoodsDetialImageLoader bannerImageLoader = new GoodsDetialImageLoader();
        //绑定图片加载器
        goodsDetialBanner.setImageLoader(bannerImageLoader);
        //设置切换时的动画效果
        goodsDetialBanner.setBannerAnimation(Transformer.Default);
        //设置轮播的标题和指示器的位置
        goodsDetialBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置点击监听
        //设置轮播间隔时间
        goodsDetialBanner.setDelayTime(4000);
        //设置是否为自动轮播，默认是true
        goodsDetialBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        goodsDetialBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许滑动切换
        goodsDetialBanner.setViewPagerIsScroll(true);
        //设置轮播标题
        //设置图片源
        goodsDetialBanner.setImages(imagePath);
        //开始轮播
        goodsDetialBanner.start();
    }

    private void getGoodsDetial() {
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/GoodsDetialServlet?id=" + goodsId;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsDetialActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                GoodsDetialGson result = JsonTool.getGoodsDetial(responseData);
                if (result == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodsDetialShowRoot.setVisibility(View.GONE);
                            goodsDetialHideRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(GoodsDetialActivity.this, "该商品不存在!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    goodsDetialGson = result;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodsDetialHideRoot.setVisibility(View.GONE);
                            goodsDetialShowRoot.setVisibility(View.VISIBLE);
                            loadDetial();
                        }
                    });


                }
            }
        });
    }

    private void loadDetial() {

        imageList.add(goodsDetialGson.img1);
        imageList.add(goodsDetialGson.img2);
        imageList.add(goodsDetialGson.img3);
        imageList.add(goodsDetialGson.img4);
        showBanner(imageList);

        String[] longImgs=goodsDetialGson.longDetialImgs.split("~");
        for (int i = 0; i < longImgs.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(layoutParams);
            RequestOptions options = new RequestOptions()
                    .override(1440, 820)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop();
            Glide.with(this).load(longImgs[i]).apply(options).into(imageView);
            longImageRoot.addView(imageView);
        }
        goodsName.setText(goodsDetialGson.goodsName);
        nowPrice.setText("¥"+goodsDetialGson.nowPrice);
        originCost.setText("原价:¥"+goodsDetialGson.originCost);
        originCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mailPrice.setText("邮费:¥"+goodsDetialGson.mailPrice);
        sellCount.setText("销售量:"+goodsDetialGson.sellCount);
    }

    private void addToCarJob()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/BuyCarExistServlet?id="+goodsId;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsDetialActivity.this,"服务器连接失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                int count=0;
                try {
                    JSONObject jsonObject=new JSONObject(responseData);
                    count=Integer.valueOf(jsonObject.getString("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(count==0)
                {
                    call.cancel();
                    addToBuyCar();
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsDetialActivity.this,"商品已经在购物车了，请勿重复添加！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void addToBuyCar()
    {
        String sqlStr="insert into buycar values('"+goodsId+"','"+userName+"','"+buyNumber.getText().toString()+"')";
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
        //Log.d(TAG, "加入购物车字符创"+url);
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsDetialActivity.this,"服务器连接异常！",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GoodsDetialActivity.this,"加入购车成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodsDetialActivity.this,"加入购车失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void buyNumberJob()
    {

        buyNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
                    buyNumber.setFocusable(false);
                    if (buyNumber.getText().toString().length() < 1) {
                        buyNumber.setText("1");

                        return true;
                    }

                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNumber.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(buyNumber.getWindowToken(), 0);
                    }
                }
                if (buyNumber.getText().toString().length() < 1) {
                    buyNumber.setText("1");
                    return;
                }
                int currentNumber = Integer.valueOf(buyNumber.getText().toString());
                if (currentNumber + 1 <= 9999) {
                    currentNumber += 1;
                    buyNumber.setText(String.valueOf(currentNumber));
                } else {
                    buyNumber.setText("9999");
                }
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNumber.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(buyNumber.getWindowToken(), 0);
                    }
                }
                if (buyNumber.getText().toString().length() < 1) {
                    buyNumber.setText("1");
                    return;
                }
                int currentNumber = Integer.valueOf(buyNumber.getText().toString());
                if (currentNumber - 1 >= 1) {
                    currentNumber -= 1;
                    buyNumber.setText(String.valueOf(currentNumber));
                } else {
                    buyNumber.setText("1");
                }
            }
        });
        buyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNumber.setFocusable(true);
                buyNumber.setFocusableInTouchMode(true);
                buyNumber.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(buyNumber, InputMethodManager.SHOW_FORCED);

            }
        });
    }

}
