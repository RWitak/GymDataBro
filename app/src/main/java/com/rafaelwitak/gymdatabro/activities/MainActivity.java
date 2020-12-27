/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;
    private IntentMaker intentMaker;
    private OnBackPressedCallback backPressedCallback;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intentMaker = new IntentMaker(this);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = GymBroDatabase.getDatabase(this);

        setStatusBarColor();
        setInfoText();
        this.backPressedCallback = getBackPressedCallback();
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

    @NonNull
    private OnBackPressedCallback getBackPressedCallback() {
        return new OnBackPressedCallback(false /* disabled by default */) {
            @Override
            public void handleOnBackPressed() {
                deactivateInfoBox();
            }
        };
    }

    private void setStatusBarColor() {
        // This must be set here because there is no ActionBar auto-setting the color
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private void setInfoText() {
        binding.mainInfoBoxText
                .setText(Html.fromHtml(getString(R.string.info_copyright)));
    }

    public void toggleInfo(View view) {
        if (binding.mainInfoBox.getVisibility() != View.VISIBLE) {
            activateInfoBox();
        }
        else {
            deactivateInfoBox();
        }
    }

    private void activateInfoBox() {
        View infoBox = binding.mainInfoBox;
        FloatingActionButton infoButton = binding.mainInfoButton;

        infoBox.setVisibility(View.VISIBLE);
        infoButton.setImageTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(
                                this,
                                R.color.colorLightBackground)));
        infoButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

        backPressedCallback.setEnabled(true);
    }

    private void deactivateInfoBox() {
        View infoBox = binding.mainInfoBox;
        FloatingActionButton infoButton = binding.mainInfoButton;

        infoBox.setVisibility(View.INVISIBLE);
        infoButton.setImageTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(
                                this,
                                R.color.colorPrimary)));
        infoButton.setImageResource(android.R.drawable.ic_dialog_info);

        backPressedCallback.setEnabled(false);
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
