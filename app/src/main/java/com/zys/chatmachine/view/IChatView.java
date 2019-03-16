package com.zys.chatmachine.view;

/**
 * Created by zm678 on 2019/3/16.
 */

public interface IChatView {

    void showMessage(int position);

    void scrollToNewMessage(int position);
}
