package com.example.rocketride.MenuActivities;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rocketride.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class BecomeDriver extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ImageView DriverImageCapture;
    private Uri DriverLicenseImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_driver);

        // Remove action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        // Driver license ImageView
        DriverImageCapture = findViewById(R.id.BecomeDriverImageCapture);

        // launcher Driver Upload Image
        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if(result.getResultCode() == RESULT_OK){
                        Uri uri = result.getData().getData();
                        System.out.println("Driver uri: " + result.getData().getData());
                        // Use the uri to load the image
                        DriverImageCapture.setImageURI(uri);
                        DriverLicenseImageUri = uri;
                    }
                    else if(result.getResultCode() == ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        ImagePicker.Companion.getError(result.getData());
                    }
                });

        // Upload driver license button
        Button uploadImageBecomeDriverButton = findViewById(R.id.uploadImageBecomeDriver);

        // upload driver license listener
        uploadImageBecomeDriverButton.setOnClickListener(view -> {
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

        // text inputs for driver details
        TextInputEditText idNumber = findViewById(R.id.IDNumberBecomeDriver),
                          plateNumber = findViewById(R.id.NumberPlateBecomeDriver);

        // confirm button
        Button confirmButton = findViewById(R.id.ConfirmBecomeDriverButton);
        confirmButton.setOnClickListener(view -> {
            System.out.println("ssssssssssssssssssssss");
            // Check if user didn't upload an image
            if (DriverLicenseImageUri == null){
                Toast.makeText(this, "Upload the required images!", Toast.LENGTH_SHORT).show();
                return;
            }

            String idNumberStr = idNumber.getText().toString(),
                    plateNumberStr = plateNumber.getText().toString(),
                    currUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Check if user didn't upload text input details
            if (idNumberStr.equals("") || plateNumberStr.equals("")){
                Toast.makeText(this, "Upload required details!", Toast.LENGTH_SHORT).show();
                return;
            }

            System.out.println(idNumberStr + " " + plateNumberStr + " " + currUID);

            // Get current user document
            Query userQuery = db.collection("users").whereEqualTo("UID", currUID);
            userQuery.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshot.getDocuments().get(0);
                    DocumentReference documentReference = documentSnapshot.getReference();

                    // Upload license image and get store the link
                    String driverLicenseImageLink = uploadDriverLicense(currUID);

                    // Set rider's new information
                    HashMap<String, Object> driverDetails = new HashMap<>();
                    driverDetails.put("driver_license_link", driverLicenseImageLink);
                    driverDetails.put("type", "driver");
                    driverDetails.put("ID", idNumberStr);
                    driverDetails.put("plate_number", plateNumberStr);
                    documentReference.update(driverDetails);

                    // Move the user back to home screen
                    this.finish();
                    Intent switchActivityIntent = new Intent(this, HomeActivity.class);
                    // Send the profile image and type to the home activity
                    switchActivityIntent.putExtra("profile_image_link",  documentSnapshot.getString("profile_image_link"));
                    switchActivityIntent.putExtra("type", "driver");
                    switchActivityIntent.putExtra("full_name", documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name"));
                    startActivity(switchActivityIntent);
                }
                else{
                    Log.d(TAG, "Error occurred in getting user document");
                }
            });


        });

        // Back arrow button
        ImageView backArrow = findViewById(R.id.leftArrowBecomeDriver);
        backArrow.setOnClickListener(l -> {
            // Switch back to HomeActivity activity
            this.finish();
            Intent switchActivityBecomeDriverIntent = new Intent(this, HomeActivity.class);
            startActivity(switchActivityBecomeDriverIntent);
        });
    }

    public String uploadDriverLicense(String UID){
        StorageReference childRef;
        UploadTask uploadTask;
        String driverLicenseDirLink = "/Images/DriverLicenses/" + UID + "/";

        // Upload the driver license to the corresponded directory
        childRef = storageRef.child(driverLicenseDirLink + DriverLicenseImageUri.getLastPathSegment());
        uploadTask = childRef.putFile(DriverLicenseImageUri);
        listenUploadProgress(uploadTask);
        return driverLicenseDirLink + DriverLicenseImageUri.getLastPathSegment();
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