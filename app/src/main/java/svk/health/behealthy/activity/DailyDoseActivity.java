package svk.health.behealthy.activity;

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

import svk.health.behealthy.R;
import svk.health.behealthy.constants.Constants;
import svk.health.behealthy.model.GenderAge;
import svk.health.behealthy.model.GenderAgeVitamins;
import svk.health.behealthy.model.Vitamin;
import svk.health.behealthy.service.DailyDoseService;
import svk.health.behealthy.service.FileService;
import svk.health.behealthy.service.JsonService;
import svk.health.behealthy.utilities.MenuHelper;
import svk.health.behealthy.utilities.SharedPreferenceEntry;
import svk.health.behealthy.utilities.SharedPreferencesHelper;
import svk.health.behealthy.utilities.UtilsHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DailyDoseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String FILE_NAME = "recommended_daily_dose.txt";
    private static final String TITLE = "Calculate vitamin daily dose";
    private static final String TAG = "DailyDoseActivity.class";
    private static final int MARGIN_BOTTOM = 20;
    private static final Float TEXT_SIZE = 22F;
    private static final int MARGIN_TOP = 70;

    private UtilsHelper utilsHelper;
    private FileService fileService;
    private DailyDoseService dailyDoseService;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: savedInstanceState=" + savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dose);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        fileService = new FileService(getApplicationContext());
        JsonService jsonService = new JsonService(getBaseContext());
        dailyDoseService = new DailyDoseService(jsonService);

        utilsHelper = new UtilsHelper();
        utilsHelper.createTitle(findViewById(R.id.titleTextView), TITLE, TEXT_SIZE, MARGIN_TOP, MARGIN_BOTTOM);

        TextView existingDailyDoseTextView = utilsHelper.createTitle(findViewById(R.id.existDailyDoseTextView), "No recommended daily dose.", TEXT_SIZE, MARGIN_TOP, MARGIN_BOTTOM);

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
        Log.i(TAG, "createRemoveExistingCalculationLayout: vitaminLayout=" + vitaminLayout);

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
        Log.i(TAG, "removeButton");

        LinearLayout removeExistingCalculationLayout = findViewById(R.id.rootLayoutRemoveId);
        removeExistingCalculationLayout.removeAllViewsInLayout();
    }

    private void populateVitamins(LinearLayout vitaminsLayout, GenderAgeVitamins genderAgeVitamins) {
        Log.i(TAG, "populateVitamins: vitaminsLayout=" + vitaminsLayout + ", genderAgeVitamins=" + genderAgeVitamins);

        GenderAge genderAge = genderAgeVitamins.getGenderAge();

        TextView vitaminTitleTextView = utilsHelper.createTitle(new TextView(getApplicationContext()), "Your last calculated daily dose for " + genderAge.getAge() + " years old " + genderAge.getGender(), TEXT_SIZE, 20, MARGIN_BOTTOM);

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
                startIntent.putExtra(Constants.VITAMIN_NAME.label, vitaminNameTextView1.getText());
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
        MenuHelper menuHelper = new MenuHelper(getApplicationContext());
        Intent startIntent = menuHelper.chooseIntent(item.getItemId());
        startActivity(startIntent);

        return super.onOptionsItemSelected(item);
    }

    public void createArrayAdapter(@IdRes int spinnerId, @ArrayRes int resourceListId) {
        Log.i(TAG, "createArrayAdapter: spinnerId=" + spinnerId + ", resourceListId=" + resourceListId);

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
        Log.i(TAG, "validateSpinnersClick: gender=" + loadedGender + ", age=" + loadedAge);

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
