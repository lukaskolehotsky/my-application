package com.example.behealthy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.behealthy.R;
import com.example.behealthy.model.DateJsonFood;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.JsonFood;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.service.FileService;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.behealthy.constants.Constants.DAY;
import static com.example.behealthy.constants.Constants.FOODS;
import static com.example.behealthy.constants.Constants.FOODS_JSON;
import static com.example.behealthy.constants.Constants.GRAMS;
import static com.example.behealthy.constants.Constants.MONTH;
import static com.example.behealthy.constants.Constants.VEGETABLES;
import static com.example.behealthy.constants.Constants.YEAR;

public class FoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "FoodActivity";

    private FileService fileService;
    private JsonService jsonService;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private String name = "Foods";
    private String grams = "Grams";

    private static final String FILE_NAME = "foods.txt";
    private static final String FILE_NAME_R_D_D = "recommended_daily_dose.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "FoodActivity.onCreate() — on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
//delete();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        fileService = new FileService(getApplicationContext());
        jsonService = new JsonService(getBaseContext());

        Intent intent = getIntent();
        int year= intent.getIntExtra(YEAR.label, 0);
        int month= intent.getIntExtra(MONTH.label, 0);
        int day= intent.getIntExtra(DAY.label, 0);
        LocalDate choosedDate = LocalDate.of(year, month, day);

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(choosedDate.getDayOfMonth() + "." + choosedDate.getMonthValue() + "." + choosedDate.getYear());

        populateLayouts(choosedDate);

        LinearLayout rootLayout2 = findViewById(R.id.rootLayout2);
        populateVitamins(rootLayout2);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodsJson = sharedPreferenceEntry.getFoodsJson();
                List<JsonFood> jsonFoodList = JsonFood.toList(foodsJson);
                populateInternalStorage(choosedDate, jsonFoodList);

                rootLayout2.removeAllViewsInLayout();
                populateVitamins(rootLayout2);
            }
        });

        FloatingActionButton addVegetablesLayoutButton = findViewById(R.id.addVegetablesLayoutButton);
        addVegetablesLayoutButton.setVisibility(View.VISIBLE);
        FloatingActionButton addFruitsLayoutButton = findViewById(R.id.addFruitsLayoutButton);
        addFruitsLayoutButton.setVisibility(View.VISIBLE);

        addVegetablesLayoutButton.setOnClickListener(v -> {
            addLayout("Vegetables");
            addVegetablesLayoutButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setVisibility(View.INVISIBLE);
            addFruitsLayoutButton.setVisibility(View.INVISIBLE);
        });
        addFruitsLayoutButton.setOnClickListener(v -> {
            addLayout("Fruits");
            addFruitsLayoutButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setVisibility(View.INVISIBLE);
            addVegetablesLayoutButton.setVisibility(View.INVISIBLE);
        });
    }

    private void populateVitamins(LinearLayout rootLayout2){
        TextView vitaminTitleTextView = new TextView(getApplicationContext());
        vitaminTitleTextView.setGravity(Gravity.CENTER);
        vitaminTitleTextView.setText("Vitamins");
        vitaminTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        vitaminTitleTextView.setTextColor(Color.BLACK);
        vitaminTitleTextView.setPadding(16, 0, 16, 16);

        rootLayout2.addView(vitaminTitleTextView);

        String recommendedVitaminsString = fileService.loadFile(FILE_NAME_R_D_D);

        final List<Vitamin> recommendedVitamins = Vitamin.toList(recommendedVitaminsString);

        List<Vitamin> calculatedVitamins = calculateVitamins();
        calculatedVitamins.forEach(calculatedVitamin -> {
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

            vitaminNameTextView.setText(calculatedVitamin.getName());
            amountUnitTextView.setText(calculatedVitamin.getAmount() + "" + calculatedVitamin.getUnit());

            linearLayout.addView(vitaminNameTextView);
            linearLayout.addView(amountUnitTextView);

            if(recommendedVitaminsString != null){

                TextView percentageTextView = new TextView(getApplicationContext());
                percentageTextView.setWidth(200);
                percentageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

                final double[] percentage = new double[1];

                recommendedVitamins.forEach(rv -> {
                    if(rv.getName().equals(calculatedVitamin.getName())){
                        percentage[0] = calculatedVitamin.getAmount() * 100 / rv.getAmount();
                        percentageTextView.setText(String.format("%.1f", percentage[0]) + "%");
                    }
                });

                if(percentage[0] >= 100){
                    percentageTextView.setTextColor(Color.GREEN);
                } else {
                    percentageTextView.setTextColor(Color.RED);
                }

                linearLayout.addView(percentageTextView);
            }

            rootLayout2.addView(linearLayout);
        });
    }

    private List<Vitamin> calculateVitamins(){
        List<Vegetable> vegetables = jsonService.processFile(R.raw.vegetables).getVegetables();
        List<Fruit> fruits = jsonService.processFile(R.raw.fruits).getFruits();

        List<Vitamin> allVitamins = new ArrayList<>();

        String foodsJson = sharedPreferenceEntry.getFoodsJson();
        List<JsonFood> jsonFoodList = JsonFood.toList(foodsJson);

        for (JsonFood jsonFood : jsonFoodList) {
            for (Vegetable vegetable : vegetables) {
                if (jsonFood.getName().equals(vegetable.getName())) {

                    List<Vitamin> vitamins = new ArrayList<>();
                    for (Vitamin vitamin : vegetable.getVitamins()) {
                        double amount = Double.parseDouble(jsonFood.getGrams().replace("g", ""));
                        double calculatedAmount = vitamin.getAmount() * (amount / 100);

                        Vitamin vit = new Vitamin();
                        vit.setName(vitamin.getName());
                        vit.setUnit(vitamin.getUnit());
                        vit.setFrom(vitamin.getFrom());
                        vit.setTo(vitamin.getTo());
                        vit.setAmount(calculatedAmount);
                        vitamins.add(vit);
                    }

                    allVitamins.addAll(vitamins);
                }
            }
            for (Fruit fruit : fruits) {
                if (jsonFood.getName().equals(fruit.getName())) {

                    List<Vitamin> vitamins = new ArrayList<>();
                    for (Vitamin vitamin : fruit.getVitamins()) {
                        double amount = Double.parseDouble(jsonFood.getGrams().replace("g", ""));
                        double calculatedAmount = vitamin.getAmount() * (amount / 100);

                        Vitamin vit = new Vitamin();
                        vit.setName(vitamin.getName());
                        vit.setUnit(vitamin.getUnit());
                        vit.setFrom(vitamin.getFrom());
                        vit.setTo(vitamin.getTo());
                        vit.setAmount(calculatedAmount);
                        vitamins.add(vit);
                    }

                    allVitamins.addAll(vitamins);
                }
            }
        }

        List<Vitamin> calculatedVitamins = new ArrayList<>();
        for (Vitamin ukazkovyVitamin : vegetables.get(0).getVitamins()) {

            Vitamin calculatedVitamin = new Vitamin();
            calculatedVitamin.setName(ukazkovyVitamin.getName());
            calculatedVitamin.setFrom(ukazkovyVitamin.getFrom());
            calculatedVitamin.setTo(ukazkovyVitamin.getTo());
            calculatedVitamin.setUnit(ukazkovyVitamin.getUnit());

            double calculatedAmount = 0;

            for (Vitamin vit : allVitamins) {
                if (ukazkovyVitamin.getName().equals(vit.getName())) {
                    calculatedAmount = calculatedAmount + vit.getAmount();
                }
            }

            String formattedCalculatedAmount = String.format("%.2f", calculatedAmount).replace(",", ".");

            calculatedVitamin.setAmount(Double.parseDouble(formattedCalculatedAmount));
            calculatedVitamins.add(calculatedVitamin);
        }

        return calculatedVitamins;
    }

    private void populateLayouts(LocalDate choosedDate) {
        Log.i(TAG, "FoodActivity.populateLayouts() — populate layouts by choosedDate " + choosedDate);

        fileService.createFile(FILE_NAME);
        String loadedDateJsonFoodsString = fileService.loadFile(FILE_NAME);

        if(loadedDateJsonFoodsString != null){
            if (!loadedDateJsonFoodsString.isEmpty()) {
                List<DateJsonFood> dateJsonFoods = jsonService.getDateJsonFoodList(loadedDateJsonFoodsString);
                if(dateJsonFoods != null){
                    dateJsonFoods.forEach(dateJsonFood -> {
                        if (dateJsonFood.getDate().equals(choosedDate)) {
                            List<JsonFood> jsonFoods = dateJsonFood.getJsonFoods();
                            jsonFoods.forEach(jsonFood -> {
                                addSavedLayout(jsonFood);
                                JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoods);
                                sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                                sharedPreferencesHelper.save(sharedPreferenceEntry);
                            });
                        }
                    });
                }
            }
        }
    }

    private void populateInternalStorage(LocalDate choosedDate, List<JsonFood> jsonFoodList) {
        Log.i(TAG, "FoodActivity.populateInternalStorage() — populate internal storage by choosedDate, jsonFoodList " + choosedDate + ", " + jsonFoodList);

        fileService.createFile(FILE_NAME);
        String loadedDateJsonFoodsString = fileService.loadFile(FILE_NAME);

                List<DateJsonFood> loadedDateJsonFoods = new ArrayList<>();
        if (loadedDateJsonFoodsString != null) {
            if (!loadedDateJsonFoodsString.isEmpty()) {
                loadedDateJsonFoods = jsonService.getDateJsonFoodList(loadedDateJsonFoodsString);
            }
        }

        fileService.deleteFile(FILE_NAME);

        DateJsonFood newDateJsonFood = new DateJsonFood(choosedDate, jsonFoodList);

        List<DateJsonFood> recalculatedDateJsonFoodList = new ArrayList<>();
        if(loadedDateJsonFoods != null){
            if (loadedDateJsonFoods.isEmpty()) {
                recalculatedDateJsonFoodList.add(newDateJsonFood);
            } else {
                loadedDateJsonFoods.forEach(loadedDateJsonFood -> {
                    if (loadedDateJsonFood.getDate().equals(choosedDate)) {
                        loadedDateJsonFood.setJsonFoods(jsonFoodList);
                        recalculatedDateJsonFoodList.add(loadedDateJsonFood);
                    } else {
                        recalculatedDateJsonFoodList.add(loadedDateJsonFood);
                    }
                });

                Optional<DateJsonFood> dateJsonFoodOpt = loadedDateJsonFoods.stream().filter(dateJsonFood -> dateJsonFood.getDate().equals(choosedDate)).findAny();
                if (!dateJsonFoodOpt.isPresent()) {
                    recalculatedDateJsonFoodList.add(newDateJsonFood);
                }
            }
        }

        fileService.saveFile(DateJsonFood.toJson(recalculatedDateJsonFoodList).toString(), FILE_NAME);
//        load();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if (firstItemName.equals(FOODS.label)) {
            name = value;
        }

        if (firstItemName.equals(GRAMS.label)) {
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
        MenuHelper menuHelper = new MenuHelper(getApplicationContext());
        Intent startIntent = menuHelper.chooseIntent(item.getItemId());
        startActivity(startIntent);

        return super.onOptionsItemSelected(item);
    }

    private void addSavedLayout(JsonFood jsonFood) {
        Log.i(TAG, "FoodActivity.addSavedLayout() — add save layout by jsonFood " + jsonFood);
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createSavedLayout(jsonFood));
    }

    private LinearLayout createSavedLayout(JsonFood jsonFood) {
        Log.i(TAG, "FoodActivity.createSavedLayout() — create saved layout by jsonFood " + jsonFood);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.START);

        String[] foodsResourceArray = {jsonFood.getName()};
        ArrayAdapter<String> foodsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodsResourceArray);

        String[] gramsResourceArray = {jsonFood.getGrams()};
        ArrayAdapter<String> gramsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gramsResourceArray);

        Spinner spinner = createSpinner(foodsArrayAdapter);
        spinner.setDropDownWidth(450);
        spinner.setEnabled(false);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT);
        spinner.setLayoutParams(lp);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);
        gramsSpinner.setEnabled(false);

        ImageButton addImageButton = new ImageButton(this);
        addImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_circle_black_24dp));
        addImageButton.setBackgroundColor(Color.TRANSPARENT);
        addImageButton.setVisibility(View.INVISIBLE);

        int width = 100;
        int height = 100;
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(width, height);
        addImageButton.setLayoutParams(layoutParams2);

        addImageButton.setOnClickListener(v -> {
            if (check(Optional.empty(), Optional.empty())) {
                String jsonString = sharedPreferenceEntry.getFoodsJson();
                List<JsonFood> jsonFoodList = JsonFood.toList(jsonString);

                JsonFood jsonFood1 = new JsonFood(name, grams);
                jsonFoodList.add(jsonFood1);

                JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoodList);
                sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                sharedPreferencesHelper.save(sharedPreferenceEntry);

                v.setVisibility(View.INVISIBLE);

                FloatingActionButton addVegetablesLayoutButton = findViewById(R.id.addVegetablesLayoutButton);
                addVegetablesLayoutButton.setVisibility(View.VISIBLE);

                FloatingActionButton addFruitsLayoutButton = findViewById(R.id.addFruitsLayoutButton);
                addFruitsLayoutButton.setVisibility(View.VISIBLE);

                FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
                floatingActionButton.setVisibility(View.VISIBLE);

                View foodSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);
                Spinner foodSpinner = (Spinner) foodSpinnerView;
                foodSpinner.setEnabled(false);

                View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);
                Spinner grmsSpinner = (Spinner) gramsSpinnerView;
                grmsSpinner.setEnabled(false);

                View removeImageButtonView = ((ViewGroup) v.getParent()).getChildAt(3);
                ImageButton removeImageButton = (ImageButton) removeImageButtonView;
                removeImageButton.setVisibility(View.VISIBLE);
            }
        });

        ImageButton removeImageButton = new ImageButton(getApplicationContext());
        removeImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeImageButton.setBackgroundColor(Color.TRANSPARENT);
        removeImageButton.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, height);
        removeImageButton.setLayoutParams(layoutParams3);

        removeImageButton.setOnClickListener(v -> {
            String spinnerValue = null;
            String gramsSpinnerValue = null;

            View foodSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);

            if (foodSpinnerView instanceof Spinner) {
                spinnerValue = ((Spinner) foodSpinnerView).getSelectedItem().toString();
            }

            View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);

            if (gramsSpinnerView instanceof Spinner) {
                gramsSpinnerValue = ((Spinner) gramsSpinnerView).getSelectedItem().toString();
            }

            if (spinnerValue != null && gramsSpinnerValue != null) {
                if (check(Optional.of(spinnerValue), Optional.of(gramsSpinnerValue))) {
                    String foodsJson = sharedPreferenceEntry.getFoodsJson();
                    List<JsonFood> jsonFoodList = JsonFood.toList(foodsJson);

                    int index = 0;

                    for (int i = 0; i < jsonFoodList.size(); i++) {
                        if (jsonFoodList.get(i).getName().equals(spinnerValue) && jsonFoodList.get(i).getGrams().equals(gramsSpinnerValue)) {
                            index = (i++);
                        }
                    }

                    jsonFoodList.remove(index);

                    JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoodList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    ((ViewGroup) v.getParent().getParent()).removeView((ViewGroup) v.getParent());
                }
            }
        });

        linearLayout.addView(spinner);
        linearLayout.addView(gramsSpinner);
        linearLayout.addView(addImageButton);
        linearLayout.addView(removeImageButton);

        return linearLayout;
    }

    private void addLayout(String type) {
        Log.i(TAG, "FoodActivity.addLayout() — add layout by type " + type);
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createLayout(type));
    }

    private LinearLayout createLayout(String type) {
        Log.i(TAG, "FoodActivity.createLayout() — create layout by type " + type);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.START);

        ArrayAdapter<String> arrayAdapter = type.equals(VEGETABLES.label) ? createArrayAdapter(R.array.vegetables) : createArrayAdapter(R.array.fruits);
        ViewGroup.LayoutParams spinnerLayoutParams = new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT);

        Spinner spinner = createSpinner(arrayAdapter);
        spinner.setLayoutParams(spinnerLayoutParams);
        spinner.setDropDownWidth(450);

        ArrayAdapter<String> gramsArrayAdapter = createArrayAdapter(R.array.grams);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);

        ViewGroup.LayoutParams addImageButtonLayoutParams = new ViewGroup.LayoutParams(100, 100);
        ImageButton addImageButton = new ImageButton(this);
        addImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_circle_black_24dp));
        addImageButton.setLayoutParams(addImageButtonLayoutParams);
        addImageButton.setBackgroundColor(Color.TRANSPARENT);

        addImageButton.setOnClickListener(v -> {
            if (check(Optional.empty(), Optional.empty())) {
                String jsonString = sharedPreferenceEntry.getFoodsJson();
                List<JsonFood> jsonFoodList = JsonFood.toList(jsonString);

                JsonFood jsonFood = new JsonFood(name, grams);
                jsonFoodList.add(jsonFood);

                JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoodList);
                sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                sharedPreferencesHelper.save(sharedPreferenceEntry);

                v.setVisibility(View.INVISIBLE);

                FloatingActionButton addVegetablesLayoutButtonView = findViewById(R.id.addVegetablesLayoutButton);
                addVegetablesLayoutButtonView.setVisibility(View.VISIBLE);

                FloatingActionButton addFruitsLayoutButtonView = findViewById(R.id.addFruitsLayoutButton);
                addFruitsLayoutButtonView.setVisibility(View.VISIBLE);

                FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
                floatingActionButton.setVisibility(View.VISIBLE);

                View spinnerView = ((ViewGroup) v.getParent()).getChildAt(0);
                Spinner vegSpinner = (Spinner) spinnerView;
                vegSpinner.setEnabled(false);

                View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);
                Spinner grmsSpinner = (Spinner) gramsSpinnerView;
                grmsSpinner.setEnabled(false);

                View removeImageButtonView = ((ViewGroup) v.getParent()).getChildAt(3);
                ImageButton removeImageButton = (ImageButton) removeImageButtonView;
                removeImageButton.setVisibility(View.VISIBLE);
            }
        });

        ImageButton removeImageButton = new ImageButton(getApplicationContext());
        removeImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeImageButton.setBackgroundColor(Color.TRANSPARENT);
        removeImageButton.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(100, 100);
        removeImageButton.setLayoutParams(layoutParams3);

        removeImageButton.setOnClickListener(v -> {
            String spinnerValue = null;
            String gramsSpinnerValue = null;

            View spinnerView = ((ViewGroup) v.getParent()).getChildAt(0);

            if (spinnerView instanceof Spinner) {
                spinnerValue = ((Spinner) spinnerView).getSelectedItem().toString();
            }

            View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);

            if (gramsSpinnerView instanceof Spinner) {
                gramsSpinnerValue = ((Spinner) gramsSpinnerView).getSelectedItem().toString();
            }

            if (spinnerValue != null && gramsSpinnerValue != null) {
                if (check(Optional.of(spinnerValue), Optional.of(gramsSpinnerValue))) {
                    String jsonString = sharedPreferenceEntry.getFoodsJson();
                    List<JsonFood> jsonFoodList = JsonFood.toList(jsonString);

                    int index = 0;

                    for (int i = 0; i < jsonFoodList.size(); i++) {
                        if (jsonFoodList.get(i).getName().equals(spinnerValue) && jsonFoodList.get(i).getGrams().equals(gramsSpinnerValue)) {
                            index = (i++);
                        }
                    }

                    jsonFoodList.remove(index);

                    JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoodList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    ((ViewGroup) v.getParent().getParent()).removeView((ViewGroup) v.getParent());
                }
            }
        });

        linearLayout.addView(spinner);
        linearLayout.addView(gramsSpinner);
        linearLayout.addView(addImageButton);
        linearLayout.addView(removeImageButton);

        return linearLayout;
    }

    private Spinner createSpinner(ArrayAdapter<String> arrayAdapter) {
        Spinner spinner = new Spinner(getApplicationContext());
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        return spinner;
    }

    private ArrayAdapter<String> createArrayAdapter(@ArrayRes int resourceListId) {
        String[] resourceArray;

        List<String> resources = Arrays.asList(getResources().getStringArray(resourceListId));

        if (resources.contains(FOODS.label)) {
            List<String> sortedResources = resources.stream().sorted().collect(Collectors.toList());

            boolean remove = false;
            int index = 0;
            for (int i = 0; i < sortedResources.size(); i++) {
                if (sortedResources.get(i).equals(FOODS.label)) {
                    index = i;
                    remove = true;
                }
            }

            if (remove) {
                sortedResources.remove(index);
                Collections.reverse(sortedResources);
                sortedResources.add(FOODS.label);
                Collections.reverse(sortedResources);
            }

            String[] itemsArray = new String[sortedResources.size()];
            itemsArray = sortedResources.toArray(itemsArray);
            resourceArray = itemsArray;
        } else {
            resourceArray = getResources().getStringArray(resourceListId);
        }

        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resourceArray);
    }

    private boolean check(Optional<String> nameOpt, Optional<String> gramsOpt) {
        Log.i(TAG, "FoodActivity.check() — check by nameOpt, gramsOpt " + nameOpt + ", " + gramsOpt);
        String vgtblNm;
        String grms;

        if (nameOpt.isPresent() && gramsOpt.isPresent()) {
            vgtblNm = nameOpt.get();
            grms = gramsOpt.get();
        } else {
            vgtblNm = name;
            grms = grams;
        }

        if (vgtblNm.equals(FOODS.label) && grms.equals(GRAMS.label)) {
            Toast.makeText(getApplicationContext(), "Choose food and grams", Toast.LENGTH_SHORT).show();
            return false;
        } else if (vgtblNm.equals(FOODS.label)) {
            Toast.makeText(getApplicationContext(), "Choose food", Toast.LENGTH_SHORT).show();
            return false;
        } else if (grms.equals(GRAMS.label)) {
            Toast.makeText(getApplicationContext(), "Choose grams", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}