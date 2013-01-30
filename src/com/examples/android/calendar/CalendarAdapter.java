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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {

    private Time currentDate;

    private int firstDayOfWeek;
	
	private Context context;

    private ArrayList<String> items;

    private DayTile[] dayTiles;


    public CalendarAdapter(Context context, Calendar monthCalendar, int firstDayOfWeek) {

        this.context = context;
        this.firstDayOfWeek = firstDayOfWeek;

        currentDate = new Time();
        currentDate.year = monthCalendar.get(Calendar.YEAR);
        currentDate.month = monthCalendar.get(Calendar.MONTH);
        currentDate.monthDay = monthCalendar.get(Calendar.DAY_OF_MONTH);
        currentDate.normalize(false);

        this.items = new ArrayList<String>();
        refreshDays();
    }
    
    public void setItems(ArrayList<String> items) {
    	for(int i = 0;i != items.size();i++){
    		if(items.get(i).length()==1) {
    		items.set(i, "0" + items.get(i));
    		}
    	}
    	this.items = items;
    }

    @Override
    public int getCount() {
        return dayTiles.length;
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

        DayTile tile = dayTiles[position];

        View v = convertView;
    	TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        	
        }
        dayView = (TextView)v.findViewById(R.id.date);
        
        if(tile.containsDay) {
            // mark current day as focused
            if(tile.isCurrentDay) {
                v.setBackgroundResource(R.drawable.item_background_focused);
            }
            else {
                v.setBackgroundResource(R.drawable.list_item_background);
            }
        } else {
            // disable empty days from the beginning
            v.setVisibility(View.INVISIBLE);
        }

        dayView.setText(Integer.toString(tile.getMonthDay()));
/*
        // create date string for comparison
        String date = days[position];
    	
        if(date.length()==1) {
    		date = "0"+date;
    	}
    	String monthStr = ""+(month.get(Calendar.MONTH)+1);
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
       
        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        if(date.length()>0 && items!=null && items.contains(date)) {        	
        	iw.setVisibility(View.VISIBLE);
        }
        else {
        	iw.setVisibility(View.INVISIBLE);
        }
        */
        return v;
    }
    
    public void refreshDays() {
    	// clear items
    	items.clear();

        Time counter = new Time(currentDate);

        counter.monthDay = 1;
        counter.normalize(false);

        int weekDayOn1st = counter.weekDay;
        int daysInMonth = counter.getActualMaximum(Time.MONTH_DAY);

        int emptyTilesCount = weekDayOn1st - firstDayOfWeek;
        int fullLength = emptyTilesCount + daysInMonth;

        dayTiles = new DayTile[emptyTilesCount + daysInMonth];

        // At first, fill with empty
        for (int i = 0; i < emptyTilesCount; i++) {
            dayTiles[i] = new DayTile();
        }

        // Rest with values
        for (int i = emptyTilesCount; i < fullLength; i++) {
            dayTiles[i] = new DayTile(counter);

            if (currentDate.monthDay == counter.monthDay) {
                dayTiles[i].isCurrentDay = true;
            }
            counter.monthDay++;
        }
    }

}