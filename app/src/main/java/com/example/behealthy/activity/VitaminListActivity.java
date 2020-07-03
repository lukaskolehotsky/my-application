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
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.utilities.MenuHelper;

import java.util.List;

import static com.example.behealthy.constants.Constants.AGE;
import static com.example.behealthy.constants.Constants.GENDER;
import static com.example.behealthy.constants.Constants.VITAMIN_LIST;
import static com.example.behealthy.constants.Constants.VITAMIN_NAME;

public class VitaminListActivity extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamin_list);

        Intent intent = getIntent();
        List<Vitamin> vitamins = (List) intent.getParcelableArrayListExtra (VITAMIN_LIST.label);

        String age = (String) intent.getStringExtra(AGE.label);
        String gender = (String) intent.getStringExtra(GENDER.label);

        LinearLayout rootLayout2 = findViewById(R.id.rootLayout2);
        populateVitamins(rootLayout2, age, gender, vitamins);
    }

    private void populateVitamins(LinearLayout rootLayout2, String age, String gender, List<Vitamin> vitamins) {
        TextView vitaminTitleTextView = new TextView(getApplicationContext());
        vitaminTitleTextView.setGravity(Gravity.CENTER);
        vitaminTitleTextView.setText("Recommended daily dose of vitamins for " + age + " year old " + gender);
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

            linearLayout.setOnClickListener(v -> {
                View linearLayoutView = ((ViewGroup) v).getChildAt(0);
                TextView vitaminNameTextView1 = (TextView) linearLayoutView;

                Intent startIntent = new Intent(getApplicationContext(), VitaminsActivity.class);
                startIntent.putExtra(VITAMIN_NAME.label, vitaminNameTextView1.getText());
                startActivity(startIntent);
            });

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
