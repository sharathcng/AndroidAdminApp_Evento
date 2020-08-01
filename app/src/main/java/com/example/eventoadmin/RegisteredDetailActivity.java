package com.example.eventoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.eventoadmin.Model.Events;
import com.example.eventoadmin.Model.RegisteredList;
import com.example.eventoadmin.ViewHolder.EventsViewHolder;
import com.example.eventoadmin.ViewHolder.RegisteredListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RegisteredDetailActivity extends AppCompatActivity {

    private RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference mRef;
    String EventId="";
    FirebaseRecyclerAdapter<RegisteredList, RegisteredListHolder> adapter;

    LayoutAnimationController layoutAnimationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Registered students");

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Registrations");

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getBaseContext(),R.anim.layout_item_from_left);
        recycler_menu = findViewById(R.id.registeredList);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        //get eventId from intent
        if (getIntent() != null) {
            EventId = getIntent().getStringExtra("EventId");
        }
        if (!EventId.isEmpty()) {
            loadNames(EventId);
        }

    }

    private void loadNames(String eventId) {

        adapter = new FirebaseRecyclerAdapter<RegisteredList, RegisteredListHolder>(
                RegisteredList.class,R.layout.registered_list_layout,RegisteredListHolder.class,mRef.child(eventId)
        ) {

            @Override
            protected void populateViewHolder(RegisteredListHolder registeredListHolder, RegisteredList registeredList, int i) {

                registeredListHolder.name.setText(registeredList.getName());
                Picasso.get().load(registeredList.getImage())
                        .into(registeredListHolder.image);
            }
        };
        recycler_menu.setAdapter(adapter);
        recycler_menu.setLayoutAnimation(layoutAnimationController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}