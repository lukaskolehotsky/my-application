package com.example.behealthy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.adapter.ListViewAdapter;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;
import com.example.behealthy.utilities.MenuHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.behealthy.constants.Constants.VEGETABLE_NAME;
import static com.example.behealthy.constants.Constants.FIRST_COLUMN;
import static com.example.behealthy.constants.Constants.SECOND_COLUMN;

public class VegetableDetailActivity extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_detail);

        Intent intent = getIntent();
        String vegetableName = intent.getStringExtra (VEGETABLE_NAME.label);

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("Amount of vitamins in 100 grams of " + vegetableName);

        ListView listView = findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();

        JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

        for(Vegetable veg: jsonProperty.getVegetables()){
            if(veg.getName().equals(vegetableName)){

                for(Vitamin vitamin: veg.getVitamins()){
                    HashMap<String, String> vegetableHashMap = new HashMap<>();
                    vegetableHashMap.put(FIRST_COLUMN.label, vitamin.getName());
                    vegetableHashMap.put(SECOND_COLUMN.label, vitamin.getAmount() + " " + vitamin.getUnit());
                    hashMapArrayList.add(vegetableHashMap);
                }

            }
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
        menuHelper = new MenuHelper(getApplicationContext());
        Intent startIntent = menuHelper.chooseIntent(item.getItemId());
        startActivity(startIntent);

        return super.onOptionsItemSelected(item);
    }
}
