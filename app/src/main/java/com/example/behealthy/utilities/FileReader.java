package com.example.behealthy.utilities;

import android.content.Context;

import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.AgeWithBmis;
import com.example.behealthy.model.Bmi;
import com.example.behealthy.model.DayJsonVegetable;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.JsonVegetable;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public FileReader(Context context){
        mContext = context;
    }
    private static Context mContext;
    private static int mResourceId;

    public JsonProperty processFile(int resourceId)
    {
        mResourceId = resourceId;
        JsonProperty contentList = null;

        String jsonStr = this.readRawTextFile();
        try {
            contentList = this.parseJsonFromString(jsonStr);
        } catch(Exception e){
            e.printStackTrace();
        }

        return contentList;
    }

    private static String readRawTextFile()
    {
        InputStream inputStream = mContext.getResources().openRawResource(mResourceId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }

    private JsonProperty parseJsonFromString(String jsonString) throws JSONException {
        ArrayList ageWithBmisList = getBmisWithDetails(jsonString);
        ArrayList womanVitaminsList = getWomanVitaminsList(jsonString);
        ArrayList manVitaminsList = getManVitaminsList(jsonString);
        ArrayList vegetablesList = getVegetablesList(jsonString);
        ArrayList fruitsList = getFruitsList(jsonString);

        JsonProperty jsonProperty = new JsonProperty(ageWithBmisList, womanVitaminsList, manVitaminsList, vegetablesList, fruitsList);
        return jsonProperty;
    }

    private ArrayList getBmisWithDetails(String jsonString) throws JSONException {
        ArrayList ageWithBmisList = new ArrayList();

        try {
            JSONObject contentJson = new JSONObject(jsonString);
            JSONArray ageWithBmisArray = contentJson.getJSONArray("ageWithBmis");

            for (int i = 0; i < ageWithBmisArray.length(); i++) {
                int ageFrom = 0;
                int ageTo = 0;
                List<Bmi> bmiList = new ArrayList<>();

                JSONObject ageWithBmiDetails = ageWithBmisArray.getJSONObject(i);

                if (!ageWithBmiDetails.isNull("ageFrom")) {
                    ageFrom = ageWithBmiDetails.getInt("ageFrom");
                }
                if (!ageWithBmiDetails.isNull("ageTo")) {
                    ageTo = ageWithBmiDetails.getInt("ageTo");
                }

                JSONArray bmiArray = ageWithBmiDetails.getJSONArray("bmis");
                for (int j = 0; j < bmiArray.length(); j++) {
                    String category = "";
                    double from = 0;
                    double to = 0;

                    JSONObject bmiDetails = bmiArray.getJSONObject(j);

                    if (!bmiDetails.isNull("from")) {
                        from = bmiDetails.getDouble("from");
                    }
                    if (!bmiDetails.isNull("to")) {
                        to = bmiDetails.getDouble("to");
                    }
                    if (!bmiDetails.isNull("category")) {
                        category = bmiDetails.getString("category");
                    }

                    Bmi bmi = new Bmi(from, to, category);
                    bmiList.add(bmi);
                }

                AgeWithBmis ageWithBmis = new AgeWithBmis(ageFrom, ageTo, bmiList);
                ageWithBmisList.add(ageWithBmis);
            }
            return ageWithBmisList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList getWomanVitaminsList(String jsonString){
        ArrayList womanVitaminsList = new ArrayList();

        try{
            JSONObject contentJson = new JSONObject(jsonString);
            JSONObject womanJsonObject = contentJson.getJSONObject("woman");
            JSONArray womanVitaminsArray = womanJsonObject.getJSONArray("vitamins");

            for(int i = 0; i < womanVitaminsArray.length(); i++) {
                String name = "";
                double from = 0.0f;
                double to = 0.0f;
                double amount = 0.0f;
                String unit = "";

                JSONObject womanVitaminsDetails = womanVitaminsArray.getJSONObject(i);

                if (!womanVitaminsDetails.isNull("name")) { name = womanVitaminsDetails.getString("name"); }
                if (!womanVitaminsDetails.isNull("from")) { from = womanVitaminsDetails.getDouble("from"); }
                if (!womanVitaminsDetails.isNull("to")) { to = womanVitaminsDetails.getDouble("to"); }
                if (!womanVitaminsDetails.isNull("amount")) { amount = womanVitaminsDetails.getDouble("amount"); }
                if (!womanVitaminsDetails.isNull("unit")) { unit = womanVitaminsDetails.getString("unit"); }

                Vitamin womanVitamin = new Vitamin(name, from, to, amount, unit);
                womanVitaminsList.add(womanVitamin);
            }
            return womanVitaminsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList getManVitaminsList(String jsonString){
        ArrayList manVitaminsList = new ArrayList();

        try{
            JSONObject contentJson = new JSONObject(jsonString);
            JSONObject manJsonObject = contentJson.getJSONObject("man");
            JSONArray manVitaminsArray = manJsonObject.getJSONArray("vitamins");

            for(int i = 0; i < manVitaminsArray.length(); i++) {
                String name = "";
                double from = 0.0f;
                double to = 0.0f;
                double amount = 0.0f;
                String unit = "";

                JSONObject manVitaminsDetails = manVitaminsArray.getJSONObject(i);

                if (!manVitaminsDetails.isNull("name")) { name = manVitaminsDetails.getString("name"); }
                if (!manVitaminsDetails.isNull("from")) { from = manVitaminsDetails.getDouble("from"); }
                if (!manVitaminsDetails.isNull("to")) { to = manVitaminsDetails.getDouble("to"); }
                if (!manVitaminsDetails.isNull("amount")) { amount = manVitaminsDetails.getDouble("amount"); }
                if (!manVitaminsDetails.isNull("unit")) { unit = manVitaminsDetails.getString("unit"); }

                Vitamin manVitamin = new Vitamin(name, from, to, amount, unit);
                manVitaminsList.add(manVitamin);
            }
            return manVitaminsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList getVegetablesList(String jsonString) {
        ArrayList vegetablesList = new ArrayList();

        try{
            JSONObject contentJson = new JSONObject(jsonString);
            JSONArray vegetablesArray = contentJson.getJSONArray("vegetables");

            for(int i = 0; i < vegetablesArray.length(); i++) {
                String vegetableName = "";
                List<Vitamin> vitamins = new ArrayList<>();

                JSONObject vegetablesDetails = vegetablesArray.getJSONObject(i);

                if (!vegetablesDetails.isNull("vegetableName")) { vegetableName = vegetablesDetails.getString("vegetableName"); }

                JSONArray vitaminsArray = vegetablesDetails.getJSONArray("vitamins");
                for(int j = 0; j < vitaminsArray.length(); j++) {
                    String name = "";
                    double amount = 0.0f;
                    String unit = "";
                    int from = 0;
                    int to = 0;

                    JSONObject vitaminsDetails = vitaminsArray.getJSONObject(j);
                    if (!vitaminsDetails.isNull("name")) { name = vitaminsDetails.getString("name"); }
                    if (!vitaminsDetails.isNull("amount")) { amount = vitaminsDetails.getDouble("amount"); }
                    if (!vitaminsDetails.isNull("unit")) { unit = vitaminsDetails.getString("unit"); }

                    Vitamin vitamin = new Vitamin(name, from, to, amount, unit);
                    vitamins.add(vitamin);
                }

                Vegetable vegetable = new Vegetable(vegetableName, vitamins);
                vegetablesList.add(vegetable);
            }
            return vegetablesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList getFruitsList(String jsonString) {
        ArrayList fruitsList = new ArrayList();

        try{
            JSONObject contentJson = new JSONObject(jsonString);
            JSONArray fruitsArray = contentJson.getJSONArray("fruits");

            for(int i = 0; i < fruitsArray.length(); i++) {
                String fruitName = "";
                List<Vitamin> vitamins = new ArrayList<>();

                JSONObject fruitsDetails = fruitsArray.getJSONObject(i);

                if (!fruitsDetails.isNull("fruitName")) { fruitName = fruitsDetails.getString("fruitName"); }

                JSONArray vitaminsArray = fruitsDetails.getJSONArray("vitamins");
                for(int j = 0; j < vitaminsArray.length(); j++) {
                    String name = "";
                    double amount = 0.0f;
                    String unit = "";
                    int from = 0;
                    int to = 0;

                    JSONObject vitaminsDetails = vitaminsArray.getJSONObject(j);
                    if (!vitaminsDetails.isNull("name")) { name = vitaminsDetails.getString("name"); }
                    if (!vitaminsDetails.isNull("amount")) { amount = vitaminsDetails.getDouble("amount"); }
                    if (!vitaminsDetails.isNull("unit")) { unit = vitaminsDetails.getString("unit"); }

                    Vitamin vitamin = new Vitamin(name, from, to, amount, unit);
                    vitamins.add(vitamin);
                }

                Fruit fruit = new Fruit(fruitName, vitamins);
                fruitsList.add(fruit);
            }
            return fruitsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList getDayJsonVegetablesList(String jsonString) {
        ArrayList dayJsonVegetablesList = new ArrayList();

        try{
            JSONObject contentJson = new JSONObject(jsonString);
            JSONArray dayJsonVegetablesArray = contentJson.getJSONArray("dayJsonVegetables");

            for(int i = 0; i < dayJsonVegetablesArray.length(); i++) {
                String date = "";
                List<Vitamin> vitamins = new ArrayList<>();

                JSONObject dayJsonVegetablesDetails = dayJsonVegetablesArray.getJSONObject(i);

                JSONObject dayJsonVegetableDetails = dayJsonVegetablesDetails.getJSONObject("dayJsonVegetable");

                if (!dayJsonVegetableDetails.isNull("date")) { date = dayJsonVegetableDetails.getString("date"); }

                List<JsonVegetable> jsonVegetableList = new ArrayList<>();
                JSONArray jsonVegetablesArray = dayJsonVegetableDetails.getJSONArray("jsonVegetables");
                for(int j = 0; j < jsonVegetablesArray.length(); j++) {
                    String vegetableName = "";
                    String grams = "";

                    JSONObject jsonVegetableDetails = jsonVegetablesArray.getJSONObject(j);
                    if (!jsonVegetableDetails.isNull("vegetableName")) { vegetableName = jsonVegetableDetails.getString("vegetableName"); }
                    if (!jsonVegetableDetails.isNull("grams")) { grams = jsonVegetableDetails.getString("grams"); }

                    JsonVegetable jsonVegetable = new JsonVegetable(vegetableName, grams);
                    jsonVegetableList.add(jsonVegetable);
                }

                DayJsonVegetable dayJsonVegetable = new DayJsonVegetable();
                dayJsonVegetable.setDate(LocalDate.parse(date));
                dayJsonVegetable.setJsonVegetables(jsonVegetableList);

                dayJsonVegetablesList.add(dayJsonVegetable);
            }
            return dayJsonVegetablesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
