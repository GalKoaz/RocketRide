package com.example.rocketride.Login.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
    private LruCache<String, Bitmap> memoryCache;


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

        // Init memory cache map
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @NonNull
    @Override
    public DriverRideRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_drivers_view_row, parent, false);
        return new DriverRideRecyclerViewAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverRideRecyclerViewAdapter.MyViewHolder holder, int position) {
        DriverRideModel currDriverRide = closeRides.get(position);
        holder.firstName.setText(currDriverRide.getFirstName());
        holder.lastName.setText(currDriverRide.getLastName());
        holder.source.setText(currDriverRide.getSource());
        holder.destination.setText(currDriverRide.getDestination());
        holder.startTime.setText(currDriverRide.getStartTime());

        // Check driver's rating
        double driverRate = Double.parseDouble(currDriverRide.getRating());
        if (driverRate == 0.0){
            holder.rating.setText("None");
        }
        else { // driver has rating
            holder.rating.setText(String.valueOf(((int)(driverRate*100)/100.0)));
        }

        holder.cardView.setOnClickListener(l -> {
            listener.onItemClicked(closeRides.get(position));
        });



        holder.constraintLayoutDriverSearch.setBackground(drawables[position % 6]);

        // Check if image is in cache
        String profileImageURL = currDriverRide.getProfileImageURL();
        Bitmap bitmap = memoryCache.get(profileImageURL);
        if (bitmap != null) {
            // Image is in cache, set it to the ImageView
            holder.profileImage.setImageBitmap(bitmap);
        } else {
            // Image is not in cache, download it and add it to the cache
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(profileImageURL);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.profileImage.setImageBitmap(image);

                // Add image to cache
                memoryCache.put(profileImageURL, image);
            }).addOnFailureListener(exception -> {
                Log.i(TAG, "Could not download image.");
            });
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
