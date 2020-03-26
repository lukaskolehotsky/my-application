package com.example.myapplication.config;

import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Man;
import com.example.myapplication.model.Vitamin;
import com.example.myapplication.model.Woman;

import java.util.List;

public class JsonProperty {

    private List<AgeWithBmis> ageWithBmis;

    private List<Vitamin> womanVitamins;

    private List<Vitamin> manVitamins;

    public JsonProperty(List<AgeWithBmis> ageWithBmis, List<Vitamin> womanVitamins, List<Vitamin> manVitamins) {
        this.ageWithBmis = ageWithBmis;
        this.womanVitamins = womanVitamins;
        this.manVitamins = manVitamins;
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

    @Override
    public String toString() {
        return "JsonProperty{" +
                "ageWithBmis=" + ageWithBmis +
                ", womanVitamins=" + womanVitamins +
                ", manVitamins=" + manVitamins +
                '}';
    }
}
