package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.BillDetialShowActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.TeaBIllActivity;
import com.example.wcedla.selltea.tool.HttpTool;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class TeaBillShowAdapter extends RecyclerView.Adapter<TeaBillShowAdapter.ViewHolder> {

    Context context;
    //View view;
    List<AllBillShowBean> allBillShowBeanList;
    RecyclerView.LayoutManager layoutManager;
    TeaBIllActivity.BillOptions billOptions;

    public TeaBillShowAdapter(Context context, List<AllBillShowBean> allBillShowBeanList, RecyclerView.LayoutManager layoutManager,TeaBIllActivity.BillOptions billOptions) {
        this.context = context;
        this.allBillShowBeanList = allBillShowBeanList;
        this.layoutManager = layoutManager;
        this.billOptions=billOptions;
    }

    @Override
    public int getItemViewType(int position) {
        return allBillShowBeanList.get(position).getGoodsBeanList().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_show_adapter_layout, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        for (int j = 0; j < i; j++) {
            View viewForAdd = LayoutInflater.from(context).inflate(R.layout.bill_item_for_add, viewHolder.allBillForAddLayout, false);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewForAdd.getLayoutParams();
            layoutParams.topMargin = (int) context.getResources().getDisplayMetrics().density * 10;
            viewForAdd.setLayoutParams(layoutParams);
            viewHolder.allBillForAddLayout.addView(viewForAdd);
        }
        viewHolder.billCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billOptions.cancel(viewHolder.billNo.getText().toString().substring(5),viewHolder.getAdapterPosition());
            }
        });
        viewHolder.billPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.billFlag.getText().toString().equals("待付款")) {
                    billOptions.pay(viewHolder.billNo.getText().toString().substring(5),viewHolder.getAdapterPosition());
                } else
                {
                    Intent showDetialIntent=new Intent(context,BillDetialShowActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("billNo",viewHolder.billNo.getText().toString().substring(5));
                    showDetialIntent.putExtras(bundle);
                    context.startActivity(showDetialIntent);
                }
            }
        });
        viewHolder.allBillForAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetialIntent=new Intent(context,BillDetialShowActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("billNo",viewHolder.billNo.getText().toString().substring(5));
                showDetialIntent.putExtras(bundle);
                context.startActivity(showDetialIntent);
            }
        });



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        setGoodsInfoLayout(viewHolder.allBillForAddLayout,i);
        float price = 0;
        for (ConfirmToBuyBean goodsInfo : allBillShowBeanList.get(i).getGoodsBeanList()) {
            price += Float.valueOf(goodsInfo.getTotalPrice());
        }

        viewHolder.billNo.setText("订单编号:" + allBillShowBeanList.get(i).getBillId());
        viewHolder.billFlag.setText(getBuyFlagText(allBillShowBeanList.get(i).getBillStatus()));
        if(viewHolder.billFlag.getText().toString().equals("已完成"))
        {
            viewHolder.billCancel.setText("删除订单");
        }
        else
        {
            viewHolder.billCancel.setText("取消订单");
        }
        if(viewHolder.billFlag.getText().toString().equals("待付款"))
        {
            viewHolder.billPayNow.setText("立即付款");
        }
        else
        {
            viewHolder.billPayNow.setText("查看订单");
        }
        viewHolder.billPrice.setText("¥" + String.valueOf(price));
        viewHolder.billCount.setText("共" + allBillShowBeanList.get(i).getGoodsBeanList().size() +
                "件商品,合计:");

    }

    @Override
    public int getItemCount() {
        return allBillShowBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout allBillForAddLayout;
        TextView billNo;
        TextView billFlag;
        TextView billPrice;
        TextView billCount;
        TextView billCancel;
        TextView billPayNow;

        public ViewHolder(View view) {
            super(view);
            allBillForAddLayout = view.findViewById(R.id.all_bill_for_add);
            billNo = view.findViewById(R.id.bill_no);
            billFlag = view.findViewById(R.id.bill_flag);
            billPrice = view.findViewById(R.id.all_bill_total_price);
            billCount = view.findViewById(R.id.bill_count_total);
            billCancel=view.findViewById(R.id.bill_cancel_or_delete);
            billPayNow=view.findViewById(R.id.bill_pay_now);
        }
    }

    private String getBuyFlagText(String status) {
        if (status.equals("0")) {
            return "待付款";
        } else if (status.equals("1")) {
            return "待发货";
        } else if (status.equals("2")) {
            return "待收货";
        } else if (status.equals("3")) {
            return "已完成";
        }
        return "未知状态";

    }

    private void setGoodsInfoLayout(LinearLayout parent,int position) {


        for (int i = 0; i < parent.getChildCount(); i++) {
            View viewForAdd=parent.getChildAt(i);
            ConfirmToBuyBean dataBean = allBillShowBeanList.get(position).getGoodsBeanList().get(i);
            ImageView imageView = viewForAdd.findViewById(R.id.confim_to_buy_img);
            TextView title = viewForAdd.findViewById(R.id.confim_to_buy_title);
            TextView buyCount = viewForAdd.findViewById(R.id.confim_to_buy_count);
            TextView totalPrice = viewForAdd.findViewById(R.id.confirm_to_buy_total_price);
            RequestOptions options = new RequestOptions()
                    .override((int) context.getResources().getDisplayMetrics().density * 80, (int) context.getResources().getDisplayMetrics().density * 80)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop();
            Glide.with(context).load(dataBean.getImg()).apply(options).into(imageView);
            title.setText(dataBean.getTitle());
            buyCount.setText("x" + dataBean.getBuyCount());
            totalPrice.setText("¥" + dataBean.getTotalPrice());
            //idStr += confirmToBuyBean.getId() + "~";
        }
    }

    public void setNewData(List<AllBillShowBean> newData)
    {
        this.allBillShowBeanList=newData;
        notifyDataSetChanged();
    }
}
