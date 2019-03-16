package com.zys.chatmachine.bean;

import java.util.Date;

/**
 * Created by zm678 on 2018/9/19.
 */

public class ChatMessage {


    private Type type;
    private String chatMessage;
    private Date date;

    public ChatMessage(String chatMessage, Type type, Date date) {
        this.chatMessage = chatMessage;
        this.type = type;
        this.date = date;
    }

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

    public enum Type {
        OUT_CHAT_MESSAGE, MY_CHAT_MESSAGE
    }
}
