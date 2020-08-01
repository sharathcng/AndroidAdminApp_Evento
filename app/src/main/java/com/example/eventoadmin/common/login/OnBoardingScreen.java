package com.example.eventoadmin.common.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.eventoadmin.HelperClasses.SliderAdapter;
import com.example.eventoadmin.R;

public class OnBoardingScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dots_layout;

    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetsStarted;
    Animation animation;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.slider);
        dots_layout = findViewById(R.id.dots_layout);
        letsGetsStarted = findViewById(R.id.get_started_btn);

        //call adapter
        sliderAdapter = new SliderAdapter(this);

        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

    }

    private void addDots(int position) {

        dots = new TextView[4];
        dots_layout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dots_layout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);
            currentPosition = position;


            if (position == 0) {

                letsGetsStarted.setVisibility(View.INVISIBLE);

            } else if (position == 1) {

                letsGetsStarted.setVisibility(View.INVISIBLE);

            } else if (position == 2) {

                letsGetsStarted.setVisibility(View.INVISIBLE);

            } else {

                animation = AnimationUtils.loadAnimation(OnBoardingScreen.this, R.anim.bottom_anim);
                letsGetsStarted.setAnimation(animation);
                letsGetsStarted.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void skip(View view) {

        startActivity(new Intent(this, StartingScreen.class));
        finish();

    }

    public void next(View view) {

        viewPager.setCurrentItem(currentPosition + 1);

    }

    public void callStartingScreen(View view) {
        startActivity(new Intent(this, StartingScreen.class));
        finish();

    }
}