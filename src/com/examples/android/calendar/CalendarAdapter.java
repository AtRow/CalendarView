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
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

public class CalendarAdapter extends BaseAdapter {

    private Time currentDate;

    private int firstDayOfWeek;
	
	private Context context;

    private DayTile[] dayTiles;

    private int offset;


    public CalendarAdapter(Context context, Time time, int firstDayOfWeek) {

        this.context = context;
        this.firstDayOfWeek = firstDayOfWeek;

        currentDate = new Time(time);

        refreshDays();
    }
    
    public void setItems(Map<Integer, DayInfo> items) {

    	for(int i = 0; i < dayTiles.length; i++){
            if (items.containsKey(i+1)) {
                DayInfo info = items.get(i+1);
                dayTiles[i].dayInfo = info;
            } else {
                dayTiles[i].dayInfo = null;
            }
    	}
    }

    @Override
    public int getCount() {
        return offset + dayTiles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
    	TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        	
        }

        if (position >= offset) {
            DayTile tile = dayTiles[position - offset];

            dayView = (TextView)v.findViewById(R.id.date);
            dayView.setText(Integer.toString(tile.getMonthDay()));
        
            if(tile.isCurrentDay) {
                v.setBackgroundResource(R.drawable.item_background_focused);
            }
            else {
                v.setBackgroundResource(R.drawable.list_item_background);
            }

            // show icon if date is not empty and it exists in the items array
            ImageView iw = (ImageView)v.findViewById(R.id.date_icon);

            if(tile.dayInfo != null) {
                iw.setVisibility(View.VISIBLE);
            } else {
                iw.setVisibility(View.INVISIBLE);
            }

            // don't forget to show reused view
            v.setVisibility(View.VISIBLE);

        } else {
            // disable empty days from the beginning
            v.setVisibility(View.INVISIBLE);
        }

        return v;
    }
    
    public void refreshDays() {

        Time counter = new Time(currentDate);

        counter.monthDay = 1;
        counter.normalize(false);

        int weekDayOn1st = counter.weekDay;
        int daysInMonth = counter.getActualMaximum(Time.MONTH_DAY);

        offset = weekDayOn1st - firstDayOfWeek;

        dayTiles = new DayTile[daysInMonth];

        for (int i = 0; i < daysInMonth; i++) {
            dayTiles[i] = new DayTile(counter);

            if (currentDate.monthDay == counter.monthDay) {
                dayTiles[i].isCurrentDay = true;
            }
            counter.monthDay++;
        }
    }

}