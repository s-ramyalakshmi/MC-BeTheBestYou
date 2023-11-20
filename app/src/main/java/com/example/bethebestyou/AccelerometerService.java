package com.example.bethebestyou;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.ArrayList;
import java.util.Date;

public class AccelerometerService extends Service implements SensorEventListener {

    private SensorManager accSensorManagerr;
    private Sensor accelSensorr;
    private long startTimee;
    private ArrayList<Integer> xCoordinateVal = new ArrayList<>();
    private ArrayList<Integer> yCoordinateVal = new ArrayList<>();
    private ArrayList<Integer> zCoordinateVal = new ArrayList<>();

    @Override
    public void onCreate(){
        accSensorManagerr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensorr = accSensorManagerr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accSensorManagerr.registerListener(this, accelSensorr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimee = new Date().getTime();
        xCoordinateVal.clear();
        yCoordinateVal.clear();
        zCoordinateVal.clear();
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            int multiplier = 100;
            xCoordinateVal.add((int)(sensorEvent.values[0]*multiplier));
            yCoordinateVal.add((int)(sensorEvent.values[1]*multiplier));
            zCoordinateVal.add((int)(sensorEvent.values[2]*multiplier));

            if(new Date().getTime() - startTimee >= 45*1000){
                Log.i("log", "Accelerometer z coordinates size: "+ zCoordinateVal.size());
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                accSensorManagerr.unregisterListener(AccelerometerService.this);
                Log.i("Log", "Service stopping");

                //Broadcast accelerometer Z values
                Intent intent = new Intent("RespDataBroadcast");
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("accelValuesZ", zCoordinateVal);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(AccelerometerService.this).sendBroadcast(intent);
            }
        });
        thread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}