package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ListViewAdapter;
import com.example.myapplication.config.JsonProperty;
import com.example.myapplication.model.Vegetable;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.utilities.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VitaminsActivity extends AppCompatActivity {

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamins);

        Intent intent = getIntent();
        String vitaminName = (String) intent.getStringExtra("VITAMIN_NAME");

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
        HashMap<String, String> columnNamesHashMap = new HashMap<>();
        columnNamesHashMap.put(FIRST_COLUMN, "Vegetable");
        columnNamesHashMap.put(SECOND_COLUMN, vitaminName + " /100g");
        hashMapArrayList.add(columnNamesHashMap);

        ArrayList<JsonProperty> jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

        List<Vegetable> vl = jsonProperty.get(0).getVegetables();
        for(Vegetable v: vl){
            System.out.println("VEGEPICAPICA " + v);
        }

        for(Vegetable vegetable: vl){
            System.out.println("vegetable: " + vegetable.getVegetableName());


            for(Vitamin vitamin: vegetable.getVitamins()){
                System.out.println("KURVA CECKY: " +vitamin.getName() + " pica cecky+ " + vitaminName);
                if(vitamin.getName().equals(vitaminName)){
                    System.out.println("pppp: " +vitamin.getName() + " kkk+ " + vitaminName);
                    HashMap<String, String> vegetableHashMap = new HashMap<>();
                    vegetableHashMap.put(FIRST_COLUMN, vegetable.getVegetableName());
                    vegetableHashMap.put(SECOND_COLUMN, vitamin.getAmount() + " " + vitamin.getUnit());
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

                Intent startIntent = new Intent(getApplicationContext(), VegetableDetailActivity.class);
                startIntent.putExtra("VEGETABLE_NAME", hashMap.get("First"));
                System.out.println("CECKYYYYYY - - - " + hashMap.get("First"));
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
