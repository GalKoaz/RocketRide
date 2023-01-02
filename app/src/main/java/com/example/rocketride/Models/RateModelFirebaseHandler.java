package com.example.rocketride.Models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RateModelFirebaseHandler {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "rates";

    public void addRateModel(RateModel rateModel) {
        // Add a new document with a generated ID
        db.collection(COLLECTION_NAME)
                .add(rateModel.getHashRateDetails())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                });
    }

    public void updateRateModel(RateModel rateModel) {
        // Update an existing document or non-existing document - then create it automatically.
        db.collection(COLLECTION_NAME).document(rateModel.getDriverID())
                .set(rateModel.getHashRateDetails())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);
                });
    }

    public void getRateModel(String driverID, OnCompleteListener<QuerySnapshot> listener) {
        // Get the RateModel document by the driverID field
        db.collection(COLLECTION_NAME).whereEqualTo("driver-id", driverID)
                .get()
                .addOnCompleteListener(listener);
    }
}
