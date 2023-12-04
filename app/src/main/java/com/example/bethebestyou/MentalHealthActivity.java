package com.example.bethebestyou;
//ADDED BY SUKRUTHI ANILKUMAR
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
//import com.example.project4.databinding.ActivityMainBinding;
import android.view.View;
import android.widget.Toast;

public class MentalHealthActivity extends AppCompatActivity {

    private Button btnAnxietyQuest;
    private Button btnDepressionQuest;

    private EditText AnxietyEditText;

    private EditText DepressionEditText;

    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentalhealthpage);

        btnAnxietyQuest = findViewById(R.id.btnAnxietyQuest);
        btnAnxietyQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.hiv.uw.edu/page/mental-health-screening/gad-7";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        btnDepressionQuest = findViewById(R.id.btnDepressionQuest);
        btnDepressionQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.hiv.uw.edu/page/mental-health-screening/phq-9";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        AnxietyEditText = findViewById(R.id.AnxietyEditText);
        DepressionEditText = findViewById(R.id.DepressionEditText);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {
            submitMentalHealthScore();
        });
    }

    private void submitMentalHealthScore() {
        Toast.makeText(this, "You have successfully submitted your scores!", Toast.LENGTH_SHORT).show();

    }




}



