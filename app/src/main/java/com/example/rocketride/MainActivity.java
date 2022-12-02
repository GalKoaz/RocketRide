package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

/**
 * ----- TEST ---
 */

public class MainActivity extends AppCompatActivity {
    // Google sign-in client
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth firebaseAuth;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RocketRide);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        this.finish();
        Intent switchActivityIntent2 = new Intent(this, seatsSelectionActivity.class);
        startActivity(switchActivityIntent2);

        // get view flag
        Bundle extras = getIntent().getExtras();
        boolean viewFlag = false;
        String userEmailExtras = "", userPasswordExtras = "";
        if(extras != null){
            viewFlag = extras.getBoolean("ViewFlag", false);
            userEmailExtras = extras.getString("userEmail", "");
            userPasswordExtras = extras.getString("userPassword", "");
        }

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
        ImageView googleSignIn = findViewById(R.id.googleSignIn);

        // LinearLayout objects
        LinearLayout signUpLayout = findViewById(R.id.singUpLayout),
                     logInLayout = findViewById(R.id.logInLayout);

        if(viewFlag){
            signUP.setTextColor(getResources().getColor(R.color.white));
            logIn.setTextColor(getResources().getColor(R.color.black));
            signUP.setBackground(getResources().getDrawable(R.drawable.switch_trcks, null));
            logIn.setBackground(null);
            signUpLayout.setVisibility(View.VISIBLE);
            logInLayout.setVisibility(View.GONE);
            signUpUserEmail.setText(userEmailExtras);
            signUpUserPassword.setText(userPasswordExtras);
            signUpUserConfirmPassword.setText(userPasswordExtras);
        }

        signUpUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO: 26/11/2022 add a gui green red yellow
                System.out.println(signUpUserPassword.getText().toString());
            }
        });

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

            // Connect the user to firebase
            signUserWithEmailPassword(userEmail, userPassword, this);
        });

        // Sign up user after clicking the SIGN UP button
        signUpButton.setOnClickListener(l -> {
            System.out.println("SIGN UP button pressed");

            // User's typed information
            String userEmail = signUpUserEmail.getText().toString();
            String userPassword = signUpUserPassword.getText().toString();
            String confirmUserPassword = signUpUserConfirmPassword.getText().toString();
            if(!userPassword.equals(confirmUserPassword)){
                Toast.makeText(MainActivity.this, "Passwords not matching.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(!userEmail.contains("@")){
                Toast.makeText(MainActivity.this, "Mail address is not legal.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (userPassword.length() < 6){ // Check if password length too
                Toast.makeText(MainActivity.this, "Password length has to be at least 6.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Activate the verification activity
            this.finish();
            Intent switchActivityIntent = new Intent(this, VerificationActivity.class);
            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
            switchActivityIntent.putExtra("userEmail", userEmail);
            switchActivityIntent.putExtra("userPassword", userPassword);
            startActivity(switchActivityIntent);

        });


        /**
         * Sign in via providers
         */
        googleSignIn.setOnClickListener(l -> {
            System.out.println("Signing with google...");
            signUserWithGoogle();
        });
    }


    protected void signUserWithEmailPassword(String userEmail, String userPassword, MainActivity mainActivity) {
        if(userEmail.equals("admin")){
            // Activate the verification activity
            this.finish();
            Intent switchActivityIntent = new Intent(this, MapsActivity.class);
            startActivity(switchActivityIntent);
        }
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            mainActivity.finish();
                            Intent switchActivityIntent = new Intent(mainActivity, MapsDriverActivity.class);
                            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
                            startActivity(switchActivityIntent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    protected void signUserWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 2) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAGtoken", "firebaseAuthWithGoogle:" + account.getId());

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAGFailed", "Google sign in failed", e);
            }
        }
    }

    protected void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGsigninsuccess", "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        // Check if the user is new and update UI accordingly.
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (isNewUser) {
                            Log.d("TAG", "Is New User!");
                            user.delete();

                            // Switch to phone verification screen to verify the user
                            this.finish();
                            Intent switchActivityIntent = new Intent(this, VerificationActivity.class);
                            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
                            switchActivityIntent.putExtra("googleUserIdToken", idToken);
                            startActivity(switchActivityIntent);
                        } else {
                            Log.d("TAG", "Is Old User!");

                            // Switch to home screen
                            this.finish();
                            Intent switchActivityIntent = new Intent(this, HomeScreenActivity.class);
                            //Intent switchActivityIntent = new Intent(this, ProfileActivity.class);
                            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
                            startActivity(switchActivityIntent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user.
                        Log.w("TAGsoigninfail", "signInWithCredential:failure", task.getException());
                    }

                });
    }
}