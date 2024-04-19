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

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.ViewHolder> {

    ArrayList<EventCategoryItem> data = new ArrayList<EventCategoryItem>();
    public void setData(ArrayList<EventCategoryItem> data) {
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

        // Format the first card
        if (data.get(position).getCategoryId().equals("Id")) {

            holder.itemView.setBackgroundResource(R.drawable.header_card_background);
            holder.categoryIdTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.categoryIdTv.setTextColor(Color.WHITE);
            holder.categoryNameTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.categoryNameTv.setTextColor(Color.WHITE);
            holder.eventCountTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.eventCountTv.setTextColor(Color.WHITE);
            holder.isActiveTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.isActiveTv.setTextColor(Color.WHITE);

        } else {

            holder.itemView.setBackgroundResource(R.drawable.other_card_background);

        }

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