package com.zys.chatmachine.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.zys.chatmachine.R;
import com.zys.chatmachine.bean.ChatMessage;
import com.zys.chatmachine.biz.ConnectBiz;
import com.zys.chatmachine.biz.IConnectBiz;
import com.zys.chatmachine.biz.OnConnectListener;
import com.zys.chatmachine.view.IChatView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zm678 on 2019/3/16.
 */

public class ChatPresenter {

    private List<ChatMessage> messagesList;
    private IConnectBiz mConnectBiz;
    private IChatView mChatView;

    public ChatPresenter(IChatView chatView) {
        mChatView = chatView;
        mConnectBiz = new ConnectBiz();
        initData((Activity) mChatView);
    }

    private Handler mHandler = new SafeHandler(this);

    public void sentMessage(final String inputMessage) {
        ChatMessage editChatMessage = new ChatMessage(inputMessage, ChatMessage.Type.MY_CHAT_MESSAGE, new Date());
        messagesList.add(editChatMessage);
        mChatView.showMessage(messagesList.size());
        mChatView.scrollToNewMessage(messagesList.size());
        mConnectBiz.setOnConnectListener(new OnConnectListener() {
            @Override
            public void connectSuccess(String response) {
                Message msg = Message.obtain();
                msg.obj = new ChatMessage(response, ChatMessage.Type.OUT_CHAT_MESSAGE, new Date());
                mHandler.sendMessage(msg);
            }

            @Override
            public void connectFail() {
                Message msg = Message.obtain();
                msg.obj = new ChatMessage(((Activity) mChatView).getString(R.string.error), ChatMessage.Type.OUT_CHAT_MESSAGE, new Date());
                mHandler.sendMessage(msg);
            }
        });
        new Thread(new MyRunnable(this, inputMessage)).start();
    }

    private void initData(Activity activity) {
        messagesList = new ArrayList<>();
        messagesList.add(new ChatMessage(activity.getString(R.string.robot_first), ChatMessage.Type.OUT_CHAT_MESSAGE, new Date()));
        messagesList.add(new ChatMessage(activity.getString(R.string.my_first), ChatMessage.Type.MY_CHAT_MESSAGE, new Date()));
    }

    public List<ChatMessage> getMessagesList() {
        return messagesList;
    }

    // 声明为静态内部类，避免内存泄漏
    private static class SafeHandler extends Handler {

        private WeakReference<ChatPresenter> ref;

        public SafeHandler(ChatPresenter presenter) {
            this.ref = new WeakReference(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatPresenter presenter = this.ref.get();
            if (presenter != null) {
                ChatMessage resultMessage = (ChatMessage) msg.obj;
                presenter.messagesList.add(resultMessage);
                presenter.mChatView.showMessage(presenter.messagesList.size());
                presenter.mChatView.scrollToNewMessage(presenter.messagesList.size());
            }
        }
    }

    // 声明为静态内部类，避免内存泄漏
    private static class MyRunnable implements Runnable{

        private String inputMessage;

        private WeakReference<ChatPresenter> ref;

        public MyRunnable(ChatPresenter presenter, String inputMessage) {
            this.ref = new WeakReference(presenter);
            this.inputMessage = inputMessage;
        }

        @Override
        public void run() {
            ChatPresenter presenter = this.ref.get();
            if (presenter != null) {
                presenter.mConnectBiz.onConnect(inputMessage);
            }
        }
    }
}
