package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.ur.mi.android.demos.healthbestie.R;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        String categoriesArrString = intent.getStringExtra(RecipeSuggestion.CATEGORIES_ARRAY);
        ArrayList<FoodItem> foodArr = new ArrayList<FoodItem>();

        try {
            JSONArray categoriesArr = new JSONArray(categoriesArrString);
            for (int i = 0; i < categoriesArr.length(); i++) {
                JSONObject itemObj = categoriesArr.getJSONObject(i);
                String foodName = itemObj.getString("strCategory");
                String foodThumbUrl = itemObj.getString("strCategoryThumb");
                String foodDescription = itemObj.getString("strCategoryDescription");

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