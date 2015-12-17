package com.akili.etc.triviacrashsaga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class OnboardingActivity extends SoloModeActivity {

    public enum OnboardingType{
        welcomeTo,
        welcomeTo2,
    }

    private FButton nextButton;
    private View backgroudnView;
    private OnboardingType onboardingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        showTitleAndBackOnly("Welcome");

        nextButton = (FButton)findViewById(R.id.nextButton);
        backgroudnView = findViewById(R.id.backgroundView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            onboardingType = (OnboardingType)extras.get("Type");
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClicked();
            }
        });
    }

    private void onNextClicked(){
        if(onboardingType == OnboardingType.welcomeTo) {
            backgroudnView.setBackground(getResources().getDrawable(R.drawable.background_onboard_2));
            onboardingType = OnboardingType.welcomeTo2;
        }else if(onboardingType == OnboardingType.welcomeTo2){
            Intent intent = new Intent(OnboardingActivity.this, OnboardindingCategorySelectionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        return;
    }
}
