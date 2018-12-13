package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

public class TeaBillShowAdapter extends RecyclerView.Adapter<TeaBillShowAdapter.ViewHolder> {

    Context context;
    View view;
    List<String> dataList;
    RecyclerView.LayoutManager layoutManager;

    public TeaBillShowAdapter(Context context, List<String> dataList, RecyclerView.LayoutManager layoutManager)
    {
        this.context=context;
        this.dataList=dataList;
        this.layoutManager=layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view=LayoutInflater.from(context).inflate(R.layout.bill_show_adapter_layout,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View view)
        {
            super(view);
        }
    }
}
