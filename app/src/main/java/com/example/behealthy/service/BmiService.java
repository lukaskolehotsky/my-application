package com.example.behealthy.service;

import com.example.behealthy.R;
import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.bmi.AgeWithBmis;
import com.example.behealthy.model.bmi.Bmi;
import com.example.behealthy.model.bmi.BmiCategory;

import java.text.DecimalFormat;

public class BmiService {

    private JsonService jsonService;

    public BmiService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public BmiCategory calculateBmi(double weight, double height, int age){
        JsonProperty jsonProperty = jsonService.processFile(R.raw.agewithbmis);

        double calculatedBmi = weight / ((height / 100) * (height / 100));
        String category = "";

        for (AgeWithBmis ageWithBmis : jsonProperty.getAgeWithBmis()) {
            if (ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                for (Bmi bmi : ageWithBmis.getBmis()) {
                    if (bmi.getFrom() < calculatedBmi && bmi.getTo() > calculatedBmi) {
                        category = bmi.getCategory();
                    }
                }
            }
        }

        BmiCategory bmiCategory = new BmiCategory();

        DecimalFormat df = new DecimalFormat("#.##");
        String formattedCalculatedBmi = df.format(calculatedBmi).replace(",", ".");
        double dd = Double.parseDouble(formattedCalculatedBmi);
        calculatedBmi = dd;

        bmiCategory.setCalculatedBmi(calculatedBmi);
        bmiCategory.setCategory(category);

        return bmiCategory;
    }

}
