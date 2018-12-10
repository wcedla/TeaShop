package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.adapter.TeaSortHeaderAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeaSortFragment extends Fragment {

    Activity myActivity;
    int args;
    View view;

    @Override
    public void onAttach(Context context) {

        myActivity = (Activity) context;
        if(getArguments()!=null)
        {
            args = getArguments().getInt("args");  //获取参数
        }
        super.onAttach(context);
    }

    public static TeaSortFragment newInstance() {
        TeaSortFragment teaSortFragment = new TeaSortFragment();
        //Bundle bundle = new Bundle();
        //bundle.putInt("number", i);
        //teaSortFragment.setArguments(bundle);   //设置参数
        return teaSortFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=getLayoutInflater().inflate(R.layout.tea_sort_fragment_layout,container,false);
        ListView headerListView=view.findViewById(R.id.tea_sort_header);
        RecyclerView concreteRecycler=view.findViewById(R.id.tea_sort_concrete);
        List<String> headerTypeList=new ArrayList<>();
        for(int i=0;i<8;i++)
        {
            headerTypeList.add("分类"+i+1);
        }
        TeaSortHeaderAdapter teaSortHeaderAdapter=new TeaSortHeaderAdapter(myActivity,R.layout.tea_sort_header_adapter,headerTypeList);
        headerListView.setAdapter(teaSortHeaderAdapter);
        return view;
    }
}
