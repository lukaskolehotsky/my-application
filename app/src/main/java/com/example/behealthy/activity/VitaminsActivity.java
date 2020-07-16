package com.example.behealthy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.utilities.MenuHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.behealthy.constants.Constants.VEGETABLE_NAME;
import static com.example.behealthy.constants.Constants.VITAMIN_NAME;

public class VitaminsActivity extends AppCompatActivity {

    private MenuHelper menuHelper;
    private JsonService jsonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamins);

        jsonService = new JsonService(getBaseContext());

        Intent intent = getIntent();
        String vitaminName = intent.getStringExtra(VITAMIN_NAME.label);

        List<Vegetable> vegetables = jsonService.processFile(R.raw.vegetables).getVegetables();

        HashMap<String, String> hm = new HashMap<>();
        for(Vegetable vegetable: vegetables){
            for(Vitamin vitamin: vegetable.getVitamins()){
                if(vitamin.getName().equals(vitaminName)){
                    hm.put(vegetable.getName(),vitamin.getAmount() + " " + vitamin.getUnit());
                }
            }
        }

        // TODO - dorobit refactor
        List<Fruit> fruits = jsonService.processFile(R.raw.fruits).getFruits();
        for(Fruit fruit: fruits){
            for(Vitamin vitamin: fruit.getVitamins()){
                if(vitamin.getName().equals(vitaminName)){
                    hm.put(fruit.getName(),vitamin.getAmount() + " " + vitamin.getUnit());
                }
            }
        }

        LinearLayout rootLayout2 = findViewById(R.id.rootLayout2);
        populateVitamins(rootLayout2, vitaminName, hm);
    }

    private void populateVitamins(LinearLayout rootLayout2, String vitaminName, HashMap<String, String> hm) {
        TextView vitaminTitleTextView = new TextView(getApplicationContext());
        vitaminTitleTextView.setGravity(Gravity.CENTER);
        vitaminTitleTextView.setText(vitaminName + " values contained in 100 grams of vegetables");
        vitaminTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        vitaminTitleTextView.setTextColor(Color.BLACK);
        vitaminTitleTextView.setPadding(16, 0, 16, 16);

        rootLayout2.addView(vitaminTitleTextView);

        for (Map.Entry<String, String> entry : hm.entrySet()) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setGravity(Gravity.START);
            linearLayout.setPadding(16, 5, 16, 5);

            TextView vitaminNameTextView = new TextView(getApplicationContext());
            TextView amountUnitTextView = new TextView(getApplicationContext());

            vitaminNameTextView.setWidth(500);
            amountUnitTextView.setWidth(280);

            vitaminNameTextView.setTextColor(Color.BLACK);
            amountUnitTextView.setTextColor(Color.BLACK);

            vitaminNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            amountUnitTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

            vitaminNameTextView.setText(entry.getKey());
            amountUnitTextView.setText(entry.getValue());

            linearLayout.addView(vitaminNameTextView);
            linearLayout.addView(amountUnitTextView);

            linearLayout.setOnClickListener(v -> {
                View linearLayoutView = ((ViewGroup) v).getChildAt(0);
                TextView vegetableNameTextView = (TextView) linearLayoutView;

                Intent startIntent = new Intent(getApplicationContext(), VegetableDetailActivity.class);
                startIntent.putExtra(VEGETABLE_NAME.label, vegetableNameTextView.getText());
                startActivity(startIntent);
            });

            rootLayout2.addView(linearLayout);
        }
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
