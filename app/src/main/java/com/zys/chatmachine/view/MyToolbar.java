package com.zys.chatmachine.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Created by zm678 on 2019/3/18.
 */

public class MyToolbar extends Toolbar {

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        insets.bottom = 0;
        super.fitSystemWindows(insets);
        return true;
    }
}
