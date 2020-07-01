package com.example.eventoadmin.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventoadmin.Interface.ItemClickListener;
import com.example.eventoadmin.R;
import com.example.eventoadmin.common.Common;

public class EventStatusUpdate extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
    public TextView event_Name,event_Date,event_Time,event_Status;
    public ImageView event_Poster;

    private ItemClickListener itemClickListener;

    public EventStatusUpdate(@NonNull View itemView) {
        super(itemView);

        event_Name = itemView.findViewById(R.id.eventName);
        event_Poster = itemView.findViewById(R.id.eventPoster);
        event_Date = itemView.findViewById(R.id.eventDate);
        event_Time = itemView.findViewById(R.id.eventTime);
        event_Status = itemView.findViewById(R.id.eventStatus);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Send notification to all");

        menu.add(0,0,getAdapterPosition(), Common.NOTIFY);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}
