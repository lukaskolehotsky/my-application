package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Bmi;
import com.example.myapplication.utilities.FileReader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BmiActivity extends AppCompatActivity {

    List<AgeWithBmis> ageWithBmis;
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

                ArrayList<AgeWithBmis> ageWithBmisList = new FileReader(getBaseContext()).processFile(R.raw.locations);

                double calculatedBmi = weight / ((height / 100) * (height / 100));
                String cat = null;
                for(AgeWithBmis ageWithBmis: ageWithBmisList) {
                    if(ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                        for(Bmi bmi: ageWithBmis.getBmis()) {
                            if(bmi.getFrom() < calculatedBmi && bmi.getTo() > calculatedBmi) {
                                cat = bmi.getCategory();
                            }
                        }
                    }
                }

                bmiTextView.setText(df2.format(calculatedBmi) + "");
                categoryTextView.setText(cat);
            }
        });
    }
}
