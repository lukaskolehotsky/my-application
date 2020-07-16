package com.example.behealthy;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.behealthy.config.JsonProperty;
import com.example.behealthy.model.AgeWithBmis;
import com.example.behealthy.model.Bmi;
import com.example.behealthy.model.DateJsonFood;
import com.example.behealthy.model.Fruit;
import com.example.behealthy.model.JsonFood;
import com.example.behealthy.model.Vegetable;
import com.example.behealthy.model.Vitamin;
import com.example.behealthy.service.JsonService;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class JsonServiceTest {

    private JsonService jsonService;

    @Before
    public void init(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        jsonService = new JsonService(context);
    }

    @Test
    public void testGetDateJsonFoodList() {
        List<DateJsonFood> dateJsonFoods = generateDateJsonFoods();

        List<DateJsonFood> response = this.jsonService.getDateJsonFoodList(DateJsonFood.toJson(dateJsonFoods).toString());

        Assert.assertEquals(dateJsonFoods, response);
    }

    @Test
    public void testGetFruitsList(){
        List<Fruit> fruits = generateFruits();

        List<Fruit> response = this.jsonService.getFruitsList(Fruit.toJson(fruits).toString());

        Assert.assertEquals(fruits, response);
    }

    @Test
    public void testGetVegetablesList() {
        List<Vegetable> vegetables = generateVegetables();

        List<Vegetable> response = this.jsonService.getVegetablesList(Vegetable.toJson(vegetables).toString());

        Assert.assertEquals(vegetables, response);
    }

    @Test
    public void testGetAgeWithBmis() throws JSONException {
        List<AgeWithBmis> ageWithBmis = generateAgeWithBmisList();

        List<AgeWithBmis> response = this.jsonService.getAgeWithBmis(AgeWithBmis.toJson(ageWithBmis).toString());

        Assert.assertEquals(ageWithBmis, response);
    }

    @Test
    public void testGetWomanVitaminsList() {
        List<Vitamin> vitamins = generateVitamins();

        List<Vitamin> response = this.jsonService.getWomanVitaminsList(Vitamin.toJsonWoman(vitamins).toString());

        Assert.assertEquals(vitamins, response);
    }

    @Test
    public void testGetManVitaminsList() {
        List<Vitamin> vitamins = generateVitamins();

        List<Vitamin> response = this.jsonService.getManVitaminsList(Vitamin.toJsonMan(vitamins).toString());

        Assert.assertEquals(vitamins, response);
    }

    @Test
    public void testProcessFile() {
        JsonProperty fruits = this.jsonService.processFile(R.raw.fruits);
        Assert.assertEquals(fruits.getFruits().size(), 8);

        JsonProperty vegetables = this.jsonService.processFile(R.raw.vegetables);
        Assert.assertEquals(vegetables.getVegetables().size(), 22);

        JsonProperty womanVitamins = this.jsonService.processFile(R.raw.womanvitamins);
        Assert.assertEquals(womanVitamins.getWomanVitamins().size(), 64);

        JsonProperty manVitamins = this.jsonService.processFile(R.raw.manvitamins);
        Assert.assertEquals(manVitamins.getManVitamins().size(), 64);

        JsonProperty ageWithBmis = this.jsonService.processFile(R.raw.agewithbmis);
        Assert.assertEquals(ageWithBmis.getAgeWithBmis().size(), 6);
    }

    private Bmi generateBmi() {
        return new Bmi(19, 24, "Obesity");
    }

    private List<Bmi> generateBmis() {
        List<Bmi> bmis = new ArrayList<>();
        bmis.add(generateBmi());
        return bmis;
    }

    private AgeWithBmis generateAgeWithBmis(){
        return new AgeWithBmis(18, 20, generateBmis());
    }

    private List<AgeWithBmis> generateAgeWithBmisList() {
        List<AgeWithBmis> ageWithBmisList = new ArrayList<>();
        ageWithBmisList.add(generateAgeWithBmis());
        return ageWithBmisList;
    }

    private List<Vegetable> generateVegetables() {
        List<Vegetable> vegetables = new ArrayList<>();
        vegetables.add(generateVegetable());
        return vegetables;
    }

    private Vegetable generateVegetable() {
        return new Vegetable("Carrot", generateVitamins());
    }

    private List<DateJsonFood> generateDateJsonFoods(){
        List<DateJsonFood> dateJsonFoodList = new ArrayList<>();
        DateJsonFood dateJsonFood = generateDateJsonFood();
        dateJsonFoodList.add(dateJsonFood);

        return dateJsonFoodList;
    }

    private DateJsonFood generateDateJsonFood() {
        return new DateJsonFood(LocalDate.now(), generateJsonFoods());
    }

    private JsonFood generateJsonFood(){
        return new JsonFood("Pineapple", "200g");
    }

    private List<JsonFood> generateJsonFoods(){
        List<JsonFood> jsonFoodList = new ArrayList<>();
        JsonFood jsonFood = generateJsonFood();
        jsonFoodList.add(jsonFood);
        return jsonFoodList;
    }

    private List<Fruit> generateFruits() {
        List<Fruit> fruits = new ArrayList<>();
        Fruit fruit = generateFruit();
        fruits.add(fruit);

        return fruits;
    }

    private Fruit generateFruit() {
        return new Fruit("Orange", generateVitamins());
    }

    private List<Vitamin> generateVitamins(){
        List<Vitamin> vitamins = new ArrayList<>();
        Vitamin vitamin = generateVitamin();
        vitamins.add(vitamin);

        return vitamins;
    }

    private Vitamin generateVitamin(){
        return new Vitamin("Vitamin C", 0.1, 0.2, 1.0, "g");
    }

}
