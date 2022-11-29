package com.example.rocketride;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String verificationID;

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

        // Image View
        ImageView PendingPhone = findViewById(R.id.PendingPhone),
                VerifyPhone = findViewById(R.id.VerifyPhone),
                Shake = findViewById(R.id.Shake),
                ShakeVerify = findViewById(R.id.ShakeVerify);


        TextView backArrow = findViewById(R.id.backArrow);

        // Verification text inputs
        TextInputEditText userPhone = findViewById(R.id.phone),
                codeVerification = findViewById(R.id.CodeVerification);


        sendButton.setOnClickListener(signUp -> {
            String phoneNumber = userPhone.getText().toString();
            System.out.println(phoneNumber);
            authPhoneNumber(phoneNumber);
            codeLayout.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.GONE);
            PendingPhone.setVisibility(View.GONE);
            VerifyPhone.setVisibility(View.VISIBLE);
        });


        confirmButton.setOnClickListener(l -> {
           String userCode = codeVerification.getText().toString();
           System.out.println(userCode);
           verifyCode(userCode, userIdToken, userEmail, userPassword);

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


    protected void authPhoneNumber(String phoneNumber){
        // Whenever verification is triggered with the whitelisted number,
// provided it is not set for auto-retrieval, onCodeSent will be triggered.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...
                        verificationID = verificationId;
                        // The corresponding whitelisted code above should be used to complete sign-in.
                        //VerificationActivity.this.enableUserManuallyInputCode();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyCode(String code, String userIdToken, String userEmail, String userPassword) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential, userIdToken, userEmail, userPassword, this);
    }

    private void signInWithCredential(PhoneAuthCredential credential, String userIdToken, String userEmail, String userPassword, VerificationActivity verificationActivity) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.

                            // Switch to profile activity
                            verificationActivity.finish();
                            Intent switchActivityIntent = new Intent(verificationActivity, ProfileActivity.class);
                            switchActivityIntent.putExtra("message", "From: " + MainActivity.class.getSimpleName());
                            switchActivityIntent.putExtra("userIdToken", userIdToken);
                            switchActivityIntent.putExtra("userEmail", userEmail);
                            switchActivityIntent.putExtra("userPassword", userPassword);
                            startActivity(switchActivityIntent);

                            Toast.makeText(VerificationActivity.this, "Success!", Toast.LENGTH_LONG).show();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(VerificationActivity.this, "code isn't correct!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
