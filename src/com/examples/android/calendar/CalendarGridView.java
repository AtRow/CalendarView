package com.examples.android.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * CalendarViewSample
 * com.examples.android.calendar
 */
public class CalendarGridView extends GridView {

    private OnChildClickListener childClickListener;

    public CalendarGridView(Context context) {
        super(context);
    }

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static interface OnChildClickListener {
        void onChildClick(View child);
    }

    public void setOnChildClickListener(OnChildClickListener childClickListener) {
        this.childClickListener = childClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        Log.d("CGV", "Got x:" + x + " y:" + y);

        int position = pointToPosition(x, y);
        if (position != INVALID_POSITION) {

            Log.d("CGV", "Got position: " + position);
            View view = getChildAt(position);

            if (childClickListener != null) {
                childClickListener.onChildClick(view);
            }
        }
        return false;
    }

}
