package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.QuizStartActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import extension.PartyModeActivity;
import extension.ResourceTool;
import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class ChallengePersonalActivity extends PartyModeActivity {

    FButton doneButton;
    View backgroundView;
    TextView playerNameTextView, questionTextView;
    Challenge challenge;

    static int challengeSeconds = 60 * 1000;

    int playerTurn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_personal);

        getWidget();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = (Challenge)extras.get("Challenge");
        }

        setView(challenge);
    }

    private void getWidget(){
        doneButton = (FButton)findViewById(R.id.doneButton);
        playerNameTextView = (TextView)findViewById(R.id.playerNameTextView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
        backgroundView = findViewById(R.id.backgroundView);
    }

    private void setView(Challenge challenge){
        showTitleAndBackOnly(challenge.challengeTitle);
        this.challenge = challenge;
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishActivity();
            }
        });
        if(challenge.moderatoTurnText != null){
            playerTurn = -1;
            questionTextView.setText(challenge.moderatoTurnText);
        }else {
            playerTurn = 0;
            if (playerTurn == PartyGame.moderatorIndex) {
                playerTurn++;
            }
            questionTextView.setText(challenge.challengeQuestion);
        }

        if(challenge.gameType == Challenge.GameType.TIMER){
            doneButton.setVisibility(View.INVISIBLE);
            CountDownTimer countDownTimer = new CountDownTimer(challengeSeconds, 1) {
                public void onTick(long millisUntilFinished) {
                    onTicking(millisUntilFinished);
                }

                public void onFinish() {
                    doneButton.setVisibility(View.VISIBLE);
                    onFinishActivity();
                }
            }.start();
        }

        showTitleAndBackOnly(challenge.challengeTitle);
        changeNavigationBarColor(challenge.categoryType, backgroundView);
        backgroundView.setBackground(ResourceTool.getBackGroundImage(this, challenge.categoryType));
    }

    private void onTicking(long millisUntilFinished){
        questionTextView.setTextSize(35.0f);
        questionTextView.setText(this.challenge.challengeQuestion + "\n" + (millisUntilFinished / 1000) + "s left");
    }

    private void onFinishActivity()
    {
//        questionTextView.setText(challenge.challengeStandard);
        Intent intent = new Intent(ChallengePersonalActivity.this, PersonalChallengeForumActivity.class);
        intent.putExtra("Challenge", challenge);
        startActivity(intent);
//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goBack();
//            }
//        });
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
            dialogBuilder
                    .withTitle("Quit")
                    .withMessage("Are you sure you want to quit?")
                    .withButton1Text("OK")
                    .withButton2Text("Cancel")
                    .withDialogColor("#ffffff")
                    .withMessageColor("#000000")
                    .withTitleColor("#000000")
                    .withButtonDrawable(R.color.navigation_color)
                    .isCancelableOnTouchOutside(false)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ChallengePersonalActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
