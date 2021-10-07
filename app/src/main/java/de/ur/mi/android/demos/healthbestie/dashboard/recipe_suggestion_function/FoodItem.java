package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;


import java.io.Serializable;

public class FoodItem implements Serializable {
    private String foodName, thumbUrl, foodDescription;

    public FoodItem(String foodName, String thumbUrl, String foodDescription) {
        this.foodName = foodName;
        this.thumbUrl = thumbUrl;
        this.foodDescription = foodDescription;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public String getFoodThumbUrl() {
        return this.thumbUrl;
    }

    public String getFoodDescription() {
        return this.foodDescription;
    }
}
