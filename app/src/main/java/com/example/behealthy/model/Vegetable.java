package com.example.behealthy.model;

import java.util.List;

public class Vegetable {
	
	private String vegetableName;
	
	private List<Vitamin> vitamins;

	public Vegetable(String vegetableName, List<Vitamin> vitamins) {
		this.vegetableName = vegetableName;
		this.vitamins = vitamins;
	}

	public String getVegetableName() {
		return vegetableName;
	}

	public void setVegetableName(String vegetableName) {
		this.vegetableName = vegetableName;
	}

	public List<Vitamin> getVitamins() {
		return vitamins;
	}

	public void setVitamins(List<Vitamin> vitamins) {
		this.vitamins = vitamins;
	}

	@Override
	public String toString() {
		return "Vegetable{" +
				"vegetableName='" + vegetableName + '\'' +
				", vitamins=" + vitamins +
				'}';
	}
}
