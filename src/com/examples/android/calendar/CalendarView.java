/*
* Copyright 2011 Lauri Nevala.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.examples.android.calendar;

import android.content.Context;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Map;


public class CalendarView extends FrameLayout {

    //static final int FIRST_DAY_OF_WEEK = Time.SUNDAY;
    static final int FIRST_DAY_OF_WEEK = Time.MONDAY;

    private static final int ROWS = 6;
    private static final int WEEK = 7;

    private Time month;
    private Time selected;

    //private CalendarGridView gridView;

    private TableLayout tableLayout;

    //private CalendarAdapter adapter;
    private Handler handler;
    private Map<Integer, DayInfo> items; // container to store some random calendar items
    private int offset;
    private DayTile[] dayTiles;
    private LinearLayout container;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.calendar_table, this, true);

        //gridView = (CalendarGridView) findViewById(R.id.gridview);

        //tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        container = (LinearLayout) findViewById(R.id.container);
    }


    public Time getMonth() {
        return month;
    }

    public Time getDate() {
        return selected;
    }

    public void setMonth(Time time) {
        month = new Time(time);
        month.hour = 0;
        month.minute = 0;
        month.second = 0;
        month.monthDay = 1;

        String day = month.format("%Y %m");
        Log.d("SCV", "Updated calendar to: " + day);

        update();
    }

    public void setDate(Time time) {
        selected = new Time(time);

        if (month == null) {
            setMonth(time);
        }

        String day = selected.format("%Y %m %d");
        Log.d("SCV", "Set selected day to: " + day);

        update();
    }

    private void update() {
        if (month != null) {
            refreshDays();
            fillTable();
        }
    }

    private void fillTable() {
        int i = 0;

        for (int r = 0; r < ROWS; r++) {

            LinearLayout row = (LinearLayout) container.getChildAt(r);

            for (int c = 0; c < WEEK; c++) {

                ViewGroup day = (ViewGroup) row.getChildAt(c);
                writeToView(day, i);

                i++;
            }

            //container.addView(row);
        }
    }

/*
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        gridView.setOnChildClickListener(new CalendarGridView.OnChildClickListener() {
            @Override
            public void onChildClick(View child) {

                child.setBackgroundResource(R.drawable.item_background_focused);

                TextView date = (TextView) child.findViewById(R.id.date);

                if (date != null && !date.getText().equals("")) {
                    String msg = "Selected: " + date.getText().toString();
                    //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                    int day = Integer.parseInt(date.getText().toString());

                    selected = new Time(month);
                    selected.monthDay = day;
                }
            }
        });
	    
    }
*/

    public void offsetMonth(int offset) {

        if (month != null) {
            month.month += offset;
            month.normalize(true);
            setMonth(month);
        }
    }

    public View writeToView(ViewGroup v, int position) {

//        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = vi.inflate(R.layout.calendar_item, null);

        if ((position >= offset) && (position - offset) < dayTiles.length) {
            TextView dayView;

            DayTile tile = dayTiles[position - offset];

            //dayView = (TextView)v.findViewById(R.id.date);

            dayView = (TextView)v.getChildAt(0);
            dayView.setText(Integer.toString(tile.getMonthDay()));

            if(tile.isCurrentDay) {
                v.setBackgroundResource(R.drawable.item_background_focused);
            }
            else {
                v.setBackgroundResource(R.drawable.list_item_background);
            }

            // show icon if date is not empty and it exists in the items array
            //ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
            ImageView iw = (ImageView) v.getChildAt(1);

            if(tile.dayInfo != null) {
                iw.setVisibility(View.VISIBLE);
            } else {
                iw.setVisibility(View.INVISIBLE);
            }

            // don't forget to show reused view
            v.setVisibility(View.VISIBLE);

        } else {
            // disable empty days from the beginning
            v.setBackgroundResource(R.drawable.list_item_background);
            v.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    public void refreshDays() {

        Time counter = new Time(month);

        counter.monthDay = 1;
        counter.normalize(true);

        int weekDayOn1st = counter.weekDay;
        int daysInMonth = counter.getActualMaximum(Time.MONTH_DAY);

        offset = weekDayOn1st - FIRST_DAY_OF_WEEK;
        if (offset < 0) {
            offset += 7;
        }

        dayTiles = new DayTile[daysInMonth];

        for (int i = 0; i < daysInMonth; i++) {
            dayTiles[i] = new DayTile(counter);

            if ((selected != null) &&
                    (counter.year == selected.year) &&
                    (counter.month == selected.month) &&
                    (counter.monthDay == selected.monthDay)) {
                dayTiles[i].isCurrentDay = true;
            } else {
                dayTiles[i].isCurrentDay = false;
            }
            counter.monthDay++;
        }
    }
}
