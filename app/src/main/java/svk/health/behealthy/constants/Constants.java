package svk.health.behealthy.constants;

public enum Constants {

    AGE("Age"),
    GENDER("Gender"),
    HEIGHT("Height"),
    WEIGHT("Weight"),
    WAIST_CIRCUMFERENCE("Waist circumference"),
    WOMAN("woman"),
    FIRST_COLUMN("First"),
    SECOND_COLUMN("Second"),
    FIRST_COLUMN_NAME_VEGETABLE("Vegetable"),
    FOODS_JSON("FoodsJson"),
    FOODS("Foods"),
    GRAMS("Grams"),
    VEGETABLES("Vegetables"),
    VITAMIN_LIST("VITAMIN_LIST"),
    VITAMIN_NAME("VITAMIN_NAME"),
    VEGETABLE_NAME("VEGETABLE_NAME"),
    YEAR("YEAR"),
    MONTH("MONTH"),
    DAY("DAY")
    ;

    public final String label;

    private Constants(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Constants{" +
                "label='" + label + '\'' +
                '}';
    }
}