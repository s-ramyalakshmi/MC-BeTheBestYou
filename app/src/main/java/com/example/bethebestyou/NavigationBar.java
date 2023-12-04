package com.example.bethebestyou;
//ADDED BY SUKRUTHI ANILKUMAR
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class NavigationBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        Button btnVitalsPage = findViewById(R.id.btnVitalsPage);
        Button btnMentalHealthPage = findViewById(R.id.btnMentalHealthPage);
        Button btnPersonalGoalPage = findViewById(R.id.btnPersonalGoalPage);
        Button btnResultsProgress = findViewById(R.id.btnResultsProgress);

        btnVitalsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MentalHealthActivity when btnMentalHealthPage is clicked
                Intent intent = new Intent(NavigationBar.this, MainActivity.class);
                startActivity(intent);
            }
        });



        btnMentalHealthPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MentalHealthActivity when btnMentalHealthPage is clicked
                Intent intent = new Intent(NavigationBar.this, MentalHealthActivity.class);
                startActivity(intent);
            }
        });

        btnPersonalGoalPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PersonalGoalActivity when btnPersonalGoalPage is clicked
                Intent intent = new Intent(NavigationBar.this, PersonalGoalActivity.class);
                startActivity(intent);
            }
        });


    }
}

