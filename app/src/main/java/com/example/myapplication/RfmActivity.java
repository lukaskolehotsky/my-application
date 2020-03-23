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

public class RfmActivity extends AppCompatActivity {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfm);

        Button resultButton = (Button) findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText genderEditText = (EditText) findViewById(R.id.genderEditText);
                EditText heightEditText = (EditText) findViewById(R.id.heightEditText);
                EditText waistCircumferenceEditText = (EditText) findViewById(R.id.waistCircumferenceEditText);
                EditText ageEditText = (EditText) findViewById(R.id.ageEditText);

                TextView rfmTextView = (TextView) findViewById(R.id.rfmTextView);
                TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);

                String gender = genderEditText.getText().toString();
                double height = Double.parseDouble(heightEditText.getText().toString());
                double waistCircumference = Double.parseDouble(waistCircumferenceEditText.getText().toString());
                int age = Integer.parseInt(ageEditText.getText().toString());

                ArrayList<AgeWithBmis> ageWithBmisList = new FileReader(getBaseContext()).processFile(R.raw.locations);

                double rfm;
                String category = null;

                if(gender.equals("woman")) {
                    rfm = 76 - (20 * height) / waistCircumference;
                } else {
                    rfm = 64 - (20 * height) / waistCircumference;
                }

                for(AgeWithBmis ageWithBmis: ageWithBmisList) {
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
}
