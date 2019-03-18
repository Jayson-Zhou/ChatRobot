package com.zys.chatmachine;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import com.zys.chatmachine.presenter.ChatPresenter;
import com.zys.chatmachine.view.IChatView;
import com.zys.chatmachine.view.MyToolbar;

public class MainActivity extends HideSoftInputActivity implements IChatView {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private Button btn_send;
    private EditText editText;
    private RecyclerView messageView;
    private MyAdapter adapter;

    private ChatPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new ChatPresenter(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //沉浸式状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Workaround.assistActivity(this);
        }

        //初始化控件
        initAllViews();

        //初始化RecyclerView
        initRecyclerView();
    }


    private void initAllViews() {

        // 初始化ToolBar
        toolbar = (MyToolbar) findViewById(R.id.title_toolBar);
        toolbar.setTitle("");

        // 通过手动给toolbar添加paddingTop，已舍弃该方法
        /*if (isHighVersion) {
           toolbar.setPadding(toolbar.getPaddingLeft(), getStatusBarHeight(this), toolbar.getPaddingRight(), toolbar.getPaddingBottom());
        }
*/
        setSupportActionBar(toolbar);


        editText = (EditText) findViewById(R.id.edit_text);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String msg = editText.getText().toString();
                editText.setText("");
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_alert), Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.sentMessage(msg);
                }

            }
        });
    }

    private void initRecyclerView() {

        messageView = (RecyclerView) findViewById(R.id.msg_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messageView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(this, mPresenter.getMessagesList());
        messageView.setAdapter(adapter);

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void showMessage(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void scrollToNewMessage(int position) {
        //自动跳转到最新消息
        messageView.smoothScrollToPosition(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter = null;
    }
}
