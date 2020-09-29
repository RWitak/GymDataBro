package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
                .databaseBuilder(getApplicationContext(), GymBroDatabase.class, "gym_data")
                .createFromAsset("gymdata.db")
                .allowMainThreadQueries()
                .build();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    public void resumeWorkout(View view) {
        Intent intent = new Intent(this, WorkoutStepActivity.class);
        startActivity(intent);
    }
}
