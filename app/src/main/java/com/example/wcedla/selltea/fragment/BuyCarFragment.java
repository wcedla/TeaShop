package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.AdressEditActivity;
import com.example.wcedla.selltea.ConfirmToBuyActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.adapter.BuyCarBean;
import com.example.wcedla.selltea.adapter.BuyCarItemAdapter;
import com.example.wcedla.selltea.gson.BuyCarDetial;
import com.example.wcedla.selltea.gson.SearchShowDetial;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class BuyCarFragment extends Fragment {

    String userName;
    View view;
    Activity myActivity;
    int number;
    int getIndex;
    int deleteIndex;
    List<String> deleteIdList = new ArrayList<>();
    ProgressDialog progressDialog;
    LinearLayout buyCarNone;
    LinearLayout buyCarShow;
    //HashMap<Integer, Boolean> isSelectedMap = new HashMap<>();
    CheckBox allSelectCheck;
    TextView allSelectText;
    TextView deleteSelected;
    TextView submitToBuy;
    TextView totalPrice;
    RecyclerView carItem;
    LinearLayoutManager linearLayoutManager;
    BuyCarItemAdapter buyCarItemAdapter;
    List<BuyCarDetial> buyCarDetialList = new ArrayList<>();
    List<BuyCarBean> buyCarBeanList = new ArrayList<>();
    List<SearchShowDetial> searchShowDetialList = new ArrayList<>();

    HashMap<Integer, Boolean> outSelectedMap = new HashMap<>();
    HashMap<Integer, Integer> outCountMap = new HashMap<>();

    @Override
    public void onAttach(Context context) {

        myActivity = (Activity) context;
        if (getArguments() != null) {
            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    public static BuyCarFragment newInstance() {
        BuyCarFragment buyCarFragment = new BuyCarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", 1);
        buyCarFragment.setArguments(bundle);   //设置参数
        return buyCarFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.buy_car_layout, container, false);
        SharedPreferences loginPreference = myActivity.getSharedPreferences("login", MODE_PRIVATE);
        userName = loginPreference.getString("username", "");
        buyCarNone = view.findViewById(R.id.car_none);
        buyCarShow = view.findViewById(R.id.buy_car_had);
        getIndex = 0;
        //deleteIndex=0;

        getCarInfo();

        final LinearLayout selectAllGoods = view.findViewById(R.id.all_select_layout);
        allSelectCheck = view.findViewById(R.id.car_all_checkbox);
        allSelectText = view.findViewById(R.id.all_select_text);
        deleteSelected = view.findViewById(R.id.delete_selected);
        submitToBuy = view.findViewById(R.id.calculate_price);
        totalPrice = view.findViewById(R.id.total_price);
        //isSelectedMap.clear();

        selectAllGoods.setSelected(false);
        selectAllGoods.setPressed(false);
        allSelectCheck.setSelected(false);
        allSelectCheck.setChecked(false);
        allSelectText.setSelected(false);
        allSelectText.setPressed(false);
        allSelectText.setText("全选");
        selectAllGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allSelectCheck.isChecked()) {
                    allSelectCheck.setChecked(true);
                    allSelectText.setText("取消全选");
                    deleteSelected.setVisibility(View.VISIBLE);
                    ((BuyCarItemAdapter) carItem.getAdapter()).doAllSelect();
                } else {
                    allSelectCheck.setChecked(false);
                    allSelectText.setText("全选");
                    deleteSelected.setVisibility(View.INVISIBLE);
                    ((BuyCarItemAdapter) carItem.getAdapter()).doAllSelectClear();
                }

            }
        });

        deleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(myActivity);
                alertDialog.setTitle("删除警告！");
                alertDialog.setMessage("确定删除吗？");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(myActivity);
                        progressDialog.setTitle("删除购物车");
                        progressDialog.setMessage("正在删除...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        deleteIdList.clear();
                        deleteIndex = 0;
                        Set<Integer> selectedSet = outSelectedMap.keySet();
                        for (Integer pos : selectedSet) {
                            deleteIdList.add(buyCarBeanList.get(pos).getId());
                        }
                        String sqlStr = "delete from buycar where id=" + deleteIdList.get(deleteIndex);
                        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                        deleteBuyCar(url);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();


            }
        });

        submitToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (outSelectedMap.size() > 0) {
                    String idStr = "";
                    String countStr = "";
                    Set<Integer> selectedPosSet = outSelectedMap.keySet();
                    for (Integer pos : selectedPosSet) {
                        idStr += buyCarBeanList.get(pos).getId() + "~";
                        countStr += buyCarBeanList.get(pos).getCount() + "~";
                    }
                    Intent confirmToBuyIntent = new Intent(myActivity, ConfirmToBuyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", idStr);
                    bundle.putString("count", countStr);
                    confirmToBuyIntent.putExtras(bundle);
                    myActivity.startActivity(confirmToBuyIntent);
                    deleteIdList.clear();
                    deleteIndex = 0;
                    Set<Integer> selectedSet = outSelectedMap.keySet();
                    for (Integer pos : selectedSet) {
                        deleteIdList.add(buyCarBeanList.get(pos).getId());
                    }
                    String sqlStr = "delete from buycar where id=" + deleteIdList.get(deleteIndex);
                    String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                    deleteBuyCar(url);
                } else {
                    Toast.makeText(myActivity, "暂未选择商品！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void calculatePrice(HashMap<Integer, Boolean> isSelectedMap, HashMap<Integer, Integer> countMap) {
        //List<Integer> goodsPosList = new ArrayList<>();
        float calculateTotalPrice = 0;
        Set<Integer> selectedPosSet = isSelectedMap.keySet();
        for (Integer pos : selectedPosSet) {
            //goodsPosList.add(pos);
            calculateTotalPrice += Float.valueOf(buyCarBeanList.get(pos).getPrice()) *
                    countMap.get(pos);
            //Log.d(TAG, "获得价钱" + calculateTotalPrice);
        }

        //calculateTotalPrice = isSelectedMap.size() * 10;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        totalPrice.setText("¥" + decimalFormat.format(calculateTotalPrice));

    }

    SelectedListener selectedListener = new SelectedListener() {
        @Override
        public void updateSelect(HashMap<Integer, Boolean> selectedMap, HashMap<Integer, Integer> countMap) {
//            Iterator iterator=selectedMap.keySet().iterator();
//            for(Map.Entry<Integer,Boolean> entry:selectedMap.entrySet())
//            {
//                Log.d(TAG, "监听选中位置: " +entry.getKey()+","+entry.getValue());
//            }
//
//            for (Map.Entry<Integer,Integer> entry:countMap.entrySet())
//            {
//                Log.d(TAG, "监听数量: " +entry.getKey()+","+entry.getValue());
//            }
            outSelectedMap = selectedMap;
            outCountMap = countMap;
            if (selectedMap.size() == buyCarBeanList.size()) {
                allSelectCheck.setChecked(true);
                allSelectText.setText("取消全选");
            } else {
                allSelectCheck.setChecked(false);
                allSelectText.setText("全选");
            }
            if (selectedMap.size() > 0) {
                deleteSelected.setVisibility(View.VISIBLE);
            } else {
                deleteSelected.setVisibility(View.INVISIBLE);
            }
            calculatePrice(selectedMap, countMap);

        }
    };

    public interface SelectedListener {

        void updateSelect(HashMap<Integer, Boolean> selectedMap, HashMap<Integer, Integer> countMap);

    }

    private void getCarInfo() {
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/BuyCarServlet?username=" + userName;
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buyCarNone.setVisibility(View.VISIBLE);
                        buyCarShow.setVisibility(View.GONE);
                        Toast.makeText(myActivity, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                buyCarDetialList = JsonTool.getBuyCarDetial(responseData);
                Log.d(TAG, "购物车数组大小" + buyCarDetialList.size());
                if (buyCarDetialList.size() < 1) {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buyCarNone.setVisibility(View.VISIBLE);
                            buyCarShow.setVisibility(View.GONE);
                            //Toast.makeText(myActivity, "数据获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getIndex = 0;
                    String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id=" + buyCarDetialList.get(getIndex).id;
                    call.cancel();
                    buyCarBeanList.clear();
                    getBuyCarInfoFromServer(url);
                }
            }
        });
    }

    private void getBuyCarInfoFromServer(String url) {
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buyCarNone.setVisibility(View.VISIBLE);
                        buyCarShow.setVisibility(View.GONE);
                        Toast.makeText(myActivity, "连接服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                searchShowDetialList = JsonTool.getShowResult(responseData);
                if (searchShowDetialList.size() < 1) {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buyCarNone.setVisibility(View.VISIBLE);
                            buyCarShow.setVisibility(View.GONE);
                            Toast.makeText(myActivity, "数据处理异常！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    for (SearchShowDetial searchShowDetial : searchShowDetialList) {
                        BuyCarBean buyCarBean = new BuyCarBean(buyCarDetialList.get(getIndex).id,
                                searchShowDetial.img,
                                searchShowDetial.title,
                                buyCarDetialList.get(getIndex).count,
                                searchShowDetial.nowPrice);
                        buyCarBeanList.add(buyCarBean);
                    }
                    getIndex += 1;
                    if (getIndex < buyCarDetialList.size()) {
                        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/ConfirmToBuy?id=" + buyCarDetialList.get(getIndex).id;
                        call.cancel();
                        getBuyCarInfoFromServer(url);
                    } else {
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buyCarNone.setVisibility(View.GONE);
                                buyCarShow.setVisibility(View.VISIBLE);
                                setBuyCarData();
                            }
                        });
                    }
                }
            }
        });
    }

    private void setBuyCarData() {
        carItem = view.findViewById(R.id.car_recycler);
        linearLayoutManager = new LinearLayoutManager(myActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        buyCarItemAdapter = new BuyCarItemAdapter(myActivity, buyCarBeanList, selectedListener);
        carItem.setLayoutManager(linearLayoutManager);
        carItem.setAdapter(buyCarItemAdapter);
    }

    private void deleteBuyCar(String url) {
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(myActivity, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                boolean result = JsonTool.getStatus(responseData);
                if (result) {
                    deleteIndex += 1;
                    if (deleteIndex < deleteIdList.size()) {
                        String sqlStr = "delete from buycar where id=" + deleteIdList.get(deleteIndex);
                        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                        call.cancel();
                        deleteBuyCar(url);
                    } else {
                        Set<Integer> selectedPosSet = outSelectedMap.keySet();
                        List<BuyCarBean> delBuyCarBeanList = new ArrayList<>();
                        for (Integer pos : selectedPosSet) {
                            delBuyCarBeanList.add(buyCarBeanList.get(pos));
                        }
                        buyCarBeanList.removeAll(delBuyCarBeanList);
                        outSelectedMap.clear();
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((BuyCarItemAdapter) carItem.getAdapter()).updateData(buyCarBeanList);
                                if(progressDialog!=null&&progressDialog.isShowing()) {
                                    progressDialog.cancel();
                                }
                                allSelectCheck.setChecked(false);
                                allSelectText.setText("全选");
                                deleteSelected.setVisibility(View.INVISIBLE);
                                totalPrice.setText("¥0.00");
//                                Toast.makeText(myActivity, "操作成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(myActivity, "操作失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
