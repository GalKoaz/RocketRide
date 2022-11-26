package com.example.rocketride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // TextView objects
        TextView backToSign = findViewById(R.id.backSigning);

        // Reset Button
        Button resetButton = findViewById(R.id.resetButton);

        // Reset text inputs
        TextInputEditText userEmail = findViewById(R.id.eMail),
                          userPhone = findViewById(R.id.phone);

        backToSign.setOnClickListener(l -> {
            this.finish();
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            switchActivityIntent.putExtra("message", "From: " + ResetPasswordActivity.class.getSimpleName());
            startActivity(switchActivityIntent);
        });

        resetButton.setOnClickListener(l -> {
            System.out.println("Reset the password...");
        });
    }

    protected void resetUserPassword(String userEmail, String userPhoneNumber){

    }
}