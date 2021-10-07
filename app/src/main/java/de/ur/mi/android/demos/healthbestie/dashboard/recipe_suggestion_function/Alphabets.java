package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Alphabets extends AppCompatActivity {

    public static final String ALPHABETS[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public static final String MEALS_ARRAY = "MEALS_ARRAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabets);

        ListView alphabetList = findViewById(R.id.alphabet_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.name, ALPHABETS);
        alphabetList.setAdapter(arrayAdapter);

        alphabetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(Alphabets.this);
                String SEARCH_FOOD_API ="https://www.themealdb.com/api/json/v1/1/search.php?f=";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, SEARCH_FOOD_API + ALPHABETS[position],
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject resObj = new JSONObject(response);
                                    JSONArray meals;
                                    try {
                                        meals = resObj.getJSONArray("meals");
                                    } catch (JSONException e) {
                                        meals = new JSONArray();
                                    }

                                    Intent intent = new Intent(Alphabets.this, SearchFood.class);
                                    intent.putExtra(MEALS_ARRAY, meals.toString());
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Alphabets.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }
}