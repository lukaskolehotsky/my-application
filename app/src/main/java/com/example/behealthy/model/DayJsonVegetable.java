package com.example.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public class DayJsonVegetable {

    private LocalDate date;

    private List<JsonVegetable> jsonVegetables;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<JsonVegetable> getJsonVegetables() {
        return jsonVegetables;
    }

    public void setJsonVegetables(List<JsonVegetable> jsonVegetables) {
        this.jsonVegetables = jsonVegetables;
    }

    public static JSONObject toJson(DayJsonVegetable dayJsonVegetable){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", dayJsonVegetable.getDate());
            jsonObject.put("jsonVegetables", JsonVegetable.toJson(dayJsonVegetable.getJsonVegetables()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject toJson(List<DayJsonVegetable> list){
        JSONObject jsonObjectKKK = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(DayJsonVegetable v: list){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dayJsonVegetable", DayJsonVegetable.toJson(v));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject jsonObjectXX = new JSONObject();
        try {
            jsonObjectXX.put("dayJsonVegetables", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObjectXX;
    }

    @Override
    public String toString() {
        return "DayJsonVegetable{" +
                "date=" + date +
                ", jsonVegetables=" + jsonVegetables +
                '}';
    }

}
