package com.example.behealthy.model;

import org.json.JSONException;
import org.json.JSONObject;

public class GenderAge {

    private String gender;

    private String age;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "GenderAge{" +
                "gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    public static JSONObject toJson(GenderAge genderAge) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gender", genderAge.getGender());
            jsonObject.put("age", genderAge.getAge());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static GenderAge toObject(String jsonString) {
        JSONObject jsonObject = null;
        if (jsonString != null) {
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                GenderAge v = new GenderAge();
                v.setGender(jsonObject.getString("gender"));
                v.setAge(jsonObject.getString("age"));
                return v;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
