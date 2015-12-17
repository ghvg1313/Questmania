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
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.rey.material.widget.Button;

import org.w3c.dom.Text;

public class ChallengeInstructionActivity extends AppCompatActivity {

    private TextView challengeText, winnerText, maskText, hintText;
    private ImageView challengeImage;
    private Button startButton;

    private CountDownTimer countDownTimer;
    private DonutProgress progressBar;

    private Challenge challenge;
    private int challengeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_instruction);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        challengeText = (TextView)findViewById(R.id.challengeInstruction);
        maskText = (TextView) findViewById(R.id.maskText);
        winnerText = (TextView) findViewById(R.id.winnerDescriptionText);
        challengeImage = (ImageView)findViewById(R.id.instructionImage);
        startButton = (Button)findViewById(R.id.startButton);
        progressBar = (DonutProgress)findViewById(R.id.progressBar);
        hintText = (TextView)findViewById(R.id.hintText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = CenterController.controller().personalChallenge.get(extras.get("categoryName")).get(extras.getInt("challengeIndex"));

        }else {
            challenge = CenterController.controller().partyGame.getNextChallenge();
            winnerText.setText(challenge.winnerStandard);
            challengeText.setText(challenge.challengeContent);
            maskText.setText(challenge.mask.get(PartyGame.categoryName));
            hintText.setText(challenge.preString);
        }
        challengeSeconds = challenge.timeInSeconds*1000;

        progressBar.setMax((int) challengeSeconds+500);
        progressBar.setProgress((int) challengeSeconds);

        winnerText.setText(challenge.winnerStandard);
        challengeText.setText(challenge.challengeContent);
        maskText.setText(challenge.mask.get(PartyGame.categoryName));
        hintText.setText(challenge.preString);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClicked();
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_instruction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void onStartClicked(){
        progressBar.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        countDownTimer = new CountDownTimer(challengeSeconds, 1) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
                progressBar.setPrefixText("");
                progressBar.setSuffixText("");
                progressBar.setTextSize(0);
                progressBar.setInnerBottomText("" + (millisUntilFinished / 1000) + "seconds");
            }

            public void onFinish() {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
                hintText.setText(challenge.afterString);

            }
        }.start();

        startButton.setText("Finish!");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChallenge();
            }
        });
    }

    private void finishChallenge(){
        Intent intent = new Intent(ChallengeInstructionActivity.this, ChooseWinerActivity.class);
        startActivity(intent);
        return;
    }
}
