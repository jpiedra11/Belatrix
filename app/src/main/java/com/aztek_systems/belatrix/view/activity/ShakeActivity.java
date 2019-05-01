package com.aztek_systems.belatrix.view.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

import com.aztek_systems.belatrix.R;

public class ShakeActivity extends Activity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetSensor;

    boolean downXCompleted = false;
    boolean upXCompleted = false;

    boolean downYCompleted = false;
    boolean upYCompleted = false;

    boolean downZCompleted = false;
    boolean upZCompleted = false;

    boolean isFirstTime = true;

    float first_x;
    float first_y;
    float first_z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_ACCELEROMETER);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {

            float current_x = event.values[SensorManager.DATA_X];
            float current_y = event.values[SensorManager.DATA_Y];
            float current_z = event.values[SensorManager.DATA_Z];

            if (isFirstTime) {

                first_x = current_x;
                first_y = current_y;
                first_z = current_z;

                isFirstTime = false;
            } else {

                if (current_x - first_x < -1) {

                    downXCompleted = true;
                }

                if (current_x - first_x > 1) {

                    upXCompleted = true;
                }

                if (current_y - first_y < -1) {

                    downZCompleted = true;
                }

                if (current_y - first_y > 1) {

                    upYCompleted = true;
                }

                if (current_z - first_z < -1) {

                    downZCompleted = true;
                }

                if (current_z - first_z > 1) {

                    upZCompleted = true;
                }

                if ((upXCompleted && downXCompleted) || (upYCompleted && downYCompleted) || (upZCompleted && downZCompleted)) {

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    if (vibrator.hasVibrator()) {

                        vibrator.vibrate(100);

                        downXCompleted = false;
                        upXCompleted = false;

                        downYCompleted = false;
                        upYCompleted = false;

                        downZCompleted = false;
                        upZCompleted = false;

                        isFirstTime = true;
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
