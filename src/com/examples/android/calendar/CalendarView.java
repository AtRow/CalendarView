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
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;


public class CalendarView extends FrameLayout {

    //static final int FIRST_DAY_OF_WEEK = Time.SUNDAY;
    static final int FIRST_DAY_OF_WEEK = Time.MONDAY;

    private Time month;
    private Time selected;

    private CalendarGridView gridView;

    private CalendarAdapter adapter;
    private Handler handler;
    private Map<Integer, DayInfo> items; // container to store some random calendar items

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
        li.inflate(R.layout.calendar, this, true);

        gridView = (CalendarGridView) findViewById(R.id.gridview);
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
            adapter = new CalendarAdapter(getContext(), month, selected, FIRST_DAY_OF_WEEK);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

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

    public void offsetMonth(int offset) {

        if (month != null) {
            month.month += offset;
            month.normalize(true);
            setMonth(month);
        }
    }
}
