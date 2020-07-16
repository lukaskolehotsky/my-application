package com.example.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class AgeWithBmis {
	
	private int ageFrom;
	
	private int ageTo;
	
	private List<Bmi> bmis;

	public AgeWithBmis(int ageFrom, int ageTo, List<Bmi> bmis) {
		this.ageFrom = ageFrom;
		this.ageTo = ageTo;
		this.bmis = bmis;
	}

	public int getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(int ageFrom) {
		this.ageFrom = ageFrom;
	}

	public int getAgeTo() {
		return ageTo;
	}

	public void setAgeTo(int ageTo) {
		this.ageTo = ageTo;
	}

	public List<Bmi> getBmis() {
		return bmis;
	}

	public void setBmis(List<Bmi> bmis) {
		this.bmis = bmis;
	}

	@Override
	public String toString() {
		return "AgeWithBmis{" +
				"ageFrom=" + ageFrom +
				", ageTo=" + ageTo +
				", bmis=" + bmis +
				'}';
	}

	public static JSONObject toJson(AgeWithBmis ageWithBmis){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("ageFrom", ageWithBmis.getAgeFrom());
			jsonObject.put("ageTo", ageWithBmis.getAgeTo());
			jsonObject.put("bmis", Bmi.toJson(ageWithBmis.getBmis()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static JSONObject toJson(List<AgeWithBmis> list){
		JSONArray jsonArray = new JSONArray();

		for(AgeWithBmis v: list){
			jsonArray.put(AgeWithBmis.toJson(v));
		}

		JSONObject jsonObjectXX = new JSONObject();
		try {
			jsonObjectXX.put("ageWithBmis", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObjectXX;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AgeWithBmis that = (AgeWithBmis) o;
		return ageFrom == that.ageFrom &&
				ageTo == that.ageTo &&
				bmis.equals(that.bmis);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ageFrom, ageTo, bmis);
	}
}
