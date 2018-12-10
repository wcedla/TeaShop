package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.GoodsDetialActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.SearchActivity;

import org.w3c.dom.Text;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class GoodSShowAdapter extends RecyclerView.Adapter<GoodSShowAdapter.ViewHolder> {

    View view;
    Context context;
    List<GoodsShowBean> goodsShowBeanList;
    RecyclerView.LayoutManager layoutManager;

    public GoodSShowAdapter(Context context,List<GoodsShowBean> goodsShowBeanList, RecyclerView.LayoutManager layoutManager)
    {
        this.context=context;
        this.goodsShowBeanList=goodsShowBeanList;
        this.layoutManager=layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_recyclerview_layout,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "货物id"+viewHolder.goodsId.getText().toString());
                Intent goodsDetialIntent=new Intent(context,GoodsDetialActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",goodsShowBeanList.get(viewHolder.getAdapterPosition()).getGoodsId());
                goodsDetialIntent.putExtras(bundle);
                context.startActivity(goodsDetialIntent);
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams=(ViewGroup.MarginLayoutParams) view.getLayoutParams();
        ViewGroup.LayoutParams parm = viewHolder.goodsPicture.getLayoutParams();
        parm.width=context.getResources().getDisplayMetrics().widthPixels/2-marginLayoutParams.getMarginEnd()-marginLayoutParams.getMarginStart();//-(int)context.getResources().getDisplayMetrics().density*10;;
        parm.height=parm.width;
        RequestOptions options = new RequestOptions()
                .override(parm.width,parm.height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        Glide.with(view).load(goodsShowBeanList.get(i).getGoodsImg()).apply(options).into(viewHolder.goodsPicture);
        viewHolder.originalCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.goodsId.setText(goodsShowBeanList.get(i).getGoodsId());
        viewHolder.goodsDescription.setText(goodsShowBeanList.get(i).getGoodsTitle());
        viewHolder.originalCost.setText("¥"+goodsShowBeanList.get(i).getOriginCost());
        viewHolder.currentPrice.setText("¥"+goodsShowBeanList.get(i).getNowPrice());
    }

    @Override
    public int getItemCount() {
        return goodsShowBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView goodsPicture;
        TextView goodsDescription;
        TextView currentPrice;
        TextView originalCost;
        TextView goodsId;
        public ViewHolder(View view)
        {
            super(view);
            goodsPicture=view.findViewById(R.id.goods_picture);
            goodsDescription=view.findViewById(R.id.goods_description);
            currentPrice=view.findViewById(R.id.current_price);
            originalCost=view.findViewById(R.id.original_cost);
            goodsId=view.findViewById(R.id.goods_id_hide);
        }
    }
}
