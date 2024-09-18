package com.example.nutrimotion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SetGoalActivity extends AppCompatActivity {

    RadioButton weightlossbtn;
    RadioButton bodybuildingbtn;

    RadioButton wellnessbtn;

    TextView quote;


    Goal myGoal;
    Button nextBtnGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        weightlossbtn = findViewById(R.id.weightlossbtn);
        bodybuildingbtn = findViewById(R.id.bodybuildingbtn);
        wellnessbtn = findViewById(R.id.wellnessbtn);
        quote = findViewById(R.id.quote);

        nextBtnGoal = findViewById(R.id.nextBtnGoal);




        //quotes
        weightlossbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                quote.setText("Every step counts. Progress, not perfection.");
                myGoal = Goal.weightLoss;
                User.setGoal(myGoal);
            }
        });
        bodybuildingbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                quote.setText("Strength comes from within. Lift, grow, repeat.");
                myGoal = Goal.bodybuilding;
                User.setGoal(myGoal);
            }

        });

        wellnessbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                quote.setText("Health is wealth. Take care of your greatest asset.");
                myGoal= Goal.wellness;
                User.setGoal(myGoal);
            }
        });


        nextButtonCeva();

    }


   private void nextButtonCeva(){
       nextBtnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.postUserData();
                startActivity(new Intent(getBaseContext(), Navigation_Menu.class));
            }
        });
   }
}