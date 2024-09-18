package com.example.nutrimotion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
public class BMICalculator extends AppCompatActivity {

    Button calculate_button;
    TextView height;
    TextView weight;
    TextView status;
    ImageView chart;
    ImageView needle;

    Spinner spinner;

    Gender gender;
    TextView age;

    Button next_button;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bmicalculator);

        calculate_button = findViewById(R.id.calculate_button);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        final TextView resultText = findViewById(R.id.result);
        status = findViewById(R.id.status);
        chart = findViewById(R.id.chart);
        needle = findViewById(R.id.needle);
        age = findViewById(R.id.age);

        next_button =findViewById(R.id.next_button);


        //dropDown with gender selection
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Spinner_items, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);


        // Define the minimum and maximum X-axis positions for the needle
        final int minPosition = 0;
        final int maxPosition = chart.getWidth() - needle.getWidth();
        float myNeedlePos = needle.getX();



        if( User.getGoal() != null){
            next_button.setVisibility(View.INVISIBLE);
        }
        calculate_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                String heightStr = height.getText().toString();
                String weightStr = weight.getText().toString();


                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                    resultText.setTextSize(20);
                    resultText.setText("Please enter both height and weight");
                    return;
                }

                double heightValue = Double.parseDouble(heightStr);
                double weightValue = Double.parseDouble(weightStr);

                double heightInMeters = heightValue / 100.0;
                double bmi = weightValue / (heightInMeters * heightInMeters);

                String weightStatus;
                if (bmi < 18.5) {
                    weightStatus = "Underweight";
                } else if (bmi < 24.9) {
                    weightStatus = "Normal weight";
                } else if (bmi < 29.9) {
                    weightStatus = "Overweight";
                } else {
                    weightStatus = "Obesity";
                }


                resultText.setText(String.format("Your BMI: %.2f", bmi));
                status.setText(String.format("( %s )", weightStatus));

                // Calculate the X-axis position for the needle based on BMI
                float position = calculatePosition(bmi, maxPosition);

                // Set the X-axis position of the needle

            }
        });


        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //baga cachuri
                    User.setHeight(Double.parseDouble(height.getText().toString()));
                }
            }
        });
        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //baga cachuri
                    User.setWeight(Double.parseDouble(weight.getText().toString()));
                }
            }
        });
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //baga cachuri
                    User.setAge(Integer.parseInt(age.getText().toString()));
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = spinner.getSelectedItem().toString();
                User.setGender(Gender.valueOf(text));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });






        configureNextButton();
    }

    private void configureNextButton() {

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(BMICalculator.this, SetGoalActivity.class)));
            }
        });
    }

    // Calculate the X-axis position based on the BMI value and chart design
    private float calculatePosition(double bmi, int maxPosition) {
        // Define the chart's minimum and maximum BMI values

        if (bmi < 10)
            bmi = 10;
        else if (bmi > 30)
            bmi = 30;

        float normalizedBmi = (float) (bmi - 10) / (30 - 10);
        float position = normalizedBmi * 1000 - 500;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) needle.getLayoutParams();
        params.horizontalBias = normalizedBmi;
        needle.setLayoutParams(params);

        return position;


    }
}
