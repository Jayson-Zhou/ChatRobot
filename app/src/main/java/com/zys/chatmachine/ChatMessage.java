package com.zys.chatmachine;

import java.util.Date;

/**
 * Created by zm678 on 2018/9/19.
 */

public class ChatMessage {

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String chatMessage;
    private Date date;

    private enum Type{
        OUT_CHAT_MESSAGE,MY_CHAT_MESSAGE
    }
}
