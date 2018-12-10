package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .override(context.getResources().getDisplayMetrics().widthPixels, (int)context.getResources().getDisplayMetrics().density*150)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        //Log.d("aaaa", "大小 "+context.getResources().getDisplayMetrics().widthPixels+","+(int)context.getResources().getDisplayMetrics().density*150);
        Glide.with(context).load((String) path).apply(options).into(imageView);
    }

}
