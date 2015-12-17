package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rey.material.widget.Button;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class ChallengeDisqualifyActivity extends AppCompatActivity {

    FButton player1Button, player2Button, player3Button, player4Button,player5Button,player6Button;
    FButton resetButton;
    FButton[] playerButtons;
    TextView titleTextView, standardTextView;
    Challenge challenge;
    LinearLayout buttonArea1, buttonArea2;

    ArrayList<Integer> remainingPlayers;
    ArrayList<Integer> deadPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_disqualify);
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
        player1Button = (FButton)findViewById(R.id.player1Button);
        player2Button = (FButton)findViewById(R.id.player2Button);
        player3Button = (FButton)findViewById(R.id.player3Button);
        player4Button = (FButton)findViewById(R.id.player4Button);
        player5Button = (FButton)findViewById(R.id.player5Button);
        player6Button = (FButton)findViewById(R.id.player6Button);
        resetButton = (FButton)findViewById(R.id.resetButton);
        titleTextView = (TextView)findViewById(R.id.titleText);
        standardTextView = (TextView)findViewById(R.id.standardTextView);
        buttonArea1 = (LinearLayout)findViewById(R.id.buttonArea1);
        buttonArea2 = (LinearLayout)findViewById(R.id.buttonArea2);

        playerButtons = new FButton[]{player1Button, player2Button, player3Button, player4Button, player5Button, player6Button};
    }

    private void setView(Challenge challenge){
        this.challenge = challenge;
        titleTextView.setText(challenge.challengeTitle);
        standardTextView.setText(challenge.challengeStandard);
        remainingPlayers = new ArrayList<>();
        deadPlayers = new ArrayList<>();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtons();
            }
        });

        for(FButton button : playerButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disqualifyPlayer((FButton)v);
                }
            });
        }

        for(int i= PartyGame.players.size(); i<playerButtons.length; i++){
            buttonArea2.removeView(playerButtons[i]);
        }

        for(int i = 0;i<PartyGame.players.size(); i++){
            if(i==1) {
                playerButtons[i].setButtonColor(getResources().getColor(R.color.color_challenge_player1));
                playerButtons[i].setShadowColor(getResources().getColor(R.color.color_challenge_player1_shadow));
            } else if(i==2){
                playerButtons[i].setButtonColor(getResources().getColor(R.color.color_challenge_player2));
                playerButtons[i].setShadowColor(getResources().getColor(R.color.color_challenge_player2_shadow));
            } else if(i==3){
                playerButtons[i].setButtonColor(getResources().getColor(R.color.color_challenge_player3));
                playerButtons[i].setShadowColor(getResources().getColor(R.color.color_challenge_player3_shadow));
            } else if(i==4){
                playerButtons[i].setButtonColor(getResources().getColor(R.color.color_challenge_player4));
                playerButtons[i].setShadowColor(getResources().getColor(R.color.color_challenge_player4_shadow));
            } else if(i==5){
                playerButtons[i].setButtonColor(getResources().getColor(R.color.color_challenge_player5));
                playerButtons[i].setShadowColor(getResources().getColor(R.color.color_challenge_player5_shadow));
            }
            playerButtons[i].setText(PartyGame.players.get(i).name);
            if (PartyGame.moderatorIndex == i) {
                playerButtons[i].setVisibility(View.INVISIBLE);
            }else {
                remainingPlayers.add(i);
            }
        }
    }

    private void resetButtons(){
        remainingPlayers = new ArrayList<>();
        deadPlayers = new ArrayList<>();
        for(int i = 0;i<PartyGame.players.size(); i++){
            if (PartyGame.moderatorIndex == i) {
                playerButtons[i].setVisibility(View.INVISIBLE);
            }else {
                showButton(playerButtons[i]);
                remainingPlayers.add(i);
            }
        }
    }

    private void disqualifyPlayer(FButton dButton)
    {
        int index = 0;
        for(int i=0;i<playerButtons.length; i++){
            if(dButton == playerButtons[i]){
                index = i;
                break;
            }
        }
        hideButton(dButton);
        //dButton.setVisibility(View.INVISIBLE);
        remainingPlayers.remove(Integer.valueOf(index));
        deadPlayers.add(Integer.valueOf(index));

        if(remainingPlayers.size()==1){
            new AlertDialog.Builder(ChallengeDisqualifyActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure "+PartyGame.players.get(remainingPlayers.get(0)).name+" is the winner?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PartyGame.players.get(remainingPlayers.get(0)).partyModeScore+=30;
                            Intent intent = new Intent(ChallengeDisqualifyActivity.this, ChallengeResultActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Integer index = deadPlayers.get(deadPlayers.size()-1);
                            deadPlayers.remove(deadPlayers.size() - 1);
                            remainingPlayers.add(index);
                            showButton(playerButtons[index]);
                            //playerButtons[index].setVisibility(View.VISIBLE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    private void hideButton(View v){
        YoYo.with(Techniques.FlipOutX)
                .duration(250)
                .playOn(v);
    }

    private void showButton(View v){
        v.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FlipInX)
                .duration(250)
                .playOn(v);
    }
}
