package com.example.nutrimotion.security;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.nutrimotion.R;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Button loginButton = findViewById(R.id.btn_login_welcome_page);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                    WelcomeScreen.this,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );

            startActivity(intent, options.toBundle());
        });

        Button registerButton = findViewById(R.id.btn_register_welcome_page);
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeScreen.this, RegisterActivity.class);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                    WelcomeScreen.this,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );

            startActivity(intent, options.toBundle());
        });
    }
}
