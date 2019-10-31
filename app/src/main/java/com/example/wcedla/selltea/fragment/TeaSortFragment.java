package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.adapter.TeaSortConcreteAdapter;
import com.example.wcedla.selltea.adapter.TeaSortHeaderAdapter;
import com.example.wcedla.selltea.adapter.TeaTypeBean;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class TeaSortFragment extends Fragment {

    Activity myActivity;
    int args;

    int selectedIndex;
    ListView headerListView;
    RecyclerView concreteRecycler;
    List<String> teaSortNameList=new ArrayList<>();
    List<TeaTypeBean> teaTypeBeanList=new ArrayList<>();
    TeaSortHeaderAdapter teaSortHeaderAdapter;

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
        View view=getLayoutInflater().inflate(R.layout.tea_sort_fragment_layout,container,false);
        selectedIndex=0;
        headerListView=view.findViewById(R.id.tea_sort_header);
        concreteRecycler=view.findViewById(R.id.tea_sort_concrete);
        getTeaSortName();
        headerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView=view.findViewById(R.id.tea_sort_header_item);
//                textView.setSelected(true);
//                Log.d(TAG, "当前点击"+textView);
                selectedIndex=position;
                teaSortHeaderAdapter.setSelectedIndex(selectedIndex);
//                teaSortHeaderAdapter.notifyDataSetInvalidated();
                String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/teaTypeServlet?name="+teaSortNameList.get(position);
                getTeaTypeList(url);
            }
        });

        return view;
    }

    private void getTeaSortName()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/teaSortNameServlet";
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(myActivity,"连接服务器失败!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                teaSortNameList=JsonTool.getTeaSortName(responseData);
                if(teaSortNameList.size()>0)
                {
                    String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/teaTypeServlet?name="+teaSortNameList.get(0);
                    call.cancel();
                    getTeaTypeList(url);
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setSortNameAdapter();
                        }
                    });

                }
                else
                {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(myActivity,"数据获取失败!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void getTeaTypeList(String url)
    {
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(myActivity,"连接服务器失败!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                teaTypeBeanList=JsonTool.getTeaType(responseData);
                if(teaTypeBeanList.size()>0)
                {
                    call.cancel();
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTeaTypeAdapter();
                        }
                    });
                }
                else
                {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(myActivity,"数据获取失败!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setSortNameAdapter()
    {
        teaSortHeaderAdapter=new TeaSortHeaderAdapter(myActivity,R.layout.tea_sort_header_adapter,teaSortNameList,selectedIndex);
        headerListView.setAdapter(teaSortHeaderAdapter);
    }

    private void setTeaTypeAdapter()
    {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(myActivity,2);
        concreteRecycler.setLayoutManager(gridLayoutManager);
        TeaSortConcreteAdapter teaSortConcreteAdapter=new TeaSortConcreteAdapter(myActivity,teaTypeBeanList,gridLayoutManager);
        concreteRecycler.setAdapter(teaSortConcreteAdapter);
    }
}
