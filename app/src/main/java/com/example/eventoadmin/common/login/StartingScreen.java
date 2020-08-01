package com.example.eventoadmin.common.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventoadmin.HomeActivity;
import com.example.eventoadmin.R;
import com.example.eventoadmin.common.SessionManager;

public class StartingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        SessionManager sessionManager = new SessionManager(StartingScreen.this);
        if (sessionManager.checkLogin()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            Toast.makeText(StartingScreen.this, "Welcome to Evento " , Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void login(View view) {

        Intent intent = new Intent(getApplicationContext(), LoginScreen.class);

        Pair[] pairs =new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.loginButton),"transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartingScreen.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }

    }

    public void signUp(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.signUpButton), "transition_signup");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartingScreen.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }



}