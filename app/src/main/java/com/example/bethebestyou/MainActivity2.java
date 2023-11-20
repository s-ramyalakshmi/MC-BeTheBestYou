package com.example.bethebestyou;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    String[] symptomsList = new String[]{"Nausea", "Headache", "Diarrhea", "Soar Throat", "Fever", "Muscle Ache", "Loss of Smell or Taste", "Cough", "Shortness of Breath", "Feeling Tired"};
    Map<String, Float> symptomRatingMaps = new HashMap<String, Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Spinner dropdown = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, symptomsList);
        dropdown.setAdapter(adapter);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int symptomInd = dropdown.getSelectedItemPosition();
                symptomRatingMaps.put(symptomsList[symptomInd], v);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ratingBar.setRating(symptomRatingMaps.get(symptomsList[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        for(int i = 0;i < symptomsList.length;i++){
            symptomRatingMaps.put(symptomsList[i], (float) 0);
        }

        Button btn5 = (Button) findViewById(R.id.button5);

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "pressed symptoms upload", Toast.LENGTH_SHORT).show();

                SQLiteDatabase db = UserInfoActivity.db;

                db.beginTransaction();
                try {
                    db.execSQL( "update "+UserInfoActivity.TableName+" set Nausea='"+symptomRatingMaps.get(symptomsList[0])+"', Headache='"+symptomRatingMaps.get(symptomsList[1])+"', Diarrhea='"+symptomRatingMaps.get(symptomsList[2])+"', Soar_throat='"+symptomRatingMaps.get(symptomsList[3])+"', Fever='"+symptomRatingMaps.get(symptomsList[4])+"', Muscle_Ache='"+symptomRatingMaps.get(symptomsList[5])+"', Loss_of_smell_or_taste='"+symptomRatingMaps.get(symptomsList[6])+"', cough='"+symptomRatingMaps.get(symptomsList[7])+"', shortness_of_breath='"+symptomRatingMaps.get(symptomsList[8])+"', Feeling_tired='"+symptomRatingMaps.get(symptomsList[9])+"' where recID = (SELECT MAX(recID) FROM "+UserInfoActivity.TableName+")" );
                    db.setTransactionSuccessful();
                }
                catch (SQLiteException e) {
                    Toast.makeText(getApplicationContext(), "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("", "SQliteException");
                }
                finally {
                    db.endTransaction();
                }
                Toast.makeText(getApplicationContext(), "db entry added", Toast.LENGTH_SHORT).show();
                Log.d("", "db entry added");
            }
        });
    }
}
