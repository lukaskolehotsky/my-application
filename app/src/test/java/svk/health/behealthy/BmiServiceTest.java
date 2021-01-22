package svk.health.behealthy;

import svk.health.behealthy.R;
import svk.health.behealthy.config.JsonProperty;
import svk.health.behealthy.model.bmi.AgeWithBmis;
import svk.health.behealthy.model.bmi.Bmi;
import svk.health.behealthy.model.bmi.BmiCategory;
import svk.health.behealthy.service.BmiService;
import svk.health.behealthy.service.JsonService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BmiServiceTest {

    @Mock
    private JsonService jsonService;

    @InjectMocks
    private BmiService bmiService;

    @Test
    public void testCalculateBmi() {

        JsonProperty jsonProperty = new JsonProperty(generateAgeWithBmis(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        Mockito.when(jsonService.processFile(R.raw.agewithbmis)).thenReturn(jsonProperty);

        BmiCategory response = this.bmiService.calculateBmi(90.0, 180.0, 32);

        Assert.assertEquals(Double.valueOf(27.78), Double.valueOf(response.getCalculatedBmi()));
        Assert.assertEquals("Overweight", response.getCategory());
    }

    private List<AgeWithBmis> generateAgeWithBmis(){
        List<Bmi> bmis1 = new ArrayList<>();
        Bmi bmi1 = generateBmi(0, 19, "Underweight");
        Bmi bmi2 = generateBmi(19, 24, "Optimal weight");
        Bmi bmi3 = generateBmi(24, 29, "Overweight");
        Bmi bmi4 = generateBmi(29, 39, "Obesity");
        Bmi bmi5 = generateBmi(39, 1000, "Morbid obesity");
        bmis1.add(bmi1);
        bmis1.add(bmi2);
        bmis1.add(bmi3);
        bmis1.add(bmi4);
        bmis1.add(bmi5);

        List<Bmi> bmis2 = new ArrayList<>();
        Bmi bmi11 = generateBmi(0, 20, "Underweight");
        Bmi bmi22 = generateBmi(20, 25, "Optimal weight");
        Bmi bmi33 = generateBmi(25, 30, "Overweight");
        Bmi bmi44 = generateBmi(30, 40, "Obesity");
        Bmi bmi55 = generateBmi(40, 1000, "Morbid obesity");
        bmis2.add(bmi11);
        bmis2.add(bmi22);
        bmis2.add(bmi33);
        bmis2.add(bmi44);
        bmis2.add(bmi55);

        List<Bmi> bmis3 = new ArrayList<>();
        Bmi bmi111 = generateBmi(0, 21, "Underweight");
        Bmi bmi222 = generateBmi(21, 26, "Optimal weight");
        Bmi bmi333 = generateBmi(26, 31, "Overweight");
        Bmi bmi444 = generateBmi(31, 41, "Obesity");
        Bmi bmi555 = generateBmi(41, 1000, "Morbid obesity");
        bmis3.add(bmi111);
        bmis3.add(bmi222);
        bmis3.add(bmi333);
        bmis3.add(bmi444);
        bmis3.add(bmi555);

        List<Bmi> bmis4 = new ArrayList<>();
        Bmi bmi1111 = generateBmi(0, 22, "Underweight");
        Bmi bmi2222 = generateBmi(22, 27, "Optimal weight");
        Bmi bmi3333 = generateBmi(27, 32, "Overweight");
        Bmi bmi4444 = generateBmi(32, 42, "Obesity");
        Bmi bmi5555 = generateBmi(42, 1000, "Morbid obesity");
        bmis4.add(bmi1111);
        bmis4.add(bmi2222);
        bmis4.add(bmi3333);
        bmis4.add(bmi4444);
        bmis4.add(bmi5555);

        List<Bmi> bmis5 = new ArrayList<>();
        Bmi bmi11111 = generateBmi(0, 23, "Underweight");
        Bmi bmi22222 = generateBmi(23, 28, "Optimal weight");
        Bmi bmi33333 = generateBmi(28, 33, "Overweight");
        Bmi bmi44444 = generateBmi(33, 43, "Obesity");
        Bmi bmi55555 = generateBmi(43, 1000, "Morbid obesity");
        bmis5.add(bmi11111);
        bmis5.add(bmi22222);
        bmis5.add(bmi33333);
        bmis5.add(bmi44444);
        bmis5.add(bmi55555);

        List<Bmi> bmis6 = new ArrayList<>();
        Bmi bmi111111 = generateBmi(0, 24, "Underweight");
        Bmi bmi222222 = generateBmi(24, 29, "Optimal weight");
        Bmi bmi333333 = generateBmi(29, 34, "Overweight");
        Bmi bmi444444 = generateBmi(34, 44, "Obesity");
        Bmi bmi555555 = generateBmi(44, 1000, "Morbid obesity");
        bmis6.add(bmi111111);
        bmis6.add(bmi222222);
        bmis6.add(bmi333333);
        bmis6.add(bmi444444);
        bmis6.add(bmi555555);

        AgeWithBmis ageWithBmis1 = new AgeWithBmis(18, 24, bmis1);
        AgeWithBmis ageWithBmis2 = new AgeWithBmis(25, 34, bmis2);
        AgeWithBmis ageWithBmis3 = new AgeWithBmis(35, 44, bmis3);
        AgeWithBmis ageWithBmis4 = new AgeWithBmis(45, 54, bmis4);
        AgeWithBmis ageWithBmis5 = new AgeWithBmis(55, 64, bmis5);
        AgeWithBmis ageWithBmis6 = new AgeWithBmis(65, 1000, bmis6);

        List<AgeWithBmis> ageWithBmis = new ArrayList<>();
        ageWithBmis.add(ageWithBmis1);
        ageWithBmis.add(ageWithBmis2);
        ageWithBmis.add(ageWithBmis3);
        ageWithBmis.add(ageWithBmis4);
        ageWithBmis.add(ageWithBmis5);
        ageWithBmis.add(ageWithBmis6);

        return ageWithBmis;
    }

    private Bmi generateBmi(double from, double to, String category) {
        return new Bmi(from, to, category);
    }

}