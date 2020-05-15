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
import com.example.behealthy.model.DayJsonVegetable;
import com.example.behealthy.model.JsonVegetable;
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

import static com.example.behealthy.constants.Constants.VEGETABLES_JSON;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;

public class FoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private String vegetableName = "Vegetables";
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

        // TODO - start fill layouts
        populateLayouts(choosedDate);
        // TODO - end fill layouts

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonProperty jsonProperty = new FileReader(getBaseContext()).processFile(R.raw.locations);
                List<Vegetable> vegetables = jsonProperty.getVegetables();

                List<Vitamin> allVitamins = new ArrayList<>();

                String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                List<JsonVegetable> jsonVegetableList = JsonVegetable.toList(vegetablesJson);

                for (JsonVegetable jsonVegetable : jsonVegetableList) {
                    for (Vegetable vegetable : vegetables) {
                        if (jsonVegetable.getVegetableName().equals(vegetable.getVegetableName())) {

                            List<Vitamin> vitamins = new ArrayList<>();
                            for (Vitamin vitamin : vegetable.getVitamins()) {
                                double amount = Double.parseDouble(jsonVegetable.getGrams().replace("g", ""));
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

                populateInternalStorage(choosedDate, jsonVegetableList);

                Intent startIntent = new Intent(FoodActivity.this, FoodFeedActivity.class);
                startIntent.putParcelableArrayListExtra(VITAMIN_LIST.label, (ArrayList<? extends Parcelable>) calculatedVitamins);
                startActivity(startIntent);
            }
        });

        FloatingActionButton addLayoutButton = findViewById(R.id.addLayoutButton);
        addLayoutButton.setVisibility(View.VISIBLE);
        addLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLayout();
                addLayoutButton.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void populateLayouts(LocalDate choosedDate) {
        createFoodsTextFile();

        String loadedDayJsonVegetables = load();
        List<DayJsonVegetable> dayJsonVegetableList = new ArrayList<>();
        if (!loadedDayJsonVegetables.isEmpty()) {
            dayJsonVegetableList = FileReader.getDayJsonVegetablesList(loadedDayJsonVegetables);
        }

        for (DayJsonVegetable dayJsonVegetable : dayJsonVegetableList) {
            if (dayJsonVegetable.getDate().equals(choosedDate)) {

                // TODO - populate layout
                List<JsonVegetable> jsonVegetables = dayJsonVegetable.getJsonVegetables();
                for (JsonVegetable jsonVegetable : jsonVegetables) {
                    addSavedLayout(jsonVegetable);
                }

                // Pridaj do shared preferences
                JSONArray jsonVegetableListJsonArray = JsonVegetable.toJson(jsonVegetables);
                sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, jsonVegetableListJsonArray.toString());
                sharedPreferencesHelper.save(sharedPreferenceEntry);
            }
        }

    }

    private void populateInternalStorage(LocalDate choosedDate, List<JsonVegetable> jsonVegetableList) {
        createFoodsTextFile();
        String loadedDayJsonVegetablesString = load();

        List<DayJsonVegetable> loadedDayJsonVegetables = new ArrayList<>();
        if (loadedDayJsonVegetablesString != null) {
            if (!loadedDayJsonVegetablesString.isEmpty()) {
                loadedDayJsonVegetables = FileReader.getDayJsonVegetablesList(loadedDayJsonVegetablesString);
            }
        }

        delete();

        DayJsonVegetable newDay = new DayJsonVegetable();
        newDay.setDate(choosedDate);
        newDay.setJsonVegetables(jsonVegetableList);

        List<DayJsonVegetable> recalculatedDayJsonVegetableList = new ArrayList<>();
        if (loadedDayJsonVegetables.isEmpty()) {
            recalculatedDayJsonVegetableList.add(newDay);
        } else {
            loadedDayJsonVegetables.forEach(loadedDayJsonVegetable -> {
                if (loadedDayJsonVegetable.getDate().equals(choosedDate)) {
                    loadedDayJsonVegetable.setJsonVegetables(jsonVegetableList);
                    recalculatedDayJsonVegetableList.add(loadedDayJsonVegetable);
                } else {
                    recalculatedDayJsonVegetableList.add(loadedDayJsonVegetable);
                }
            });

            Optional<DayJsonVegetable> dayJsonVegetableOpt = loadedDayJsonVegetables.stream().filter(dayJsonVegetable -> dayJsonVegetable.getDate().equals(choosedDate)).findAny();
            if (!dayJsonVegetableOpt.isPresent()) {
                recalculatedDayJsonVegetableList.add(newDay);
            }
        }

        save(DayJsonVegetable.toJson(recalculatedDayJsonVegetableList).toString());
        load();  // TODO - mozes vymazat
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        if (firstItemName.equals("Vegetables")) {
            vegetableName = value;
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

    private void addSavedLayout(JsonVegetable jsonVegetable) {
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createSavedLayout(jsonVegetable));
    }

    private LinearLayout createSavedLayout(JsonVegetable jsonVegetable) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.LEFT);

        String[] vegetablesResourceArray = {jsonVegetable.getVegetableName()};
        ArrayAdapter<String> vegetablesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vegetablesResourceArray);

        String[] gramsResourceArray = {jsonVegetable.getGrams()};
        ArrayAdapter<String> gramsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gramsResourceArray);

        Spinner vegetablesSpinner = createSpinner(vegetablesArrayAdapter);
        vegetablesSpinner.setDropDownWidth(450);
        vegetablesSpinner.setEnabled(false);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT);
        vegetablesSpinner.setLayoutParams(lp);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);
        gramsSpinner.setEnabled(false);

        ImageButton addVegetableButton = new ImageButton(this);
        addVegetableButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_circle_black_24dp));
        addVegetableButton.setBackgroundColor(Color.TRANSPARENT);
        addVegetableButton.setVisibility(View.INVISIBLE);

        int width = 100;
        int height = 100;
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(width, height);
        addVegetableButton.setLayoutParams(layoutParams2);

        addVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(Optional.empty(), Optional.empty())) {
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> jsonVegetableList = JsonVegetable.toList(vegetablesJson);

                    JsonVegetable jsonVegetable = new JsonVegetable(vegetableName, grams);
                    jsonVegetableList.add(jsonVegetable);

                    JSONArray jsonVegetableListJsonArray = JsonVegetable.toJson(jsonVegetableList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, jsonVegetableListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    v.setVisibility(View.INVISIBLE);

                    FloatingActionButton addButtonView = findViewById(R.id.addLayoutButton);
                    addButtonView.setVisibility(View.VISIBLE);

                    FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
                    floatingActionButton.setVisibility(View.VISIBLE);

                    View vegetableSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);
                    Spinner vegSpinner = (Spinner) vegetableSpinnerView;
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

        ImageButton removeVegetableButton = new ImageButton(getApplicationContext());
        removeVegetableButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeVegetableButton.setBackgroundColor(Color.TRANSPARENT);
        // REMOVE
        removeVegetableButton.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, height);
        removeVegetableButton.setLayoutParams(layoutParams3);

        removeVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vegetableSpinnerValue = null;
                String gramsSpinnerValue = null;

                View vegetableSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);

                if (vegetableSpinnerView instanceof Spinner) {
                    vegetableSpinnerValue = ((Spinner) vegetableSpinnerView).getSelectedItem().toString();
                }

                View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);

                if (gramsSpinnerView instanceof Spinner) {
                    gramsSpinnerValue = ((Spinner) gramsSpinnerView).getSelectedItem().toString();
                }

                if (check(Optional.of(vegetableSpinnerValue), Optional.of(gramsSpinnerValue))) {
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> vegList = JsonVegetable.toList(vegetablesJson);

                    int index = 0;

                    for (int i = 0; i < vegList.size(); i++) {
                        if (vegList.get(i).getVegetableName().equals(vegetableSpinnerValue) && vegList.get(i).getGrams().equals(gramsSpinnerValue)) {
                            index = (i++);
                        }
                    }

                    vegList.remove(index);

                    JSONArray vegListJsonArray = JsonVegetable.toJson(vegList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, vegListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    ((ViewGroup) v.getParent().getParent()).removeView((ViewGroup) v.getParent());
                }
            }
        });

        linearLayout.addView(vegetablesSpinner);
        linearLayout.addView(gramsSpinner);
        linearLayout.addView(addVegetableButton);
        linearLayout.addView(removeVegetableButton);

        return linearLayout;
    }

    private void addLayout() {
        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        linearLayout.addView(createLayout());
    }

    private LinearLayout createLayout() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.LEFT);

        ArrayAdapter<String> vegetablesArrayAdapter = createArrayAdapter(R.array.vegetables);
        ArrayAdapter<String> gramsArrayAdapter = createArrayAdapter(R.array.grams);

        Spinner vegetablesSpinner = createSpinner(vegetablesArrayAdapter);
        vegetablesSpinner.setDropDownWidth(450);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(450, ViewGroup.LayoutParams.MATCH_PARENT);
        vegetablesSpinner.setLayoutParams(lp);
        Spinner gramsSpinner = createSpinner(gramsArrayAdapter);

        ImageButton addVegetableButton = new ImageButton(this);
        addVegetableButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_circle_black_24dp));
        addVegetableButton.setBackgroundColor(Color.TRANSPARENT);

        int width = 100;
        int height = 100;
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(width, height);
        addVegetableButton.setLayoutParams(layoutParams2);

        addVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(Optional.empty(), Optional.empty())) {
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> jsonVegetableList = JsonVegetable.toList(vegetablesJson);

                    JsonVegetable jsonVegetable = new JsonVegetable(vegetableName, grams);
                    jsonVegetableList.add(jsonVegetable);

                    JSONArray jsonVegetableListJsonArray = JsonVegetable.toJson(jsonVegetableList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, jsonVegetableListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    v.setVisibility(View.INVISIBLE);

                    FloatingActionButton addButtonView = findViewById(R.id.addLayoutButton);
                    addButtonView.setVisibility(View.VISIBLE);

                    FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
                    floatingActionButton.setVisibility(View.VISIBLE);

                    View vegetableSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);
                    Spinner vegSpinner = (Spinner) vegetableSpinnerView;
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

        ImageButton removeVegetableButton = new ImageButton(getApplicationContext());
        removeVegetableButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_forever_black_24dp));
        removeVegetableButton.setBackgroundColor(Color.TRANSPARENT);
        // REMOVE
        removeVegetableButton.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams layoutParams3 = new ViewGroup.LayoutParams(width, height);
        removeVegetableButton.setLayoutParams(layoutParams3);

        removeVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vegetableSpinnerValue = null;
                String gramsSpinnerValue = null;

                View vegetableSpinnerView = ((ViewGroup) v.getParent()).getChildAt(0);

                if (vegetableSpinnerView instanceof Spinner) {
                    vegetableSpinnerValue = ((Spinner) vegetableSpinnerView).getSelectedItem().toString();
                }

                View gramsSpinnerView = ((ViewGroup) v.getParent()).getChildAt(1);

                if (gramsSpinnerView instanceof Spinner) {
                    gramsSpinnerValue = ((Spinner) gramsSpinnerView).getSelectedItem().toString();
                }

                if (check(Optional.of(vegetableSpinnerValue), Optional.of(gramsSpinnerValue))) {
                    String vegetablesJson = sharedPreferenceEntry.getVegetablesJson();
                    List<JsonVegetable> vegList = JsonVegetable.toList(vegetablesJson);

                    int index = 0;

                    for (int i = 0; i < vegList.size(); i++) {
                        if (vegList.get(i).getVegetableName().equals(vegetableSpinnerValue) && vegList.get(i).getGrams().equals(gramsSpinnerValue)) {
                            index = (i++);
                        }
                    }

                    vegList.remove(index);

                    JSONArray vegListJsonArray = JsonVegetable.toJson(vegList);
                    sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, VEGETABLES_JSON.label, vegListJsonArray.toString());
                    sharedPreferencesHelper.save(sharedPreferenceEntry);

                    ((ViewGroup) v.getParent().getParent()).removeView((ViewGroup) v.getParent());
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

    private boolean check(Optional<String> vegetableNameOpt, Optional<String> gramsOpt) {
        String vgtblNm = null;
        String grms = null;

        if (vegetableNameOpt.isPresent() && gramsOpt.isPresent()) {
            vgtblNm = vegetableNameOpt.get();
            grms = gramsOpt.get();
        } else {
            vgtblNm = vegetableName;
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
