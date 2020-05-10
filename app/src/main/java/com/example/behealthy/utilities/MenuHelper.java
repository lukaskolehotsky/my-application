package com.example.behealthy.utilities;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.activity.BmiActivity;
import com.example.behealthy.activity.DailyFeedActivity;
import com.example.behealthy.activity.FoodActivity;
import com.example.behealthy.activity.RfmActivity;

public class MenuHelper {

    private Context packageContext;

    public MenuHelper(Context packageContext) {
        this.packageContext = packageContext;
    }

    public Intent chooseIntent(int itemId){
        if(itemId == R.id.vitaminsItem){
            return new Intent(packageContext, DailyFeedActivity.class);
        }

        if(itemId == R.id.rfmItem){
            return new Intent(packageContext, RfmActivity.class);
        }

        if(itemId == R.id.bmiItem){
            return new Intent(packageContext, BmiActivity.class);
        }

        if(itemId == R.id.foodItem){
            return new Intent(packageContext, FoodActivity.class);
        }

        return null;
    }

}
