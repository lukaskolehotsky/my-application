package com.example.behealthy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.behealthy.R;

import java.util.ArrayList;

import static com.example.behealthy.constants.Constants.AGE;
import static com.example.behealthy.constants.Constants.GENDER;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;

public class CalendarActivity extends Activity {
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_calendar);

        //initializes the calendarview
        initializeCalendar();
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Intent startIntent = new Intent(getApplicationContext(), FoodActivity.class);
                startIntent.putExtra("YEAR", String.valueOf(year));
                startIntent.putExtra("MONTH", String.valueOf(month+1));
                startIntent.putExtra("DAY", String.valueOf(day));
                startActivity(startIntent);
            }
        });
    }

}
