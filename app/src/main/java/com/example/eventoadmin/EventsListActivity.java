package com.example.eventoadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventoadmin.Interface.ItemClickListener;
import com.example.eventoadmin.Model.Events;
import com.example.eventoadmin.ViewHolder.EventsViewHolder;
import com.example.eventoadmin.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import info.hoang8f.widget.FButton;

public class EventsListActivity extends AppCompatActivity {

    private RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    MaterialEditText edtName,edtDate,edtTime,edtDesc,edtUrl;
    FButton btnUpload,btnSelect;
    Events newEvent;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    DatePickerDialog datePicker;
    TimePickerDialog picker;

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Events, EventsViewHolder> adapter;

    LayoutAnimationController layoutAnimationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Events");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddEventDialog();



                }

        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getBaseContext(),R.anim.layout_item_from_left);
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadEvents();

    }

    private void showAddEventDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventsListActivity.this);
        alertDialog.setTitle("Add new Event");
        alertDialog.setMessage("Please fill all fields");

        LayoutInflater inflater = EventsListActivity.this.getLayoutInflater();
        View add_event_layout = inflater.inflate(R.layout.add_new_event_layout,null);

        edtName = add_event_layout.findViewById(R.id.editName);
        edtDate = add_event_layout.findViewById(R.id.editDate);
        edtTime = add_event_layout.findViewById(R.id.editTime);
        edtDesc = add_event_layout.findViewById(R.id.editDescription);
        edtUrl = add_event_layout.findViewById(R.id.editUrl);
        btnSelect = add_event_layout.findViewById(R.id.btnSelect);
        btnUpload = add_event_layout.findViewById(R.id.btnUpload);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(EventsListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(EventsListActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edtTime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        alertDialog.setView(add_event_layout);
        alertDialog.setIcon(R.drawable.ic_action_name);

        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (newEvent != null)
                {
                    mRef.push().setValue(newEvent);
                    Toast.makeText(EventsListActivity.this,"New Event "+newEvent.getName()+" is added",Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void uploadImage() {

        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final String status="0";
            final StorageReference imageFolder = storageReference.child("posters/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(EventsListActivity.this, "Uploaded..!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    newEvent = new Events(edtName.getText().toString(),uri.toString(),edtDate.getText().toString(),edtTime.getText().toString(),edtDesc.getText().toString(),edtUrl.getText().toString(),status);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(EventsListActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploading "+progress+"%");
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE))
        {
            showUpdateEventDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {

        mRef.child(key).removeValue();

    }

    private void showUpdateEventDialog(final String key, final Events item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventsListActivity.this);
        alertDialog.setTitle("Edit Event Details");
        alertDialog.setMessage("Please fill all fields");

        LayoutInflater inflater = EventsListActivity.this.getLayoutInflater();
        View add_event_layout = inflater.inflate(R.layout.add_new_event_layout,null);

        edtName = add_event_layout.findViewById(R.id.editName);
        edtDate = add_event_layout.findViewById(R.id.editDate);
        edtTime = add_event_layout.findViewById(R.id.editTime);
        edtDesc = add_event_layout.findViewById(R.id.editDescription);
        edtUrl = add_event_layout.findViewById(R.id.editUrl);
        btnSelect = add_event_layout.findViewById(R.id.btnSelect);
        btnUpload = add_event_layout.findViewById(R.id.btnUpload);


        edtName.setText(item.getName());
        edtDate.setText(item.getDate());
        edtTime.setText(item.getTime());
        edtDesc.setText(item.getDescription());
        edtUrl.setText(item.getUrl());

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(EventsListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(EventsListActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edtTime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_event_layout);
        alertDialog.setIcon(R.drawable.ic_action_name);

        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                    item.setName(edtName.getText().toString());
                    item.setDate(edtDate.getText().toString());
                    item.setTime(edtTime.getText().toString());
                    item.setDescription(edtDesc.getText().toString());
                    item.setUrl(edtUrl.getText().toString());

                    mRef.child(key).setValue(item);
                    Toast.makeText(EventsListActivity.this,"Details of "+item.getName()+" is edited",Toast.LENGTH_SHORT).show();


            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),PICK_IMAGE_REQUEST);

    }

    private void changeImage(final Events item) {

        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("posters/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(EventsListActivity.this, "Uploaded..!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setPoster(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(EventsListActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploading "+progress+"%");
                        }
                    });
        }

    }

    private void loadEvents() {

        adapter = new FirebaseRecyclerAdapter<Events, EventsViewHolder>(
                Events.class,R.layout.event_list,EventsViewHolder.class,mRef
        ) {
            @Override
            protected void populateViewHolder(EventsViewHolder eventsViewHolder, Events events, int i) {

                eventsViewHolder.event_Name.setText(events.getName());
                eventsViewHolder.event_Date.setText(events.getDate());
                Picasso.get().load(events.getPoster())
                        .into(eventsViewHolder.event_Poster);
                final Events clickItem = events;
                eventsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(EventsListActivity.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent eventDetail = new Intent(EventsListActivity.this,EventDetailActivity.class);
                        eventDetail.putExtra("EventId",adapter.getRef(position).getKey());
                        startActivity(eventDetail);

                    }
                });


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