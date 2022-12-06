package com.example.rocketride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DriverRideRecyclerViewAdapter extends RecyclerView.Adapter<DriverRideRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DriverRideModel> closeRides;

    public DriverRideRecyclerViewAdapter(Context context, ArrayList<DriverRideModel> closeRides) {
        this.context = context;
        this.closeRides = closeRides;
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
    }

    @Override
    public int getItemCount() {
        return closeRides.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayoutDriverSearch;
        ImageView profileImage;
        TextView firstName, lastName, source, destination, startTime, rating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutDriverSearch = itemView.findViewById(R.id.constraintLayoutDriverSearch);
            firstName = itemView.findViewById(R.id.firstNameDriverSearch);
            lastName = itemView.findViewById(R.id.lastNameDriverSearch);
            source = itemView.findViewById(R.id.sourceDriverSearch);
            destination = itemView.findViewById(R.id.destinationDriverSearch);
            startTime = itemView.findViewById(R.id.startTimeDriverSearch);
            rating = itemView.findViewById(R.id.ratingDriverSearch);
        }
    }
}
