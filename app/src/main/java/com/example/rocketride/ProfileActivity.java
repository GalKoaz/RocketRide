package com.example.rocketride;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class ProfileActivity extends AppCompatActivity {

    private ImageView RiderImageCapture, DriverImageCapture;
    Uri uri;
    String path;
    TextView captureTXT;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // launcher Driver Upload Image
        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        // Use the uri to load the image
                        DriverImageCapture.setImageURI(uri);
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
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        ImagePicker.Companion.getError(result.getData());
                    }
                });

        // ImageView Rider
        RiderImageCapture = findViewById(R.id.RiderImageCapture);

        // ImageView Driver
        DriverImageCapture = findViewById(R.id.DriverImageCapture);

        // Buttons
        Button uploadImage = findViewById(R.id.uploadImage),
                ConfirmRiderButton = findViewById(R.id.ConfirmRiderButton),
                uploadImageDriver = findViewById(R.id.uploadImageDriver),
                ConfirmDriverButton = findViewById(R.id.ConfirmDriverButton);


        // Driver text inputs
        TextInputEditText NickNameDriver = findViewById(R.id.NickNameDriver),
                IDNumber = findViewById(R.id.IDNumber),
                NumberPlate = findViewById(R.id.NumberPlate);

        // Rider text inputs
        TextInputEditText NickNameRider = findViewById(R.id.NickNameRider);

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

        // upload Image Driver Event
        uploadImageDriver.setOnClickListener(view -> {
            System.out.println("Enter To the UploadImageDriver");
            ImagePicker.Companion.with(this)
                    .crop()
                    .cropOval()
                    .maxResultSize(170, 80, true)
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
                    .maxResultSize(100, 100, true)
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
    }
}