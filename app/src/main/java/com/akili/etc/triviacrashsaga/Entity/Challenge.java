package com.akili.etc.triviacrashsaga.Entity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kangleif on 10/28/2015.
 */

public class Challenge implements Serializable {

    public enum PreGameType {
        VIDEO_TEXT,
        TEXT_ONLY,
        PERSONAL
    }

    public enum PreTimerType{
        TEXT_TIMER,
        PERSONAL
    }

    public enum GameType{
        TEXT_IMAGE_DISQUALIFY,
        PLAYER_TURN,
        PERSONAL,
        TIMER
    }

    public String challengeContent;
    public String winnerStandard;
    //TODO: Mask is only temporary, delete this and anything related to it
    public HashMap<String, String> mask = new HashMap<>();
    public HashMap<String, Integer> maskAudio = new HashMap<>();
    public int imageResourceId = -1;
    public int timeInSeconds = 0;
    public String preString, afterString;

    public PreGameType preGameType;
    public PreTimerType preTimerType;
    public GameType gameType;
    public ArrayList<String> preTexts;
    public ArrayList<Integer> audioTexts = new ArrayList<>();
    public String challengeTitle;
    public String challengeQuestion;
    public String challengePrepareString;
    public String challengeStandard;

    public String moderatoTurnText;

    public AchievementSystem.CategoryType categoryType;


    public Challenge(PreGameType preType, PreTimerType timerType, GameType type, String title, ArrayList<String> preTexts, String challengeQuestion, String challengePrepareString, String standardString) {
        maskAudio = new HashMap<>();
        audioTexts = new ArrayList<>();
        preGameType = preType;
        preTimerType = timerType;
        gameType = type;
        challengeTitle = title;
        challengeStandard = standardString;
        this.preTexts = (ArrayList<String>) preTexts.clone();
        this.challengeQuestion = challengeQuestion;
        this.challengePrepareString = challengePrepareString;
    }

    public void setChallengeCategory(AchievementSystem.CategoryType type){
        categoryType = type;
    }

    public Challenge(String challengeTitle, ArrayList<String> preTexts, String prepareString, String question, String finishString){
        preGameType = PreGameType.PERSONAL;
        preTimerType = PreTimerType.PERSONAL;
        gameType = GameType.PERSONAL;
        this.challengeTitle = challengeTitle;
        this.preTexts = preTexts;
        this.challengePrepareString = prepareString;
        this.challengeQuestion = question;
        this.challengeStandard = finishString;
    }
}
