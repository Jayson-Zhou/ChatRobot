package com.zys.chatmachine.biz;

/**
 * 网络连接类接口
 * Created by zm678 on 2019/3/16.
 */

public interface IConnectBiz {

    void onConnect(String inputMessage);

    void setOnConnectListener(OnConnectListener onConnectlistener);
}
