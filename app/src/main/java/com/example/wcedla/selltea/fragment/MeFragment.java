package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wcedla.selltea.AdressManageActivity;
import com.example.wcedla.selltea.EditPasswordActivity;
import com.example.wcedla.selltea.MainActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.TeaBIllActivity;
import com.example.wcedla.selltea.communication.ActivityEventManger;
import com.example.wcedla.selltea.communication.Event;
import com.example.wcedla.selltea.communication.EventBean;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static org.litepal.LitePalBase.TAG;

public class MeFragment extends Fragment {

    Activity myActivity;
    int number;
    View view;
    CircleImageView circleImageView;
    LinearLayout meUserLayout;
    LinearLayout imgAndTextLayout;
    String userName;
    TextView meUserName;
    RelativeLayout allBillRoot;
    LinearLayout noPayLayout;
    LinearLayout noMailLayout;
    LinearLayout noConfirmLayout;
    LinearLayout billFinishLayout;
    RelativeLayout meAdressEdit;
    Button logOutButton;

    @Override
    public void onAttach(Context context) {

        myActivity = (Activity) context;
        if (getArguments() != null) {
            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    public static MeFragment newInstance() {
        MeFragment meFragment = new MeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("number", 1);
//        meFragment.setArguments(bundle);   //设置参数
        return meFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=LayoutInflater.from(myActivity).inflate(R.layout.me_fragment_layout,container,false);
        SharedPreferences loginPreference = myActivity.getSharedPreferences("login", MODE_PRIVATE);
        userName = loginPreference.getString("username", "");
        circleImageView=view.findViewById(R.id.me_img);
        meUserLayout=view.findViewById(R.id.me_user_layout);
        RequestOptions options = new RequestOptions()
                .override((int)myActivity.getResources().getDisplayMetrics().widthPixels,
                        (int)myActivity.getResources().getDisplayMetrics().density*170)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        Log.d(TAG, "背景大小"+(int)myActivity.getResources().getDisplayMetrics().widthPixels+","+(int)myActivity.getResources().getDisplayMetrics().density*170);
        Glide.with(myActivity).load(R.drawable.me_bg).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                meUserLayout.setBackground(resource);
            }
        });
        imgAndTextLayout=view.findViewById(R.id.img_and_text_layout);
        imgAndTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUserNameIntent=new Intent(myActivity,EditPasswordActivity.class);
                myActivity.startActivity(editUserNameIntent);
            }
        });
        meUserName=view.findViewById(R.id.me_username);
        meUserName.setText(userName);
        allBillRoot=view.findViewById(R.id.me_all_bill_root);
        allBillRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "全部订单");
                Intent allBillShowIntent=new Intent(myActivity,TeaBIllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",0);
                allBillShowIntent.putExtras(bundle);
                myActivity.startActivity(allBillShowIntent);
            }
        });
        noPayLayout=view.findViewById(R.id.me_not_pay_layout);
        noPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allBillShowIntent=new Intent(myActivity,TeaBIllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",1);
                allBillShowIntent.putExtras(bundle);
                myActivity.startActivity(allBillShowIntent);
            }
        });
        noMailLayout=view.findViewById(R.id.me_no_mail_layout);
        noMailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allBillShowIntent=new Intent(myActivity,TeaBIllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",2);
                allBillShowIntent.putExtras(bundle);
                myActivity.startActivity(allBillShowIntent);
            }
        });
        noConfirmLayout=view.findViewById(R.id.no_confirm_bill_layout);
        noConfirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allBillShowIntent=new Intent(myActivity,TeaBIllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",3);
                allBillShowIntent.putExtras(bundle);
                myActivity.startActivity(allBillShowIntent);
            }
        });
        billFinishLayout=view.findViewById(R.id.bill_finish_layout);
        billFinishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allBillShowIntent=new Intent(myActivity,TeaBIllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("flag",4);
                allBillShowIntent.putExtras(bundle);
                myActivity.startActivity(allBillShowIntent);
            }
        });
        meAdressEdit=view.findViewById(R.id.me_adress_edit);
        meAdressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adressintent=new Intent(myActivity,AdressManageActivity.class);
                myActivity.startActivity(adressintent);
            }
        });
        logOutButton=view.findViewById(R.id.logout_btn);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor loginEditor = myActivity.getSharedPreferences("login", MODE_PRIVATE).edit();
                loginEditor.putBoolean("isLogin",false);
                loginEditor.putString("username","null");
                loginEditor.apply();
                EventBean eventBean=new EventBean();
                eventBean.what=1;
                MainActivity.activityEventManger.callEvent(eventBean);
            }
        });
        return view;
    }


}
