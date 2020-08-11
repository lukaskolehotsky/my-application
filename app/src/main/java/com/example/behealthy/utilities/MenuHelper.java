package com.example.behealthy.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.behealthy.R;
import com.example.behealthy.activity.BmiActivity;
import com.example.behealthy.activity.CalendarActivity;
import com.example.behealthy.activity.DailyDoseActivity;
import com.example.behealthy.activity.RfmActivity;

public class MenuHelper {

    private Context packageContext;

    public MenuHelper(Context packageContext) {
        this.packageContext = packageContext;
    }

    public Intent chooseIntent(int itemId){
        if(itemId == R.id.dailyDoseItem){
            return new Intent(packageContext, DailyDoseActivity.class);
        }

        if(itemId == R.id.rfmItem){
            return new Intent(packageContext, RfmActivity.class);
        }

        if(itemId == R.id.bmiItem){
            return new Intent(packageContext, BmiActivity.class);
        }

        if(itemId == R.id.calendarItem){
            return new Intent(packageContext, CalendarActivity.class);
        }

        return null;
    }

}
