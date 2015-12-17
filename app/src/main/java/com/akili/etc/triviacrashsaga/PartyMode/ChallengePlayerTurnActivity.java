package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rey.material.widget.Button;

import org.w3c.dom.Text;

import extension.StringTool;
import info.hoang8f.widget.FButton;

public class ChallengePlayerTurnActivity extends AppCompatActivity {

    FButton doneButton;
    TextView playerNameTextView, titleTextView, questionTextView;
    Challenge challenge;

    int playerTurn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_player_turn);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));
        getWidget();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = (Challenge)extras.get("Challenge");
        }

        setView(challenge);
    }

    private void getWidget(){
        doneButton = (FButton)findViewById(R.id.doneButton);
        titleTextView = (TextView)findViewById(R.id.titleText);
        playerNameTextView = (TextView)findViewById(R.id.playerNameTextView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
    }

    private void setView(Challenge challenge){
        this.challenge = challenge;
        titleTextView.setText(challenge.challengeTitle);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTurn();
            }
        });
        if(challenge.moderatoTurnText != null){
            playerTurn = -1;
            playerNameTextView.setText(PartyGame.getModerator().name);
            questionTextView.setText(StringTool.preparePartyText(challenge.moderatoTurnText));
        }else {
            playerTurn = 0;
            if (playerTurn == PartyGame.moderatorIndex) {
                playerTurn++;
            }
            playerNameTextView.setText(PartyGame.players.get(playerTurn).name);
            questionTextView.setText(StringTool.preparePartyText(challenge.challengeQuestion));
        }
    }

    private void nextTurn()
    {
        questionTextView.setText(StringTool.preparePartyText(challenge.challengeQuestion));
        playerTurn++;
        int turnMax =this.challenge.moderatoTurnText==null?2:1;
        if(playerTurn>=turnMax*PartyGame.players.size()){
            Intent intent = new Intent(ChallengePlayerTurnActivity.this, ChooseWinerActivity.class);
            intent.putExtra("Challenge", challenge);
            startActivity(intent);
        }else{
            if(playerTurn%PartyGame.players.size() == PartyGame.moderatorIndex){
                nextTurn();
            }else {
                YoYo.with(Techniques.FadeOutRight)
                        .duration(500)
                        .playOn(playerNameTextView);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //any delayed code
                        playerNameTextView.setText(PartyGame.players.get(playerTurn%PartyGame.players.size()).name);
                    }
                }, 510);
                YoYo.with(Techniques.FadeInLeft)
                        .duration(500)
                        .delay(550)
                        .playOn(playerNameTextView);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        return;
    }
}
