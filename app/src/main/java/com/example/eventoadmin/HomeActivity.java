package com.example.eventoadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;


public class HomeActivity extends AppCompatActivity {

    GridLayout mainGrid;
    FrameLayout logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
        logo = findViewById(R.id.logo);
        // setSingleEvent(mainGrid);
        setSingleActivity(mainGrid);


    }

    private void setSingleActivity(GridLayout mainGrid) {

        for (int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int I = i;
            cardView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    if (I==0)
                    {
                        Intent intent = new Intent(HomeActivity.this, EventStatus.class);
                        startActivity(intent);
                    }
                    else if (I==1)
                    {
                        Intent intent = new Intent(HomeActivity.this, EventsListActivity.class);
                        startActivity(intent);

                    }
                    else if (I==2)
                    {
                        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                    else if (I==3)
                    {
                        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }

    }
}