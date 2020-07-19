package com.example.behealthy.model.bmi;

public class BmiCategory {

    private double calculatedBmi;

    private String category;

    public double getCalculatedBmi() {
        return calculatedBmi;
    }

    public void setCalculatedBmi(double calculatedBmi) {
        this.calculatedBmi = calculatedBmi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "BmiCategory{" +
                "calculatedBmi=" + calculatedBmi +
                ", category='" + category + '\'' +
                '}';
    }

}
