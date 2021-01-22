package svk.health.behealthy.activity;

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

import svk.health.behealthy.R;
import svk.health.behealthy.model.Fruit;
import svk.health.behealthy.model.Vegetable;
import svk.health.behealthy.model.Vitamin;
import svk.health.behealthy.service.JsonService;
import svk.health.behealthy.utilities.MenuHelper;
import svk.health.behealthy.utilities.UtilsHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import svk.health.behealthy.constants.Constants;

public class VegetableDetailActivity extends AppCompatActivity {

    private JsonService jsonService;
    private UtilsHelper utilsHelper;
    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_detail);

        jsonService = new JsonService(getBaseContext());

        Intent intent = getIntent();
        String vegetableName = intent.getStringExtra (Constants.VEGETABLE_NAME.label);

        List<List<Vitamin>> vitamins = new ArrayList<>();
        for(Vegetable vegetable: jsonService.processFile(R.raw.vegetables).getVegetables()){
            if(vegetable.getName().equals(vegetableName)){
                vitamins.add(vegetable.getVitamins());
            }
        }

        // TODO - refactor - to one object
        for(Fruit fruit: jsonService.processFile(R.raw.fruits).getFruits()){
            if(fruit.getName().equals(vegetableName)){
                vitamins.add(fruit.getVitamins());
            }
        }

        System.out.println(vitamins);

        LinearLayout rootLayout2 = findViewById(R.id.rootLayout2);
        populateVitamins(rootLayout2, vegetableName, vitamins.get(0));
    }

    private void populateVitamins(LinearLayout rootLayout2, String vegetableName, List<Vitamin> vitamins) {
        utilsHelper = new UtilsHelper();
        TextView vitaminTitleTextView = utilsHelper.createTitle(new TextView(getApplicationContext()), "Vitamins in 100 / g of " + vegetableName, (float) 22, 70, 20);
        rootLayout2.addView(vitaminTitleTextView);

        Comparator<Vitamin> comparator = (Vitamin v1, Vitamin v2) -> Double.valueOf(v1.getAmount()).compareTo(v2.getAmount());
        vitamins.sort(comparator.reversed());

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
