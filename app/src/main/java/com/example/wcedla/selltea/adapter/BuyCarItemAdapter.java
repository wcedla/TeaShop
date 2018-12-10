package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.fragment.BuyCarFragment;

import java.util.HashMap;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class BuyCarItemAdapter extends RecyclerView.Adapter<BuyCarItemAdapter.ViewHolder> {

    View view;
    Context context;
    List<BuyCarBean> buyCarBeanList;
    RecyclerView.LayoutManager layoutManager;
    HashMap<Integer, Boolean> isSelectMap = new HashMap<>();
    BuyCarFragment.SelectedListener selectedListener;


    public BuyCarItemAdapter(Context context, List<BuyCarBean> buyCarBeanList, HashMap<Integer, Boolean> isSelectMap, RecyclerView.LayoutManager layoutManager, BuyCarFragment.SelectedListener selectedListener) {
        this.context = context;
        this.buyCarBeanList = buyCarBeanList;
        this.layoutManager = layoutManager;
        this.isSelectMap = isSelectMap;
        this.selectedListener = selectedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.buy_car_item_recycler, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.itemSelect.setOnCheckedChangeListener(null);
//        Log.d(TAG, "出现的view"+i);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "view点击");
                viewHolder.carNumber.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(viewHolder.carNumber.getWindowToken(), 0);
                    }
                }
                if (viewHolder.carNumber.getText().toString().length() < 1) {
                    viewHolder.carNumber.setText("1");
                }
            }
        });
        viewHolder.subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.carNumber.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(viewHolder.carNumber.getWindowToken(), 0);
                    }
                }
                if (viewHolder.carNumber.getText().toString().length() < 1) {
                    viewHolder.carNumber.setText("1");
                    return;
                }
                int currentNumber = Integer.valueOf(viewHolder.carNumber.getText().toString());
                if (currentNumber - 1 >= 1) {
                    currentNumber -= 1;
                    viewHolder.carNumber.setText(String.valueOf(currentNumber));
                } else {
                    viewHolder.carNumber.setText("1");
                }
            }
        });
        viewHolder.adddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.carNumber.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(viewHolder.carNumber.getWindowToken(), 0);
                    }
                }
                if (viewHolder.carNumber.getText().toString().length() < 1) {
                    viewHolder.carNumber.setText("1");
                    return;
                }
                int currentNumber = Integer.valueOf(viewHolder.carNumber.getText().toString());
                if (currentNumber + 1 <= 9999) {
                    currentNumber += 1;
                    viewHolder.carNumber.setText(String.valueOf(currentNumber));
                } else {
                    viewHolder.carNumber.setText("9999");
                }
            }
        });
        viewHolder.carNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.carNumber.setFocusable(true);
                viewHolder.carNumber.setFocusableInTouchMode(true);
                viewHolder.carNumber.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(viewHolder.carNumber, InputMethodManager.SHOW_FORCED);
            }
        });
        viewHolder.carNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
                    viewHolder.carNumber.setFocusable(false);
                    if (viewHolder.carNumber.getText().toString().length() < 1) {
                        viewHolder.carNumber.setText("1");

                        return true;
                    }

                }
                return false;
            }
        });
        viewHolder.itemSelect.setChecked(false);
        if (isSelectMap.containsKey(i)) {
            //Log.d(TAG, "该位置为选中状态" + i);
            viewHolder.itemSelect.setChecked(isSelectMap.get(i));
        }
        viewHolder.itemSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isSelectMap.put(i, isChecked);
                } else {
                    isSelectMap.remove(i);
                }
                selectedListener.getSelectMap(isSelectMap);
                //Log.d(TAG, "数量"+isSelectMap.size()+","+imageList.size());
//                if (isSelectMap.size()==imageList.size())
//                {
//                    Log.d(TAG, "状态:变为取消全选");
//                    //BuyCarFragment.setSelectStatus(true);
//                }
//                else
//                {
//                    Log.d(TAG, "状态：变为全选");
//                   // BuyCarFragment.setSelectStatus(false);
//                }
            }
        });

        RequestOptions options = new RequestOptions()
                .override((int)context.getResources().getDisplayMetrics().density*80, (int)context.getResources().getDisplayMetrics().density*80)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop();
        Glide.with(context).load(buyCarBeanList.get(viewHolder.getAdapterPosition()).getImg()).apply(options).into(viewHolder.imageView);
        viewHolder.title.setText(buyCarBeanList.get(viewHolder.getAdapterPosition()).getTitle());
        viewHolder.price.setText("¥"+buyCarBeanList.get(viewHolder.getAdapterPosition()).getPrice());
        viewHolder.carNumber.setText(buyCarBeanList.get(viewHolder.getAdapterPosition()).getCount());

    }

    @Override
    public int getItemCount() {
        return buyCarBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button subButton;
        Button adddButton;
        EditText carNumber;
        CheckBox itemSelect;
        ImageView imageView;
        TextView title;
        TextView price;

        public ViewHolder(View view) {
            super(view);
            subButton = view.findViewById(R.id.car_number_sub);
            adddButton = view.findViewById(R.id.car_number_add);
            carNumber = view.findViewById(R.id.car_buy_number);
            itemSelect = view.findViewById(R.id.car_item_select);
            imageView = view.findViewById(R.id.buy_car_adapter_img);
            title = view.findViewById(R.id.buy_car_adapter_title);
            price = view.findViewById(R.id.buy_car_adapter_price);
        }
    }

}
