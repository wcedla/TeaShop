package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

public class TeaSortHeaderAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> sortHeaderList;
    int resource;
    View view;

    public TeaSortHeaderAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        sortHeaderList=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.textView=view.findViewById(R.id.tea_sort_header_item);
            view.setTag(viewHolder);
        }
        else
        {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        return view;

    }

    @Override
    public int getCount() {
        return sortHeaderList.size();
    }

    class ViewHolder
    {
        TextView textView;
    }
}


