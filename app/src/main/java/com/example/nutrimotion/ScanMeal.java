package com.example.nutrimotion;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ScanMeal extends AppCompatActivity {

    Button btn_scan;
    public TextView tx;

    String barcode;

    TextView product_name;

    TextView aux_pannel;
    ImageView tick_button;
    ImageView cancel_button;
    public TextView kcal_number;

    Button nr_servings_btn;
    public Map<String, Double> myMap;
    ImageView eating_icon;

    TextView number_of_servings_label;
    TextView kcal_label;


    public ResponseProducts foodData;

    Double quantityOfUsedFood;

    Meal mealType;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_meal);

        aux_pannel = findViewById(R.id.aux_pannel);
        product_name = findViewById(R.id.product_name);
        tx = findViewById(R.id.responseTextView);
        btn_scan = findViewById(R.id.btn_scan);
        tick_button = findViewById(R.id.tick_button);
        cancel_button = findViewById(R.id.cancel_button);
        kcal_number = findViewById(R.id.kcal_number);
        nr_servings_btn= findViewById(R.id.number_of_servings_button);
        number_of_servings_label = findViewById(R.id.number_of_servings_label);
        kcal_label = findViewById(R.id.kcal_label);
        eating_icon = findViewById(R.id.eating_icon);




        btn_scan.setOnClickListener(v -> {
            scanCode();

            tick_button.postDelayed(new Runnable() {
                @Override
                public void run() {


                    aux_pannel.setVisibility(View.VISIBLE);
                    product_name.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.VISIBLE);
                    kcal_number.setVisibility(View.VISIBLE);
                    nr_servings_btn.setVisibility((View.VISIBLE));
                    kcal_label.setVisibility(View.VISIBLE);

                    cancel_button.setVisibility(View.VISIBLE);
                    tick_button.setVisibility(View.VISIBLE);
                    number_of_servings_label.setVisibility(View.VISIBLE);
                    eating_icon.setVisibility(View.INVISIBLE);

                }
            }, 2000);


            //adding food to journal
            //substracting used kcal

            tick_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mealType= KcalMenu.myMeal;


                    DayEntry dayEntry = User.journal.get(User.currentJournalDay);
                    if(dayEntry == null)
                    {
                        dayEntry = new DayEntry();
                        User.journal.put(User.currentJournalDay, dayEntry);
                    }

                    switch (mealType) {
                        case Breakfast:
                            dayEntry.getBreakfastList().put(foodData, quantityOfUsedFood);
                            break;
                        case Lunch:
                            dayEntry.getLunchList().put(foodData, quantityOfUsedFood);
                            break;
                        case Dinner:
                            dayEntry.getDinnerList().put(foodData, quantityOfUsedFood);
                            break;
                        case Snacks:
                            dayEntry.getSnacksList().put(foodData, quantityOfUsedFood);
                            break;
                    }
//                    User.setKcalCount(Double.valueOf(User.getKcalCount() - usedKcal));
                    startActivity(new Intent(ScanMeal.this, KcalMenu.class));
                    User.postUserData();
                }
            });

        });

        nr_servings_btn.setOnClickListener(v ->{
            showPopup();
        });


    }



    public void sendRequest(){

        String myApiKey = "myApiKey";
        String url = "https://api.barcodelookup.com/v3/products?barcode=" + barcode + "&key=" + myApiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                String responseRawText = response.toString();
                tx.setText(response.toString());
                Gson gson = new Gson();
                ResponseProducts test;
                foodData = gson.fromJson(responseRawText, ResponseProducts.class);
                test = foodData;
                String testString = test.products.get(0).title + test.products.get(0).nutrition_facts;
                myMap = getNutritionalValues(foodData.products.get(0).nutrition_facts);
                tx.setText(formatMap(myMap));
                product_name.setText(foodData.products.get(0).title);
                Double kcal = myMap.get("Energy") ;
                if(kcal == null)
                    return; // Handle energy not found.
                kcal_number.setText(kcal.toString());
                foodData.products.get(0).number_of_calories = kcal;
                Double fat = myMap.get("Fat");
                if(fat == null)
                    return;
                foodData.products.get(0).grams_of_fat = fat;
                Double carbohydrates = myMap.get("Carbohydrates");
                if(carbohydrates == null)
                    return;
                foodData.products.get(0).grams_of_carbs = carbohydrates;
                Double protein = myMap.get("Protein");
                if(protein == null)
                    return;
                foodData.products.get(0).grams_of_protein = protein;



            }
            //Verbesserung:
            @SuppressLint("SetTextI18n")
            public void onErrorResponse(VolleyError error) {
                tx.setText("Fehler bei der Verarbeitung der Antwort: " + error.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tx.setText("error "+error.toString());
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.i(e.toString(), "MyTag");
        }




    }

    private void showPopup(){
        Spinner measurements;
        ImageButton save;
        ImageButton cancel;
        EditText number_of_servings;

        Dialog dialog = new Dialog(this, R.style.DialogSyule);
        dialog.setContentView(R.layout.pop_up_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        dialog.show();

        measurements = dialog.findViewById(R.id.spinner_measurements);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);
        number_of_servings = dialog.findViewById(R.id.number_of_servings);
        number_of_servings.setText("1");


        //editing quantity, number of servings etc...
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String usedMesurements = measurements.getSelectedItem().toString();
                Double auxMeasurements = Double.valueOf(usedMesurements.replaceAll("[^0-9]", ""));
                Double numerOfServing = Double.valueOf(number_of_servings.getText().toString());

                 quantityOfUsedFood = auxMeasurements * numerOfServing / 100;




                Double totalKcalNumber = calculateUsedKcal(foodData, quantityOfUsedFood);



                for(  Map.Entry<String,Double> entry: myMap.entrySet()){
                    entry.setValue(entry.getValue()* (quantityOfUsedFood));
                }


                tx.setText(formatMap(myMap));
                kcal_number.setText(totalKcalNumber.toString());
                nr_servings_btn.setText(numerOfServing.toString());


            }
        });







       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Spinner_items_measurements, R.layout.spinner_color_measurements_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        measurements.setAdapter(adapter);






    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // Handle the scanned barcode
            barcode= result.getContents();
            sendRequest();
        }
    });




    static Map<String, Double> getNutritionalValues(String nutritionsFacts) {

        String[] elementsArray = nutritionsFacts.split(", ");
        Map<String, Double> resultMap = new HashMap<>();

        // Process each part of the split array
        for ( String element : elementsArray) {
            String rawElement = removeParentheses(element);
            String[] elementParts = rawElement.split(" ");
            for (String e : elementParts) {
                if(isNumeric(e)) {
                    Double value = Double.parseDouble(e);
                    String key= elementParts[0] ;
                    resultMap.put(key, value);
                    break;
                }
            }
        }
        return resultMap;
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static String removeParentheses(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideParentheses = false;

        for (char c : input.toCharArray()) {
            if (c == '(') {
                insideParentheses = true;
            } else if (c == ')' && insideParentheses) {
                insideParentheses = false;
                continue;
            }

            if (!insideParentheses) {
                    result.append(c);
            }
        }

        return result.toString();
    }


    private static String formatMap(Map<String, Double> map) {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            result.append(entry.getKey()).append(": ").append(df.format(entry.getValue())).append("\n");
        }
        return result.toString();
    }



    public Double calculateUsedKcal(ResponseProducts foodData,  Double quantity) {
        Double s = Double.valueOf(myMap.get("Energy").toString());
        return (quantityOfUsedFood * s) ;

    }

//Verbesserung:
//    public Double calculateUsedKcal(ResponseProducts foodData,  Double quantity) {
//        Double s = Double.valueOf(myMap.get("Energy").toString());
//        return (quantity * s) ;
//
//    }


}


