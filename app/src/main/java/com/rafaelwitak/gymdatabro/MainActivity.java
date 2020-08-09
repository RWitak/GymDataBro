package com.rafaelwitak.gymdatabro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.os.Bundle;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class MainActivity extends AppCompatActivity {
    public GymBroDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
                .databaseBuilder(getApplicationContext(), GymBroDatabase.class, "gym_data")
                .allowMainThreadQueries()
                .build();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
