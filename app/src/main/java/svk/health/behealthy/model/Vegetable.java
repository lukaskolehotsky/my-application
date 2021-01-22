package svk.health.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class Vegetable {
	
	private String name;
	
	private List<Vitamin> vitamins;

	public Vegetable(String name, List<Vitamin> vitamins) {
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
		return "Vegetable{" +
				"name='" + name + '\'' +
				", vitamins=" + vitamins +
				'}';
	}

	public static JSONObject toJson(Vegetable vegetable){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", vegetable.getName());
			jsonObject.put("vitamins", Vitamin.toJson(vegetable.getVitamins()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static JSONObject toJson(List<Vegetable> list){
		JSONArray jsonArray = new JSONArray();

		for(Vegetable v: list){
			jsonArray.put(Vegetable.toJson(v));
		}

		JSONObject jsonObjectXX = new JSONObject();
		try {
			jsonObjectXX.put("vegetables", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObjectXX;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vegetable vegetable = (Vegetable) o;
		return name.equals(vegetable.name) &&
				vitamins.equals(vegetable.vitamins);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, vitamins);
	}
}
