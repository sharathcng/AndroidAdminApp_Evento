package com.example.eventoadmin.common.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventoadmin.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpScreen extends AppCompatActivity {

    ImageView backBtn;
    Button next, login;
    TextView titleText;

    TextInputLayout fullName,userName,email,password;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //hooks
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);


        fullName = findViewById(R.id.signup_fullname);
        email = findViewById(R.id.signup_email);
        userName = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_next_button);

    }




    public void callNextSignUpScreen(View view) {

        if (!validateFullName() | !validateUsername() | !validateEmail() | !validatePassword()) {
            return;
        }

        String fullNames = fullName.getEditText().getText().toString();
        String emails = email.getEditText().getText().toString();
        String userNames = userName.getEditText().getText().toString();
        String passwords = password.getEditText().getText().toString();

        Intent intent = new Intent(getApplicationContext(),SignUpScreen2.class);
        intent.putExtra("fullName",fullNames);
        intent.putExtra("userName",userNames);
        intent.putExtra("email",emails);
        intent.putExtra("password",passwords);

        //add transition
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(backBtn,"transition_back_arrow_btn");
        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
        pairs[2] = new Pair<View,String>(login,"transition_login_btn");
        pairs[3] = new Pair<View,String>(titleText,"transition_title_text");



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpScreen.this);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }

    }


    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullName.setError("Field can not be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = userName.getEditText().getText().toString().trim();
//        String checkspaces = "Aw{1,20}z";

        if (val.isEmpty()) {
            userName.setError("Field can not be empty");
            return false;
        }else if (val.length() < 3) {
            userName.setError("Username is too small!");
            return false;
        } else if (val.length() > 20) {
            userName.setError("Username is too large!");
            return false;
//        } else if (!val.matches(checkspaces)) {
//            userName.setError("No White spaces are allowed!");
//            return false;
        } else {
            userName.setError(null);
            userName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email I" +
                    "d!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim().replaceAll("\\s", "");;
        String checkPassword = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }else if (val.length() < 5) {
            userName.setError("Password is too small!");
            return false;
        }else if (!val.matches(checkPassword)) {
            password.setError("Password should contain at least 1 digit,1 lower and" +
                    " 1 upper case letter, 1 special character!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}