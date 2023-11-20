package com.example.bethebestyou;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class UserInfoActivity extends AppCompatActivity {

    static SQLiteDatabase db;
    static String TableName;
    private EditText editTextName, editTextAge, editTextHeight, editTextWeight, editTextEmergencyContact;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {
            submitUserInfo();
        });
    }

    private void submitUserInfo() {
        String name = editTextName.getText().toString();
        String ageStr = editTextAge.getText().toString();
        String heightStr = editTextHeight.getText().toString();
        String weightStr = editTextWeight.getText().toString();
        String emergencyContact = editTextEmergencyContact.getText().toString();
        if (isValidField(name) && isValidField(ageStr) && isValidField(heightStr)
                && isValidField(weightStr)) {
            if (isValidEmergencyContact(emergencyContact)) {
                int age = Integer.parseInt(ageStr);
                double height = Double.parseDouble(heightStr);
                double weight = Double.parseDouble(weightStr);
                double bmi = calculateBMI(height, weight);
                insertUserDetails(name, age, height, weight, emergencyContact, bmi);
            } else {
                Toast.makeText(this, "Emergency contact must be exactly 10 digits.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in all mandatory fields correctly.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isValidField(String value) {
        return !value.trim().isEmpty();
    }

    private boolean isValidEmergencyContact(String emergencyContact) {
        return isValidField(emergencyContact) && emergencyContact.matches("\\d{10}");
    }

    private double calculateBMI(double height, double weight) {
        return weight / ((height / 100) * (height / 100));
    }

    private void createTableIfNotExists() {
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name=?", new String[]{TableName});
            if (cursor != null) {
                cursor.moveToFirst();
                int tableCount = cursor.getInt(0);
                cursor.close();

                if (tableCount == 0) {
                    // Table doesn't exist, create it
                    db.execSQL("CREATE TABLE " + TableName + " (" +
                            " recID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " name TEXT, " +
                            " age INTEGER, " +
                            " height REAL, " +
                            " weight REAL, " +
                            " emergency_contact TEXT, " +
                            " bmi REAL, " +
                            " heart_rate REAL, " +
                            " respiratory_rate REAL, " +
                            " Nausea REAL, " +
                            " Headache REAL, " +
                            " Diarrhea REAL, " +
                            " Soar_throat REAL, " +
                            " Fever REAL, " +
                            " Muscle_Ache REAL, " +
                            " Loss_of_smell_or_taste REAL, " +
                            " cough REAL, " +
                            " shortness_of_breath REAL, " +
                            " Feeling_tired REAL);");
                }
            }
        } catch (SQLiteException e) {
            Log.i("", e.getMessage().toString());
        }
    }

    private void insertUserDetails(String name, int age, double height, double weight, String emergencyContact, double bmi) {
        try {
            File f = new File(getExternalFilesDir(null), "ramya.db");

            TableName = "User_Record";
            db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);
            db.beginTransaction();
            try {
                // Create the table if it doesn't exist
                createTableIfNotExists();

                // Insert user details into the table
                String insertQuery = "INSERT INTO " + TableName +
                        " (name, age, height, weight, emergency_contact, bmi) VALUES (?, ?, ?, ?, ?, ?)";
                db.execSQL(insertQuery, new Object[]{name, age, height, weight, emergencyContact, bmi});

                db.setTransactionSuccessful();
            } catch (SQLiteException e) {
                Log.i("", e.getMessage().toString());
            } finally {
                db.endTransaction();
            }
        } catch (SQLException e) {
            Log.i("", e.getMessage().toString());
        }
    }

}