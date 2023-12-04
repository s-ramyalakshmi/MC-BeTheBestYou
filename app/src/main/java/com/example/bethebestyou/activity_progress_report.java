package com.example.bethebestyou;

import static com.example.bethebestyou.MainActivity.db;
import static com.example.bethebestyou.UserInfoActivity.TableName;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class activity_progress_report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report);
        Spinner spinner = findViewById(R.id.spinner);
        LineChart lineChart = new LineChart(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        FrameLayout chartContainer = findViewById(R.id.chart_container);

        String[] columns = {"recID", "name", "age", "height", "weight", "emergency_contact", "bmi",
                "heart_rate", "respiratory_rate", "Nausea", "Headache", "Diarrhea", "Soar_throat",
                "Fever", "Muscle_Ache", "Loss_of_smell_or_taste", "cough", "shortness_of_breath",
                "Feeling_tired", "anxiety_score", "depression_score"};
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        lineChart.setLayoutParams(layoutParams);

        // Add the LineChart to the FrameLayout
        chartContainer.addView(lineChart);

        // Create sample data points (x, y)
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 0)); // Sample value 1
        entries.add(new Entry(2, 0)); // Sample value 2
        entries.add(new Entry(3, 0)); // Sample value 3
        entries.add(new Entry(4, 0)); // Sample value 4
        entries.add(new Entry(5, 0)); // Sample value 5

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
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
                            for (Entry entry : entries) {
                                if (entry.getX() == i) { // Check for the desired X-axis value (1 in this case)
                                    entry.setY((float) y_variable); // Update the Y-axis value to 30
                                    break; // Exit the loop after updating the entry
                                }
                            }
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
                // Create a dataset from the entries
                LineDataSet dataSet = new LineDataSet(entries, "Sample Data");
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.BLACK);

                // Create a LineData object from the dataset
                LineData lineData = new LineData(dataSet);
                lineChart.setBackgroundColor(Color.WHITE); // Set background color of the chart to white

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Label1", "Label2", "Label3", "Label4", "Label5"}));
                xAxis.setGranularity(1f);
                xAxis.setTextColor(Color.BLACK); // Set X-axis label text color

                YAxis yAxisLeft = lineChart.getAxisLeft();
                yAxisLeft.setTextColor(Color.BLACK); // Customize Y-axis text color
                yAxisLeft.setValueFormatter(new LargeValueFormatter());

                YAxis yAxisRight = lineChart.getAxisRight();
                yAxisRight.setEnabled(false);

                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.BLACK);

                // Set LineData to the chart
                lineChart.setData(lineData);

                // Refresh the chart
                lineChart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected
            }
        });


        /*Cursor cursor = null;

        try {
            db.beginTransaction();

            String query = "SELECT * FROM " + TableName + " WHERE recID = (SELECT MAX(recID) FROM " + TableName + ");";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") long recID1 = cursor.getLong(cursor.getColumnIndex("recID"));
                @SuppressLint("Range") String name1 = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int age1 = cursor.getInt(cursor.getColumnIndex("age"));
                @SuppressLint("Range") float y_variable = cursor.getColumnIndex("weight");
                Log.e("Variable", String.valueOf(y_variable));
            }
            db.setTransactionSuccessful();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }*/


        // Create a dataset from the entries
        LineDataSet dataSet = new LineDataSet(entries, "Sample Data");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        // Create a LineData object from the dataset
        LineData lineData = new LineData(dataSet);
        lineChart.setBackgroundColor(Color.WHITE); // Set background color of the chart to white

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Label1", "Label2", "Label3", "Label4", "Label5"}));
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK); // Set X-axis label text color

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.BLACK); // Customize Y-axis text color
        yAxisLeft.setValueFormatter(new LargeValueFormatter());

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        // Set LineData to the chart
        lineChart.setData(lineData);

        // Refresh the chart
        lineChart.invalidate();
    }



}
