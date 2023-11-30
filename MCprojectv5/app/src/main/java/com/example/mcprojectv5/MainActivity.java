package com.example.mcprojectv5;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ArrayList<Float> columnData = new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double respiratoryRate=0.0f;
    private boolean isCollectingData = false;
    private long dataCollectionDuration = 45000L;  //45 milliseconds
    private Handler stopHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String videoUri = getVideoUriFromAssets(this,"sample.mp4");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Button btnHRating=findViewById(R.id.btn3);
        btnHRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ContentResolver contentResolver = getContentResolver();
                //String HRate=convertMediaUriToPath(videoUri,contentResolver);
                ExecuteThread(videoUri);
                Log.e("Heart rate:","Heart rate calc has started");
            }
        });
        Button btnOpenSymptoms = findViewById(R.id.btn1);
        btnOpenSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_symptoms.class);
                intent.putExtra("HeartRate",70f);
                intent.putExtra("RespiratoryRate",respiratoryRate);
                startActivity(intent);
            }
        });
        Button btnLRating=findViewById(R.id.btn4);
        btnLRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDataCollection();
                Log.e("check", "Button has begun");
                calculateRespiratoryRating();
            }
        });
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        columnData.add(x);
        columnData.add(y);
        columnData.add(z);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    private void startDataCollection() {
        if (!isCollectingData) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            isCollectingData = true;
            stopHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopDataCollection();
                }
            }, dataCollectionDuration);
        }
    }

    private void stopDataCollection() {
        if (isCollectingData) {
            sensorManager.unregisterListener(this);
            isCollectingData = false;
        }
    }
    private void calculateRespiratoryRating(){
        float previousValue = 0f;
        float currentValue = 0f;
        previousValue = 10f;
        int k = 0;
        for (int i = 0; i <columnData.size()/3; i++) {
            currentValue = (float) Math.sqrt(
                    Math.pow(columnData.get(3*i), 2.0) +
                            Math.pow(columnData.get(3*i+1), 2.0) +
                            Math.pow(columnData.get(3*i+2), 2.0)
            );
            if (Math.abs(previousValue - currentValue) > 0.15) {
                k++;
            }
            previousValue = currentValue;
        }
        Double ret = (double) (k/45);
        respiratoryRate=ret*30;
        String message=Double.toString(ret*30);
        Log.e("check", message);
    }
    public String getVideoUriFromAssets(Context context, String videoFileName) {
        AssetFileDescriptor assetFileDescriptor = null;
        Log.e("in GetVideoUri", "getVideoUriFromAssets: " );
        try {
            assetFileDescriptor = context.getAssets().openFd(videoFileName);
            String name="file://" + assetFileDescriptor.getFileDescriptor();
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convertMediaInBackground(String uriString) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Log.e("check", "In Thread execution");
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() {
                return performConversion(uriString);
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }

    private String performConversion(String uriString) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        List<Bitmap> frameList = new ArrayList<>();
        Log.e("inside perform conversion","inside performance conversion" );
        try {
            retriever.setDataSource(uriString);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT);
            int aduration = Integer.parseInt(duration);
            int i = 10;
            while (i < aduration) {
                Bitmap bitmap = retriever.getFrameAtIndex(i);
                frameList.add(bitmap);
                i += 5;
            }
            retriever.release();
        } catch (Exception e) {}
        finally {
            long redBucket = 0;
            long pixelCount = 0;
            List<Long> a = new ArrayList<>();

            for (Bitmap frame : frameList) {
                redBucket = 0;
                for (int y = 550; y < 650; y++) {
                    for (int x = 550; x < 650; x++) {
                        int c = frame.getPixel(x, y);
                        pixelCount++;
                        redBucket += Color.red(c) + Color.blue(c) + Color.green(c);
                    }
                }
                a.add(redBucket);
            }

            List<Long> b = new ArrayList<>();
            for (int i = 0; i < a.size() - 5; i++) {
                long temp = (a.get(i) + a.get(i + 1) + a.get(i + 2) + a.get(i + 3) + a.get(i + 4)) / 4;
                b.add(temp);
            }

            long x = b.get(0);
            int count = 0;
            for (int i = 1; i < b.size() - 1; i++) {
                long p = b.get(i);
                if ((p - x) > 3500) {
                    count++;
                }
                x = b.get(i);
            }
            int rate = (int) ((count * 60.0) / 45.0);
            Log.e(String.valueOf(rate/2), "Output message check);");
            return String.valueOf(rate / 2);
        }
    }
    public String ExecuteThread(String fName){
        String output = convertMediaInBackground(fName);
        return output;
    }
}