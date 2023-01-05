package com.example.rocketride.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rocketride.Models.RideModel;
import com.example.rocketride.R;

import java.util.ArrayList;

public class ActiveDrivesActivityViewAdapter extends RecyclerView.Adapter<ActiveDrivesActivityViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<RideModel> ActDrive;
    private ActiveDriveListener listener;

    public ActiveDrivesActivityViewAdapter(Context context, ArrayList<RideModel> ActDrive, ActiveDriveListener listener) {
        this.context = context;
        this.ActDrive = ActDrive;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActiveDrivesActivityViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_active_drive_row, parent, false);
        return new ActiveDrivesActivityViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveDrivesActivityViewAdapter.MyViewHolder holder, int position) {
        RideModel currDriverRide = ActDrive.get(position);
        holder.source.setText(currDriverRide.getSource());
        holder.destination.setText(currDriverRide.getDestination());
        holder.date.setText(currDriverRide.getDate());
        holder.pickup.setText(currDriverRide.getPickup());
        holder.arrow.setOnClickListener(l -> {
                System.out.println("arrow clicked!");
                listener.onItemClicked(currDriverRide);
        });
    }

    @Override
    public int getItemCount() {
        return ActDrive.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ActiveDrivesLayout;
        TextView  source, destination, date, pickup;
        ImageView arrow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ActiveDrivesLayout = itemView.findViewById(R.id.ActiveDrivesLayout);
            source = itemView.findViewById(R.id.srcActiveDriveTextView);
            destination = itemView.findViewById(R.id.destActiveDriveTextView);
            date = itemView.findViewById(R.id.activeDriveDateTimeTextView);
            pickup = itemView.findViewById(R.id.picupActiveDriveTextView);
            arrow = itemView.findViewById(R.id.arrow_active_view);
        }
    }
}
