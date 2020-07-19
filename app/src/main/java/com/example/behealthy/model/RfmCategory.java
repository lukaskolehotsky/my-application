package com.example.behealthy.model;

public class RfmCategory {

    private double calculatedRfm;

    private String category;

    public double getCalculatedRfm() {
        return calculatedRfm;
    }

    public void setCalculatedRfm(double calculatedRfm) {
        this.calculatedRfm = calculatedRfm;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "RfmCategory{" +
                "calculatedRfm=" + calculatedRfm +
                ", category='" + category + '\'' +
                '}';
    }

}
