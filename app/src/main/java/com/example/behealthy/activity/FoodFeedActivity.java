package com.example.behealthy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.adapter.ListViewAdapter;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.MenuHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.behealthy.constants.Constants.FIRST_COLUMN;
import static com.example.behealthy.constants.Constants.SECOND_COLUMN;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;

public class FoodFeedActivity  extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamin_list);

        Intent intent = getIntent();
        List<Vitamin> vitamins = (List) intent.getParcelableArrayListExtra (VITAMIN_LIST.label);

        ListView listView = findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();

        for(Vitamin vitamin: vitamins){
            HashMap<String, String> vitaminHashMap = new HashMap<>();
            vitaminHashMap.put(FIRST_COLUMN.label, vitamin.getName());
            vitaminHashMap.put(SECOND_COLUMN.label, vitamin.getAmount() + "" + vitamin.getUnit());
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
        menuHelper = new MenuHelper(getApplicationContext());
        Intent startIntent = menuHelper.chooseIntent(item.getItemId());
        startActivity(startIntent);

        return super.onOptionsItemSelected(item);
    }
}
