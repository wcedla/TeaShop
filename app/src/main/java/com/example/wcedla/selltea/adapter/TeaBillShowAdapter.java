package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class TeaBillShowAdapter extends RecyclerView.Adapter<TeaBillShowAdapter.ViewHolder> {

    Context context;
    //View view;
    List<AllBillShowBean> allBillShowBeanList;
    RecyclerView.LayoutManager layoutManager;
    static int indedx=0;

    public TeaBillShowAdapter(Context context, List<AllBillShowBean> allBillShowBeanList, RecyclerView.LayoutManager layoutManager)
    {
        this.context=context;
        this.allBillShowBeanList=allBillShowBeanList;
        this.layoutManager=layoutManager;
        indedx=0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_show_adapter_layout,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
//        int count=allBillShowBeanList.get(viewHolder.getAdapterPosition()).getGoodsBeanList().size();
        Log.d(TAG, "数组大小"+viewHolder.getAdapterPosition());
//        for(int j=0;j<2;j++) {
//            View billViewForAdd = LayoutInflater.from(context).inflate(R.layout.bill_item_for_add, viewHolder.allBillForAddLayout, false);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) billViewForAdd.getLayoutParams();
//            layoutParams.topMargin = (int) context.getResources().getDisplayMetrics().density * 10;
//            billViewForAdd.setLayoutParams(layoutParams);
//            viewHolder.allBillForAddLayout.addView(billViewForAdd);
//        }


        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        int count=0;
        float price=0;
         for(ConfirmToBuyBean goodsInfo:allBillShowBeanList.get(i).getGoodsBeanList())
         {
             count += Integer.valueOf(goodsInfo.getBuyCount());
             price += Float.valueOf(goodsInfo.getTotalPrice());
         }

        viewHolder.billNo.setText("订单编号:"+allBillShowBeanList.get(i).getBillId());
        viewHolder.billFlag.setText(getBuyFlagText(allBillShowBeanList.get(i).getBillStatus()));
        viewHolder.billPrice.setText("¥"+String.valueOf(price));
        viewHolder.billCount.setText("共"+allBillShowBeanList.get(i).getGoodsBeanList().size()+
        "件商品,合计:");

    }

    @Override
    public int getItemCount() {
        return allBillShowBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {


        int position;
        LinearLayout allBillForAddLayout;
        TextView billNo;
        TextView billFlag;
        TextView billPrice;
        TextView billCount;

        public ViewHolder(View view)
        {
            super(view);
            allBillForAddLayout=view.findViewById(R.id.all_bill_for_add);
            billNo=view.findViewById(R.id.bill_no);
            billFlag=view.findViewById(R.id.bill_flag);
            billPrice=view.findViewById(R.id.all_bill_total_price);
            billCount=view.findViewById(R.id.bill_count_total);
            position=indedx;
            indedx+=1;
        }
    }

    private String getBuyFlagText(String status)
    {
        if(status.equals("0"))
        {
            return "待付款";
        }
        else if(status.equals("1"))
        {
            return "待发货";
        }
        else if(status.equals("2"))
        {
            return "待收货";
        }
        else if(status.equals("3"))
        {
            return "已完成";
        }
        return "未知状态";

    }
}
