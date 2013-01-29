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
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class SwitcherCalendarView extends LinearLayout {

    private RealViewSwitcher switcher;

    private CalendarView[] calendarViews;

    public SwitcherCalendarView(Context context) {
        super(context);
        init();
    }

    public SwitcherCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        switcher = new RealViewSwitcher(getContext());
        switcher.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        calendarViews = new CalendarView[3];

        for (int i = 0; i < 3; i++) {
            calendarViews[i] = new CalendarView(getContext());
            switcher.addView(calendarViews[i]);
        }

        addView(switcher);

    }

}
