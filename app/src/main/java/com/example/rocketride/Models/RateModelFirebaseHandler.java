package com.example.rocketride.Models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Task<Optional<RateModel>> getRateModelTask(String driverId) {
        // Return the task for the query
        return db.collection(COLLECTION_NAME).whereEqualTo("driver-id", driverId).get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    return Optional.of(new RateModel(
                            document.getString("driver-id"),
                            (double) document.get("avg"),
                            Math.toIntExact(document.getLong("voters_num"))
                    ));
                }
                // If the document is not found, return an empty Optional object
            } else {
                // If the query fails, log the error and return an empty Optional object
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
            return Optional.empty();
        });
    }
}
