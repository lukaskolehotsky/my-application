package svk.health.behealthy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import svk.health.behealthy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        LinearLayout linearLayout0 = findViewById(R.id.dashboard_header_id);
        linearLayout0.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startIntent);
        });

        LinearLayout linearLayout1 = findViewById(R.id.dashboard_img_1);
        linearLayout1.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), BmiActivity.class);
            startActivity(startIntent);
        });

        LinearLayout linearLayout2 = findViewById(R.id.dashboard_img_2);
        linearLayout2.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), RfmActivity.class);
            startActivity(startIntent);
        });

        LinearLayout linearLayout3 = findViewById(R.id.dashboard_img_3);
        linearLayout3.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), DailyDoseActivity.class);
            startActivity(startIntent);
        });

        LinearLayout linearLayout4 = findViewById(R.id.dashboard_img_4);
        linearLayout4.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(startIntent);
        });
    }

}
