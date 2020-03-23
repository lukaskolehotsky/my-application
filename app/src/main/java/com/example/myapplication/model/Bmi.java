package com.example.myapplication.model;

public class Bmi {

	private double from;

	private double to;

	private String category;

	public Bmi(double from, double to, String category) {
		this.from = from;
		this.to = to;
		this.category = category;
	}

	public double getFrom() {
		return from;
	}

	public void setFrom(double from) {
		this.from = from;
	}

	public double getTo() {
		return to;
	}

	public void setTo(double to) {
		this.to = to;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Bmi [from=" + from + ", to=" + to + ", category=" + category + "]";
	}

}
