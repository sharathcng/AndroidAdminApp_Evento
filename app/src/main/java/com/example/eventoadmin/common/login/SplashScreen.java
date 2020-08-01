package com.example.eventoadmin.common.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventoadmin.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 3000;

    ImageView backgroundImage;
    TextView title,tagline;
    Animation bottomAnim,sideAnim,expand;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        backgroundImage = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        tagline = findViewById(R.id.tagline);

        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        expand = AnimationUtils.loadAnimation(this,R.anim.expand);

        backgroundImage.setAnimation(expand);
        title.setAnimation(sideAnim);
        tagline.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);

                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                if (isFirstTime){

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.apply();

                    Intent intent = new Intent(SplashScreen.this,OnBoardingScreen.class);
                    startActivity(intent);
                    finish();

                }
                else {

                    Intent intent = new Intent(SplashScreen.this, StartingScreen.class);
                    startActivity(intent);
                    finish();

                }



            }
        },SPLASH_TIMER);

    }
}