package com.aztek_systems.belatrix.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aztek_systems.belatrix.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToShake(View view) {

        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }

    public void goToCompass(View view) {

        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }

    public void goToVerticalStepper(View view) {

        Intent intent = new Intent(this, VerticalStepperActivity.class);
        startActivity(intent);
    }

    public void goToHorizontalStepper(View view) {

        Intent intent = new Intent(this, HorizontalStepperActivity.class);
        startActivity(intent);
    }
}
