package com.example.rocketride;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ----- TEST ---
 */

public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView objects
        TextView signUP = findViewById(R.id.singUp),
                 logIn = findViewById(R.id.logIn);

        // LinearLayout objects
        LinearLayout signUpLayout = findViewById(R.id.singUpLayout),
                     logInLayout = findViewById(R.id.logInLayout);


        // On clicking the sign up textView
        signUP.setOnClickListener(signUp -> {
            signUP.setTextColor(getResources().getColor(R.color.white));
            logIn.setTextColor(getResources().getColor(R.color.black));
            signUP.setBackground(getResources().getDrawable(R.drawable.switch_trcks, null));
            logIn.setBackground(null);
            signUpLayout.setVisibility(View.VISIBLE);
            logInLayout.setVisibility(View.GONE);
        });

        // On clicking the login textView
        logIn.setOnClickListener( login -> {
            signUP.setTextColor(getResources().getColor(R.color.black));
            logIn.setTextColor(getResources().getColor(R.color.white));
            logIn.setBackground(getResources().getDrawable(R.drawable.switch_trcks, null));
            signUP.setBackground(null);
            signUpLayout.setVisibility(View.GONE);
            logInLayout.setVisibility(View.VISIBLE);
        });

    }
}