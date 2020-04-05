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
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Bmi;
import com.example.myapplication.utilities.FileReader;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BmiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String AGE = "age";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        createArrayAdapter(R.id.ageSpinner, R.array.ageList);
        createArrayAdapter(R.id.weightSpinner, R.array.weightList);
        createArrayAdapter(R.id.heightSpinner, R.array.heightList);

        Button resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView bmiTextView = (TextView) findViewById(R.id.bmiTextView);
                TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);

                double weight = Double.parseDouble(loadData(WEIGHT));
                double height = Double.parseDouble(loadData(HEIGHT));
                int age = Integer.parseInt(loadData(AGE));

                ArrayList<JsonProperty> jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

                double calculatedBmi = weight / ((height / 100) * (height / 100));
                String cat = null;
                for(AgeWithBmis ageWithBmis: jsonProperty.get(0).getAgeWithBmis()) {
                    if(ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                        for(Bmi bmi: ageWithBmis.getBmis()) {
                            if(bmi.getFrom() < calculatedBmi && bmi.getTo() > calculatedBmi) {
                                cat = bmi.getCategory();
                            }
                        }
                    }
                }

                bmiTextView.setText(df2.format(calculatedBmi));
                categoryTextView.setText(cat);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if(firstItemName.equals("Age")){
            saveData(AGE, text);
        }

        if(firstItemName.equals("Weight")){
            saveData(WEIGHT, text);
        }

        if(firstItemName.equals("Height")){
            saveData(HEIGHT, text);
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

}
