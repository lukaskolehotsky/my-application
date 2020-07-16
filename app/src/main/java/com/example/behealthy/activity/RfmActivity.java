package com.example.behealthy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.constants.Constants;
import com.example.behealthy.model.AgeWithBmis;
import com.example.behealthy.model.Bmi;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class RfmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private JsonService jsonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfm);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        jsonService = new JsonService(getBaseContext());

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);
        createArrayAdapter(R.id.heightSpinner, R.array.heightList);
        createArrayAdapter(R.id.waistCircumferenceSpinner, R.array.waistCircumferenceList);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rfmTextView = findViewById(R.id.rfmTextView);
                TextView categoryTextView = findViewById(R.id.categoryTextView);

                SharedPreferenceEntry entry = sharedPreferencesHelper.get();
                String loadedGender = entry.getGender();
                String loadedHeight = entry.getHeight();
                String loadedWaistCircumference = entry.getWaistCircumference();
                String loadedAge = entry.getAge();

                boolean validateSpinnersClick = validateSpinnersClick(loadedGender, loadedHeight, loadedWaistCircumference, loadedAge);
                if (validateSpinnersClick) {
                    double height = Double.parseDouble(loadedHeight);
                    double waistCircumference = Double.parseDouble(loadedWaistCircumference);
                    int age = Integer.parseInt(loadedAge);

                    double rfm;
                    String category = null;

                    if (loadedGender.equals(Constants.WOMAN.label)) {
                        rfm = 76 - (20 * height) / waistCircumference;
                    } else {
                        rfm = 64 - (20 * height) / waistCircumference;
                    }

                    for (AgeWithBmis ageWithBmis : jsonService.processFile(R.raw.agewithbmis).getAgeWithBmis()) {
                        if (ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                            for (Bmi bmi : ageWithBmis.getBmis()) {
                                if (bmi.getFrom() < rfm && bmi.getTo() > rfm) {
                                    category = bmi.getCategory();
                                }
                            }
                        }
                    }

                    rfmTextView.setText(df2.format(rfm));
                    categoryTextView.setText(category);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        menuHelper = new MenuHelper(getApplicationContext());
        Intent startIntent = menuHelper.chooseIntent(item.getItemId());
        startActivity(startIntent);

        return super.onOptionsItemSelected(item);
    }

    public void createArrayAdapter(@IdRes int spinnerId, @ArrayRes int resourceListId) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(resourceListId)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, firstItemName, value);
        sharedPreferencesHelper.save(sharedPreferenceEntry);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validateSpinnersClick(String loadedGender, String loadedHeight, String loadedWaistCircumference, String loadedAge) {
        if (loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, height, waist circumference, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose height, waist circumference, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, waist circumference, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && !loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, height, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, height, waist circumference", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose waist circumference, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && !loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose height, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label)) {
            Toast.makeText(getBaseContext(), "Please choose height, waist circumference", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && !loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, waist circumference", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && !loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && !loadedHeight.equals(Constants.HEIGHT.label) && loadedWaistCircumference.equals(Constants.WAIST_CIRCUMFERENCE.label)) {
            Toast.makeText(getBaseContext(), "Please choose waist circumference", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && loadedHeight.equals(Constants.HEIGHT.label)) {
            Toast.makeText(getBaseContext(), "Please choose height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
