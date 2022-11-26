package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ----- TEST ---
 */

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        // Sign in & sign up buttons
        Button signInButton = findViewById(R.id.signInButton),
                signUpButton = findViewById(R.id.signUpButton);

        // Sign up text inputs
        TextInputEditText signUpUserEmail = findViewById(R.id.eMails),
                          signUpUserPassword = findViewById(R.id.passwordss),
                          signUpUserConfirmPassword = findViewById(R.id.passwords01);

        // Sign in text inputs
        TextInputEditText signInUserEmail = findViewById(R.id.eMail),
                          signInUserPassword = findViewById(R.id.passwords);

        // TextView objects
        TextView signUP = findViewById(R.id.singUp),
                 logIn = findViewById(R.id.logIn),
                 forgotPassword = findViewById(R.id.forgotpass);

        // ImageView objects
        ImageView facebookSignIn = findViewById(R.id.facebookSignIn),
                  googleSignIn = findViewById(R.id.googleSignIn),
                  twitterSignIn = findViewById(R.id.twitterSignIn);

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
        logIn.setOnClickListener(login -> {
            signUP.setTextColor(getResources().getColor(R.color.black));
            logIn.setTextColor(getResources().getColor(R.color.white));
            logIn.setBackground(getResources().getDrawable(R.drawable.switch_trcks, null));
            signUP.setBackground(null);
            signUpLayout.setVisibility(View.GONE);
            logInLayout.setVisibility(View.VISIBLE);
        });

        // On clicking the forgot password textView
        forgotPassword.setOnClickListener(forgotPass -> {
            System.out.println("Forgot password has pressed!");
            this.finish();
            Intent switchActivityIntent = new Intent(this, ResetPasswordActivity.class);
            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
            startActivity(switchActivityIntent);
        });


        // Sign in user after clicking the SIGN IN button
        signInButton.setOnClickListener(l -> {
            System.out.println("SIGN IN button pressed");
            String userEmail = signInUserEmail.getText().toString();
            String userPassword = signInUserPassword.getText().toString();
            System.out.println(userEmail + "\n" + userPassword);
        });

        // Sign up user after clicking the SIGN UP button
        signUpButton.setOnClickListener(l -> {
            System.out.println("SIGN UP button pressed");

            // User's typed information
            String userEmail = signUpUserEmail.getText().toString();
            //String userPhone = signUpUserPhoneNumber.getText().toString();
            String userPassword = signUpUserPassword.getText().toString();
            String confirmUserPassword = signUpUserConfirmPassword.getText().toString();

            // Register the user to firebase
            createFirebaseUserEmailPassword(userEmail, userPassword);
            System.out.println(userEmail + '\n'  + userPassword + '\n' + confirmUserPassword);
        });


        /**
         * Sign in via providers
         */
        facebookSignIn.setOnClickListener(l -> {
            System.out.println("Signing with facebook...");
        });

        googleSignIn.setOnClickListener(l -> {
            System.out.println("Signing with google...");
        });

        twitterSignIn.setOnClickListener(l -> {
            System.out.println("Signing with twitter...");
        });
    }

    protected void createFirebaseUserEmailPassword(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "New user " + userEmail + " registration: " + task.isSuccessful());

                    // Check if succeeded creating the user in firebase
                    if (!task.isSuccessful()) {
                        Log.d(TAG,"Authentication failed. " + task.getException());
                    }
//                        else {
//                            SignupActivity.this.startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                            SignupActivity.this.finish();
//                        }
                });
    }
}