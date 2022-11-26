package com.example.rocketride;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class VerificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        // LinearLayout objects
        LinearLayout codeLayout = findViewById(R.id.codeLayout),
                phoneLayout = findViewById(R.id.phoneLayout);

        // Buttons
        Button sendButton = findViewById(R.id.sendButton),
                confirmButton = findViewById(R.id.confirmButton);

        // Verification text inputs
        TextInputEditText userPhone = findViewById(R.id.phone),
                CodeVerification = findViewById(R.id.CodeVerification);

        sendButton.setOnClickListener(signUp -> {
            codeLayout.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.GONE);
        });
    }
}
