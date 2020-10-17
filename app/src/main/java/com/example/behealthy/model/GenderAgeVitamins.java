package com.example.behealthy.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GenderAgeVitamins {

    private GenderAge genderAge;

    private List<Vitamin> vitamins;

    public GenderAge getGenderAge() {
        return genderAge;
    }

    public void setGenderAge(GenderAge genderAge) {
        this.genderAge = genderAge;
    }

    public List<Vitamin> getVitamins() {
        return vitamins;
    }

    public void setVitamins(List<Vitamin> vitamins) {
        this.vitamins = vitamins;
    }

    @Override
    public String toString() {
        return "GenderAgeVitamins{" +
                "genderAge=" + genderAge +
                ", vitamins=" + vitamins +
                '}';
    }

    public static JSONObject toJson(GenderAgeVitamins genderAgeVitamins) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("genderAge", GenderAge.toJson(genderAgeVitamins.getGenderAge()));
            jsonObject.put("vitamins", Vitamin.toJson(genderAgeVitamins.getVitamins()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static GenderAgeVitamins toObject(String jsonString) {
        JSONObject jsonObject = null;
        if (jsonString != null) {
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                GenderAgeVitamins v = new GenderAgeVitamins();
                v.setGenderAge(GenderAge.toObject(jsonObject.getString("genderAge")));
                v.setVitamins(Vitamin.toList(jsonObject.getJSONArray("vitamins").toString()));
                return v;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
