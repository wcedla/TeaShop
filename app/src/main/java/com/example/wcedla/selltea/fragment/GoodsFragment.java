package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.MainActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.SearchActivity;
import com.example.wcedla.selltea.ShowSearchWindowActivity;
import com.example.wcedla.selltea.adapter.BannerImageLoader;
import com.example.wcedla.selltea.adapter.SortGridAdapter;
import com.example.wcedla.selltea.adapter.SortGridBean;
import com.example.wcedla.selltea.database.BannerHotImageTable;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class GoodsFragment extends Fragment {

    Banner banner;
    Activity myActivity;
    View view;
    List<String> imagePath=new ArrayList<>();
    List<String> imageTitle=new ArrayList<>();
    Integer[] backgroundArray = new Integer[]{R.drawable.sort_grid_one, R.drawable.sort_grid_two, R.drawable.sort_grid_three, R.drawable.sort_grid_four, R.drawable.sort_grid_five, R.drawable.sort_grid_six, R.drawable.sort_grid_seven, R.drawable.sort_grid_eight};
    Integer[] imageArray = new Integer[]{R.drawable.grid_leaf_1, R.drawable.grid_leaf_2, R.drawable.grid_leaf_3, R.drawable.grid_leaf_4, R.drawable.grid_leaf_5, R.drawable.grid_leaf_6, R.drawable.grid_leaf_7, R.drawable.grid_leaf_8};
    String[] titleArray = new String[]{"普洱", "铁观音", "红茶", "绿茶", "乌龙", "大红袍", "丁香叶", "茉莉花"};

    @Override
    public void onAttach(Context context) {
        myActivity = (Activity) context;
        super.onAttach(context);
    }

    public static GoodsFragment newInstance() {
        GoodsFragment goodsFragment = new GoodsFragment();
        return goodsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.frame_main_layout, container, false);
        final TextView searchTextview = view.findViewById(R.id.search_editor);
        searchTextview.setFocusable(false);
        searchTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent=new Intent(myActivity,ShowSearchWindowActivity.class);
                startActivity(searchIntent);
            }
        });
        GridView sortGridView = view.findViewById(R.id.sort_grid);
        List<SortGridBean> sortGridBeanList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            SortGridBean sortGridBean = new SortGridBean(imageArray[i], titleArray[i], backgroundArray[i]);
            sortGridBeanList.add(sortGridBean);
        }
        SortGridAdapter sortGridAdapter = new SortGridAdapter(myActivity, sortGridBeanList);
        sortGridView.setAdapter(sortGridAdapter);
        sortGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent showSearchIntent = new Intent(myActivity, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", titleArray[position]);
                showSearchIntent.putExtras(bundle);
                startActivity(showSearchIntent);
            }
        });
        showBanner();
        //初始化数据
        return view;
    }


    public void showBanner() {

        List<BannerHotImageTable> bannerHotImageTableList= LitePal.findAll(BannerHotImageTable.class);
        imagePath.clear();
        imageTitle.clear();
        for(BannerHotImageTable bannerHotImageTable : bannerHotImageTableList)
        {
            imagePath.add(bannerHotImageTable.getImgResource());
            imageTitle.add(bannerHotImageTable.getTitle());
        }

        //获得banner实例
        banner = view.findViewById(R.id.banner);
        //实例化图片加载器
        BannerImageLoader bannerImageLoader = new BannerImageLoader();
        //绑定图片加载器
        banner.setImageLoader(bannerImageLoader);
        //设置切换时的动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播的标题和指示器的位置
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置点击监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(final int position) {
                Intent showSearchIntent = new Intent(myActivity, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", imageTitle.get(position));
                showSearchIntent.putExtras(bundle);
                startActivity(showSearchIntent);
            }
        });
        //设置轮播间隔时间
        banner.setDelayTime(4000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许滑动切换
        banner.setViewPagerIsScroll(true);
        //设置轮播标题
        banner.setBannerTitles(imageTitle);
        //设置图片源
        banner.setImages(imagePath);
        //Log.d(TAG, "空" + imagePath.size());
        //开始轮播
        banner.start();
    }


}
