package com.example.behealthy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.behealthy.R;
import com.example.behealthy.constants.Constants;
import com.example.behealthy.model.GenderAge;
import com.example.behealthy.model.GenderAgeVitamins;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.service.DailyDoseService;
import com.example.behealthy.service.FileService;
import com.example.behealthy.service.JsonService;
import com.example.behealthy.utilities.MenuHelper;
import com.example.behealthy.utilities.SharedPreferenceEntry;
import com.example.behealthy.utilities.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import static com.example.behealthy.constants.Constants.VITAMIN_NAME;

import java.util.List;

public class DailyDoseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String FILE_NAME = "recommended_daily_dose.txt";

    private MenuHelper menuHelper;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    private FileService fileService;
    private JsonService jsonService;
    private DailyDoseService dailyDoseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dose);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        fileService = new FileService(getApplicationContext());
        jsonService = new JsonService(getBaseContext());
        dailyDoseService = new DailyDoseService(jsonService);

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("Calculate vitamin daily dose");
        titleTextView.setText("Calculate vitamin daily dose");
        titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);

        TextView existingDailyDoseTextView = findViewById(R.id.existDailyDoseTextView);
        existingDailyDoseTextView.setText("No recommended daily dose.");

        createArrayAdapter(R.id.genderSpinner, R.array.genderList);
        createArrayAdapter(R.id.ageSpinner, R.array.ageList);

        LinearLayout vitaminsLayout = findViewById(R.id.rootLayout2);
        String genderAgeVitaminsString = null;
        try {
            genderAgeVitaminsString = fileService.loadFile(FILE_NAME);
        } catch (Exception e) {
            fileService.createFile(FILE_NAME);
        }

        if (genderAgeVitaminsString == null) {
            existingDailyDoseTextView.setVisibility(View.VISIBLE);
        } else {
            GenderAgeVitamins genderAgeVitamins = GenderAgeVitamins.toObject(genderAgeVitaminsString);
            if (genderAgeVitamins.getGenderAge() != null && genderAgeVitamins.getVitamins() != null) {
                existingDailyDoseTextView.setVisibility(View.INVISIBLE);
                populateVitamins(vitaminsLayout, genderAgeVitamins);
                createRemoveExistingCalculationLayout(vitaminsLayout);
            }
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_1);
        floatingActionButton.setOnClickListener(v -> {
            SharedPreferenceEntry entry = sharedPreferencesHelper.get();
            String loadedGender = entry.getGender();
            String loadedAge = entry.getAge();

            if (validateSpinnersClick(loadedGender, loadedAge)) {
                List<Vitamin> dailyDoseVitamins = dailyDoseService.getDailyDoseVitamins(loadedAge, loadedGender);
                fileService.deleteFile(FILE_NAME);
                fileService.createFile(FILE_NAME);

                GenderAgeVitamins existingGenderAgeVitamins = new GenderAgeVitamins();
                GenderAge genderAge = new GenderAge();
                genderAge.setGender(loadedGender);
                genderAge.setAge(loadedAge);
                existingGenderAgeVitamins.setGenderAge(genderAge);
                existingGenderAgeVitamins.setVitamins(dailyDoseVitamins);

                fileService.saveFile(GenderAgeVitamins.toJson(existingGenderAgeVitamins).toString(), FILE_NAME);

                existingDailyDoseTextView.setVisibility(View.INVISIBLE);
                populateVitamins(vitaminsLayout, existingGenderAgeVitamins);
                createRemoveExistingCalculationLayout(vitaminsLayout);
            }
        });
    }

    private void createRemoveExistingCalculationLayout(LinearLayout vitaminLayout) {
        LinearLayout removeExistingCalculationLayout = findViewById(R.id.rootLayoutRemoveId);

        if (findViewById(R.id.removeButtonId) == null) {
            Button removeExistingCalculationButton = new Button(this);
            removeExistingCalculationButton.setId(R.id.removeButtonId);
            removeExistingCalculationButton.setText("Remove existing calculation");

            removeExistingCalculationLayout.addView(removeExistingCalculationButton);

            removeExistingCalculationButton.setOnClickListener(v -> {
                vitaminLayout.removeAllViewsInLayout();
                fileService.deleteFile(FILE_NAME);

                ScrollView scrollView = findViewById(R.id.scrollViewId);
                scrollView.fullScroll(ScrollView.FOCUS_UP);

                TextView existDailyDoseTextView = findViewById(R.id.existDailyDoseTextView);
                existDailyDoseTextView.setVisibility(View.VISIBLE);

                removeButton();
            });
        }
    }

    private void removeButton() {
        LinearLayout removeExistingCalculationLayout = findViewById(R.id.rootLayoutRemoveId);
        removeExistingCalculationLayout.removeAllViewsInLayout();
    }

    private void populateVitamins(LinearLayout vitaminsLayout, GenderAgeVitamins genderAgeVitamins) {
        GenderAge genderAge = genderAgeVitamins.getGenderAge();
        TextView vitaminTitleTextView = new TextView(getApplicationContext());
        vitaminTitleTextView.setGravity(Gravity.CENTER);
        vitaminTitleTextView.setText("Your last calculated daily dose for " + genderAge.getAge() + " years old " + genderAge.getGender());
        vitaminTitleTextView.setTypeface(vitaminTitleTextView.getTypeface(), Typeface.BOLD);
        vitaminTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        vitaminTitleTextView.setTextColor(Color.BLACK);
        vitaminTitleTextView.setPadding(16, 0, 16, 16);

        vitaminsLayout.removeAllViewsInLayout();
        vitaminsLayout.addView(vitaminTitleTextView);

        genderAgeVitamins.getVitamins().forEach(dailyDoseVitamin -> {
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

            vitaminNameTextView.setText(dailyDoseVitamin.getName());
            amountUnitTextView.setText(dailyDoseVitamin.getAmount() + "" + dailyDoseVitamin.getUnit());

            linearLayout.addView(vitaminNameTextView);
            linearLayout.addView(amountUnitTextView);

            linearLayout.setOnClickListener(v -> {
                View linearLayoutView = ((ViewGroup) v).getChildAt(0);
                TextView vitaminNameTextView1 = (TextView) linearLayoutView;

                Intent startIntent = new Intent(getApplicationContext(), VitaminsActivity.class);
                startIntent.putExtra(VITAMIN_NAME.label, vitaminNameTextView1.getText());
                startActivity(startIntent);
            });

            vitaminsLayout.addView(linearLayout);
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

    public void createArrayAdapter(@IdRes int spinnerId, @ArrayRes int resourceListId) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(resourceListId)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        String firstItemName = parent.getAdapter().getItem(0).toString();

        sharedPreferencesHelper.populateSharedPreferenceEntry(sharedPreferenceEntry, firstItemName, value);
        sharedPreferencesHelper.save(sharedPreferenceEntry);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validateSpinnersClick(String loadedGender, String loadedAge) {
        if (loadedGender.equals(Constants.GENDER.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender, age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!loadedGender.equals(Constants.GENDER.label) && loadedAge.equals(Constants.AGE.label)) {
            Toast.makeText(getBaseContext(), "Please choose age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (loadedGender.equals(Constants.GENDER.label)) {
            Toast.makeText(getBaseContext(), "Please choose gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
