package com.galaxy.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Dell on 2016/3/17.
 */
public class OnlyTouchRelative extends RelativeLayout {
    public OnlyTouchRelative(Context context) {
        super(context);
    }

    public OnlyTouchRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
