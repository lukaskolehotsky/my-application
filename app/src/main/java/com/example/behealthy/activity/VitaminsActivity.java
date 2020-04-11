package com.example.behealthy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behealthy.R;
import com.example.behealthy.adapter.ListViewAdapter;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.behealthy.constants.Constants.FIRST_COLUMN;
import static com.example.behealthy.constants.Constants.SECOND_COLUMN;
import static com.example.behealthy.constants.Constants.FIRST_COLUMN_NAME_VEGETABLE;
import static com.example.behealthy.constants.Constants.VITAMIN_NAME;
import static com.example.behealthy.constants.Constants.VEGETABLE_NAME;

public class VitaminsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamins);

        Intent intent = getIntent();
        String vitaminName = intent.getStringExtra(VITAMIN_NAME.label);

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(vitaminName + " values contained in 100 grams of vegetables");

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();

        JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

        List<Vegetable> vl = jsonProperty.getVegetables();

        for(Vegetable vegetable: vl){
            for(Vitamin vitamin: vegetable.getVitamins()){
                if(vitamin.getName().equals(vitaminName)){
                    HashMap<String, String> vegetableHashMap = new HashMap<>();
                    vegetableHashMap.put(FIRST_COLUMN.label, vegetable.getVegetableName());
                    vegetableHashMap.put(SECOND_COLUMN.label, vitamin.getAmount() + " " + vitamin.getUnit());
                    hashMapArrayList.add(vegetableHashMap);
                }
            }
        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(hashMapArrayList, this);

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = (HashMap<String, String>) parent.getAdapter().getItem(position);

                if(!hashMap.get(FIRST_COLUMN.label).equals(FIRST_COLUMN_NAME_VEGETABLE.label)){
                    Intent startIntent = new Intent(getApplicationContext(), VegetableDetailActivity.class);
                    startIntent.putExtra(VEGETABLE_NAME.label, hashMap.get(FIRST_COLUMN.label));
                    startActivity(startIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Please choose vegetable", Toast.LENGTH_SHORT).show();
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
