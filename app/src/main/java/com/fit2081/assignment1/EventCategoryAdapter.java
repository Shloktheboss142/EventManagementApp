package com.fit2081.assignment1;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.provider.EventCategory;

import java.util.ArrayList;

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.ViewHolder> {

    ArrayList<EventCategory> data = new ArrayList<EventCategory>();
    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_category_card_layout, parent, false); //CardView inflated as RecyclerView list item
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Set the text for the details
        holder.categoryIdTv.setText(data.get(position).getCategoryId());
        holder.categoryNameTv.setText(data.get(position).getCategoryName());
        holder.eventCountTv.setText(data.get(position).getEventCount());

        // Set text for the active field
        if (data.get(position).getIsActive().equals("true")) {
            holder.isActiveTv.setText("Yes");
        } else if (data.get(position).getIsActive().equals("false")){
            holder.isActiveTv.setText("No");
        } else {
            holder.isActiveTv.setText(data.get(position).getIsActive());
        }

        // Set the background color
        holder.itemView.setBackgroundResource(R.drawable.other_card_background);

        // Set the onClickListener for the card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start the GoogleMapActivity and pass the location of the category
                Intent intent = new Intent(view.getContext(), GoogleMapActivity.class);
                intent.putExtra("categoryLocation", data.get(position).getEventLocation());
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryIdTv;
        public TextView categoryNameTv;
        public TextView eventCountTv;
        public TextView isActiveTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIdTv = itemView.findViewById(R.id.cardEventId);
            categoryNameTv = itemView.findViewById(R.id.cardEventName);
            eventCountTv = itemView.findViewById(R.id.cardTicketCount);
            isActiveTv = itemView.findViewById(R.id.isActive);
        }

    }

}