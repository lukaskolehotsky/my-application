package svk.health.behealthy.service;

import svk.health.behealthy.R;
import svk.health.behealthy.constants.Constants;
import svk.health.behealthy.model.RfmCategory;
import svk.health.behealthy.model.bmi.AgeWithBmis;
import svk.health.behealthy.model.bmi.Bmi;

import java.text.DecimalFormat;

public class RfmService {

    private JsonService jsonService;

    public RfmService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public RfmCategory calculateRfm(String loadedHeight, String loadedWaistCircumference, String loadedAge, String loadedGender) {
        double height = Double.parseDouble(loadedHeight);
        double waistCircumference = Double.parseDouble(loadedWaistCircumference);
        int age = Integer.parseInt(loadedAge);

        double calculatedRfm;
        String category = "";

        if (loadedGender.equals(Constants.WOMAN.label)) {
            calculatedRfm = 76 - (20 * height) / waistCircumference;
        } else {
            calculatedRfm = 64 - (20 * height) / waistCircumference;
        }

        for (AgeWithBmis ageWithBmis : jsonService.processFile(R.raw.agewithbmis).getAgeWithBmis()) {
            if (ageWithBmis.getAgeFrom() <= age && ageWithBmis.getAgeTo() >= age) {
                for (Bmi bmi : ageWithBmis.getBmis()) {
                    if (bmi.getFrom() < calculatedRfm && bmi.getTo() > calculatedRfm) {
                        category = bmi.getCategory();
                    }
                }
            }
        }

        RfmCategory rfmCategory = new RfmCategory();

        DecimalFormat df = new DecimalFormat("#.##");
        calculatedRfm = Double.valueOf(df.format(calculatedRfm).replace(",", "."));

        rfmCategory.setCalculatedRfm(calculatedRfm);
        rfmCategory.setCategory(category);

        return rfmCategory;
    }

}
