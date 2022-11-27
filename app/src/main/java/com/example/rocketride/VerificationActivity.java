package com.example.rocketride;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail"),
               userPassword = extras.getString("userPassword"),
               userIdToken = extras.getString("googleUserIdToken");

        // LinearLayout objects
        LinearLayout codeLayout = findViewById(R.id.codeLayout),
                phoneLayout = findViewById(R.id.phoneLayout);

        // Buttons
        Button sendButton = findViewById(R.id.sendButton),
                confirmButton = findViewById(R.id.confirmButton);

        TextView backArrow = findViewById(R.id.backArrow);

        // Verification text inputs
        TextInputEditText userPhone = findViewById(R.id.phone),
                CodeVerification = findViewById(R.id.CodeVerification);
        sendButton.setOnClickListener(signUp -> {
            codeLayout.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.GONE);
        });


        confirmButton.setOnClickListener(l -> {
            // if user has signed up with google, then call the associated Firebase function
            if (userIdToken != null) {
                firebaseAuthWithGoogle(userIdToken);
                return;
            }
            // Register the user to firebase
            createFirebaseUserEmailPassword(userEmail, userPassword);
            System.out.println(userEmail + '\n'  + userPassword);
        });

        backArrow.setOnClickListener(l -> {
            // Activate the verification activity
            this.finish();
            Intent switchActivityIntent = new Intent(this, MainActivity.class);
            switchActivityIntent.putExtra("ViewFlag", true);
            switchActivityIntent.putExtra("userEmail", userEmail);
            switchActivityIntent.putExtra("userPassword", userPassword);
            startActivity(switchActivityIntent);
        });
    }


    protected void createFirebaseUserEmailPassword(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "New user " + userEmail + " registration: " + task.isSuccessful());

                    // Check if succeeded creating the user in firebase
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Authentication failed. " + task.getException());
                        Toast.makeText(VerificationActivity.this, "SignUp failed - try again.",
                                Toast.LENGTH_SHORT).show();
                        // Activate the verification activity
                        this.finish();
                        Intent switchActivityIntent = new Intent(this, MainActivity.class);
                        switchActivityIntent.putExtra("ViewFlag", true);
                        switchActivityIntent.putExtra("userEmail", userEmail);
                        switchActivityIntent.putExtra("userPassword", userPassword);
                        startActivity(switchActivityIntent);
                        return;
                    }
//                  // Activate the verification activity
                    this.finish();
                    Intent switchActivityIntent = new Intent(this, MainActivity.class);
                    startActivity(switchActivityIntent);
                });
    }

    protected void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGsigninsuccess", "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(VerificationActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        // Activate the verification activity
                        this.finish();
                        Intent switchActivityIntent = new Intent(this, MainActivity.class);
                        switchActivityIntent.putExtra("ViewFlag", true);
                        switchActivityIntent.putExtra("userEmail", "");
                        switchActivityIntent.putExtra("userPassword", "");
                        startActivity(switchActivityIntent);
                        return;

                    }
                    else {
                        Toast.makeText(VerificationActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user.
                        Log.w("TAGsoigninfail", "signInWithCredential:failure", task.getException());
                    }

                });
    }
}