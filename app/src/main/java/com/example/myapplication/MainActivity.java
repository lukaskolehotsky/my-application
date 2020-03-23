package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bmiActivityButton = (Button) findViewById(R.id.bmiActivityButton);
        bmiActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), BmiActivity.class);
                startActivity(startIntent);
            }
        });

        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ageEditText = (EditText) findViewById(R.id.ageEditText);
                TextView resultTextView = (TextView) findViewById(R.id.bmiTextView);

                resultTextView.setText(ageEditText.getText());
            }
        });
    }
}
