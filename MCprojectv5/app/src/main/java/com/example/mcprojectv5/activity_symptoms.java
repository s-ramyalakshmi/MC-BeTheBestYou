package com.example.mcprojectv5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_symptoms  extends AppCompatActivity {

    private Spinner spinner;
    private RatingBar ratingBar;
    private Button btnUploadSymptoms;
    private double HRate=0.0f;
    private double RRate=0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-databasev3").allowMainThreadQueries().build();
        setContentView(R.layout.activity_symptoms);
        Intent intent = getIntent();
        HRate=intent.getDoubleExtra("HeartRate",0.0);
        RRate=intent.getDoubleExtra("RespiratoryRate",0.0);
        spinner = findViewById(R.id.spinnerSymptoms);
        String[] symptomsArray = getResources().getStringArray(R.array.symptomList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, symptomsArray);
        spinner.setAdapter(adapter);

        ratingBar = findViewById(R.id.ratingBar);
        btnUploadSymptoms = findViewById(R.id.btnUploadSymptoms);

        btnUploadSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selectedSymptom = spinner.getSelectedItem().toString();
                final int rating = (int)ratingBar.getRating();
                Rating current = new Rating();
                int sType=0;
                if("Nausea".equals(selectedSymptom))
                    sType=1;
                else if ("Headache".equals(selectedSymptom))
                    sType=2;
                else if ("Diarrhea".equals(selectedSymptom))
                    sType=3;
                else if ("Soar Throat".equals(selectedSymptom))
                    sType=4;
                else if ("Fever".equals(selectedSymptom))
                    sType=5;
                else if ("Muscle Ache".equals(selectedSymptom))
                    sType=6;
                else if ("Loss of Smell or taste".equals(selectedSymptom))
                    sType=7;
                else if ("Cough".equals(selectedSymptom))
                    sType=8;
                else if ("Shortness of Breath".equals(selectedSymptom))
                    sType=9;
                else if ("Feeling tired".equals(selectedSymptom))
                    sType=10;
                current.setValue((float) HRate,(float) RRate,sType,rating);
                database.ratingDao().insertRating(current);
                List<Rating> ratings = database.ratingDao().getAllRatings();
                System.out.println(ratings);
                }
                });
            }
        }


