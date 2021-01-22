package svk.health.behealthy.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonFood {

    private String name;

    private String grams;

    public JsonFood(String name, String grams) {
        this.name = name;
        this.grams = grams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public static JSONArray toJson(List<JsonFood> list){
        JSONArray jsonArray = new JSONArray();

        for(JsonFood v: list){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", v.getName());
                jsonObject.put("grams", v.getGrams());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static List<JsonFood> toList(String jsonString){
        List<JsonFood> jsonFoodList = new ArrayList<>();
        if(jsonString != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JsonFood v = new JsonFood(jsonObject.getString("name"), jsonObject.getString("grams"));
                    jsonFoodList.add(v);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonFoodList;
    }

    @Override
    public String toString() {
        return "JsonFood{" +
                "name='" + name + '\'' +
                ", grams='" + grams + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonFood jsonFood = (JsonFood) o;
        return name.equals(jsonFood.name) &&
                grams.equals(jsonFood.grams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, grams);
    }
}
