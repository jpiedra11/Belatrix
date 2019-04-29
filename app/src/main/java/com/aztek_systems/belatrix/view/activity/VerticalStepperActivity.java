package com.aztek_systems.belatrix.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aztek_systems.belatrix.R;
import com.aztek_systems.belatrix.view.control.Stepper;

public class VerticalStepperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepper);
    }

    public void previous(View view) {

        Stepper stepper = findViewById(R.id.stepper);
        stepper.setCurrentIndex(stepper.getCurrentIndex() - 1);
    }

    public void next(View view) {

        Stepper stepper = findViewById(R.id.stepper);
        stepper.setCurrentIndex(stepper.getCurrentIndex() + 1);
    }

    public void complete(View view) {

        Stepper stepper = findViewById(R.id.stepper);
        stepper.completeStep(stepper.getCurrentIndex());
    }
}
