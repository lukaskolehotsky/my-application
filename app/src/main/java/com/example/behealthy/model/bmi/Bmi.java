package com.example.behealthy.model.bmi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

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

	public static JSONObject toJson(Bmi bmi){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("from", bmi.getFrom());
			jsonObject.put("to", bmi.getTo());
			jsonObject.put("category", bmi.getCategory());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static JSONArray toJson(List<Bmi> list){
		JSONArray jsonArray = new JSONArray();

		for(Bmi v: list){
			jsonArray.put(Bmi.toJson(v));
		}

//		JSONObject jsonObjectXX = new JSONObject();
//		try {
//			jsonObjectXX.put("bmis", jsonArray);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		return jsonArray;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bmi bmi = (Bmi) o;
		return Double.compare(bmi.from, from) == 0 &&
				Double.compare(bmi.to, to) == 0 &&
				category.equals(bmi.category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to, category);
	}
}
