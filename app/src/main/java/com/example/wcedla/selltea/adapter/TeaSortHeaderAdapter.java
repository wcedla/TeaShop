package com.example.wcedla.selltea.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wcedla.selltea.R;

import java.util.List;

import static org.litepal.tablemanager.Generator.TAG;

public class TeaSortHeaderAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> sortHeaderList;
    int resource;
    View view;
    int selectedIndex;

    public TeaSortHeaderAdapter(Context context, int resource, List<String> objects,int selectedIndex) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        sortHeaderList=objects;
        this.selectedIndex=selectedIndex;
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
        //Log.d(TAG, "位置"+selectedIndex+","+position);
        if(position==selectedIndex)
        {
            view.setBackgroundColor(Color.parseColor("#dfdfdf"));
        }
        else
        {
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        viewHolder.textView.setText(sortHeaderList.get(position));

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

    public void setSelectedIndex(int index)
    {
        this.selectedIndex=index;
        notifyDataSetInvalidated();
    }

}


