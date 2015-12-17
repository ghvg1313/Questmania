package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.Entity.Player;
import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.ProfileActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.rey.material.widget.Button;

import java.util.ArrayList;

import extension.PartyModeActivity;
import extension.SoloModeActivity;
import extension.StringTool;
import info.hoang8f.widget.FButton;

public class ChallengePreTextActivity extends SoloModeActivity {

    FButton nextButton, previousButton, beginButton, backButton;
    TextView preTextView;
    ImageView videoView;
    LinearLayout backgroundLayout, indexButtonsLayout, proceedButtonsLayout;

    Challenge challenge;

    String categoryName;

    private int currentTextIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        getWidgets();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            challenge = CenterController.controller().personalChallenge.get(extras.get("categoryName")).get(extras.getInt("challengeIndex"));
        }else {
            challenge = CenterController.controller().partyGame.getNextChallenge();
        }

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            challenge = (Challenge)extras.get("Challenge");
//        }

        setView(challenge);
        validButtonState();
    }


    private void getWidgets(){
        nextButton = (FButton)findViewById(R.id.nextButton);
        previousButton = (FButton)findViewById(R.id.previousButton);
        beginButton = (FButton)findViewById(R.id.beginButton);
        backButton = (FButton)findViewById(R.id.backButton);
        preTextView = (TextView)findViewById(R.id.preText);
        videoView = (ImageView)findViewById(R.id.videoView);
        indexButtonsLayout = (LinearLayout)findViewById(R.id.indexButtonsLayout);
        backgroundLayout = (LinearLayout)findViewById(R.id.challengeBackground);
        proceedButtonsLayout = (LinearLayout)findViewById(R.id.proceedButtonsLayout);

        backgroundLayout.removeView(proceedButtonsLayout);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrevious();
            }
        });

        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrevious();
            }
        });

    }

    private void setView(Challenge challenge){
        this.challenge = challenge;
        if(this.challenge.gameType != Challenge.GameType.PERSONAL && this.challenge.gameType != Challenge.GameType.TIMER) {
            AchievementSystem.CategoryType type = AchievementSystem.categoryTypeFromName(PartyGame.categoryName);

            this.challenge.preTexts.add(0, this.challenge.mask.get(PartyGame.categoryName));
            if(challenge.maskAudio.size()>0)
                this.challenge.audioTexts.add(0, this.challenge.maskAudio.get(PartyGame.categoryName));

            if(challenge.maskAudio.size()>0)
                BackGroundController.playAudio(this, this.challenge.audioTexts.get(0));
            preTextView.setText(StringTool.preparePartyText(challenge.preTexts.get(currentTextIndex)));
        } else{
            preTextView.setText(challenge.preTexts.get(currentTextIndex));
        }

        if(challenge.gameType == Challenge.GameType.PLAYER_TURN){
            videoView.setBackground(getResources().getDrawable(challenge.imageResourceId));
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.challenge_forum_background));
        } else if(challenge.gameType == Challenge.GameType.TEXT_IMAGE_DISQUALIFY){
            if(challenge.moderatoTurnText == null) {
                videoView.setBackground(getResources().getDrawable(R.drawable.pic_challenge_2));
            } else{
                videoView.setBackground(getResources().getDrawable(R.drawable.pic_challenge_3));
            }
            videoView.setBackground(getResources().getDrawable(challenge.imageResourceId));
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.challenge_forum_background));
        } else if(challenge.gameType == Challenge.GameType.PERSONAL || challenge.gameType == Challenge.GameType.TIMER){
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.background_challenge_solo));
            if(getIntent().getExtras().get("categoryName").equals("Teacher")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_teacher_0));
            } else if(getIntent().getExtras().get("categoryName").equals("Doctor")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_doctor_0));
            }else if(getIntent().getExtras().get("categoryName").equals("Writer")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_writer_0));
            } else if(getIntent().getExtras().get("categoryName").equals("Game Designer")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_gamer_0));
            }else if(getIntent().getExtras().get("categoryName").equals("Journalist")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_journalist_0));
            }else if(getIntent().getExtras().get("categoryName").equals("Artist")){
                videoView.setBackground(getResources().getDrawable(R.drawable.solo_challenge_artist_0));
            }
            categoryName = (String)getIntent().getExtras().get("categoryName");
            changeNavigationBarColor(challenge.categoryType, backgroundLayout);
        }

        if(challenge.preGameType == Challenge.PreGameType.PERSONAL)
            showTitleAndBackOnly(challenge.challengeTitle);
        else
            showTitle(challenge.challengeTitle);
        hideActionButtons();
    }

    private void validButtonState() {
        if(previousButton.getParent() == null) {
            indexButtonsLayout.addView(previousButton,0);
        }
        if(nextButton.getParent() == null){
            indexButtonsLayout.addView(nextButton);
        }

        if (currentTextIndex == 0 && previousButton.getParent() != null){
            indexButtonsLayout.removeView(previousButton);
        }
    }

    private void setPreText(){
        if(challenge.gameType == Challenge.GameType.PLAYER_TURN) {
            preTextView.setText(StringTool.preparePartyText(challenge.preTexts.get(currentTextIndex)));
            if(currentTextIndex != 0)
                BackGroundController.playAudio(this, this.challenge.audioTexts.get(currentTextIndex-1));
        }  else if(this.challenge.gameType == Challenge.GameType.PERSONAL) {
            preTextView.setText(challenge.preTexts.get(currentTextIndex));
        }  else if(challenge.audioTexts.size()>0) {
            preTextView.setText(StringTool.preparePartyText(challenge.preTexts.get(currentTextIndex)));
            if(currentTextIndex != 0)
                BackGroundController.playAudio(this, this.challenge.audioTexts.get(currentTextIndex-1));
        } else{
            preTextView.setText(StringTool.preparePartyText(challenge.preTexts.get(currentTextIndex)));
        }
        preTextView.setGravity(Gravity.CENTER);
    }

    private void goToPrevious(){
        if (currentTextIndex == challenge.preTexts.size()) {
            backgroundLayout.addView(indexButtonsLayout);
            backgroundLayout.removeView(proceedButtonsLayout);
            currentTextIndex = 0;
        } else {
            currentTextIndex--;
        }
        setPreText();
        validButtonState();
    }

    private void goToNext(){
        currentTextIndex++;
        if(currentTextIndex == challenge.preTexts.size()){
            //Ready page
            backgroundLayout.removeView(indexButtonsLayout);
            backgroundLayout.addView(proceedButtonsLayout);
            preTextView.setText("Ready to Play?");
            preTextView.setGravity(Gravity.RIGHT);
        } else if(currentTextIndex < challenge.preTexts.size()){
            //Next
            setPreText();
            validButtonState();
        } else{
            //Go to challenge
            currentTextIndex--;
            Intent intent = new Intent(ChallengePreTextActivity.this, ChallengeReadyActivity.class);
            intent.putExtra("Challenge", challenge);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
            return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(challenge.preGameType == Challenge.PreGameType.PERSONAL) {
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
                                finish();
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
        }

        return super.onOptionsItemSelected(item);
    }

}
