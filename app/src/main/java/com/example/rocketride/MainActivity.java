package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rocketride.Login.ResetPasswordActivity;
import com.example.rocketride.MenuActivities.HomeActivity;
import com.example.rocketride.Login.VerificationActivity;
import com.example.rocketride.Ride.seatsSelectionActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ----- TEST ---
 */

public class MainActivity extends AppCompatActivity {
    // Google sign-in client
    private GoogleSignInClient mGoogleSignInClient;

    // Firebase authentication reference
    private FirebaseAuth firebaseAuth;

    // Firebase firestore database reference
    private FirebaseFirestore db;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RocketRide);
        setContentView(R.layout.activity_main);

        // Init auth and database objects
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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

        // If view flag is on then sign up layout is presented
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
        ProgressBar strongpassword = findViewById(R.id.progressBar);
        // Password text field on change events.
        Drawable progressDrawable2 = strongpassword.getProgressDrawable().mutate();
        progressDrawable2.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        strongpassword.setProgressDrawable(progressDrawable2);
        strongpassword.setProgress(0);
        signUpUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView ENL = findViewById(R.id.PTS);
                ENL.setVisibility(View.GONE);
                String str = Objects.requireNonNull(signUpUserPassword.getText()).toString();
                int strong = printStrongNess(str);
                if (strong == 3){
                    Drawable progressDrawable = strongpassword.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                    strongpassword.setProgressDrawable(progressDrawable);
                    strongpassword.setProgress(100);
                }
                else if (strong == 2){
                    Drawable progressDrawable = strongpassword.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
                    strongpassword.setProgressDrawable(progressDrawable);
                    strongpassword.setProgress(66);
                }
                else {
                    Drawable progressDrawable = strongpassword.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                    strongpassword.setProgressDrawable(progressDrawable);
                    strongpassword.setProgress(33);
                }
            }
        });
        signUpUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                TextView ENL = findViewById(R.id.ENL);
                ENL.setVisibility(View.GONE);
            }
        });
        signUpUserConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                TextView ENL = findViewById(R.id.PNM_text);
                ENL.setVisibility(View.GONE);
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
            String userEmail = Objects.requireNonNull(signInUserEmail.getText()).toString();
            String userPassword = Objects.requireNonNull(signInUserPassword.getText()).toString();
            System.out.println(userEmail + "\n" + userPassword);
            // Connect the user to firebase
            signUserWithEmailPassword(userEmail, userPassword);
        });

        // Sign up user after clicking the SIGN UP button
        signUpButton.setOnClickListener(l -> {
            System.out.println("SIGN UP button pressed");

            // User's typed information
            String userEmail = Objects.requireNonNull(signUpUserEmail.getText()).toString();
            String userPassword = Objects.requireNonNull(signUpUserPassword.getText()).toString();
            String confirmUserPassword = Objects.requireNonNull(signUpUserConfirmPassword.getText()).toString();
            boolean close = false;
            if(!userPassword.equals(confirmUserPassword)){
                // TextView objects
                TextView PNM = findViewById(R.id.PNM_text);
                PNM.setVisibility(View.VISIBLE);
                close = true;
            }
            if(!userEmail.contains("@")){
                TextView ENL = findViewById(R.id.ENL);
                ENL.setVisibility(View.VISIBLE);
                close = true;
            }

            if (userPassword.length() < 6){ // Check if password length too
                TextView ENL = findViewById(R.id.PTS);
                ENL.setVisibility(View.VISIBLE);
                close = true;

            }
            if(close){
                Drawable progressDrawable = strongpassword.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                strongpassword.setProgressDrawable(progressDrawable);
                strongpassword.setProgress(0);
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

    public static int printStrongNess(String input)
    {
        // Checking lower alphabet in string
        int n = input.length();
        boolean hasLower = false, hasUpper = false,
                hasDigit = false, specialChar = false;
        Set<Character> set = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));
        for (char i : input.toCharArray())
        {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                specialChar = true;
            if (!Character.isDigit(i) && !Character.isLetter(i) && !Character.isSpace(i)) {
                specialChar = true;
            }
        }

        // Strength of password
        if (hasDigit && hasLower && hasUpper && specialChar
                && (n >= 8))
            return 3;
        else if ((hasLower || hasUpper || specialChar)
                && (n >= 6))
            return 2;
        else
            return 1;
    }


    /**
     * Signing in the user with the given email and password,
     * if authentication succeeded user is moved to the home activity.
     * @param userEmail current user's email address.
     * @param userPassword current user's password.
     */
    private void signUserWithEmailPassword(String userEmail, String userPassword) {
        if(userEmail.equals("") || userPassword.equals("")){
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        System.out.println("USER ID: " + user.getUid());

                        // Switch to home screen
                        moveToHomeActivity(user.getUid());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * A part of configuration for google authentication.
     */
    private void signUserWithGoogle(){
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

    /**
     * Method signs up the current user for google authentication.
     * Then deletes the gmail authentication for signing up the user after completing
     * the signing process and move the user to the phone verification activity.
     * @param idToken google's token id.
     */
    private void firebaseAuthWithGoogle(String idToken) {
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
                        }
                        else { // current user isn't a new user
                            Log.d("TAG", "Is Old User!");

                            // Switch to home screen
                            moveToHomeActivity(user.getUid());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user.
                        Log.w("TAGsoigninfail", "signInWithCredential:failure", task.getException());
                    }

                });
    }

    /**
     * Method gets current user's id and send his details to the home activity.
     * @param UID current user's id
     */
    private void moveToHomeActivity(String UID) {
        // Get current user document
        Query userQuery = db.collection("users").whereEqualTo("UID", UID);

        userQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot queryDocumentSnapshot = task.getResult();
                DocumentSnapshot documentReference = queryDocumentSnapshot.getDocuments().get(0);

                // Get the link of current user's profile image and type (driver/rider) for permissions
                String profileImageLink = documentReference.getString("profile_image_link"),
                       userType = documentReference.getString("type"),
                       userFirstName = documentReference.getString("first_name"),
                       userLastName = documentReference.getString("last_name");

                // Move the user to home activity
                this.finish();
                Intent switchActivityIntent = new Intent(this, HomeActivity.class);

                // Send the profile image and type to the home activity
                switchActivityIntent.putExtra("profile_image_link", profileImageLink);
                switchActivityIntent.putExtra("type", userType);
                switchActivityIntent.putExtra("full_name", userFirstName + " " + userLastName);

                startActivity(switchActivityIntent);
            }
            else{
                Log.d(TAG, "Error occurred in getting user document");
            }
        });
    }
}