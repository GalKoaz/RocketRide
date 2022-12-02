package com.example.rocketride;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ImageView RiderImageCapture, DriverImageCapture, DriverProfileImageCapture;
    private Uri RiderImageUri, DriverProfileImageUri, DriverLicenseImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        // get view flag
        Bundle extras = getIntent().getExtras();
        String userIdToken = extras.getString("userIdToken", null),
        userEmailExtras = extras.getString("userEmail", ""),
        userPasswordExtras = extras.getString("userPassword", ""),
        userPhoneNumberExtras = extras.getString("userPhoneNumber", "");


        // launcher Driver Profile Upload Image
        ActivityResultLauncher<Intent> launcher0 =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        System.out.println("Driver uri: " + result.getData().getData());
                        // Use the uri to load the image
                        DriverProfileImageCapture.setImageURI(uri);
                        DriverLicenseImageUri = uri;
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        ImagePicker.Companion.getError(result.getData());
                    }
                });

        // launcher Driver Upload Image
        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        System.out.println("Driver uri: " + result.getData().getData());
                        // Use the uri to load the image
                        DriverImageCapture.setImageURI(uri);
                        DriverProfileImageUri = uri;
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        ImagePicker.Companion.getError(result.getData());
                    }
                });

        // launcher Driver Upload Image
        ActivityResultLauncher<Intent> launcher2=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        // Use the uri to load the image
                        RiderImageCapture.setImageURI(uri);
                        RiderImageUri = uri;
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        ImagePicker.Companion.getError(result.getData());
                    }
                });

        // ImageView Rider
        RiderImageCapture = findViewById(R.id.RiderImageCapture);

        // ImageView Driver
        DriverImageCapture = findViewById(R.id.DriverImageCapture);

        DriverProfileImageCapture = findViewById(R.id.DriverProfileImageCapture);

        // Buttons
        Button uploadImage = findViewById(R.id.uploadImage),
                ConfirmRiderButton = findViewById(R.id.ConfirmRiderButton),
                uploadImageDriver = findViewById(R.id.uploadImageDriver),
                ConfirmDriverButton = findViewById(R.id.ConfirmDriverButton),
                DriverProfileUploadImage = findViewById(R.id.DriverProfileUploadImage);


        // Driver text inputs
        TextInputEditText FirstNameDriver = findViewById(R.id.NickNameDriver),
                LastNameDriver = findViewById(R.id.LastNameDriver),
                IDNumber = findViewById(R.id.IDNumber),
                NumberPlate = findViewById(R.id.NumberPlate);

        // Rider text inputs
        TextInputEditText FirstNameRider = findViewById(R.id.FirstNameRider),
                          LastNameRider = findViewById(R.id.LastNameRider);

        // TextView objects
        TextView Rider = findViewById(R.id.Rider),
                Driver = findViewById(R.id.Driver);


        // LinearLayout objects
        LinearLayout RiderLayout = findViewById(R.id.RiderLayout),
                DriverLayout = findViewById(R.id.DriverLayout);

        // Rider Layout switcher
        Rider.setOnClickListener(rider -> {
            Rider.setTextColor(getResources().getColor(R.color.white));
            Driver.setTextColor(getResources().getColor(R.color.black));
            Rider.setBackground(getResources().getDrawable(R.drawable.switch_prof, null));
            Driver.setBackground(null);
            RiderLayout.setVisibility(View.VISIBLE);
            DriverLayout.setVisibility(View.GONE);
        });

        // Driver Layout switcher
        Driver.setOnClickListener(driver -> {
            Rider.setTextColor(getResources().getColor(R.color.black));
            Driver.setTextColor(getResources().getColor(R.color.white));
            Driver.setBackground(getResources().getDrawable(R.drawable.switch_prof, null));
            Rider.setBackground(null);
            RiderLayout.setVisibility(View.GONE);
            DriverLayout.setVisibility(View.VISIBLE);
        });

        // upload Image Profile Driver Event
        DriverProfileUploadImage.setOnClickListener(view -> {
            System.out.println("Enter To the UploadImageDriver");
            ImagePicker.Companion.with(this)
                    .crop()
                    .cropOval()
                    .maxResultSize(480, 270, true)
                    .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                    .createIntentFromDialog((Function1) (new Function1() {
                        public Object invoke(Object var1) {
                            this.invoke((Intent) var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Intent it) {
                            Intrinsics.checkNotNullParameter(it, "it");
                            launcher0.launch(it);
                        }
                    }));
        });

        // upload Image Driver Event
        uploadImageDriver.setOnClickListener(view -> {
            System.out.println("Enter To the UploadImageDriver");
            ImagePicker.Companion.with(this)
                    .crop()
                    .cropOval()
                    .maxResultSize(480, 270, true)
                    .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                    .createIntentFromDialog((Function1) (new Function1() {
                        public Object invoke(Object var1) {
                            this.invoke((Intent) var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Intent it) {
                            Intrinsics.checkNotNullParameter(it, "it");
                            launcher.launch(it);
                        }
                    }));
        });

        // upload Image Rider Event
        uploadImage.setOnClickListener(view -> {
            System.out.println("Enter To the UploadImageDriver");
            ImagePicker.Companion.with(this)
                    .crop()
                    .cropOval()
                    .maxResultSize(480, 270, true)
                    .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                    .createIntentFromDialog((Function1) (new Function1() {
                        public Object invoke(Object var1) {
                            this.invoke((Intent) var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Intent it) {
                            Intrinsics.checkNotNullParameter(it, "it");
                            launcher2.launch(it);
                        }
                    }));
        });

        ConfirmRiderButton.setOnClickListener(l -> {
            if (RiderImageUri == null){
                Toast.makeText(ProfileActivity.this, "Upload the required images!", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstNameRider = FirstNameRider.getText().toString(),
                    lastNameRider = LastNameRider.getText().toString();
            storeRiderDetailsInFirestore(userEmailExtras, userPhoneNumberExtras, firstNameRider, lastNameRider);
            register(userIdToken, userEmailExtras, userPasswordExtras);
        });

        ConfirmDriverButton.setOnClickListener(l -> {
            if (DriverProfileImageUri == null || DriverLicenseImageUri == null){
                Toast.makeText(ProfileActivity.this, "Upload the required images!", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstNameDriver = FirstNameDriver.getText().toString(),
                    lastNameDriver = LastNameDriver.getText().toString(),
                    idNumber = IDNumber.getText().toString(),
                    plateNumber = NumberPlate.getText().toString();

            storeDriverDetailsInFirestore(userEmailExtras, userPhoneNumberExtras, firstNameDriver, lastNameDriver, idNumber, plateNumber);
            register(userIdToken, userEmailExtras, userPasswordExtras);

        });
    }

    protected void createFirebaseUserEmailPassword(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "New user " + userEmail + " registration: " + task.isSuccessful());

                    // Check if succeeded creating the user in firebase
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Authentication failed. " + task.getException());
                        Toast.makeText(ProfileActivity.this, "SignUp failed - try again.",
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
                        Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ProfileActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user.
                        Log.w("TAGsoigninfail", "signInWithCredential:failure", task.getException());
                    }

                });
    }

    protected void register(String tokenId, String email, String password){
        // if user has signed up with google, then call the associated Firebase function
        if (tokenId != null) {
            firebaseAuthWithGoogle(tokenId);
            return;
        }
        // Register the user to firebase
        createFirebaseUserEmailPassword(email, password);
        System.out.println(email + '\n'  + password);
    }

    protected void storeRiderDetailsInFirestore(String userEmail, String userPhone, String firstName, String lastName){
        Map<String, Object> riderUserMap = new HashMap<>();
        riderUserMap.put("email", userEmail);
        riderUserMap.put("phone_number", userPhone);
        riderUserMap.put("first_name", firstName);
        riderUserMap.put("last_name", lastName);
        riderUserMap.put("type", "rider");

        addUser(riderUserMap, "rider");
    }

    protected void storeDriverDetailsInFirestore(String userEmail, String userPhone, String firstName, String lastName, String idNumber, String plateNumber){
        Map<String, Object> driverUserMap = new HashMap<>();
        driverUserMap.put("email", userEmail);
        driverUserMap.put("phone_number", userPhone);
        driverUserMap.put("first_name", firstName);
        driverUserMap.put("last_name", lastName);
        driverUserMap.put("ID", idNumber);
        driverUserMap.put("plate_number", plateNumber);
        driverUserMap.put("type", "driver");

        addUser(driverUserMap, "driver");
    }

    protected void addUser(Map<String, Object> userMap, String type){
        // Add a new document with a generated ID
        db.collection("users")
                .add(userMap)
                .addOnSuccessListener(documentReference -> {
                    String userID = documentReference.getId();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + userID);
                    uploadProfileImageByType(type, userID, documentReference);
        });
    }

    protected void uploadProfileImageByType(String type, String userID, DocumentReference documentReference){
        StorageReference childRef;
        UploadTask uploadTask;
        String profileImageDirLink = "/Images/Profiles/" + userID + "/";
        String driverLicenseDirLink = "/Images/DriverLicenses/" + userID + "/";

        // If the current user is a rider then upload his image
        if (type.equals("rider")) {
            childRef = storageRef.child(profileImageDirLink + RiderImageUri.getLastPathSegment());
            uploadTask = childRef.putFile(RiderImageUri);
            documentReference.update("profile_image_link", profileImageDirLink + RiderImageUri.getLastPathSegment());
            listenUploadProgress(uploadTask);
            return;
        }

        // current user is a driver then upload his image
        // Upload driver profile image.
        childRef = storageRef.child(profileImageDirLink + DriverProfileImageUri.getLastPathSegment());
        uploadTask = childRef.putFile(DriverProfileImageUri);
        documentReference.update("profile_image_link",  profileImageDirLink + DriverProfileImageUri.getLastPathSegment());
        listenUploadProgress(uploadTask);

        // Upload the driver license to the corresponded directory
        childRef = storageRef.child(driverLicenseDirLink + DriverLicenseImageUri.getLastPathSegment());
        uploadTask = childRef.putFile(DriverLicenseImageUri);
        documentReference.update("driver_license_link",  driverLicenseDirLink + DriverLicenseImageUri.getLastPathSegment());
        listenUploadProgress(uploadTask);

    }

    protected void listenUploadProgress(UploadTask uploadTask){
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "-------------- Failure --------------");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                Log.d(TAG, "-------------- Success --------------");

            }
        }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}

