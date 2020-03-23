package com.example.myapplication.model;

import java.util.List;

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
}
