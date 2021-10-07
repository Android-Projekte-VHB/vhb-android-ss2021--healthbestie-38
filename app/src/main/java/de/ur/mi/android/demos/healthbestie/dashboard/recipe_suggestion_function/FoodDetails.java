package de.ur.mi.android.demos.healthbestie.dashboard.recipe_suggestion_function;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import de.ur.mi.android.demos.healthbestie.R;

public class FoodDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        TextView foodNameText = findViewById(R.id.food_name);
        TextView foodDescriptionText = findViewById(R.id.food_description);
        ImageView foodImageView = findViewById(R.id.food_image);

        FoodItem foodItem = (FoodItem) getIntent().getSerializableExtra(FoodAdapter.FOOD_DETAILS);
        foodNameText.setText(foodItem.getFoodName());
        foodDescriptionText.setText(foodItem.getFoodDescription());
        new DownloadImageTask(foodImageView).execute(foodItem.getFoodThumbUrl());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Toast.makeText(FoodDetails.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}