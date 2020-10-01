package com.rafaelwitak.gymdatabro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.viewRows.ChooseProgramRow;

public class ChooseProgramActivity extends AppCompatActivity {
    GymBroDatabase database;
    LinearLayout programList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = MainActivity.database;
        setContentView(R.layout.activity_choose_program);
        programList = findViewById(R.id.program_list);

        setupRows();
    }

    private void setupRows() {
        for (Program program : database.programDAO().getAllPrograms()) {
            ChooseProgramRow chooseProgramRow = new ChooseProgramRow(this);
            chooseProgramRow.setTextViewText(getProgramName(program));
            programList.addView(chooseProgramRow);
        }
    }

    private String getProgramName(Program program) {
        return program.name;
    }
}