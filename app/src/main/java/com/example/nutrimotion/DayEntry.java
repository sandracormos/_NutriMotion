package com.example.nutrimotion;

import static com.example.nutrimotion.ScanMeal.getNutritionalValues;

import java.util.HashMap;
import java.util.Map;

public class DayEntry {


    private Map<ResponseProducts, Double> breakfastList = new HashMap<>();
    private Map<ResponseProducts, Double> lunchList= new HashMap<>();
    private Map<ResponseProducts, Double> dinnerList= new HashMap<>();
    private Map<ResponseProducts, Double> snacksList= new HashMap<>();


    public  Map<ResponseProducts, Double> getBreakfastList(){return breakfastList;}
    public  Map<ResponseProducts, Double> getLunchList(){return lunchList;}
    public  Map<ResponseProducts, Double> getDinnerList(){return dinnerList;}
    public  Map<ResponseProducts, Double> getSnacksList(){return snacksList;}



    public Double getUsedKcal(ResponseProducts responseProduct, Double quantity){
        Map<String, Double> myMap = getNutritionalValues(responseProduct.products.get(0).nutrition_facts);
        Double s = Double.valueOf(myMap.get("Energy").toString());
        return  (quantity * s) ;

    }

    public Double calculateUsedKcalPerDay(){
        Double total = 0.0;
        total+= CalculateKcalPerMeal(snacksList);
        total+= CalculateKcalPerMeal(breakfastList);
        total+= CalculateKcalPerMeal(lunchList);
        total+= CalculateKcalPerMeal(dinnerList);
        return total;
    }
    public Double CalculateKcalPerMeal(Map<ResponseProducts, Double> map)
    {
        Double total = 0.0;
        for ( Map.Entry<ResponseProducts, Double> entry : map.entrySet()){
            total += getUsedKcal((entry.getKey()), entry.getValue());
        }
        return total;
    }






}
