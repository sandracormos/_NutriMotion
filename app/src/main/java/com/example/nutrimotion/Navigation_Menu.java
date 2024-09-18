package com.example.nutrimotion;


import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class Navigation_Menu extends AppCompatActivity {
    ImageButton fasting_tracker_button;
    ImageButton water_intake_button;
    ImageButton journal_button;
    ImageButton settings_button;
    ImageButton bmi_button;
    ImageButton gps_button;

    ImageButton progress_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);


        water_intake_button = findViewById(R.id.water_intake_button);
        journal_button = findViewById(R.id.journal_button);
        settings_button = findViewById(R.id.settings_button);
        bmi_button = findViewById(R.id.bmi_button);
        gps_button = findViewById(R.id.gps_button);
        progress_button = findViewById(R.id.progress_button);
        fasting_tracker_button = findViewById(R.id.fasting_tracker_button);

            User.fetchUserData(this);

//            checkIfDataInitialised();



       // User.saveUsersFeatures();
        journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), KcalMenu.class));
            }
        });

        bmi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), BMICalculator.class));
            }
        });

        gps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), NearbyGymsActivity.class ));
            }
        });

        fasting_tracker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), IntermittentFastingActivity.class ));
            }
        });
        water_intake_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), WaterIntakeActivity.class ));
            }
        });

        progress_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Fortschrittsfotos.class ));
            }
        });

    }

    public void launchBmiActivity()
    {
        startActivity(new Intent(getBaseContext(), BMICalculator.class ));
    }

    private void checkIfDataInitialised() {
        if (User.getGoal() == null){
            startActivity(new Intent(getBaseContext(), BMICalculator.class));
        }
    }
}