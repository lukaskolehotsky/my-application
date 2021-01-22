package svk.health.behealthy.service;

import svk.health.behealthy.R;
import svk.health.behealthy.constants.Constants;
import svk.health.behealthy.model.Vitamin;

import java.util.List;
import java.util.stream.Collectors;

public class DailyDoseService {

    private JsonService jsonService;

    public DailyDoseService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public List<Vitamin> getDailyDoseVitamins(String loadedAge, String loadedGender) {
        int age = Integer.parseInt(loadedAge);

        List<Vitamin> vitamins = loadedGender.equals(Constants.WOMAN.label) ? jsonService.processFile(R.raw.womanvitamins).getWomanVitamins() : jsonService.processFile(R.raw.manvitamins).getManVitamins();

        return vitamins.stream().filter(vitamin -> vitamin.getFrom() <= age && vitamin.getTo() >= age).collect(Collectors.toList());
    }

}
