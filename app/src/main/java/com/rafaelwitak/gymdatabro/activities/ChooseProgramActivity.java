/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.programHandling.ChooseProgramAdapter;

public class ChooseProgramActivity extends AppCompatActivity {
    GymBroDatabase database;
    Toolbar chooseProgramToolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = GymBroDatabase.getDatabase(this);
        setContentView(R.layout.activity_choose_program);
        chooseProgramToolbar = findViewById(R.id.choose_program_toolbar);
        recyclerView = findViewById(R.id.choose_program_recycler);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    private void setupViews() {
        setupToolbar();
        setupRecyclerView();
        setupFab();
    }

    private void setupFab() {
        FloatingActionButton fabAdd = findViewById(R.id.choose_program_fab_add);
        fabAdd.setOnClickListener(v ->
                startActivity(new IntentMaker(getBaseContext()).getCreateProgramIntent()));
    }

    private void setupToolbar() {
        chooseProgramToolbar.setTitle(R.string.choose_program_toolbar_title);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        this,
                        RecyclerView.VERTICAL,
                        false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ChooseProgramAdapter(database.masterDao().getAllPrograms()));
    }
}