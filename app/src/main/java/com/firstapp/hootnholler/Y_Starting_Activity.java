package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class Y_Starting_Activity extends AppCompatActivity {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // launch activity(page)
        setContentView(R.layout.activity_starting);
        // find the element by its id
        Button LoginBtn = findViewById(R.id.start_login_button);
        Button SignUpBtn = findViewById(R.id.start_signup_button);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);
                //from main activity to login page
                Intent intent = new Intent(Y_Starting_Activity.this, Y_Login_Activity.class);
                startActivity(intent);
            }
        });
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                //from main page to register page
                Intent intent = new Intent(Y_Starting_Activity.this, Y_Register_Activity.class);
                startActivity(intent);
            }
        });
    }
    }
