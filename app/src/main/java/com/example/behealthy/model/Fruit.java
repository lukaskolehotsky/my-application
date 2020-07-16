package com.example.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class Fruit {

    private String name;

    private List<Vitamin> vitamins;

    public Fruit(String name, List<Vitamin> vitamins) {
        this.name = name;
        this.vitamins = vitamins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vitamin> getVitamins() {
        return vitamins;
    }

    public void setVitamins(List<Vitamin> vitamins) {
        this.vitamins = vitamins;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", vitamins=" + vitamins +
                '}';
    }

    public static JSONObject toJson(Fruit fruit){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", fruit.getName());
            jsonObject.put("vitamins", Vitamin.toJson(fruit.getVitamins()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject toJson(List<Fruit> list){
        JSONArray jsonArray = new JSONArray();

        for(Fruit v: list){
            jsonArray.put(Fruit.toJson(v));
        }

        JSONObject jsonObjectXX = new JSONObject();
        try {
            jsonObjectXX.put("fruits", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectXX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fruit fruit = (Fruit) o;
        return name.equals(fruit.name) &&
                vitamins.equals(fruit.vitamins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vitamins);
    }
}
