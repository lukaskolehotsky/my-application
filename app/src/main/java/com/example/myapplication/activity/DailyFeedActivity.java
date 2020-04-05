package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.utilities.FileReader;

import java.util.ArrayList;
import java.util.List;

public class DailyFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String GENDER = "gender";
    public static final String AGE = "age";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_feed);

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);

        Button listViewButton = (Button) findViewById(R.id.listViewButton);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gender = loadData(GENDER);
                int age = Integer.parseInt(loadData(AGE));

                ArrayList<JsonProperty> jsonPropertyList = new FileReader(getBaseContext()).processFile(R.raw.locations);
                List<Vitamin> vitamins = gender.equals("woman") ? jsonPropertyList.get(0).getWomanVitamins() : jsonPropertyList.get(0).getManVitamins();

                List<Vitamin> vitaminList = new ArrayList<Vitamin>();
                for(Vitamin vitamin: vitamins) {
                    if(vitamin.getFrom() <= age && vitamin.getTo() >= age) {
                        vitaminList.add(vitamin);
                    }
                }

                Intent startIntent = new Intent(DailyFeedActivity.this, VitaminListActivity.class);
                startIntent.putParcelableArrayListExtra("VITAMIN_LIST", (ArrayList) vitaminList);

                startActivity(startIntent);
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
        if(item.getItemId() == R.id.bmiItem){
            Intent startIntent = new Intent(getApplicationContext(), BmiActivity.class);
            startActivity(startIntent);
        }

        if(item.getItemId() == R.id.rfmItem){
            Intent startIntent = new Intent(getApplicationContext(), RfmActivity.class);
            startActivity(startIntent);
        }

        if(item.getItemId() == R.id.vitaminsItem){
            Intent startIntent = new Intent(getApplicationContext(), DailyFeedActivity.class);
            startActivity(startIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createArrayAdapter(@IdRes int spinnerId, @ArrayRes int resourceListId){
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
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
        String text = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if(firstItemName.equals("Gender")){
            saveData(GENDER, text);
        }

        if(firstItemName.equals("Age")){
            saveData(AGE, text);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveData(String name, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(name, value);

        editor.apply();

        System.out.println("SAVING" + name + " " + value);
    }

    private String loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String value = sharedPreferences.getString(name, "no value");

        System.out.println("LOADING" + name);

        return value;
    }

}
