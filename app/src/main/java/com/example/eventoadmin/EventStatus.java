package com.example.eventoadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventoadmin.Interface.ItemClickListener;
import com.example.eventoadmin.Model.EventStatusModel;
import com.example.eventoadmin.ViewHolder.EventStatusUpdate;
import com.example.eventoadmin.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

public class EventStatus extends AppCompatActivity {

    private RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    MaterialSpinner spinner;

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<EventStatusModel, EventStatusUpdate> adapter;

    LayoutAnimationController layoutAnimationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_status);


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Events");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getBaseContext(),R.anim.layout_item_from_left);
        recycler_menu = findViewById(R.id.listEvents);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadEventStatus();

    }

    private void loadEventStatus() {

        adapter = new FirebaseRecyclerAdapter<EventStatusModel, EventStatusUpdate>(
                EventStatusModel.class,R.layout.event_list_status_layout,EventStatusUpdate.class,mRef
        ) {
            @Override
            protected void populateViewHolder(EventStatusUpdate eventStatusUpdate, EventStatusModel eventStatus, int i) {

                eventStatusUpdate.event_Name.setText(eventStatus.getName());
                eventStatusUpdate.event_Date.setText(eventStatus.getDate());
                eventStatusUpdate.event_Time.setText(eventStatus.getTime());
                eventStatusUpdate.event_Status.setText(Common.convertCodeToStatus(eventStatus.getStatus()));
                Picasso.get().load(eventStatus.getPoster())
                        .into(eventStatusUpdate.event_Poster);
                final EventStatusModel clickItem = eventStatus;
                eventStatusUpdate.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(EventStatus.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent eventDetail = new Intent(EventStatus.this,RegisteredDetailActivity.class);
                        eventDetail.putExtra("EventId",adapter.getRef(position).getKey());
                        startActivity(eventDetail);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
        recycler_menu.setLayoutAnimation(layoutAnimationController);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.NOTIFY))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE))
        {
            deleteEvent(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteEvent(String key) {

        mRef.child(key).removeValue();

    }

    private void showUpdateDialog(String key, final EventStatusModel item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventStatus.this);
        alertDialog.setTitle("Send notification");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_event_status,null);

        spinner = view.findViewById(R.id.statusSpinner);
        spinner.setItems("organized","2 days to go","1 day to go","Today","postponed","preponed");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                mRef.child(localKey).setValue(item);

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}