package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.QuizStartActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.akili.etc.triviacrashsaga.Singleton.ProfileController;

import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class PersonalChallengeForumActivity extends SoloModeActivity {

    Challenge challenge;
    private FButton doneButton, cancelButton;
    private TextView titleTextView, standardTextView, feelTextView;
    private EditText feelingEditText;

    private String feelings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_challenge_forum);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = (Challenge)extras.get("Challenge");
        }
        showTitleAndBackOnly(challenge.challengeTitle);

        getViews(challenge);
    }

    private void getViews(Challenge challenge)
    {
        standardTextView = (TextView)findViewById(R.id.standardTextView);
        feelTextView = (TextView)findViewById(R.id.feelTextView);
        feelingEditText = (EditText)findViewById(R.id.feelingEditText);
        titleTextView = (TextView)findViewById(R.id.challengeTitle);

        doneButton = (FButton)findViewById(R.id.completeButton);
        cancelButton = (FButton) findViewById(R.id.cancelButton);

        titleTextView.setText("You've competed "+challenge.challengeTitle);
        standardTextView.setText(challenge.challengeStandard);
        feelTextView.setText(challenge.challengeTitle);

        feelingEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChallenge(true);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChallenge(false);
            }
        });
    }

    private void finishChallenge(Boolean finished)
    {
//        String categoryName = "";
//        if(challenge.challengeTitle.equals("Small Talk")){
//            categoryName = "Teacher";
//        }else if(challenge.challengeTitle.equals("Know your Body")){
//            categoryName = "Doctor";
//        }else if(challenge.challengeTitle.equals("One Word Story")){
//            categoryName = "Writer";
//        }
//        if(finished) {
//            CenterController.controller().completeChallenge(this,categoryName);
//            BackGroundController.playWinBGM(this);
//        }
        goBack(finished);
    }

    private void goBack(Boolean finished)
    {
        ProfileController.addToJournalist("You completed "+challenge.challengeTitle, feelingEditText.getText().toString());
        BackGroundController.stop();
        Intent intent = new Intent(this, QuizStartActivity.class);
        intent.putExtra("categoryName", AchievementSystem.categoryNameFromType(challenge.categoryType));
        intent.putExtra("Unlock", finished);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        goBack(false);
    }
}
