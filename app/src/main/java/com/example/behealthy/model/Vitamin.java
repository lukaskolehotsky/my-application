package com.example.behealthy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vitamin implements Parcelable {

	private String name;

	private double from;

	private double to;

	private double amount;

	private String unit;

	public Vitamin(String name, double from, double to, double amount, String unit) {
		this.name = name;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.unit = unit;
	}

	protected Vitamin(Parcel in) {
		name = in.readString();
		from = in.readDouble();
		to = in.readDouble();
		amount = in.readDouble();
		unit = in.readString();
	}

	public static final Creator<Vitamin> CREATOR = new Creator<Vitamin>() {
		@Override
		public Vitamin createFromParcel(Parcel in) {
			return new Vitamin(in);
		}

		@Override
		public Vitamin[] newArray(int size) {
			return new Vitamin[size];
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Vitamin [name=" + name + ", from=" + from + ", to=" + to + ", amount=" + amount + ", unit=" + unit
				+ "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeDouble(from);
		dest.writeDouble(to);
		dest.writeDouble(amount);
		dest.writeString(unit);
	}
}
