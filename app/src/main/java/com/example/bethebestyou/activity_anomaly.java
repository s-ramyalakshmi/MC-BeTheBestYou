package com.example.bethebestyou;

import static com.example.bethebestyou.MainActivity.db;
import static com.example.bethebestyou.UserInfoActivity.TableName;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;


public class activity_anomaly extends AppCompatActivity {
    String doctor="Physician";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomaly);
        TextView textView = findViewById(R.id.outputText);
        /*try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("9945689481", null, "Your friend Pallav haas abnormal health vitals , please check up on them", null, null);
            Toast.makeText(getApplicationContext(), "SMS sent to emergency contact!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again later.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }*/
        Button nextButton= findViewById(R.id.buttonMaps);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String[] columns = {"recID", "name", "age", "height", "weight", "emergency_contact", "bmi",
                "heart_rate", "respiratory_rate", "Nausea", "Headache", "Diarrhea", "Soar_throat",
                "Fever", "Muscle_Ache", "Loss_of_smell_or_taste", "cough", "shortness_of_breath",
                "Feeling_tired", "anxiety_score", "depression_score"};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String Anomaly="Vitals are normal";
                textView.setText(Anomaly);
                switch (selectedItem)
                {
                    case "weight":doctor="Nutritionist";
                    break;
                    case "heart_rate": doctor="Cardiologist";
                    break;
                    case "respiratory_rate": doctor="Pulmonologist";
                    break;
                    case "Nausea": doctor="Physician";
                    break;
                    case "Headache": doctor="Neurologist";
                    break;
                    case "Diarrhea": doctor="Gastroenterologist";
                        break;
                    case "Fever": doctor="Physician";
                        break;
                    case "Muscle_Ache": doctor="Orthopedic";
                        break;
                    case "Loss_of_smell_or_taste": doctor="Otolaryngologist";
                        break;
                    case "Soar_throat": doctor="Otolaryngologist";
                        break;
                    case "cough": doctor="Otolaryngologist";
                        break;
                    case "shortness_of_breath": doctor="Pulmonologist";
                        break;
                    case "Feeling_tired": doctor="Physician";
                        break;
                    case "anxiety_score": doctor="Psychiatrist";
                        break;
                    case "depression_score": doctor="Psychiatrist";
                        break;
                }
                Double previous_value=0.0;
                boolean flag=false;
                for (int i=1;i<=5;i++)
                {
                    Cursor cursor = null;

                    try {
                        db.beginTransaction();

                        String query = "SELECT * FROM " + TableName + " WHERE recID = (SELECT "+String.valueOf(i)+" FROM " + TableName + ");";
                        cursor = db.rawQuery(query, null);

                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") long recID1 = cursor.getLong(cursor.getColumnIndex("recID"));
                            @SuppressLint("Range") String name1 = cursor.getString(cursor.getColumnIndex("name"));
                            @SuppressLint("Range") int age1 = cursor.getInt(cursor.getColumnIndex("age"));
                            @SuppressLint("Range") double y_variable = cursor.getDouble(cursor.getColumnIndex(selectedItem));
                            if((y_variable-previous_value)>2 & previous_value!=0.0)
                            {
                                flag=true;
                                break;
                            }
                            previous_value=y_variable;
                            Log.e("Variable", String.valueOf(y_variable));
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        db.endTransaction();
                    }
                }
                if(flag)
                {
                    Anomaly="This Vital has abnormal fluctuation, please visit a "+doctor;
                    textView.setText(Anomaly);
                    Cursor cursor = null;
                    try {
                        db.beginTransaction();

                        String query = "SELECT * FROM " + TableName + " WHERE recID = (SELECT 1 FROM " + TableName + ");";
                        cursor = db.rawQuery(query, null);

                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") String name1 = cursor.getString(cursor.getColumnIndex("name"));
                            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("emergency_contact"));
                            String message = "Hello! your friend"+name1+", is having abnormal health vitals, please check up on them";
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phone, null, message, null, null);
                                Toast.makeText(getApplicationContext(), "SMS sent to emergency contact!", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "SMS failed, please try again later.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            Log.e("Variable", phone);
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        db.endTransaction();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_anomaly.this, activity_maps.class);
                intent.putExtra("place", doctor);
                startActivity(intent);
            }
        });
    }
}
