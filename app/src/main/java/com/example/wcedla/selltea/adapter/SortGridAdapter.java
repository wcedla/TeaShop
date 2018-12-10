package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SortGridAdapter extends BaseAdapter {

    Context context;
    List<SortGridBean> sortGridBeanList;

    public SortGridAdapter(Context context, List<SortGridBean> sortGridBeanList) {
        this.context = context;
        this.sortGridBeanList = sortGridBeanList;
    }

    @Override
    public int getCount() {
        return sortGridBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return sortGridBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sort_grid_layout, null);
            holder = new Holder();
            holder.imageView = convertView.findViewById(R.id.sort_image);
            holder.textView = convertView.findViewById(R.id.sort_title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
            holder.imageView.setImageResource(sortGridBeanList.get(position).getImageId());
            holder.textView.setText(sortGridBeanList.get(position).getTitle());
            holder.imageView.setBackgroundResource(sortGridBeanList.get(position).getBackgroundId());
        return convertView;
    }

}

class Holder {
    ImageView imageView;
    TextView textView;
}
