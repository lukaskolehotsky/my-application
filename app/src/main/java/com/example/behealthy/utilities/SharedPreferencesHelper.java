package com.example.behealthy.utilities;

import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String WEIGHT = "weight";
    private static final String HEIGHT = "height";
    private static final String WAIST_CIRCUMFERENCE = "waistCircumference";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean save(SharedPreferenceEntry sharedPreferenceEntry){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GENDER, sharedPreferenceEntry.getGender());
        editor.putString(AGE, sharedPreferenceEntry.getAge());
        editor.putString(HEIGHT, sharedPreferenceEntry.getHeight());
        editor.putString(WEIGHT, sharedPreferenceEntry.getWeight());
        editor.putString(WAIST_CIRCUMFERENCE, sharedPreferenceEntry.getWaistCircumference());
        return editor.commit();
    }

    public SharedPreferenceEntry get() {
        String gender = sharedPreferences.getString(GENDER, "");
        String age = sharedPreferences.getString(AGE, "");
        String height = sharedPreferences.getString(HEIGHT, "");
        String weight = sharedPreferences.getString(WEIGHT, "");
        String waistCircumference = sharedPreferences.getString(WAIST_CIRCUMFERENCE, "");

        SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();
        sharedPreferenceEntry.setGender(gender);
        sharedPreferenceEntry.setAge(age);
        sharedPreferenceEntry.setHeight(height);
        sharedPreferenceEntry.setWeight(weight);
        sharedPreferenceEntry.setWaistCircumference(waistCircumference);

        return sharedPreferenceEntry;
    }

}

