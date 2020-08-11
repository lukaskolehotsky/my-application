package com.example.behealthy;

import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.service.DailyDoseService;
import com.example.behealthy.service.JsonService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DailyDoseServiceTest {

    @InjectMocks
    private DailyDoseService dailyDoseService;

    @Mock
    private JsonService jsonService;

    @Test
    public void testGetDailyDoseVitamins() {
        JsonProperty jsonProperty = new JsonProperty(Collections.emptyList(), Collections.emptyList(), generateManVitamins(),Collections.emptyList(),Collections.emptyList());
        when(jsonService.processFile(R.raw.manvitamins)).thenReturn(jsonProperty);

        List<Vitamin> response = this.dailyDoseService.getDailyDoseVitamins("32", "man");

        Assert.assertEquals(1, response.size());
        Assert.assertEquals("Vitamin C", response.get(0).getName());
        Assert.assertEquals(Double.valueOf(19.0), Double.valueOf(response.get(0).getFrom()));
        Assert.assertEquals(Double.valueOf(50.0), Double.valueOf(response.get(0).getTo()));
        Assert.assertEquals(Double.valueOf(90.0), Double.valueOf(response.get(0).getAmount()));
        Assert.assertEquals("mg", response.get(0).getUnit());
    }

    private List<Vitamin> generateManVitamins() {
        List<Vitamin> vitamins = new ArrayList<>();
        Vitamin vitamin1 = generateVitamin("Vitamin C", 9.0, 13.0, 45.0, "mg");
        Vitamin vitamin2 = generateVitamin("Vitamin C", 14.0, 18.0, 75.0, "mg");
        Vitamin vitamin3 = generateVitamin("Vitamin C", 19.0, 50.0, 90.0, "mg");
        Vitamin vitamin4 = generateVitamin("Vitamin C", 51.0, 100.0, 90.0, "mg");

        vitamins.add(vitamin1);
        vitamins.add(vitamin2);
        vitamins.add(vitamin3);
        vitamins.add(vitamin4);
        return vitamins;
    }

    private Vitamin generateVitamin(String name, double from, double to, double amount, String unit) {
        Vitamin vitamin = new Vitamin();
        vitamin.setName(name);
        vitamin.setFrom(from);
        vitamin.setTo(to);
        vitamin.setAmount(amount);
        vitamin.setUnit(unit);
        return vitamin;
    }

}
