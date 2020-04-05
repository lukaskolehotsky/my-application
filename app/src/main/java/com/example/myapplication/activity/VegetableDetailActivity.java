package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ListViewAdapter;
import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.Fruit;
import com.example.myapplication.model.Vegetable;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.utilities.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VegetableDetailActivity extends AppCompatActivity {

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_detail);

        Intent intent = getIntent();
        String vegetableName = (String) intent.getStringExtra ("VEGETABLE_NAME");

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
        HashMap<String, String> columnNamesHashMap = new HashMap<>();
        columnNamesHashMap.put(FIRST_COLUMN, vegetableName);
        columnNamesHashMap.put(SECOND_COLUMN, "amount / 100g");
        hashMapArrayList.add(columnNamesHashMap);

        ArrayList<JsonProperty> jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

        for(Vegetable veg: jsonProperty.get(0).getVegetables()){
            if(veg.getVegetableName().equals(vegetableName)){

                for(Vitamin vitamin: veg.getVitamins()){
                    HashMap<String, String> vegetableHashMap = new HashMap<>();
                    vegetableHashMap.put(FIRST_COLUMN, vitamin.getName());
                    vegetableHashMap.put(SECOND_COLUMN, vitamin.getAmount() + " " + vitamin.getUnit());
                    hashMapArrayList.add(vegetableHashMap);
                }

            }
        }

//        for(Fruit fruit: jsonProperty.get(0).getFruits()){
//            if(fruit.getFruitName().equals(vegetableName)){
//
//                for(Vitamin vitamin: veg.getVitamins()){
//                    HashMap<String, String> vegetableHashMap = new HashMap<>();
//                    vegetableHashMap.put(FIRST_COLUMN, vitamin.getName());
//                    vegetableHashMap.put(SECOND_COLUMN, vitamin.getAmount() + " " + vitamin.getUnit());
//                    hashMapArrayList.add(vegetableHashMap);
//                }
//
//            }
//        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(hashMapArrayList, this);

        listView.setAdapter(listViewAdapter);
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
