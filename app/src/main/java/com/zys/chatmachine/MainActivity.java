package com.zys.chatmachine;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends HideSoftInputActivity {

    private static final String TAG = "MainActivity";

    //记录版本的标志位
    private boolean isHighVersion = false;

    private Toolbar toolbar;
    private Button btn_send;
    private EditText editText;
    private RecyclerView messageView;
    private MyAdapter adapter;

    private List<ChatMessage> messageList;

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            ChatMessage resultMessage = (ChatMessage) msg.obj;
            messageList.add(resultMessage);
            adapter.notifyDataSetChanged();

            //自动跳转到最新消息
            messageView.smoothScrollToPosition(messageList.size());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        测试用
        new Thread(new Runnable() {
            @Override
            public void run() {
                String test = HttpUtils.sendMessage("你叫什么名字？");
                Log.d(TAG,test);
            }
        }).start();*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //沉浸式状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Workaround.assistActivity(this);

            isHighVersion = true;

        }

        //初始化控件
        initAllViews();

        //初始化数据
        initData();

        //初始化
        initRecyclerView();

    }


    private void initAllViews() {

        //初始化ToolBar
        toolbar = (Toolbar) findViewById(R.id.title_toolBar);
        toolbar.setTitle("");
        if (isHighVersion) {
            toolbar.setPadding(toolbar.getPaddingLeft(), getStatusBarHeight(this)
                    , toolbar.getPaddingRight(), toolbar.getPaddingBottom());
        }

        setSupportActionBar(toolbar);


        editText = (EditText) findViewById(R.id.edit_text);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String msg = editText.getText().toString();
                editText.setText("");
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_alert)
                            , Toast.LENGTH_SHORT).show();
                } else {
                    ChatMessage editChatMessage = new ChatMessage(msg, ChatMessage.Type.MY_CHAT_MESSAGE
                            , new Date());
                /*edit_msg.setType(ChatMessage.Type.MY_CHAT_MESSAGE);
                edit_msg.setDate(new Date());
                edit_msg.setChatMessage(editText.getText().toString());*/
                    messageList.add(editChatMessage);
                    adapter.notifyDataSetChanged();

                    //跳转到最新消息
                    messageView.smoothScrollToPosition(messageList.size());

                    //开启一个线程发送信息给机器人
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String responseJson = HttpUtils.sendMessage(msg);
                            String responseMessage = null;

                            //引入并使用Json解析框架Gson
                            try {
                                ResponseResult response = new Gson().fromJson(responseJson
                                        , ResponseResult.class);
                                responseMessage = response.getResults().get(0).getValues().getText();
//                            Log.d(TAG,responseMessage);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                responseMessage = getString(R.string.error);
                            }
                            ChatMessage resultMessage = new ChatMessage(responseMessage
                                    , ChatMessage.Type.OUT_CHAT_MESSAGE
                                    , new Date());
                            Message m = Message.obtain();
                            m.obj = resultMessage;
                            myHandler.sendMessage(m);

                        }
                    }).start();

                }

            }
        });
    }

    private void initData() {
        messageList = new ArrayList<ChatMessage>();
        messageList.add(new ChatMessage(getString(R.string.xidan_first)
                , ChatMessage.Type.OUT_CHAT_MESSAGE
                , new Date()));
        messageList.add(new ChatMessage(getString(R.string.my_first)
                , ChatMessage.Type.MY_CHAT_MESSAGE, new Date()));
    }

    private void initRecyclerView() {

        messageView = (RecyclerView) findViewById(R.id.msg_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messageView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(this, messageList);
        messageView.setAdapter(adapter);

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "clicked RecyclerView");
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    //通过反射获取状态栏高度
    private int getStatusBarHeight(Context context) {
        int statusBarHeight = toolbar.getPaddingTop();
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }
}
