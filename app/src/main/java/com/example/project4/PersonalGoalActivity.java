package com.example.project4;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PersonalGoalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner goalsSpinner;
    private ListView linkListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_goal); // Replace "your_layout" with the actual name of your XML layout file

        // Assuming you have a reference to the Spinner in your layout
        goalsSpinner = findViewById(R.id.goalsSpinner);
        linkListView = findViewById(R.id.linkListView);

        // Define an array of goals
        String[] goals = {"Set you goal", "Weight Loss", "Stress Reduction", "Weight Gain"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(adapter);

        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        goalsSpinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // Handle the selection of a goal
        String selectedGoal = (String) parentView.getItemAtPosition(position);

        // Display links based on the selected goal
        displayLinks(selectedGoal);
    }

    public void onNothingSelected(AdapterView<?> parentView) {
        // Do nothing here
    }

    private void displayLinks(String selectedGoal) {
        // Define links based on the selected goal
        List<String> links = new ArrayList<>();

        if (selectedGoal.equals("Weight Loss")) {
            links.add("https://www.google.com");
            links.add("https://www.youtube.com/watch?v=LhL5SNZfnQs");
            links.add("https://www.eatingwell.com/article/7905005/walking-for-weight-loss-plan/");
            links.add("https://www.womenshealthmag.com/uk/fitness/fat-loss/a34448055/walking-for-weight-loss/");
        } else if (selectedGoal.equals("Stress Reduction")) {
            links.add("https://www.healthline.com/nutrition/16-ways-relieve-stress-anxiety");
            links.add("https://ctrinstitute.com/resources/stress-reduction-exercises/");
            links.add("https://www.youtube.com/watch?v=z6X5oEIg6Ak");
        } else if (selectedGoal.equals("Weight Gain")) {
            links.add("https://www.youtube.com/watch?v=FDpM-CGMXcw");
            links.add("https://www.youtube.com/watch?v=wBQ8wf3wHDA");
            links.add("https://www.eatingwell.com/article/2060706/healthy-weight-gain-meal-plan/");
        }

        // Set up the adapter for the ListView
        ArrayAdapter<String> linkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, links);
        linkListView.setAdapter(linkAdapter);

        // Set item click listener to handle link clicks
        linkListView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedLink = links.get(position);
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
}