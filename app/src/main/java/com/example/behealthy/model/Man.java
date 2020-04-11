package com.example.behealthy.model;

import java.util.List;

public class Man {

	private List<Vitamin> vitamins;

	public List<Vitamin> getVitamins() {
		return vitamins;
	}

	public Man(List<Vitamin> vitamins) {
		this.vitamins = vitamins;
	}

	public void setVitamins(List<Vitamin> vitamins) {
		this.vitamins = vitamins;
	}

	@Override
	public String toString() {
		return "Woman [vitamins=" + vitamins + "]";
	}
	
}
