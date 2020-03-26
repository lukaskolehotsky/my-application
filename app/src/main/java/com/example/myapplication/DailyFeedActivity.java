package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.utilities.FileReader;

import java.util.ArrayList;
import java.util.List;

public class DailyFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_feed);

        Button listViewButton = (Button) findViewById(R.id.listViewButton);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText genderEditText = (EditText) findViewById(R.id.genderEditText);
                EditText ageEditText = (EditText) findViewById(R.id.ageEditText);

                String gender = genderEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());

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

}
