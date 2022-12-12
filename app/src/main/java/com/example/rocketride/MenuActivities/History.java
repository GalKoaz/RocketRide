package com.example.rocketride.MenuActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.rocketride.MapsDriverActivity;
import com.example.rocketride.R;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Back arrow button
        ImageView backArrow = findViewById(R.id.leftArrowHistory);
        backArrow.setOnClickListener(l -> {
            // Switch back to MapsDriverActivity activity
            this.finish();
            Intent switchActivityBecomeDriverIntent = new Intent(this, MapsDriverActivity.class);
            startActivity(switchActivityBecomeDriverIntent);
        });
    }
}