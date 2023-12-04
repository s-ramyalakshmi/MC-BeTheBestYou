package com.example.bethebestyou;

import static java.lang.Math.abs;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase db = UserInfoActivity.db;
    private Float respiratoryDataa = null, heartRateDataa = null;
    private Uri fileUri;
    private int scale = 6;
    private boolean heartRateProcIsRunningg = false;
    private boolean RespiratoryProcIsRunningg = false;
    private static final int VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button butn6 = (Button) findViewById(R.id.button6);

        if(!hasCameraa()){
            butn6.setEnabled(false);
        }
        butn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(heartRateProcIsRunningg == true) {

                } else {
                    startRecordingg();
                }
            }
        });

        Button butn1 = (Button) findViewById(R.id.button);
        TextView textView1 = (TextView) findViewById(R.id.textView3);

        butn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File mediaFile = new File(getExternalFilesDir(null), "myvideo.mp4");
                fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".provider", mediaFile);

                if(heartRateProcIsRunningg == true) {
                    Toast.makeText(MainActivity.this, "Heart Rate calculation Progress already in progress!",
                            Toast.LENGTH_SHORT).show();
                } else if (mediaFile.exists()) {
                    heartRateProcIsRunningg = true;
                    textView1.setText("Measuring");

                    System.gc();
                    Intent hInt = new Intent(MainActivity.this, HeartRateCalcService.class);
                    startService(hInt);

                } else {
                    Toast.makeText(MainActivity.this, "Please record a video first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button butn2 = (Button) findViewById(R.id.button2);
        TextView texView2 = (TextView) findViewById(R.id.textView2);

        butn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "pressed respiratory", Toast.LENGTH_SHORT).show();
                if(RespiratoryProcIsRunningg == true) {
                    Toast.makeText(MainActivity.this, "Respiratory Rate Calculation Progress already in progress!",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Please lay down and place the phone on your chest \nfor 45s", Toast.LENGTH_LONG).show();
                    RespiratoryProcIsRunningg = true;
                    texView2.setText("Measuring");

                    Intent respIntent = new Intent(MainActivity.this, AccelerometerService.class);
                    startService(respIntent);
                }
            }
        });

        Button butn3 = (Button) findViewById(R.id.button3);

        butn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Rate your symptoms according to their severity", Toast.LENGTH_SHORT).show();
                Intent mA2In = new Intent(view.getContext(), MainActivity2.class);
                startActivity(mA2In);
            }
        });
        Button butn4 = (Button) findViewById(R.id.button4);

        butn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "pressed upload", Toast.LENGTH_SHORT).show();

                if(heartRateDataa != null && respiratoryDataa != null){
                    addRespiratoryHeartData(heartRateDataa, respiratoryDataa);
                    Log.d("Log", "db entry added");
                    Toast.makeText(getApplicationContext(), "Heart Rate and Respiratory Rate added to DB", Toast.LENGTH_SHORT).show();
                }
                else if (heartRateDataa == null || respiratoryDataa == null){
                    addRespiratoryHeartData(heartRateDataa, respiratoryDataa);
                    Log.d("Log", "db entry added");
                    Toast.makeText(getApplicationContext(), "Added to DB. (One of the values is null)", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Null values found. Please measure heart rate and respiratory rate again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                float breathingRate;
                ArrayList<Integer> accelValuesZ;
                Bundle bundle = intent.getExtras();
                accelValuesZ = bundle.getIntegerArrayList("accelValuesZ");
                ArrayList<Integer> accelValuesXDenoised = denoise(accelValuesZ, 10);
                int  zeroCrossings = findPeak(accelValuesXDenoised);
                breathingRate = (zeroCrossings*60)/90;
                Log.i("log", "Respiratory rate" + breathingRate);
                texView2.setText(breathingRate + "");
                respiratoryDataa = breathingRate;
                Toast.makeText(MainActivity.this, "Respiratory rate calculated!", Toast.LENGTH_SHORT).show();
                RespiratoryProcIsRunningg = false;
                bundle.clear();
                System.gc();
            }
        }, new IntentFilter("RespDataBroadcast"));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                float heartRatee = 0;
                ArrayList<Integer> heartDataa = null;
                heartDataa = b.getIntegerArrayList("heartData");
                if(heartDataa == null){
                    Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                }
                Log.i("log", "heartData size: "+heartDataa.size());
                ArrayList<Integer> denoisedRednes = denoise(heartDataa, 2);
                float zeroCrossing = findPeak(denoisedRednes);
                heartRatee += zeroCrossing/2;
                Log.i("log", "heart rate: " + zeroCrossing/2);
                heartRatee = (heartRatee*scale)*12/9;
                Log.i("log", "Heart rate: " + heartRatee);
                textView1.setText(heartRatee + "");
                heartRateDataa = heartRatee;
                heartRateProcIsRunningg = false;
                Toast.makeText(MainActivity.this, "Heart rate calculated!", Toast.LENGTH_SHORT).show();
                System.gc();
                b.clear();

            }
        }, new IntentFilter("broadcastingHeartData"));

        Button butn7 = (Button) findViewById(R.id.button7);

        butn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mA2In = new Intent(view.getContext(), MainActivity2.class);
                startActivity(mA2In);
            }
        });

    }

    public void startRecordingg()
    {
        File mediaFile = new File(getExternalFilesDir(null), "myvideo.mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,45);
        fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".provider", mediaFile);
        Log.d("", fileUri.getPath().toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    private boolean hasCameraa() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Integer> denoise(ArrayList<Integer> data, int filter){
        ArrayList<Integer> movingAvgArr = new ArrayList<>();
        int movingAvg = 0;
        for(int i=0; i< data.size(); i++){
            movingAvg += data.get(i);
            if(i+1 < filter) {
                continue;
            }
            movingAvgArr.add((movingAvg)/filter);
            movingAvg -= data.get(i+1 - filter);
        }
        return movingAvgArr;
    }

    public int findPeak(ArrayList<Integer> data) {
        int diff, prev, slope = 0, zeroCrossings = 0;
        int j = 0;
        prev = data.get(0);
        while(slope == 0 && j + 1 < data.size()){
            diff = data.get(j + 1) - data.get(j);
            if(diff != 0){
                slope = diff/abs(diff);
            }
            j++;
        }

        for(int i = 1; i<data.size(); i++) {
            diff = data.get(i) - prev;
            prev = data.get(i);
            if(diff == 0) continue;
            int currSlope = diff/abs(diff);
            if(currSlope == -1* slope){
                slope *= -1;
                zeroCrossings++;
            }
        }
        return zeroCrossings;
    }

    public void addRespiratoryHeartData(Float heartData, Float respData){
        if(heartData == null) heartData = Float.parseFloat("0");
        if(respData == null) respData = Float.parseFloat("0");
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("heart_rate", heartData);
            values.put("respiratory_rate", respData);
            db.execSQL( "update "+UserInfoActivity.TableName+" set heart_rate='"+heartData+"', respiratory_rate='"+respData+"' where recID = (SELECT MAX(recID) FROM "+UserInfoActivity.TableName+")" );
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("", "SQliteException" + e.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }

}