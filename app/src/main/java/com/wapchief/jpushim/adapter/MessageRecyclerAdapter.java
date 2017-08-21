package com.wapchief.jpushim.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wapchief.jpushim.R;
import com.wapchief.jpushim.activity.AddFriendMsgActivity;
import com.wapchief.jpushim.activity.UserInfoActivity;
import com.wapchief.jpushim.entity.MessageBean;
import com.wapchief.jpushim.framework.helper.SharedPrefHelper;
import com.wapchief.jpushim.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by wapchief on 2017/7/18.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MessageBean> data = new ArrayList<MessageBean>();
    private SharedPrefHelper helper;

    public void addList(List<MessageBean> newList) {
        if (null != newList && newList.size() > 0) {
            data.addAll(newList);
            notifyDataSetChanged();
        }

    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public MessageRecyclerAdapter(List<MessageBean> data, Context context) {
        this.data = data;
        this.context = context;
        helper = SharedPrefHelper.getInstance();
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
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_message, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
            if (!data.equals("")) {
                ((ItemViewHolder) holder).content.setText(data.get(position).content);
                ((ItemViewHolder) holder).title.setText(data.get(position).title);
                ((ItemViewHolder) holder).time.setText(data.get(position).time);
                Picasso.with(context)
                        .load(data.get(position).getImg())
                        .placeholder(R.mipmap.icon_user)
                        .into(((ItemViewHolder) holder).img);
                //会话列表
                if (data.get(position).type == 0) {
//                    Log.e("msgtype", "type:" + data.get(position).type+"\n"+data.get(position).time);
                    if (Integer.valueOf(data.get(position).time) <= 0) {
                        ((ItemViewHolder) holder).time.setText(data.get(position).time+"条未读消息");
                        ((ItemViewHolder) holder).time.setTextColor(Color.parseColor("#66000000"));
                    }else {
                        ((ItemViewHolder) holder).time.setText(data.get(position).time+"条未读消息");
                        ((ItemViewHolder) holder).time.setTextColor(Color.parseColor("#E5955D"));
                    }
                }
                //好友推荐
                if (data.get(position).type == 1) {
                    if (data.get(position).getFriends()){
                        ((ItemViewHolder) holder).button.setText("已添加");
                        ((ItemViewHolder) holder).button.setEnabled(false);
                    }else {
                        ((ItemViewHolder) holder).button.setText("添加");
                        ((ItemViewHolder) holder).button.setEnabled(true);
                    }
                    ((ItemViewHolder) holder).button.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).button.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, AddFriendMsgActivity.class);
                                    intent.putExtra("ID", data.get(position).getContent());
                                    intent.putExtra("NAME", data.get(position).getTitle());
//                        intent.putExtra("ICON", userInfo.getAvatar());
                                    context.startActivity(intent);
                                }
                            });
                }
                //好友验证
                if (data.get(position).type==2){

                    JMessageClient.getUserInfo(data.get(position).getUserName(), new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i==0 ){
                                Log.e("isFriends", userInfo.getUserName()+"...." + userInfo.isFriend());
                                if (userInfo.isFriend()) {
                                    ((ItemViewHolder) holder).button.setText("已同意");
                                    ((ItemViewHolder) holder).button.setEnabled(false);
                                }else {
                                    ((ItemViewHolder) holder).button.setText("同意");
                                    ((ItemViewHolder) holder).button.setEnabled(true);
                                }
                                ((ItemViewHolder) holder).button.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    ((ItemViewHolder) holder).button.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ContactManager.acceptInvitation(data.get(position).getUserName(), "", new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i==0){
                                                ((ItemViewHolder) holder).button.setText("已同意");
                                            }else {
                                                Toast.makeText(context,"验证失败："+s,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                }

            }
        }
    }

    //展示的item
    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, content;
        CircleImageView img;
        Button button;

        public ItemViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_main_username);
            time = (TextView) view.findViewById(R.id.item_main_time);
            content = (TextView) view.findViewById(R.id.item_main_content);
            img = (CircleImageView) view.findViewById(R.id.item_main_img);
            button = (Button) view.findViewById(R.id.item_main_bt);
        }
    }


}
