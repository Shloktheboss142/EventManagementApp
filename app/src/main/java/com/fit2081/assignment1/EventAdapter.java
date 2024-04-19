package com.fit2081.assignment1;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    ArrayList<EventItem> data = new ArrayList<EventItem>();
    public void setData(ArrayList<EventItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false); //CardView inflated as RecyclerView list item
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Set the data for all the details
        holder.eventIdTv.setText(data.get(position).getEventId());
        holder.eventNameTv.setText(data.get(position).getEventName());
        holder.categoryIdTv.setText(data.get(position).getCategoryId());
        holder.ticketsAvailableTv.setText(data.get(position).getTicketsAvailable());

        // Set the text for the active field
        if (data.get(position).getIsActive().equals("true")) {
            holder.isActiveTv.setText("Active");
        } else {
            holder.isActiveTv.setText("Inactive");
        }

        holder.itemView.setBackgroundResource(R.drawable.other_card_background);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventIdTv;
        public TextView eventNameTv;
        public TextView categoryIdTv;
        public TextView ticketsAvailableTv;
        public TextView isActiveTv;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            eventIdTv = itemView.findViewById(R.id.cardEventId);
            eventNameTv = itemView.findViewById(R.id.cardEventName);
            categoryIdTv = itemView.findViewById(R.id.cardCategoryId);
            ticketsAvailableTv = itemView.findViewById(R.id.cardTicketCount);
            isActiveTv = itemView.findViewById(R.id.isActive);

        }

    }

}