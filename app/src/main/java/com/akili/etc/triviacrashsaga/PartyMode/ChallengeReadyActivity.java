package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.rey.material.widget.Button;

import extension.PartyModeActivity;
import extension.SoloModeActivity;
import extension.StringTool;

public class ChallengeReadyActivity extends PartyModeActivity {

    TextView normalTextView, extraTExtView, topicLabel;
    LinearLayout backgroundLayout;

    Challenge challenge;

    private DonutProgress progressBar;
    static int challengeSeconds = 10 * 1000;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_ready);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        getWidgets();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = (Challenge)extras.get("Challenge");
        }

        showTitle(challenge.challengeTitle);
        setView(challenge);
        startTimer();
    }

    private void getWidgets() {
        normalTextView = (TextView) findViewById(R.id.normalTextView);
        extraTExtView = (TextView) findViewById(R.id.extraTextView);
        topicLabel = (TextView) findViewById(R.id.topicLabel);
        progressBar = (DonutProgress)findViewById(R.id.progressBar);
        backgroundLayout = (LinearLayout)findViewById(R.id.challengeBackground);
    }


    private void setView(Challenge challenge){
        this.challenge = challenge;

        if(challenge.gameType == Challenge.GameType.PLAYER_TURN) {
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.challenge_background_1));
            normalTextView.setText(StringTool.preparePartyText(challenge.challengeQuestion));
            extraTExtView.setText(StringTool.preparePartyText(challenge.challengePrepareString));
        } else if(challenge.gameType == Challenge.GameType.PERSONAL || challenge.gameType == Challenge.GameType.TIMER){
            challengeSeconds = 5 * 1000;
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.background_challenge_solo));
            normalTextView.setText(StringTool.preparePartyText(challenge.challengePrepareString));
            topicLabel.setVisibility(View.INVISIBLE);
            extraTExtView.setText("");
        } else if(challenge.gameType == Challenge.GameType.TEXT_IMAGE_DISQUALIFY){
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.challenge_background_2));
            normalTextView.setText(StringTool.preparePartyText(challenge.challengePrepareString));
            topicLabel.setVisibility(View.INVISIBLE);
            extraTExtView.setText("");
        }

        progressBar.setMax((int) challengeSeconds + 10);
        progressBar.setProgress((int) challengeSeconds);

        showTitle(challenge.challengeTitle);
        hideActionButtons();
        changeNavigationBarColor(challenge.categoryType, backgroundLayout);
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(challengeSeconds, 1) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
                progressBar.setPrefixText("");
                progressBar.setSuffixText("");
                progressBar.setTextSize(0);
                progressBar.setInnerBottomText("" + (millisUntilFinished / 1000) + "s");
            }

            public void onFinish() {
                progressBar.setProgress(0);
                goToChallenge();

            }
        }.start();
    }

    private void goToChallenge(){
        if(challenge.gameType == Challenge.GameType.PLAYER_TURN) {
            Intent intent = new Intent(ChallengeReadyActivity.this, ChallengePlayerTurnActivity.class);
            intent.putExtra("Challenge", challenge);
            startActivity(intent);
        }else if(challenge.gameType == Challenge.GameType.TEXT_IMAGE_DISQUALIFY){
            Intent intent = new Intent(ChallengeReadyActivity.this, ChallengeDisqualifyActivity.class);
            intent.putExtra("Challenge", challenge);
            startActivity(intent);
        }else if(challenge.gameType == Challenge.GameType.PERSONAL || challenge.gameType == Challenge.GameType.TIMER){
            Intent intent = new Intent(ChallengeReadyActivity.this, ChallengePersonalActivity.class);
            intent.putExtra("Challenge", challenge);
            startActivity(intent);
        }

        if(challenge.gameType != Challenge.GameType.PERSONAL && challenge.gameType != Challenge.GameType.TIMER){
            BackGroundController.playChallengeBGM(this);
        }
    }
    
    @Override
    public void onBackPressed()
    {
        return;
    }
}
