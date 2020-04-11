package com.example.behealthy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.adapter.ListViewAdapter;
import com.example.behealthy.model.Vitamin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.behealthy.constants.Constants.FIRST_COLUMN;
import static com.example.behealthy.constants.Constants.SECOND_COLUMN;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;
import static com.example.behealthy.constants.Constants.VITAMIN_NAME;
import static com.example.behealthy.constants.Constants.GENDER;
import static com.example.behealthy.constants.Constants.AGE;

public class VitaminListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamin_list);

        Intent intent = getIntent();
        List<Vitamin> vitamins = (List) intent.getParcelableArrayListExtra (VITAMIN_LIST.label);

        String age = (String) intent.getStringExtra(AGE.label);
        String gender = (String) intent.getStringExtra(GENDER.label);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("Recommended daily dose of vitamins for " + age + " year old " + gender);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();

        assert vitamins != null;
        for(Vitamin vitamin: vitamins){
            HashMap<String, String> vitaminHashMap = new HashMap<>();
            vitaminHashMap.put(FIRST_COLUMN.label, vitamin.getName());
            vitaminHashMap.put(SECOND_COLUMN.label, vitamin.getAmount() + "" + vitamin.getUnit());
            hashMapArrayList.add(vitaminHashMap);
        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(hashMapArrayList, this);

        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> hashMap = (HashMap<String, String>) parent.getAdapter().getItem(position);

            Intent startIntent = new Intent(getApplicationContext(), VitaminsActivity.class);
                startIntent.putExtra(VITAMIN_NAME.label, hashMap.get(FIRST_COLUMN.label));
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
