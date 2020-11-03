package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = GymBroDatabase.getDatabase(this);

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void resumeWorkout(View view) {
        Intent intent = new Intent(this, WorkoutStepActivity.class);
        startActivity(intent);
    }

    public void chooseProgram(View view) {
        Intent intent = new Intent(this, ChooseProgramActivity.class);
        startActivity(intent);
    }
}
