package com.example.eventoadmin.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventoadmin.R;

public class RegisteredListHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView image;

    public RegisteredListHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        image = itemView.findViewById(R.id.profile_photo);
    }
}
