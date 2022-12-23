package com.example.rocketride.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rocketride.Models.DriverRideModel;
import com.example.rocketride.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class DriverRideRecyclerViewAdapter extends RecyclerView.Adapter<DriverRideRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DriverRideModel> closeRides;
    private SelectDriverListener listener;
    private Drawable[] drawables;

    @SuppressLint("UseCompatLoadingForDrawables")
    public DriverRideRecyclerViewAdapter(Context context, ArrayList<DriverRideModel> closeRides, SelectDriverListener listener) {
        this.context = context;
        this.closeRides = closeRides;
        this.listener = listener;


        System.out.println("close rides size: " + closeRides.size());
        this.drawables = new Drawable[6];
        drawables[0] = context.getResources().getDrawable(R.drawable.gradient_blue);
        drawables[1] = context.getResources().getDrawable(R.drawable.gradient_blue_purple);
        drawables[2] = context.getResources().getDrawable(R.drawable.gradient_orange);
        drawables[3] = context.getResources().getDrawable(R.drawable.gradient_pink);
        drawables[4] = context.getResources().getDrawable(R.drawable.gradient_turquoise);
        drawables[5] = context.getResources().getDrawable(R.drawable.gradient_turquoise_purple);
    }

    @NonNull
    @Override
    public DriverRideRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_drivers_view_row, parent, false);
        return new DriverRideRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRideRecyclerViewAdapter.MyViewHolder holder, int position) {
        DriverRideModel currDriverRide = closeRides.get(position);
        holder.firstName.setText(currDriverRide.getFirstName());
        holder.lastName.setText(currDriverRide.getLastName());
        holder.source.setText(currDriverRide.getSource());
        holder.destination.setText(currDriverRide.getDestination());
        holder.startTime.setText(currDriverRide.getStartTime());
        holder.rating.setText(currDriverRide.getRating());

        holder.cardView.setOnClickListener(l -> {
            listener.onItemClicked(closeRides.get(position));
        });



        holder.constraintLayoutDriverSearch.setBackground(drawables[position % 6]);

        // Upload image to the rounded image view
        String profileImageURL = currDriverRide.getProfileImageURL();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        System.out.println("image is:  " + profileImageURL);

        if (profileImageURL == null){
            return;
        }

        // Profile image url shouldn't be empty
        if (!profileImageURL.equals("")) {
            storageRef.child(profileImageURL).getBytes(Long.MAX_VALUE).addOnSuccessListener(imageBytes -> {
                // Use the bytes to display the image
                // Convert the byte array into a Bitmap object
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Set the Bitmap as the image for the ImageView
                holder.profileImage.setImageBitmap(bitmap);
            }).addOnFailureListener(exception -> Log.d(TAG, "error in downloading the image!"));
        }
    }

    @Override
    public int getItemCount() {
        return closeRides.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayoutDriverSearch;
        RoundedImageView profileImage;
        TextView firstName, lastName, source, destination, startTime, rating;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutDriverSearch = itemView.findViewById(R.id.constraintLayoutDriverSearch);
            firstName = itemView.findViewById(R.id.firstNameDriverSearch);
            lastName = itemView.findViewById(R.id.lastNameDriverSearch);
            source = itemView.findViewById(R.id.sourceDriverSearch);
            destination = itemView.findViewById(R.id.destinationDriverSearch);
            startTime = itemView.findViewById(R.id.startTimeDriverSearch);
            rating = itemView.findViewById(R.id.ratingDriverSearch);
            cardView = itemView.findViewById(R.id.driverRow);
            profileImage = itemView.findViewById(R.id.profileRoundedRecyclerView);
        }
    }


}
