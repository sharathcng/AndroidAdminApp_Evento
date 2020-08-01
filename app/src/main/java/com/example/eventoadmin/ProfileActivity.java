package com.example.eventoadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventoadmin.common.SessionManager;
import com.example.eventoadmin.common.login.StartingScreen;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, email, dob, gender;
    CollapsingToolbarLayout collapsingToolbarLayout;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        imageView = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        collapsingToolbarLayout.setTitle(userDetails.get(SessionManager.KEY_USERNAME));
        name.setText(userDetails.get(SessionManager.KEY_FULLNAME));
        email.setText(userDetails.get(SessionManager.KEY_EMAIL));
        dob.setText(userDetails.get(SessionManager.KEY_DATE));
        gender.setText(userDetails.get(SessionManager.KEY_GENDER));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.signout:
                Toast.makeText(this,"Signout",Toast.LENGTH_SHORT).show();
                sessionManager.logout();
                Intent intent = new Intent(ProfileActivity.this, StartingScreen.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

