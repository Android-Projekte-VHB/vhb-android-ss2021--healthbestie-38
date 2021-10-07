package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import de.ur.mi.android.demos.healthbestie.R;

public class SearchFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        Intent intent = getIntent();
        String mealsArrString = intent.getStringExtra(Alphabets.MEALS_ARRAY);
        ArrayList<FoodItem> foodArr = new ArrayList<FoodItem>();

        try {
            JSONArray mealsArr = new JSONArray(mealsArrString);
            for (int i = 0; i < mealsArr.length(); i++) {
                JSONObject itemObj = mealsArr.getJSONObject(i);
                String foodName = itemObj.getString("strMeal");
                String foodThumbUrl = itemObj.getString("strMealThumb");
                String foodDescription = itemObj.getString("strInstructions");

                FoodItem foodItem = new FoodItem(foodName, foodThumbUrl, foodDescription);
                foodArr.add(foodItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView categoriesList = findViewById(R.id.categories_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        categoriesList.setLayoutManager(linearLayoutManager);

        FoodAdapter customAdapter = new FoodAdapter(this, foodArr);
        categoriesList.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }
}