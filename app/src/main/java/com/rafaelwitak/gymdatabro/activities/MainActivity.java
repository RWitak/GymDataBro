package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;
    private IntentMaker intentMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intentMaker = new IntentMaker(this);
        setContentView(R.layout.activity_main);

        database = GymBroDatabase.getDatabase(this);

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void resumeWorkoutOrRedirect(View view) {
        Intent intent = intentMaker.getIntentToResumeWorkoutOrChooseProgram();
        startActivity(intent);
    }

    public void chooseProgram(View view) {
        Intent intent = intentMaker.getChooseProgramIntent();
        startActivity(intent);
    }

    public void addExercise(View view) {
        Intent intent = intentMaker.getEditExerciseIntent();
        startActivity(intent);
    }

    public void addWorkoutStep(View view) {
        Intent intent = intentMaker.getEditWorkoutStepIntent();
        startActivity(intent);
    }


}
