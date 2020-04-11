package com.example.behealthy.utilities;

public class SharedPreferenceEntry {

    private String gender;

    private String age;

    private String weight;

    private String height;

    private String waistCircumference;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWaistCircumference() {
        return waistCircumference;
    }

    public void setWaistCircumference(String waistCircumference) {
        this.waistCircumference = waistCircumference;
    }

    @Override
    public String toString() {
        return "SharedPreferenceEntry{" +
                "gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", waistCircumference='" + waistCircumference + '\'' +
                '}';
    }
}
