package com.example.behealthy.model;

import java.util.List;

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

}
