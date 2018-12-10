package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

public class HotGridAdapter extends BaseAdapter {

    Context context;
    List<String> hotItemName;

    public HotGridAdapter(Context context, List<String> hotItemName)
    {
        this.context=context;
        this.hotItemName=hotItemName;
    }

    @Override
    public int getCount() {
        return hotItemName.size();
    }

    @Override
    public Object getItem(int position) {
        return hotItemName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.hot_grid_layout, null);
            holder = new HotHolder();
            holder.hotName = convertView.findViewById(R.id.hot_name);
            convertView.setTag(holder);
        } else {
            holder = (HotHolder) convertView.getTag();
        }
        holder.hotName.setText(hotItemName.get(position));
        return convertView;
    }
}
class HotHolder
{
    TextView hotName;
}
