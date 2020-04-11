package com.example.behealthy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Woman implements Parcelable {

	private List<Vitamin> vitamins;

	public Woman(List<Vitamin> vitamins) {
		this.vitamins = vitamins;
	}

	protected Woman(Parcel in) {
	}

	public static final Creator<Woman> CREATOR = new Creator<Woman>() {
		@Override
		public Woman createFromParcel(Parcel in) {
			return new Woman(in);
		}

		@Override
		public Woman[] newArray(int size) {
			return new Woman[size];
		}
	};

	public List<Vitamin> getVitamins() {
		return vitamins;
	}

	public void setVitamins(List<Vitamin> vitamins) {
		this.vitamins = vitamins;
	}

	@Override
	public String toString() {
		return "Woman [vitamins=" + vitamins + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}
