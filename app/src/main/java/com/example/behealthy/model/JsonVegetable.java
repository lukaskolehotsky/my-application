package com.example.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonVegetable {

    private String vegetableName;

    private String grams;

    public JsonVegetable(String vegetableName, String grams) {
        this.vegetableName = vegetableName;
        this.grams = grams;
    }

    public String getVegetableName() {
        return vegetableName;
    }

    public void setVegetableName(String vegetableName) {
        this.vegetableName = vegetableName;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public static JSONArray toJson(List<JsonVegetable> list){
        JSONArray jsonArray = new JSONArray();

        for(JsonVegetable v: list){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("vegetableName", v.getVegetableName());
                jsonObject.put("grams", v.getGrams());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static List<JsonVegetable> toList(String jsonString){
        List<JsonVegetable> jsonVegetableList = new ArrayList<>();
        if(jsonString != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JsonVegetable v = new JsonVegetable(jsonObject.getString("vegetableName"), jsonObject.getString("grams"));
                    jsonVegetableList.add(v);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonVegetableList;
    }

    @Override
    public String toString() {
        return "JsonVegetable{" +
                "vegetableName='" + vegetableName + '\'' +
                ", grams='" + grams + '\'' +
                '}';
    }
}
