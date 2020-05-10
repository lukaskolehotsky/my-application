package com.example.behealthy.utilities;

import android.content.SharedPreferences;

import com.example.behealthy.constants.Constants;
import java.util.Set;

import static com.example.behealthy.constants.Constants.GENDER;
import static com.example.behealthy.constants.Constants.AGE;
import static com.example.behealthy.constants.Constants.HEIGHT;
import static com.example.behealthy.constants.Constants.WEIGHT;
import static com.example.behealthy.constants.Constants.WAIST_CIRCUMFERENCE;
import static com.example.behealthy.constants.Constants.VEGETABLES_JSON;

public class SharedPreferencesHelper {

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean save(SharedPreferenceEntry sharedPreferenceEntry){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GENDER.label, sharedPreferenceEntry.getGender());
        editor.putString(AGE.label, sharedPreferenceEntry.getAge());
        editor.putString(HEIGHT.label, sharedPreferenceEntry.getHeight());
        editor.putString(WEIGHT.label, sharedPreferenceEntry.getWeight());
        editor.putString(WAIST_CIRCUMFERENCE.label, sharedPreferenceEntry.getWaistCircumference());
        editor.putString(VEGETABLES_JSON.label, sharedPreferenceEntry.getVegetablesJson());
        return editor.commit();
    }

    public SharedPreferenceEntry get() {
        String gender = sharedPreferences.getString(GENDER.label, "");
        String age = sharedPreferences.getString(AGE.label, "");
        String height = sharedPreferences.getString(HEIGHT.label, "");
        String weight = sharedPreferences.getString(WEIGHT.label, "");
        String waistCircumference = sharedPreferences.getString(WAIST_CIRCUMFERENCE.label, "");
        String vegetablesJson = sharedPreferences.getString(VEGETABLES_JSON.label, "");

        SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();
        sharedPreferenceEntry.setGender(gender);
        sharedPreferenceEntry.setAge(age);
        sharedPreferenceEntry.setHeight(height);
        sharedPreferenceEntry.setWeight(weight);
        sharedPreferenceEntry.setWaistCircumference(waistCircumference);
        sharedPreferenceEntry.setVegetablesJson(vegetablesJson);

        return sharedPreferenceEntry;
    }

    public void populateSharedPreferenceEntry(SharedPreferenceEntry sharedPreferenceEntry, String firstItemName, String value){
        if(firstItemName.equals(Constants.AGE.label)){
            sharedPreferenceEntry.setAge(value);
        }

        if(firstItemName.equals(Constants.WEIGHT.label)){
            sharedPreferenceEntry.setWeight(value);
        }

        if(firstItemName.equals(Constants.HEIGHT.label)){
            sharedPreferenceEntry.setHeight(value);
        }

        if(firstItemName.equals(Constants.GENDER.label)){
            sharedPreferenceEntry.setGender(value);
        }

        if(firstItemName.equals(Constants.WAIST_CIRCUMFERENCE.label)){
            sharedPreferenceEntry.setWaistCircumference(value);
        }

        if(firstItemName.equals(Constants.VEGETABLES_JSON.label)){
            System.out.println("ADDING LIST INTO SH_PRFCS - " + value);
            sharedPreferenceEntry.setVegetablesJson(value);
        }

    }

}

