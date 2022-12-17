package com.example.rocketride.Login;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.rocketride.MainActivity;
import com.example.rocketride.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
            String userMailStr = userEmail.getText().toString();
            resetUserPassword(userMailStr, "");
        });
    }

    protected void resetUserPassword(String userEmail, String userPhoneNumber){
        firebaseAuth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "Sending reset password to:  " + userEmail + " " + task.isSuccessful());

                    // Check if succeeded creating the user in firebase
                    if (!task.isSuccessful()) {
                        Log.d(TAG,"Incorrect details: " + task.getException());
                    }
        });
    }
}