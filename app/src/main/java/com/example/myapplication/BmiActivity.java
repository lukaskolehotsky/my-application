package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Bmi;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.utilities.FileReader;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BmiActivity extends AppCompatActivity {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        Button resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weightEditText = (EditText) findViewById(R.id.weightEditText);
                EditText heightEditText = (EditText) findViewById(R.id.heightEditText);
                EditText ageEditText = (EditText) findViewById(R.id.ageEditText);
                TextView bmiTextView = (TextView) findViewById(R.id.bmiTextView);
                TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);

                double weight = Double.parseDouble(weightEditText.getText().toString());
                double height = Double.parseDouble(heightEditText.getText().toString());
                int age = Integer.parseInt(ageEditText.getText().toString());

//                ArrayList<AgeWithBmis> ageWithBmisList = new FileReader(getBaseContext()).processFile(R.raw.locations);
                ArrayList<JsonProperty> jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

                for(Vitamin vitamin: jsonProperty.get(0).getWomanVitamins()){
                    System.out.println(vitamin);
                }
                for(Vitamin vitamin: jsonProperty.get(0).getManVitamins()){
                    System.out.println(vitamin);
                }

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

}
