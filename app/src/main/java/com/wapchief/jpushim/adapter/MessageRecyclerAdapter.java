package com.wapchief.jpushim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wapchief.jpushim.R;
import com.wapchief.jpushim.entity.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wapchief on 2017/7/18.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MessageBean> data=new ArrayList<MessageBean>();

    public MessageRecyclerAdapter(List<MessageBean> data, Context context) {
        this.data = data;
        this.context = context;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if(null != data && data.size()>0){
            return data.size() ;
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_main_message, parent, false);
            return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
                //监听回调
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, index);
                    }
                });

            }
            if (!data.equals("")) {
                ((ItemViewHolder) holder).content.setText(data.get(position).content);
                ((ItemViewHolder) holder).title.setText(data.get(position).title);
                ((ItemViewHolder) holder).time.setText(data.get(position).time);
//            ((ItemViewHolder) holder).title.setText(data.get(position).title);
            }
        }
    }

    //展示的item
    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title,time,content;
        ImageView img;

        public ItemViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_main_username);
            time = (TextView) view.findViewById(R.id.item_main_time);
            content = (TextView) view.findViewById(R.id.item_main_content);
            img = (ImageView) view.findViewById(R.id.item_main_img);
        }
    }


}
