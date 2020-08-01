package com.example.eventoadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eventoadmin.common.SessionManager;
import com.example.eventoadmin.common.login.StartingScreen;
import com.example.eventoadmin.EventStatus;
import com.example.eventoadmin.EventsListActivity;
import com.example.eventoadmin.EventsResultsActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    GridLayout mainGrid;
    FrameLayout logo;

    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageView menuIcon, profile_icon;
    LinearLayout contentView;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        logo = findViewById(R.id.logo);

        //menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        profile_icon = findViewById(R.id.profile_icon);
        contentView = findViewById(R.id.content);


        navigationDrawer();

        // setSingleEvent(mainGrid);
        setSingleActivity(mainGrid);


    }

    //navigation drawer
    private void navigationDrawer() {

        //navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);
        sessionManager = new SessionManager(HomeActivity.this);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);

                else drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.checkLogin()) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(HomeActivity.this, StartingScreen.class);
                    startActivity(intent);
                }

            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {


        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
//        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimaryDark));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.manage_events) {
            Intent intent = new Intent(HomeActivity.this, EventStatus.class);
            startActivity(intent);

        } else if (id == R.id.add_new_event) {
            Intent intent = new Intent(HomeActivity.this, EventsListActivity.class);
            startActivity(intent);

        } else if (id == R.id.results) {
            Intent intent = new Intent(HomeActivity.this, EventsResultsActivity.class);
            startActivity(intent);

        } else if (id == R.id.announcements) {
            Intent intent = new Intent(HomeActivity.this, Announcements.class);
            startActivity(intent);

        } else if (id == R.id.profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            Toast.makeText(this, "Signout", Toast.LENGTH_SHORT).show();
            sessionManager.logout();
            Intent intent = new Intent(HomeActivity.this, StartingScreen.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSingleActivity(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int I = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (I == 0) {
                        Intent intent = new Intent(HomeActivity.this, EventStatus.class);
                        startActivity(intent);
                    } else if (I == 1) {
                        Intent intent = new Intent(HomeActivity.this, EventsListActivity.class);
                        startActivity(intent);

                    } else if (I == 2) {
                        Intent intent = new Intent(HomeActivity.this, EventsResultsActivity.class);
                        startActivity(intent);
                    } else if (I == 3) {
                        Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }

    }
}