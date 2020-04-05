package com.example.myapplication.activity;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Bmi;
import com.example.myapplication.utilities.FileReader;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RfmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String GENDER = "gender";
    public static final String HEIGHT = "height";
    public static final String WAIST_CIRCUMFERENCE = "waistCircumference";
    public static final String AGE = "age";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfm);

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);
        createArrayAdapter(R.id.heightSpinner, R.array.heightList);
        createArrayAdapter(R.id.waistCircumferenceSpinner, R.array.waistCircumferenceList);

        Button resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView rfmTextView = (TextView) findViewById(R.id.rfmTextView);
                TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);

                String gender = loadData(GENDER);
                double height = Double.parseDouble(loadData(HEIGHT));
                double waistCircumference = Double.parseDouble(loadData(WAIST_CIRCUMFERENCE));
                int age = Integer.parseInt(loadData(AGE));

                ArrayList<JsonProperty> jsonPropertyList = new FileReader(getBaseContext()).processFile(R.raw.locations);

                double rfm;
                String category = null;

                if(gender.equals("woman")) {
                    rfm = 76 - (20 * height) / waistCircumference;
                } else {
                    rfm = 64 - (20 * height) / waistCircumference;
                }

                for(AgeWithBmis ageWithBmis: jsonPropertyList.get(0).getAgeWithBmis()) {
                    if(ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                        for(Bmi bmi: ageWithBmis.getBmis()) {
                            if(bmi.getFrom() < rfm && bmi.getTo() > rfm) {
                                category = bmi.getCategory();
                            }
                        }
                    }
                }

                rfmTextView.setText(df2.format(rfm));
                categoryTextView.setText(category);
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

        if(firstItemName.equals("Height")){
            saveData(HEIGHT, text);
        }

        if(firstItemName.equals("Waist circumference")){
            saveData(WAIST_CIRCUMFERENCE, text);
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
