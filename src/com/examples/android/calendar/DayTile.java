package com.examples.android.calendar;

import android.text.format.Time;

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
}
