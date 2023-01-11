package com.example.rocketride.MenuActivities.Adapters;

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

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<RideModel> pastRides;
    private HistoryRideListener listener;

    public HistoryRecyclerViewAdapter(Context context, ArrayList<RideModel> pastRides, HistoryRideListener listener) {
        this.context = context;
        this.pastRides = pastRides;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_history_view_row, parent, false);
        return new HistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    // TODO: need to extract if drive is cancelled by the driver - via cancelled flag attribute
    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        RideModel currRide = pastRides.get(position);
        holder.source.setText(currRide.getSource());
        holder.destination.setText(currRide.getDestination());
        holder.dayTime.setText(currRide.getDate());
        holder.pickup.setText(currRide.getPickup());

        holder.historyConstraintLayout.setOnClickListener(l -> {
            if (currRide.getCanceled()) return;
            System.out.println("history pressed");
            listener.onItemClicked(pastRides.get(position));
        });

        // Check if current ride isn't canceled
        if (!currRide.getCanceled()){
            holder.checkMark.setVisibility(View.VISIBLE);
            holder.completedTextView.setVisibility(View.VISIBLE);
            holder.cancelMark.setVisibility(View.GONE);
            holder.canceledTextView.setVisibility(View.GONE);
        }
        else{
            holder.checkMark.setVisibility(View.GONE);
            holder.completedTextView.setVisibility(View.GONE);
            holder.cancelMark.setVisibility(View.VISIBLE);
            holder.canceledTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pastRides.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView source, destination, dayTime, pickup;
        ImageView checkMark, cancelMark;
        TextView canceledTextView, completedTextView;
        ConstraintLayout historyConstraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.srcHistoryTextView);
            destination = itemView.findViewById(R.id.destHistoryTextView);
            dayTime = itemView.findViewById(R.id.historyDateTimeTextView);
            pickup = itemView.findViewById(R.id.pickupHistoryTextView);
            checkMark = itemView.findViewById(R.id.historyCheckMark);
            cancelMark = itemView.findViewById(R.id.historyCancelMark);
            canceledTextView = itemView.findViewById(R.id.canceledTextView);
            completedTextView = itemView.findViewById(R.id.completedTextView);
            historyConstraintLayout = itemView.findViewById(R.id.constraintLayoutHistory);
        }
    }
}
