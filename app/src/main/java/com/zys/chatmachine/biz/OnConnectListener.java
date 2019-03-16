package com.zys.chatmachine.biz;

/**
 * Created by zm678 on 2019/3/16.
 */

public interface OnConnectListener {

    void connectSuccess(String response);

    void connectFail();
}
