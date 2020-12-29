/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.programHandling.ChooseProgramRow;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChooseProgramActivity extends AppCompatActivity {
    GymBroDatabase database;
    LinearLayout programList;
    Toolbar chooseProgramToolbar;
//    FloatingActionButton createProgramFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = GymBroDatabase.getDatabase(this);
        setContentView(R.layout.activity_choose_program);
        chooseProgramToolbar = findViewById(R.id.choose_program_toolbar);
        // createProgramFAB = findViewById(R.id.choose_program_button_create);
        programList = findViewById(R.id.program_list);

        setupViews();
    }

    private void setupViews() {
        setupToolbar();
        setupRows();
//        setupFAB(this);
    }

/*
    private void setupFAB(Context context) {
        createProgramFAB.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditProgramActivity.class);
            startActivity(intent);
        });
    }
*/

    private void setupToolbar() {
        chooseProgramToolbar.setTitle(R.string.choose_program_toolbar_title);
    }

    private void setupRows() {
        for (Program program : database.programDAO().getAllPrograms()) {
            ChooseProgramRow chooseProgramRow = new ChooseProgramRow(this);
            chooseProgramRow.setTextViewText(getProgramName(program));
            chooseProgramRow.setOnClickListener(getRowOnClickListener(program));
            chooseProgramRow.setOnLongClickListener(getRowOnLongClickListener(program));
            programList.addView(chooseProgramRow);
        }
    }

    private View.OnClickListener getRowOnClickListener(Program program) {
        return view -> {
            startActivity(new IntentMaker(this).getIntentToResumeProgram(program.id));
            finishAndRemoveTask();
        };
    }

    private View.OnLongClickListener getRowOnLongClickListener(Program program) {
        return view -> {
            Intent intent = new Intent(getApplicationContext(), EditProgramActivity.class);
            intent.putExtra("programID", program.id);
            startActivity(intent);
            return true;
        };
    }

    private String getProgramName(Program program) {
        return program.name;
    }
}