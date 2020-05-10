package com.example.behealthy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.constants.Constants;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;

import java.util.ArrayList;
import java.util.List;

import static com.example.behealthy.constants.Constants.VITAMIN_LIST;
import static com.example.behealthy.constants.Constants.GENDER;
import static com.example.behealthy.constants.Constants.AGE;

public class DailyFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_feed);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);

        Button listViewButton = findViewById(R.id.listViewButton);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceEntry entry = sharedPreferencesHelper.get();
                String loadedGender = entry.getGender();
                String loadedAge = entry.getAge();

                boolean validateSpinnersClick = validateSpinnersClick(loadedGender, loadedAge);
                if(validateSpinnersClick){
                    int age = Integer.parseInt(loadedAge);

                    JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);
                    List<Vitamin> vitamins = loadedGender.equals(Constants.WOMAN.label) ? jsonProperty.getWomanVitamins() : jsonProperty.getManVitamins();

                    List<Vitamin> vitaminList = new ArrayList<>();
                    for(Vitamin vitamin: vitamins) {
                        if(vitamin.getFrom() <= age && vitamin.getTo() >= age) {
                            vitaminList.add(vitamin);
                        }
                    }

                    Intent startIntent = new Intent(DailyFeedActivity.this, VitaminListActivity.class);
                    startIntent.putParcelableArrayListExtra(VITAMIN_LIST.label, (ArrayList<? extends Parcelable>) vitaminList);
                    startIntent.putExtra(GENDER.label, loadedGender);
                    startIntent.putExtra(AGE.label, loadedAge);
                    startActivity(startIntent);
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

    public void createArrayAdapter(@IdRes int spinnerId, @ArrayRes int resourceListId){
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

    private boolean validateSpinnersClick(String loadedGender, String loadedAge){
        if(loadedGender.equals(Constants.GENDER.label) && loadedAge.equals(Constants.AGE.label)){
            Toast.makeText(getBaseContext(), "Please choose gender, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!loadedGender.equals(Constants.GENDER.label) && loadedAge.equals(Constants.AGE.label)){
            Toast.makeText(getBaseContext(), "Please choose age", Toast.LENGTH_SHORT).show();
            return false;
        } else if(loadedGender.equals(Constants.GENDER.label)){
            Toast.makeText(getBaseContext(), "Please choose gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
