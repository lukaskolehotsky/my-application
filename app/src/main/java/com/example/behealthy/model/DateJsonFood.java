package com.example.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public class DateJsonFood {

    private LocalDate date;

    private List<JsonFood> jsonFoods;

    public DateJsonFood(LocalDate date, List<JsonFood> jsonFoods) {
        this.date = date;
        this.jsonFoods = jsonFoods;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<JsonFood> getJsonFoods() {
        return jsonFoods;
    }

    public void setJsonFoods(List<JsonFood> jsonFoods) {
        this.jsonFoods = jsonFoods;
    }

    public static JSONObject toJson(DateJsonFood dateJsonFood){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", dateJsonFood.getDate());
            jsonObject.put("jsonFoods", JsonFood.toJson(dateJsonFood.getJsonFoods()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject toJson(List<DateJsonFood> list){
        JSONObject jsonObjectKKK = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(DateJsonFood v: list){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dateJsonFood", DateJsonFood.toJson(v));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject jsonObjectXX = new JSONObject();
        try {
            jsonObjectXX.put("dateJsonFoods", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObjectXX;
    }

    @Override
    public String toString() {
        return "DateJsonFood{" +
                "date=" + date +
                ", jsonFoods=" + jsonFoods +
                '}';
    }

}
