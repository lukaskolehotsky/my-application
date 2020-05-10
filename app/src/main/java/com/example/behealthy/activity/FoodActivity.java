package com.example.behealthy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.JsonVegetable;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.behealthy.constants.Constants.VEGETABLES_JSON;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;

public class FoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private String vegetableName = "Vegetables";
    private String grams = "Grams";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        Button addLayoutButton = findViewById(R.id.addLayoutButton);
        addLayoutButton.setVisibility(View.INVISIBLE);
        addLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check(Optional.empty(), Optional.empty())){
                    addLayout();
                    addLayoutButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button resultButton = findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);
                List<Vegetable> vegetables = jsonProperty.getVegetables();

                List<Vitamin> allVitamins = new ArrayList<>();

                String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                List<JsonVegetable> jsonVegetableList = JsonVegetable.toList(vegetablesJson);

                for(JsonVegetable jsonVegetable: jsonVegetableList){
                    for(Vegetable vegetable: vegetables){
                        if(jsonVegetable.getVegetableName().equals(vegetable.getVegetableName())){

                            List<Vitamin> vitamins = vegetable.getVitamins();
                            for(Vitamin vitamin: vitamins){
                                double amount = Double.parseDouble(jsonVegetable.getGrams().replace("g", ""));
                                double calculatedAmount = vitamin.getAmount()*(amount/100);
                                vitamin.setAmount(calculatedAmount);
                            }

                            allVitamins.addAll(vitamins);
                        }
                    }
                }

                List<Vitamin> calculatedVitamins = new ArrayList<>();
                for(Vitamin ukazkovyVitamin: vegetables.get(0).getVitamins()) {

                    Vitamin calculatedVitamin = new Vitamin();
                    calculatedVitamin.setName(ukazkovyVitamin.getName());
                    calculatedVitamin.setFrom(ukazkovyVitamin.getFrom());
                    calculatedVitamin.setTo(ukazkovyVitamin.getTo());
                    calculatedVitamin.setUnit(ukazkovyVitamin.getUnit());

                    double calculatedAmount = 0;

                    for(Vitamin vit: allVitamins){
                        if(ukazkovyVitamin.getName().equals(vit.getName())){
                            calculatedAmount = calculatedAmount + vit.getAmount();
                        }
                    }
                    calculatedVitamin.setAmount(calculatedAmount);
                    calculatedVitamins.add(calculatedVitamin);
                }

                Intent startIntent = new Intent(FoodActivity.this, FoodFeedActivity.class);
                startIntent.putParcelableArrayListExtra(VITAMIN_LIST.label, (ArrayList<? extends Parcelable>) calculatedVitamins);
                startActivity(startIntent);
            }

        });

        addLayout();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if(firstItemName.equals("Vegetables")){
            vegetableName = value;
        }

        if(firstItemName.equals("Grams")){
            grams = value;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void addLayout() {
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createLayout());
    }

    private LinearLayout createLayout() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 0, 20, 0);

        ArrayAdapter<String> vegetablesArrayAdapter = createArrayAdapter(R.array.vegetables);
        ArrayAdapter<String> gramsArrayAdapter = createArrayAdapter(R.array.grams);

        Spinner vegetablesSpinner = createSpinner(vegetablesArrayAdapter);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);

        Button addVegetableButton = new Button(getApplicationContext());
        addVegetableButton.setText("+");
        addVegetableButton.setTextColor(Color.WHITE);
        addVegetableButton.setBackgroundColor(Color.GREEN);

        int width=100;
        int heigth=100;
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(width, heigth);
        addVegetableButton.setLayoutParams(layoutParams2);

        addVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check(Optional.empty(), Optional.empty())){
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> jsonVegetableList = JsonVegetable.toList(vegetablesJson);

                    JsonVegetable jsonVegetable = new JsonVegetable(vegetableName, grams);
                    jsonVegetableList.add(jsonVegetable);

                    JSONArray jsonVegetableListJsonArray = JsonVegetable.toJson(jsonVegetableList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, jsonVegetableListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    System.out.println("ADDED new jsonVegetable into jsonVegetableList" + jsonVegetable);
                    System.out.println("jsonVegetableList contains " + jsonVegetableList);

                    v.setVisibility(View.INVISIBLE);

                    // Disallow addButton
                    Button addButtonView = findViewById(R.id.addLayoutButton);
                    addButtonView.setVisibility(View.VISIBLE);
                }
            }
        });

        Button removeVegetableButton = new Button(getApplicationContext());
        removeVegetableButton.setText("-");
        removeVegetableButton.setTextColor(Color.WHITE);
        removeVegetableButton.setBackgroundColor(Color.RED);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, heigth);
        addVegetableButton.setLayoutParams(layoutParams3);

        removeVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vegetableSpinnerValue = null;
                String gramsSpinnerValue = null;

                View vegetableSpinnerView = ((ViewGroup)v.getParent()).getChildAt(0);

                if (vegetableSpinnerView instanceof Spinner){
                    vegetableSpinnerValue = ((Spinner) vegetableSpinnerView).getSelectedItem().toString();
                }

                View gramsSpinnerView = ((ViewGroup)v.getParent()).getChildAt(1);

                if (gramsSpinnerView instanceof Spinner){
                    gramsSpinnerValue = ((Spinner) gramsSpinnerView).getSelectedItem().toString();
                }

                if(check(Optional.of(vegetableSpinnerValue), Optional.of(gramsSpinnerValue))){
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> vegList = JsonVegetable.toList(vegetablesJson);

                    int index = 0;

                    for(int i=0;i<vegList.size();i++){
                        if(vegList.get(i).getVegetableName().equals(vegetableSpinnerValue)){
                            index = (i++);
                        }
                    }

                    vegList.remove(index);

                    JSONArray vegListJsonArray = JsonVegetable.toJson(vegList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, vegListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    ((ViewGroup)v.getParent().getParent()).removeView((ViewGroup)v.getParent());
                }
            }
        });

        linearLayout.addView(vegetablesSpinner);
        linearLayout.addView(gramsSpinner);
        linearLayout.addView(addVegetableButton);
        linearLayout.addView(removeVegetableButton);

        return linearLayout;
    }

    private Spinner createSpinner(ArrayAdapter<String> arrayAdapter) {
        Spinner spinner = new Spinner(getApplicationContext());
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        return spinner;
    }

    private ArrayAdapter<String> createArrayAdapter(@ArrayRes int resourceListId) {
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(resourceListId));
    }

    private boolean check(Optional<String> vegetableNameOpt, Optional<String> gramsOpt){
        String vgtblNm = null;
        String grms = null;

        if(vegetableNameOpt.isPresent() && gramsOpt.isPresent()){
            vgtblNm = vegetableNameOpt.get();
            grms = gramsOpt.get();
        } else {
            vgtblNm = vegetableName;
            grms = grams;
        }

        if(vgtblNm.equals("Vegetables") && grms.equals("Grams")){
            Toast.makeText(getApplicationContext(), "Choose vegetable and grams", Toast.LENGTH_SHORT).show();
            return false;
        } else if(vgtblNm.equals("Vegetables")){
            Toast.makeText(getApplicationContext(), "Choose vegetable", Toast.LENGTH_SHORT).show();
            return false;
        } else if(grms.equals("Grams")){
            Toast.makeText(getApplicationContext(), "Choose grams", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            System.out.println("VEGETABLE_NAME " + vegetableNameOpt + " GRAMS " + gramsOpt);
            return true;
        }
    }

}
