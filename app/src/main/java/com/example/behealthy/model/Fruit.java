package com.example.behealthy.model;

import java.util.List;

public class Fruit {

    private String fruitName;

    private List<Vitamin> vitamins;

    public Fruit(String fruitName, List<Vitamin> vitamins) {
        this.fruitName = fruitName;
        this.vitamins = vitamins;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
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
                "fruitName='" + fruitName + '\'' +
                ", vitamins=" + vitamins +
                '}';
    }

}
