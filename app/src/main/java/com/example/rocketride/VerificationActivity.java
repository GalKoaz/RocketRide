package com.example.rocketride;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail"),
               userPassword = extras.getString("userPassword");

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


        confirmButton.setOnClickListener(l -> {
            // Register the user to firebase
            createFirebaseUserEmailPassword(userEmail, userPassword);
            System.out.println(userEmail + '\n'  + userPassword);
        });
    }


    protected void createFirebaseUserEmailPassword(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "New user " + userEmail + " registration: " + task.isSuccessful());

                    // Check if succeeded creating the user in firebase
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Authentication failed. " + task.getException());
                    }
//                        else {
//                            SignupActivity.this.startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                            SignupActivity.this.finish();
//                        }
                });
    }
}