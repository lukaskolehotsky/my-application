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
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.constants.Constants;
import com.example.behealthy.model.RfmCategory;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.service.RfmService;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.example.behealthy.utilities.UtilsHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RfmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TITLE = "Calculate RFM";
    private static final int MARGIN_BOTTOM = 20;
    private static final Float TEXT_SIZE = 22F;
    private static final int MARGIN_TOP = 70;

    private RfmService rfmService;
    private UtilsHelper utilsHelper;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfm);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        JsonService jsonService = new JsonService(getBaseContext());
        rfmService = new RfmService(jsonService);

        utilsHelper = new UtilsHelper();
        utilsHelper.createTitle(findViewById(R.id.titleTextView), TITLE, TEXT_SIZE, MARGIN_TOP, MARGIN_BOTTOM);

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);
        createArrayAdapter(R.id.heightSpinner, R.array.heightList);
        createArrayAdapter(R.id.waistCircumferenceSpinner, R.array.waistCircumferenceList);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setOnClickListener(v -> {
            SharedPreferenceEntry entry = sharedPreferencesHelper.get();
            String loadedGender = entry.getGender();
            String loadedHeight = entry.getHeight();
            String loadedWaistCircumference = entry.getWaistCircumference();
            String loadedAge = entry.getAge();

            boolean validateSpinnersClick = validateSpinnersClick(loadedGender, loadedHeight, loadedWaistCircumference, loadedAge);
            if (validateSpinnersClick) {
                RfmCategory rfmCategory = rfmService.calculateRfm(loadedHeight, loadedWaistCircumference, loadedAge, loadedGender);

                utilsHelper.createTitle(findViewById(R.id.rfmTextView), String.valueOf(rfmCategory.getCalculatedRfm()), TEXT_SIZE, MARGIN_TOP, MARGIN_BOTTOM);
                utilsHelper.createTitle(findViewById(R.id.categoryTextView), rfmCategory.getCategory(), TEXT_SIZE, 20, MARGIN_BOTTOM);
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
        MenuHelper menuHelper = new MenuHelper(getApplicationContext());
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
