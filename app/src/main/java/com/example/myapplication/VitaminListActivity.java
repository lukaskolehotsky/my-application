package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.myapplication.adapter.ListViewAdapter;
import com.example.myapplication.model.Vitamin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VitaminListActivity extends AppCompatActivity {

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamin_list);

        ListView listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        List<Vitamin> vitamins = (List) intent.getParcelableArrayListExtra ("VITAMIN_LIST");

        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
        HashMap<String, String> columnNamesHashMap = new HashMap<>();
        columnNamesHashMap.put(FIRST_COLUMN, "Name");
        columnNamesHashMap.put(SECOND_COLUMN, "Daily Amount");
        hashMapArrayList.add(columnNamesHashMap);

        for(Vitamin vitamin: vitamins){
            HashMap<String, String> vitaminHashMap = new HashMap<>();
            vitaminHashMap.put(FIRST_COLUMN, vitamin.getName());
            vitaminHashMap.put(SECOND_COLUMN, vitamin.getAmount() + "" + vitamin.getUnit());
            hashMapArrayList.add(vitaminHashMap);
        }

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
