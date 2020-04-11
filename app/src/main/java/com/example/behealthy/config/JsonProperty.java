package com.example.behealthy.config;

import com.example.behealthy.model.AgeWithBmis;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;

import java.util.List;

public class JsonProperty {

    private List<AgeWithBmis> ageWithBmis;

    private List<Vitamin> womanVitamins;

    private List<Vitamin> manVitamins;

    private List<Vegetable> vegetables;

    private List<Fruit> fruits;

    public JsonProperty(List<AgeWithBmis> ageWithBmis, List<Vitamin> womanVitamins, List<Vitamin> manVitamins, List<Vegetable> vegetables, List<Fruit> fruits) {
        this.ageWithBmis = ageWithBmis;
        this.womanVitamins = womanVitamins;
        this.manVitamins = manVitamins;
        this.vegetables = vegetables;
        this.fruits = fruits;
    }

    public List<AgeWithBmis> getAgeWithBmis() {
        return ageWithBmis;
    }

    public void setAgeWithBmis(List<AgeWithBmis> ageWithBmis) {
        this.ageWithBmis = ageWithBmis;
    }

    public List<Vitamin> getWomanVitamins() {
        return womanVitamins;
    }

    public void setWomanVitamins(List<Vitamin> womanVitamins) {
        this.womanVitamins = womanVitamins;
    }

    public List<Vitamin> getManVitamins() {
        return manVitamins;
    }

    public void setManVitamins(List<Vitamin> manVitamins) {
        this.manVitamins = manVitamins;
    }

    public List<Vegetable> getVegetables() {
        return vegetables;
    }

    public void setVegetables(List<Vegetable> vegetables) {
        this.vegetables = vegetables;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public void setFruits(List<Fruit> fruits) {
        this.fruits = fruits;
    }

    @Override
    public String toString() {
        return "JsonProperty{" +
                "ageWithBmis=" + ageWithBmis +
                ", womanVitamins=" + womanVitamins +
                ", manVitamins=" + manVitamins +
                ", vegetables=" + vegetables +
                ", fruits=" + fruits +
                '}';
    }
}
