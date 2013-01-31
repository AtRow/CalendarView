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
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


public class SwitcherCalendarView extends FrameLayout {

    private static final int LEFT = 0;
    private static final int CENTER = 1;
    private static final int RIGHT = 2;

    private HorizontalPager pager;

    private CalendarView[] calendarViews;

    private FrameLayout leftScreen;
    private FrameLayout centerScreen;
    private FrameLayout rightScreen;

    private CalendarView prevCalendar;
    private CalendarView currCalendar;
    private CalendarView nextCalendar;


    public SwitcherCalendarView(Context context) {
        super(context);
        init();
    }

    public SwitcherCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        pager = new HorizontalPager(getContext());
        pager.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        leftScreen = new FrameLayout(getContext());
        centerScreen = new FrameLayout(getContext());
        rightScreen = new FrameLayout(getContext());

        prevCalendar = new CalendarView(getContext());
        currCalendar = new CalendarView(getContext());
        nextCalendar = new CalendarView(getContext());

        calendarViews = new CalendarView[] {prevCalendar, currCalendar, nextCalendar};

        Time time = new Time();
        time.setToNow();
        time.monthDay = 1;
        time.hour = 0;
        time.minute = 0;
        time.second = 0;
        time.month--;

        for (int i = 0; i < 3; i++) {
            time.normalize(false);
            calendarViews[i].setDate(time);
            time.month++;
        }


        leftScreen.addView(prevCalendar);
        centerScreen.addView(currCalendar);
        rightScreen.addView(nextCalendar);

        pager.addView(leftScreen);
        pager.addView(centerScreen);
        pager.addView(rightScreen);

        pager.setCurrentScreen(CENTER, false);

        pager.setOnScreenSwitchListener(onScreenSwitchListener);


        final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onSingleClick();
                return true;
            }
        });

        pager.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        addView(pager);
    }

    private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener = new HorizontalPager.OnScreenSwitchListener() {
        public void onScreenSwitched(final int screen) {
            /*
            * this method is executed if a screen has been activated, i.e. the screen is
			* completely visible and the animation has stopped (might be useful for
			* removing / adding new views)
			*/

            //
            switch (screen) {

                case LEFT:

                    // release currCalendar
                    centerScreen.removeAllViews();
                    // release prevCalendar
                    leftScreen.removeAllViews();
                    centerScreen.addView(prevCalendar);
                    pager.setCurrentScreen(CENTER, false);

                    // release nextCalendar
                    rightScreen.removeAllViews();

                    rightScreen.addView(currCalendar);
                    leftScreen.addView(nextCalendar);

                    nextCalendar.offsetMonth(-3);

                    prevCalendar = (CalendarView) leftScreen.getChildAt(0);
                    currCalendar = (CalendarView) centerScreen.getChildAt(0);
                    nextCalendar = (CalendarView) rightScreen.getChildAt(0);

                    break;

                case RIGHT:

                    // release currCalendar
                    centerScreen.removeAllViews();
                    // release nextCalendar
                    rightScreen.removeAllViews();
                    centerScreen.addView(nextCalendar);
                    pager.setCurrentScreen(CENTER, false);

                    // release prevCalendar
                    leftScreen.removeAllViews();

                    rightScreen.addView(prevCalendar);
                    leftScreen.addView(currCalendar);

                    prevCalendar.offsetMonth(+3);

                    prevCalendar = (CalendarView) leftScreen.getChildAt(0);
                    currCalendar = (CalendarView) centerScreen.getChildAt(0);
                    nextCalendar = (CalendarView) rightScreen.getChildAt(0);

                    break;

            }
        }
    };

    private void onSingleClick() {

        Log.e("RVS", "!! onSingleClick");

        int i = pager.getCurrentScreen();

        FrameLayout fl = (FrameLayout) pager.getChildAt(i);
        CalendarView selectedView = (CalendarView) fl.getChildAt(0);

        //CalendarView selectedView = calendarViews[i];

        String day = selectedView.getDate().format("%Y.%m.%d %H:%M:%S");

        String msg = "Selected: " + day;
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
