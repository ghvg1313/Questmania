package com.akili.etc.triviacrashsaga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.akili.etc.triviacrashsaga.Singleton.CenterController;

import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class OnBoardingFinishActivity extends SoloModeActivity {

    private FButton thanksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_finish);

        thanksButton = (FButton)findViewById(R.id.thanksButton);

        thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterController.controller().firstOnboardingShown = true;
                CenterController.controller().saveData();
                Intent intent = new Intent(OnBoardingFinishActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        showTitleAndBackOnly("Good luck");
    }
}
