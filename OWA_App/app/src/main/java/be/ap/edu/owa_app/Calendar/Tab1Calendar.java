package be.ap.edu.owa_app.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.ap.edu.owa_app.R;

/**
 * Created by isa_l on 12-01-18.
 */

class Tab1Calendar extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_calendar, container, false);


        /*final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);*/

        final TextView text = rootView.findViewById(R.id.section_label);
        text.setText(dateFormatMonth.format(new Date()));

        compactCalendar = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October

        com.github.sundeepk.compactcalendarview.domain.Event ev1 = new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLACK, 1514779505000L, "Teachers' Professional Day");
        compactCalendar.addEvent(ev1);

        com.github.sundeepk.compactcalendarview.domain.Event ev2 = new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLACK, 1512446705000L, "Test");
        compactCalendar.addEvent(ev2);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                text.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }

        });


        return rootView;
    }
}
