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
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.DateJsonFood;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.JsonFood;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.FileReader;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.behealthy.constants.Constants.FOODS_JSON;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;

public class FoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private String name = "Foods";
    private String grams = "Grams";

    private static final String FILE_NAME = "foods.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
//delete();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        Intent intent = getIntent();
        String year = intent.getStringExtra("YEAR");
        String month = intent.getStringExtra("MONTH");
        String day = intent.getStringExtra("DAY");
        LocalDate choosedDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(day + "." + month + "." + year);

        populateLayouts(choosedDate);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);
                List<Vegetable> vegetables = jsonProperty.getVegetables(); // TODO - vegetable a fruit prerobit na jeden objekt
                List<Fruit> fruits = jsonProperty.getFruits();

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
                    // TODO 2 start - po prerobeni vid hore mozes zmazat
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
                    // TODO 2 end - po prerobeni vid hore mozes zmazat
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

                populateInternalStorage(choosedDate, jsonFoodList);

                Intent startIntent = new Intent(FoodActivity.this, FoodFeedActivity.class);
                startIntent.putParcelableArrayListExtra(VITAMIN_LIST.label, (ArrayList<? extends Parcelable>) calculatedVitamins);
                startActivity(startIntent);
            }
        });

        FloatingActionButton addVegetablesLayoutButton = findViewById(R.id.addVegetablesLayoutButton);
        addVegetablesLayoutButton.setVisibility(View.VISIBLE);
        FloatingActionButton addFruitsLayoutButton = findViewById(R.id.addFruitsLayoutButton);
        addFruitsLayoutButton.setVisibility(View.VISIBLE);

        addVegetablesLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLayout("Vegetables");
                addVegetablesLayoutButton.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
                addFruitsLayoutButton.setVisibility(View.INVISIBLE);
            }
        });
        addFruitsLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLayout("Fruits");
                addFruitsLayoutButton.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
                addVegetablesLayoutButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void populateLayouts(LocalDate choosedDate) {
        createFoodsTextFile();

        String loadedDateJsonFoods = load();
        List<DateJsonFood> dateJsonFoodList = new ArrayList<>();
        if (!loadedDateJsonFoods.isEmpty()) {
            dateJsonFoodList = FileReader.getDateJsonFoodList(loadedDateJsonFoods);
        }

        for (DateJsonFood dateJsonFood : dateJsonFoodList) {
            if (dateJsonFood.getDate().equals(choosedDate)) {

                List<JsonFood> jsonFoods = dateJsonFood.getJsonFoods();
                for (JsonFood jsonFood : jsonFoods) {
                    addSavedLayout(jsonFood);
                }

                JSONArray jsonFoodListJsonArray = JsonFood.toJson(jsonFoods);
                sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, FOODS_JSON.label, jsonFoodListJsonArray.toString());
                sharedPreferencesHelper.save(sharedPreferenceEntry);
            }
        }

    }

    private void populateInternalStorage(LocalDate choosedDate, List<JsonFood> jsonFoodList) {
        createFoodsTextFile();
        String loadedDateJsonFoodsString = load();

        List<DateJsonFood> loadedDateJsonFoods = new ArrayList<>();
        if (loadedDateJsonFoodsString != null) {
            if (!loadedDateJsonFoodsString.isEmpty()) {
                loadedDateJsonFoods = FileReader.getDateJsonFoodList(loadedDateJsonFoodsString);
            }
        }

        delete();

        DateJsonFood newDay = new DateJsonFood();
        newDay.setDate(choosedDate);
        newDay.setJsonFoods(jsonFoodList);

        List<DateJsonFood> recalculatedDateJsonFoodList = new ArrayList<>();
        if (loadedDateJsonFoods.isEmpty()) {
            recalculatedDateJsonFoodList.add(newDay);
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
                recalculatedDateJsonFoodList.add(newDay);
            }
        }

        save(DateJsonFood.toJson(recalculatedDateJsonFoodList).toString());
        load();  // TODO - mozes vymazat
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if (firstItemName.equals("Foods")) {
            name = value;
        }

        if (firstItemName.equals("Grams")) {
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

    private void addSavedLayout(JsonFood jsonFood) {
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createSavedLayout(jsonFood));
    }

    private LinearLayout createSavedLayout(JsonFood jsonFood) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.LEFT);

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

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(Optional.empty(), Optional.empty())) {
                    String jsonString = sharedPreferenceEntry.getFoodsJson();
                    List<JsonFood> jsonFoodList = JsonFood.toList(jsonString);

                    JsonFood jsonFood = new JsonFood(name, grams);
                    jsonFoodList.add(jsonFood);

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
            }
        });

        ImageButton removeImageButton = new ImageButton(getApplicationContext());
        removeImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeImageButton.setBackgroundColor(Color.TRANSPARENT);
        removeImageButton.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, height);
        removeImageButton.setLayoutParams(layoutParams3);

        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createLayout(type));
    }

    private LinearLayout createLayout(String type) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.LEFT);

        ArrayAdapter<String> arrayAdapter;
        if(type.equals("Vegetables")){
            arrayAdapter = createArrayAdapter(R.array.vegetables);
        } else {
            arrayAdapter = createArrayAdapter(R.array.fruits);
        }
        ArrayAdapter<String> gramsArrayAdapter = createArrayAdapter(R.array.grams);

        Spinner spinner = createSpinner(arrayAdapter);
        spinner.setDropDownWidth(450);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT);
        spinner.setLayoutParams(lp);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);

        ImageButton addImageButton = new ImageButton(this);
        addImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_circle_black_24dp));
        addImageButton.setBackgroundColor(Color.TRANSPARENT);

        int width = 100;
        int height = 100;
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(width, height);
        addImageButton.setLayoutParams(layoutParams2);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        ImageButton removeImageButton = new ImageButton(getApplicationContext());
        removeImageButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeImageButton.setBackgroundColor(Color.TRANSPARENT);
        removeImageButton.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, height);
        removeImageButton.setLayoutParams(layoutParams3);

        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String[] resourceArray = null;

        List<String> resources = Arrays.asList(getResources().getStringArray(resourceListId));

        if (resources.contains("Vegetables")) {
            List<String> sortedResources = resources.stream().sorted().collect(Collectors.toList());

            boolean remove = false;
            int index = 0;
            for (int i = 0; i < sortedResources.size(); i++) {
                if (sortedResources.get(i).equals("Vegetables")) {
                    index = i;
                    remove = true;
                }
            }

            if (remove) {
                sortedResources.remove(index);
                Collections.reverse(sortedResources);
                sortedResources.add("Vegetables");
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
        String vgtblNm = null;
        String grms = null;

        if (nameOpt.isPresent() && gramsOpt.isPresent()) {
            vgtblNm = nameOpt.get();
            grms = gramsOpt.get();
        } else {
            vgtblNm = name;
            grms = grams;
        }

        if (vgtblNm.equals("Vegetables") && grms.equals("Grams")) {
            Toast.makeText(getApplicationContext(), "Choose vegetable and grams", Toast.LENGTH_SHORT).show();
            return false;
        } else if (vgtblNm.equals("Vegetables")) {
            Toast.makeText(getApplicationContext(), "Choose vegetable", Toast.LENGTH_SHORT).show();
            return false;
        } else if (grms.equals("Grams")) {
            Toast.makeText(getApplicationContext(), "Choose grams", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void delete() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (file.exists()) {
            deleteFile(FILE_NAME);
            System.out.println();
            System.out.println("DELETUJEM" + FILE_NAME);
            System.out.println();
        }
    }

    public void createFoodsTextFile() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println();
                System.out.println("CREATUJEM" + FILE_NAME);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String text) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            System.out.println();
            System.out.println("SAVEUJEM" + text);
            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String load() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            System.out.println();
            System.out.println("LOADUJEM" + sb.toString());
            System.out.println();

            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
