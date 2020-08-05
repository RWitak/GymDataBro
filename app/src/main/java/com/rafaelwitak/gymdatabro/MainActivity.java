package com.rafaelwitak.gymdatabro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.os.Bundle;

import com.rafaelwitak.gymdatabro.database.GymDatabase;

public class MainActivity extends AppCompatActivity {
    public GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
                .databaseBuilder(getApplicationContext(), GymDatabase.class, "gym_data")
                .allowMainThreadQueries()
                .build();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
