package com.example.myapplication.utilities;

import android.content.Context;

import com.example.myapplication.model.AgeWithBmis;
import com.example.myapplication.model.Bmi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public FileReader(Context context){
        mContext = context;
    }
    private static Context mContext;
    private static int mResourceId;

    public ArrayList processFile(int resourceId)
    {
        mResourceId = resourceId;
        ArrayList contentList = new ArrayList();

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

    private ArrayList parseJsonFromString(String jsonString)
            throws JSONException {

        ArrayList ageWithBmisList = new ArrayList();

        try {
            JSONObject contentJson = new JSONObject(jsonString);

            JSONArray ageWithBmisArray = contentJson.getJSONArray("ageWithBmis");

            for(int i = 0; i < ageWithBmisArray.length(); i++) {
                int ageFrom = 0;
                int ageTo = 0;
                List<Bmi> bmiList = new ArrayList<>();

                JSONObject ageWithBmiDetails = ageWithBmisArray.getJSONObject(i);

                if (!ageWithBmiDetails.isNull("ageFrom")) { ageFrom = ageWithBmiDetails.getInt("ageFrom"); }
                if (!ageWithBmiDetails.isNull("ageTo")) { ageTo = ageWithBmiDetails.getInt("ageTo"); }

                JSONArray bmiArray = ageWithBmiDetails.getJSONArray("bmis");
                for(int j = 0; j < bmiArray.length(); j++) {
                    String category="";
                    double from = 0;
                    double to = 0;

                    JSONObject bmiDetails = bmiArray.getJSONObject(j);

                    if (!bmiDetails.isNull("from")) { from = bmiDetails.getDouble("from"); }
                    if (!bmiDetails.isNull("to")) { to = bmiDetails.getDouble("to"); }
                    if (!bmiDetails.isNull("category")) { category = bmiDetails.getString("category"); }

                    Bmi bmi = new Bmi(from, to, category);
                    bmiList.add(bmi);
                }

                AgeWithBmis ageWithBmis = new AgeWithBmis(ageFrom, ageTo, bmiList);
                ageWithBmisList.add(ageWithBmis);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ageWithBmisList;
    }
}
