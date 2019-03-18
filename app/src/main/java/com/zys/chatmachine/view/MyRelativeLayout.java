package com.zys.chatmachine.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by zm678 on 2019/3/18.
 */

public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context) {
        super(context);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        insets.top = 0;
        insets.bottom = 0;
        super.fitSystemWindows(insets);
        return false;
    }
}
