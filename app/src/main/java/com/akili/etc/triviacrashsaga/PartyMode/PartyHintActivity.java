package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.Entity.Player;
import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.QuizChallengeActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.rey.material.app.ThemeManager;


import org.w3c.dom.Text;

import java.util.ArrayList;

import extension.PartyModeActivity;
import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class PartyHintActivity extends AppCompatActivity {
    private int type = 0;

    private TextView roundText, hintText,instructionText,instructionText2;
    private Button startButton;
    private FButton nextButton;
    private RelativeLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.init(this, 1, 0, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_hint);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));


     //   ThemeManager.init(this, 1, 0, null);

        roundText = (TextView)findViewById(R.id.roundText);
        hintText = (TextView)findViewById(R.id.hintText);
        startButton = (Button)findViewById(R.id.startButtonHint);
        nextButton  =   (FButton) findViewById(R.id.nextButtonHint);
        instructionText = (TextView) findViewById(R.id.instructionText);
        instructionText2 = (TextView) findViewById(R.id.instructionText2);
        background = (RelativeLayout)findViewById(R.id.background);
        View backgroundView = findViewById(R.id.background);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = (int)extras.get("type");
        }

        Player moderator, component;
        switch (type){
            //Round N hint
            case 0:

                startButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
                backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_2));
                moderator = CenterController.controller().partyGame.getModerator();
                roundText.setText("ROUND " + CenterController.controller().partyGame.roundIndex + " \n\n\n\n\n");
                roundText.setTextSize(40f);
                roundText.setTypeface(null, Typeface.BOLD);
                hintText.setTextSize(30f);
                hintText.setText("\n\n\nExpert \n" +moderator.name+
                        "\n\n" +
                        "");

                break;
            //Versus hint
            case 1:
                startButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
                backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_2));
                moderator = CenterController.controller().partyGame.getModerator();
                component = CenterController.controller().partyGame.getComponent();
                roundText.setText("");
                hintText.setTextSize(30f);
                hintText.setText("Expert: " + moderator.name + "\n" +
                        "\n" +
                        "Vs\n" +
                        "\n" +
                        "Challenger: " + component.name);
                break;
            //Challenge round hint
            case 2:
                startButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                moderator = CenterController.controller().partyGame.getModerator();
                backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_1));
                instructionText.setText("Great job " + moderator.name + "!\n" +
                        "Now challengers can earn \n" +
                        "more points with a Skill Challenge.\n" +
                        "But " + moderator.name + " gets to pick the winner!");
                BackGroundController.playNormalBGM(this);
                roundText.setText("Challenge Round!");
                roundText.setTypeface(null, Typeface.BOLD);
                hintText.setText("");
                break;
            // Start Tutorial
            case 3:
                startButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                CenterController.startShown = true;
                instructionText.setText("Welcome to Party Mode! I'm Chamy!\n" );
                instructionText.setTypeface(null,Typeface.BOLD);
                instructionText2.setText( "\nWhere do your strengths lie\n" +
                        "compared to your friends?\n" +
                        "Grab a few friends and find out!");
                //roundText.setText("Welcome to Questions!");
                //hintText.setText("This game combines knowledge questions with fun challenges.\n" +
                //      "\n" +
                //     "Players sit in a circle. The first player will be the Expert and the others will be the Challengers.\n" +
                //    " \n" +
                //        "Challengers will compete against the Expert by answering questions. \n" +
                //        "\n" +
                //        "Then Challengers play a Bonus Challenge. ");
                break;
            //Trivia Tutorial
            case 4:
                backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_forum_background));
                CenterController.triviaShown = true;
                roundText.setText("Question Round"+"\n\n");
                hintText.setText("\n\n\nThe Expert chooses their Expert Topic. The Expert answers a round of questions against each Challenger in the topic. \n" +
                        "\n" +
                        "A question is displayed on a split screen. The first player to tap I Know on their side gets to answer the question. \n" +
                        "\n" +
                        "If the player gets the question right, they get points. \n" +
                        "\n" +
                        "If the player gets the question wrong, the other player gets a chance to answer. \n" +
                        "\n" +
                        "The Expert plays against each player in turn.");
                break;
            //Challenge Tutorial
            case 5:

                backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_forum_background));
                CenterController.challengeshown = true;
                roundText.setText("Challenge Round\n\n");
                hintText.setText("\n\n\nThe Challengers now play a Bonus Challenge in the Expert Topic to earn extra points. The Expert will choose the Winner of the points.");
                break;
            //Final result
            case 6:
                BackGroundController.playFinalWinBGM(this);
                roundText.setText("Congratulations\n\n\n");
                roundText.setTypeface(null, Typeface.BOLD);
                hintText.setTextSize(40f);
                ArrayList<Player> players = CenterController.controller().partyGame.getWinners();
                String resultString = "\n\nYou are now Master "+players.get(0).name;
                for (int i=1;i<players.size();i++){
                    if(i==0){
                        resultString += (" and Master "+players.get(i).name);
                    }
                }
                hintText.setText(resultString);
                hintText.setTextSize(30f);
                break;

        }

        if(type == 6){

            backgroundView.setBackground(getResources().getDrawable(R.drawable.winner));
            startButton.setText("Finish");
            startButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        } else if(type == 3){

            backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_1));
            startButton.setText("Next");
            startButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 0) {
                    //Player is moderator -> moderator vs player
                    Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    if(CenterController.triviaShown) {
                        intent.putExtra("type", 1);
                    } else{

                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }
                    startActivity(intent);
                } else if(type == 1){
                    //moderator vs player -> Quiz
                    if(!PartyGame.roundCategorySelected) {
                        PartyGame.roundCategorySelected = true;
                        startActivity(new Intent(PartyHintActivity.this, ChallengeCategorySelection.class));
                    } else{
                        Intent intent = new Intent(PartyHintActivity.this, QuizChallengeActivity.class);
                        intent.putExtra("categoryName",PartyGame.categoryName);
                        startActivity(intent);
                    }

                } else if(type == 2){
                    //Quiz finish -> Challenge
                   // if(CenterController.challengeshown) {
                        startActivity(new Intent(PartyHintActivity.this, ChallengePreTextActivity.class));
                   // } else{
                   //     Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    //    intent.putExtra("type", 5);
                    //    startActivity(intent);
                  //  }
                } else if(type == 3){
                    //Welcome -> Add player
                    startActivity(new Intent(PartyHintActivity.this, GetPlayerNumber.class));
                } else if(type == 4){
                    //Quiz -> Actual Quiz(Moderator vs player)
                    Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                } else if(type == 5){
                    View backgroundView = findViewById(R.id.challengeBackground);
                    backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_2));
                    startActivity(new Intent(PartyHintActivity.this, ChallengePreTextActivity.class));
                } else if(type == 6){

                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=1KxSWKBIoQQ")));

                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PartyHintActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                return;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 0) {
                    //Player is moderator -> moderator vs player
                    Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    if(CenterController.triviaShown) {
                        intent.putExtra("type", 1);
                    } else{

                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }
                    startActivity(intent);
                } else if(type == 1){
                    //moderator vs player -> Quiz
                    if(!PartyGame.roundCategorySelected) {
                        PartyGame.roundCategorySelected = true;
                        startActivity(new Intent(PartyHintActivity.this, ChallengeCategorySelection.class));
                    } else{
                        Intent intent = new Intent(PartyHintActivity.this, QuizChallengeActivity.class);
                        intent.putExtra("categoryName",PartyGame.categoryName);
                        startActivity(intent);
                    }

                } else if(type == 2){
                    //Quiz finish -> Challenge
                    // if(CenterController.challengeshown) {
                    startActivity(new Intent(PartyHintActivity.this, ChallengePreTextActivity.class));
                    // } else{
                    //     Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    //    intent.putExtra("type", 5);
                    //    startActivity(intent);
                    //  }
                } else if(type == 3){
                    //Welcome -> Add player
                    startActivity(new Intent(PartyHintActivity.this, GetPlayerNumber.class));
                } else if(type == 4){
                    //Quiz -> Actual Quiz(Moderator vs player)
                    Intent intent = new Intent(PartyHintActivity.this, PartyHintActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                } else if(type == 5){
                    View backgroundView = findViewById(R.id.challengeBackground);
                    backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_background_2));
                    startActivity(new Intent(PartyHintActivity.this, ChallengePreTextActivity.class));
                } else if(type == 6){

                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=1KxSWKBIoQQ")));

                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PartyHintActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                return;
            }
        });

    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
        return;

    }
    private void showQuitDialog(){


        new AlertDialog.Builder(PartyHintActivity.this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quiz? You will loose all your progress!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PartyGame.roundCategorySelected= false;
                        //finish();
                        Intent intent = new Intent(PartyHintActivity.this, MainMenuActivity.class);
                        //intent.putExtra("type",3);
                        startActivity(intent);
                    }
                }).setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
