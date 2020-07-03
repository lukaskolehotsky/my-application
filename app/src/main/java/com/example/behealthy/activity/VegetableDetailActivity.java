package com.example.behealthy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;
import com.example.behealthy.utilities.MenuHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.behealthy.constants.Constants.VEGETABLE_NAME;

public class VegetableDetailActivity extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_detail);

        Intent intent = getIntent();
        String vegetableName = intent.getStringExtra (VEGETABLE_NAME.label);

        JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);

        List<List<Vitamin>> vitamins = new ArrayList<>();
        for(Vegetable vegetable: jsonProperty.getVegetables()){
            if(vegetable.getName().equals(vegetableName)){
                vitamins.add(vegetable.getVitamins());
            }
        }

        // TODO - refactor - to one object
        for(Fruit fruit: jsonProperty.getFruits()){
            if(fruit.getName().equals(vegetableName)){
                vitamins.add(fruit.getVitamins());
            }
        }

        System.out.println(vitamins);

        LinearLayout rootLayout2 = findViewById(R.id.rootLayout2);
        populateVitamins(rootLayout2, vegetableName, vitamins.get(0));
    }

    private void populateVitamins(LinearLayout rootLayout2, String vegetableName, List<Vitamin> vitamins) {
        TextView vitaminTitleTextView = new TextView(getApplicationContext());
        vitaminTitleTextView.setGravity(Gravity.CENTER);
        vitaminTitleTextView.setText("Amount of vitamins in 100 grams of " + vegetableName);
        vitaminTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        vitaminTitleTextView.setTextColor(Color.BLACK);
        vitaminTitleTextView.setPadding(16, 0, 16, 16);

        rootLayout2.addView(vitaminTitleTextView);

        vitamins.forEach(vitamin -> {
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

            vitaminNameTextView.setText(vitamin.getName());
            amountUnitTextView.setText(vitamin.getAmount() + "" + vitamin.getUnit());

            linearLayout.addView(vitaminNameTextView);
            linearLayout.addView(amountUnitTextView);

            rootLayout2.addView(linearLayout);
        });
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
