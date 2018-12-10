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
import android.widget.Toast;

import com.example.wcedla.selltea.AdressEditActivity;
import com.example.wcedla.selltea.AdressManageActivity;
import com.example.wcedla.selltea.ConfirmToBuyActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.communication.Event;
import com.example.wcedla.selltea.communication.EventBean;
import com.example.wcedla.selltea.communication.EventManager;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalBase.TAG;

public class AdressItemAdapter extends RecyclerView.Adapter<AdressItemAdapter.ViewHolder> {

    Context context;
    List<AdressBean> adressBeanList;
    RecyclerView.LayoutManager layoutManager;


    public AdressItemAdapter(Context contex, List<AdressBean> adressBeanList, RecyclerView.LayoutManager layoutManager)
    {
        this.context=contex;
        this.adressBeanList=adressBeanList;
        this.layoutManager=layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.adress_manage_item_recycler,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.adressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adressEditIntent=new Intent(context,AdressEditActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("adressId",adressBeanList.get(viewHolder.getAdapterPosition()).getId());
                bundle.putString("adressName",adressBeanList.get(viewHolder.getAdapterPosition()).getAdressName());
                bundle.putString("adressPhone",adressBeanList.get(viewHolder.getAdapterPosition()).getAdressPhone());
                bundle.putString("adressText",adressBeanList.get(viewHolder.getAdapterPosition()).getAdressText());
                adressEditIntent.putExtras(bundle);
                context.startActivity(adressEditIntent);
                //Log.d(TAG, "当前点击"+adressBeanList.get(viewHolder.getAdapterPosition()).getId());
            }
        });
        viewHolder.adressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> adressInfoList=new ArrayList<>();
                adressInfoList.add(adressBeanList.get(viewHolder.getAdapterPosition()).getAdressName());
                adressInfoList.add(adressBeanList.get(viewHolder.getAdapterPosition()).getAdressPhone());
                adressInfoList.add(adressBeanList.get(viewHolder.getAdapterPosition()).getAdressText());
                EventBean eventBean=new EventBean();
                eventBean.what=1;
                eventBean.obj=adressInfoList;
                //EventManager.callEvent(eventBean);
                if(ConfirmToBuyActivity.activityEventManger!=null)
                {
                    ConfirmToBuyActivity.activityEventManger.callEvent(eventBean);
                }
                ((AdressManageActivity)context).finish();
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.userName.setText(adressBeanList.get(i).getAdressName());
        viewHolder.userPhone.setText(adressBeanList.get(i).getAdressPhone());
        viewHolder.userAdress.setText(adressBeanList.get(i).getAdressText());
    }

    @Override
    public int getItemCount() {
        return adressBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout adressSelect;
        TextView userName;
        TextView userPhone;
        TextView userAdress;
        TextView adressEdit;
        //TextView adressId;

        public ViewHolder(View view)
        {
            super(view);
            adressSelect=view.findViewById(R.id.adress_select_root);
            userName=view.findViewById(R.id.adress_user_name);
            userPhone=view.findViewById(R.id.adress_user_phone);
            userAdress=view.findViewById(R.id.adress_user_adress);
            adressEdit=view.findViewById(R.id.adress_manage_edit);
            //adressId=view.findViewById(R.id.adress_id);
        }
    }
}
