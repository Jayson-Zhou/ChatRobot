package com.zys.chatmachine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zys.chatmachine.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 适配器类,Item拥有两种形式
 * Created by zm678 on 2018/9/21.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    //约定的getItemType对应返回值
    private final int OUT_CHAT_MESSAGE = 10000;
    private final int MY_CHAT_MESSAGE = 10001;

    private LayoutInflater inflater;
    private List<ChatMessage> list;
    private MyViewHolder mholder;

    public MyAdapter(Context context, List<ChatMessage> list) {

        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    /**
     * 通过Type值判断msg的显示形式
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (list != null) {
            if (list.get(position).getType() == ChatMessage.Type.OUT_CHAT_MESSAGE) {
                return OUT_CHAT_MESSAGE;
            }
            if (list.get(position).getType() == ChatMessage.Type.MY_CHAT_MESSAGE) {
                return MY_CHAT_MESSAGE;
            }

        }
        return super.getItemViewType(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OUT_CHAT_MESSAGE) {
            mholder = new MyViewHolder(inflater.inflate(R.layout.out_chat, parent, false), ChatMessage.Type.OUT_CHAT_MESSAGE);
        }
        if (viewType == MY_CHAT_MESSAGE) {
            mholder = new MyViewHolder(inflater.inflate(R.layout.my_chat, parent, false), ChatMessage.Type.MY_CHAT_MESSAGE);
        }
        return mholder;
    }

    /**
     * 绑定数据至ViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder != null) {
            //将日期转换为标准格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            holder.dateTv.setText(df.format(list.get(position).getDate()));
            holder.messageTv.setText(list.get(position).getChatMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
