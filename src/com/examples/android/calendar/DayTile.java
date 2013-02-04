package com.examples.android.calendar;

import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * CalendarViewSample
 * com.examples.android.calendar
 */
public class DayTile {

    public DayInfo dayInfo;

    public boolean containsDay;

    public boolean isCurrentDay;

    private final Time time;

    public boolean hasIcon;

    private OnClickListener clickListener;

    private DayTile self = this;

    private ViewGroup tileView;

    public void setSelected(boolean selected) {
        if (selected) {
            tileView.setBackgroundResource(R.drawable.item_background_focused);
        } else {
            tileView.setBackgroundResource(R.drawable.item_background);
        }
    }

    public static interface OnClickListener {
        void onClick(DayTile dayTile);
    }

    public DayTile() {
        time = new Time();
        containsDay = false;
    }

    public DayTile(Time time) {
        this.time = new Time(time);
        this.time.normalize(true);
        containsDay = true;
    }

    public int getMonthDay() {
        return time.monthDay;
    }

    public boolean isHoliday() {
        return (time.weekDay == Time.SATURDAY ||
                time.weekDay == Time.SUNDAY);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void renderTo(ViewGroup tileView) {

        this.tileView = tileView;

        TextView dayView = (TextView)tileView.getChildAt(0);
        dayView.setText(Integer.toString(time.monthDay));

        if(isCurrentDay) {
            tileView.setBackgroundResource(R.drawable.item_background_focused);
        }
        else {
            tileView.setBackgroundResource(R.drawable.list_item_background);
        }

        // show icon if date is not empty and it exists in the items array
        //ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        ImageView iw = (ImageView) tileView.getChildAt(1);

        if(dayInfo != null) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }

        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(self);
                }
            }
        });
    }
}
