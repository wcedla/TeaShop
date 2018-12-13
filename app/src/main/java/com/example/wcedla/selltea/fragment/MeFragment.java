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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wcedla.selltea.EditPasswordActivity;
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
                myActivity.startActivity(allBillShowIntent);
            }
        });
        return view;
    }


}
