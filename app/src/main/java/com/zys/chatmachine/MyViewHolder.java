package com.zys.chatmachine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zys.chatmachine.bean.ChatMessage;

/**
 * Created by zm678 on 2018/9/21.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView dateTv;
    TextView messageTv;

    public MyViewHolder(View itemView, ChatMessage.Type type) {
        super(itemView);
        if (type == ChatMessage.Type.OUT_CHAT_MESSAGE) {
            dateTv = (TextView) itemView.findViewById(R.id.out_date);
            messageTv = (TextView) itemView.findViewById(R.id.out_msg);
        }
        if (type == ChatMessage.Type.MY_CHAT_MESSAGE) {
            dateTv = (TextView) itemView.findViewById(R.id.my_date);
            messageTv = (TextView) itemView.findViewById(R.id.my_msg);
        }
    }
}
