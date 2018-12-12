package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.SearchActivity;

import java.util.List;

public class TeaSortConcreteAdapter extends RecyclerView.Adapter<TeaSortConcreteAdapter.ViewHolder> {

    Context context;
    List<TeaTypeBean> teaTypeBeanList;
    RecyclerView.LayoutManager layoutManager;
    View view;


    public TeaSortConcreteAdapter(Context context, List<TeaTypeBean> teaTypeBeanList, RecyclerView.LayoutManager layoutManager)
    {
        this.context=context;
        this.teaTypeBeanList=teaTypeBeanList;
        this.layoutManager=layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view =LayoutInflater.from(context).inflate(R.layout.tea_sort_concrete_adapter,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent=new Intent(context,SearchActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",teaTypeBeanList.get(viewHolder.getAdapterPosition()).getName());
                searchIntent.putExtras(bundle);
                context.startActivity(searchIntent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.teaTypeName.setText(teaTypeBeanList.get(i).getName());
        RequestOptions options = new RequestOptions()
                .override((int)context.getResources().getDisplayMetrics().density*100,
                        (int)context.getResources().getDisplayMetrics().density*100)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        Glide.with(context).load(teaTypeBeanList.get(i).getImg()).apply(options).into(viewHolder.teaTypeImg);

    }

    @Override
    public int getItemCount() {
        return teaTypeBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView teaTypeImg;
        TextView teaTypeName;

        public ViewHolder(View itemView) {
            super(itemView);
            teaTypeImg=itemView.findViewById(R.id.tea_type_img);
            teaTypeName=itemView.findViewById(R.id.tea_type_name);
        }
    }
}
