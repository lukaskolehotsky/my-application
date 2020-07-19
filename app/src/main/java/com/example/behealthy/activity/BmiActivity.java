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
import com.example.behealthy.constants.Constants;
import com.example.behealthy.model.bmi.BmiCategory;
import com.example.behealthy.service.BmiService;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BmiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private JsonService jsonService;
    private BmiService bmiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        jsonService = new JsonService(getBaseContext());
        bmiService = new BmiService(jsonService);

        createArrayAdapter(R.id.ageSpinner, R.array.ageList);
        createArrayAdapter(R.id.weightSpinner, R.array.weightList);
        createArrayAdapter(R.id.heightSpinner, R.array.heightList);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setOnClickListener(v -> {
            TextView bmiTextView = findViewById(R.id.bmiTextView);
            TextView categoryTextView = findViewById(R.id.categoryTextView);

            SharedPreferenceEntry entry = sharedPreferencesHelper.get();
            String loadedWeight = entry.getWeight();
            String loadedHeight = entry.getHeight();
            String loadedAge = entry.getAge();

            boolean validateSpinnersClick = validateSpinnersClick(loadedAge, loadedWeight, loadedHeight);
            if (validateSpinnersClick) {
                double weight = Double.parseDouble(loadedWeight);
                double height = Double.parseDouble(loadedHeight);
                int age = Integer.parseInt(loadedAge);

                BmiCategory bmiCategory = bmiService.calculateBmi(weight, height, age);

                bmiTextView.setText(String.valueOf(bmiCategory.getCalculatedBmi()));
                categoryTextView.setText(bmiCategory.getCategory());
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

    private boolean validateSpinnersClick(String loadedAge, String loadedWeight, String loadedHeight) {
        if (loadedWeight.equals(Constants.WEIGHT.label) && loadedHeight.equals(Constants.HEIGHT.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age, weight, height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedWeight.equals(Constants.WEIGHT.label) && loadedHeight.equals(Constants.HEIGHT.label)) {
            Toast.makeText(getBaseContext(), "Please choose weight, height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedWeight.equals(Constants.WEIGHT.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age, weight", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedHeight.equals(Constants.HEIGHT.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age, height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedWeight.equals(Constants.WEIGHT.label)) {
            Toast.makeText(getBaseContext(), "Please choose weight", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedHeight.equals(Constants.HEIGHT.label)) {
            Toast.makeText(getBaseContext(), "Please choose height", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
