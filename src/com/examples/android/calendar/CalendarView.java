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

    private Time time;

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


    public Time getDate() {
        return time;
    }

    public void setDate(Time time) {
        this.time = new Time(time);

        String day = this.time.format("%Y %m");
        Log.w("SCV", "Updated calendar to: " + day);

        if (time != null) {
            adapter = new CalendarAdapter(getContext(), time, FIRST_DAY_OF_WEEK);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

/*

	    TextView title  = (TextView) findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

	    TextView previous  = (TextView) findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
	    
	    TextView next  = (TextView) findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				}
				refreshCalendar();
				
			}
		});

*/
        gridView.setOnChildClickListener(new CalendarGridView.OnChildClickListener() {
            @Override
            public void onChildClick(View child) {

                child.setBackgroundResource(R.drawable.item_background_focused);

                TextView date = (TextView) child.findViewById(R.id.date);

                if (date != null && !date.getText().equals("")) {
                    String msg = "Selected: " + date.getText().toString();
                    //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                    int day = Integer.parseInt(date.getText().toString());
                    time.monthDay = day;
                }
            }
        });
	    
    }

	
/*	public void refreshCalendar()
	{
		TextView title  = (TextView) findViewById(R.id.title);
		
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); // generate some random calendar items				
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items = new HashMap<Integer, DayInfo>();
*//*			// format random values. You can implement a dedicated class to provide real values
			for(int i=0;i<31;i++) {
				Random r = new Random();

				if(r.nextInt(10)>6)
				{
					items.put(i, new DayInfo());
				}
			}*//*

            items.put(1, new DayInfo());
            items.put(2, new DayInfo());
            items.put(3, new DayInfo());
            items.put(6, new DayInfo());
            items.put(15, new DayInfo());
            items.put(30, new DayInfo());
            items.put(31, new DayInfo());
            items.put(29, new DayInfo());

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};*/
}
