package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.ur.mi.android.demos.healthbestie.R;

public class RecipeSuggestion extends AppCompatActivity {

    public static final String CATEGORIES_ARRAY = "CATEGORY_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestion);

        CardView categoriesBtn = findViewById(R.id.categories);
        CardView searchBtn = findViewById(R.id.search_food);

        categoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(RecipeSuggestion.this);
                String CATEGORIES_API ="https://www.themealdb.com/api/json/v1/1/categories.php";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, CATEGORIES_API,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject resObj = new JSONObject(response);
                                    JSONArray categories = resObj.getJSONArray("categories");
                                    Intent intent = new Intent(RecipeSuggestion.this, Categories.class);
                                    intent.putExtra(CATEGORIES_ARRAY, categories.toString());
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecipeSuggestion.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipeSuggestion.this, Alphabets.class));
            }
        });
    }
}