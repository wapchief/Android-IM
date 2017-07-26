package com.wapchief.jpushim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.greendao.model.SearchAdd;

import java.util.List;

/**
 * Created by wapchief on 2017/7/25.
 */

public class AddSearchAdapter extends BaseAdapter {
    private Context context;
    private List<SearchAdd> list;

    public AddSearchAdapter(Context context, List<SearchAdd> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchAdd tv = (SearchAdd) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_search, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.item_add_tv);
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,tv.getContent()+"",Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(tv.getContent());

        return convertView;
    }

    //创建ViewHolder类
    class ViewHolder {
        TextView tv;

    }
}
