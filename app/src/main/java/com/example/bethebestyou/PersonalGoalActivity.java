package com.example.bethebestyou;
//ADDED BY SUKRUTHI ANILKUMAR


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalGoalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner goalsSpinner;
    private ListView linkListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_goal);

        goalsSpinner = findViewById(R.id.goalsSpinner);
        linkListView = findViewById(R.id.linkListView);

        String[] goals = {"Set you goal", "Weight Loss", "Stress Reduction", "Weight Gain"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(adapter);

        goalsSpinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String selectedGoal = (String) parentView.getItemAtPosition(position);

        displayLinks(selectedGoal);
    }

    public void onNothingSelected(AdapterView<?> parentView) {
        // Do nothing here
    }

    private void displayLinks(String selectedGoal) {
        List<LinkItem> linkItems = new ArrayList<>();

        if (selectedGoal.equals("Weight Loss")) {
            linkItems.add(new LinkItem("Weight Loss Exercises Video", "https://www.youtube.com/watch?v=LhL5SNZfnQs", R.drawable.weight_loss_thumbnail));
            linkItems.add(new LinkItem("Walking for Weight Loss", "https://www.eatingwell.com/article/7905005/walking-for-weight-loss-plan/", R.drawable.walking_thumbnail));
            linkItems.add(new LinkItem("Meal plan for Weight Loss", "https://www.womenshealthmag.com/uk/fitness/fat-loss/a34448055/walking-for-weight-loss/", R.drawable.diet_thumbnail));
        } else if (selectedGoal.equals("Stress Reduction")) {
            linkItems.add(new LinkItem("Stress Reduction Tips", "https://www.healthline.com/nutrition/16-ways-relieve-stress-anxiety", R.drawable.stress_reduction_thumbnail));
            linkItems.add(new LinkItem("Stress Reduction Exercises", "https://ctrinstitute.com/resources/stress-reduction-exercises/", R.drawable.stress_reduction_thumbnail2));
            linkItems.add(new LinkItem("Stress Reduction Meditation", "https://www.youtube.com/watch?v=z6X5oEIg6Ak", R.drawable.stress_reduction_thumbnail3));
        } else if (selectedGoal.equals("Weight Gain")) {
            linkItems.add(new LinkItem("Weight Gain Exercises Video", "https://www.youtube.com/watch?v=FDpM-CGMXcw", R.drawable.weight_gain_thumbnail));
            linkItems.add(new LinkItem("Weight Gain Tips", "https://www.youtube.com/watch?v=wBQ8wf3wHDA", R.drawable.weight_gain_thumbnail2));
            linkItems.add(new LinkItem("Healthy Weight Gain Meal Plan", "https://www.eatingwell.com/article/2060706/healthy-weight-gain-meal-plan/", R.drawable.weight_gain_meal_plan_thumbnail));
        }

        LinkAdapter linkAdapter = new LinkAdapter(this, R.layout.link_item, linkItems);
        linkListView.setAdapter(linkAdapter);

        linkListView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedLink = linkItems.get(position).getUrl();
            openWebPage(clickedLink);
        });
    }

    private void openWebPage(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening URL", Toast.LENGTH_SHORT).show();
        }
    }

    private static class LinkItem {
        private String title;
        private String url;
        private int imageResource;

        public LinkItem(String title, String url, int imageResource) {
            this.title = title;
            this.url = url;
            this.imageResource = imageResource;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public int getImageResource() {
            return imageResource;
        }
    }

    private static class LinkAdapter extends ArrayAdapter<LinkItem> {

        public LinkAdapter(@NonNull AppCompatActivity context, int resource, @NonNull List<LinkItem> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.link_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.linkImage);
            TextView textView = convertView.findViewById(R.id.linkTitle);

            LinkItem linkItem = getItem(position);
            if (linkItem != null) {
                imageView.setImageResource(linkItem.getImageResource());
                textView.setText(linkItem.getTitle());
            }

            return convertView;
        }
    }
}

