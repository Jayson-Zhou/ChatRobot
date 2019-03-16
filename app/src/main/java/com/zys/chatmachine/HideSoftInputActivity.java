package com.zys.chatmachine;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*功能虽然实现了，但是有一个问题，getCurrentFocus() 获取到的view是上一次点击的view,如何在弹出键盘后拿到
发送按钮的view？*/

/**
 * 点击空白处隐藏键盘
 */
public class HideSoftInputActivity extends AppCompatActivity {

    private final String TAG = "HideSoftInputAcitivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取点击焦点和事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
//            Log.d(TAG, view.toString());
            if (isHideSoftInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否需要隐藏键盘
     *
     * @param v
     * @param ev
     * @return
     */
    private boolean isHideSoftInput(View v, MotionEvent ev) {

        DisplayMetrics dm;
        int screenWidth;

        if (v != null && (v instanceof EditText)) {

            //获取屏幕的宽度
            dm = getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;

            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = screenWidth;
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param token
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
