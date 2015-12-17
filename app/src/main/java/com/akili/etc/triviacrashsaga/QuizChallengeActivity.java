package com.akili.etc.triviacrashsaga;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.Entity.Player;
import com.akili.etc.triviacrashsaga.Entity.Quiz;
import com.akili.etc.triviacrashsaga.PartyMode.PartyHintActivity;
import com.akili.etc.triviacrashsaga.QuizFinishActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;


import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import extension.ResizeAnimation;

public class QuizChallengeActivity extends AppCompatActivity  {
    private TextSwitcher quizViewUp,quizViewDown;
    private Quiz currentQuiz;
    private String categoryName;

    private FButton answerButton1, answerButton2, answerButton3, answerButton4;
    private FButton answerButton5, answerButton6, answerButton7, answerButton8;
    private FButton rightAnswerButtonDown,selectedAnswerButton,rightAnswerButtonUp;
    private FButton nextButtondown,nextButtonup;
    private Button buzzup,buzzdown;
    private FButton[] buttons1,buttons2;

    private CountDownTimer countDownTimer;
    private TextView countDownTextViewUp,countDownTextViewDown;
    private TextView player1Name,player2Name,player2Score,player1Score;
    private TextView getReadyTop,getReadyDown;
    private Boolean countDownTicking = false;
    private Boolean dialougeShown =false;

    private int quizIndex = 0;
    private int  maxQuizIndex = 2;
    private int chance = 0;
    private ArrayList<Integer> indexPool;
    final static int targetHeight =2400;
    final static int animationDuration = 500;
    final static int countDownMilliseconds = 1000 * 10;
    final static int quizAnswerFlyingDelayStep = 100;
    final static float quizButtonsLayoutOffset = 1000f;
    final static int quizViewExpansionHeight = 400;
    final static int quizViewFoldHeight = 200;
    private long totalTime = 6000;

    private LinearLayout layout1;
    private LinearLayout layout2;

    //private ImageView player2BGTop,player1BGTop;
    private GridLayout glayout1;
    private GridLayout glayout2;

    private Player moderator,component;

    public  Animation quizOut;
    private Category category;
    private ArrayList<Quiz> quizs;

    public static final Integer[] Backgrounds = {
            R.drawable.player_1_background_box, R.drawable.player_2_background_box,
            R.drawable.player_3_background_box, R.drawable.player_4_background_box,
            R.drawable.player_5_background_box, R.drawable.player_6_background_box
    };

    public static final Integer[] BackgroundsRotate = {
            R.drawable.player_1_background_box_rotate, R.drawable.player_2_background_box_rotate,
            R.drawable.player_3_background_box_rotate, R.drawable.player_4_background_box_rotate,
            R.drawable.player_5_background_box_rotate, R.drawable.player_6_background_box_rotate
    };


    public static final Integer[] buttonColor ={
            R.color.challenge_quiz_button_p1,R.color.challenge_quiz_button_p2,
            R.color.challenge_quiz_button_p3,R.color.challenge_quiz_button_p4,
            R.color.challenge_quiz_button_p5,R.color.challenge_quiz_button_p6

    };

    public static final Integer[] buttonColorShadow ={
            R.color.challenge_quiz_button_p1_shadow,R.color.challenge_quiz_button_p2_shadow,
            R.color.challenge_quiz_button_p3,R.color.challenge_quiz_button_p4,
            R.color.challenge_quiz_button_p5,R.color.challenge_quiz_button_p6

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_challenge);
        getSupportActionBar().hide();


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            categoryName = extras.get("categoryName").toString();
        }
        moderator = CenterController.controller().partyGame.getModerator();
        component =  CenterController.controller().partyGame.getComponent();

        indexPool = new ArrayList<Integer>();
        int maxQuizNumber = CenterController.controller().quizCategories.get(categoryName).size();
        for(int i=0;i< maxQuizNumber;i++) {
            indexPool.add(new Integer(i));
        }

       // currentQuiz = CenterController.controller().quizCategories.get(categoryName.toString()).get(getRandomIndex());
        quizs = new ArrayList<>();
        category = CenterController.controller().categoryMap.get(categoryName);
        currentQuiz = getNextQuiz();
        quizViewUp = (TextSwitcher)findViewById(R.id.textSwitcherUp);
        quizViewUp.setFactory(switcherFactory);
        quizViewUp.setText(currentQuiz.quizContent);
        quizViewDown = (TextSwitcher)findViewById(R.id.textSwitcherDown);
        quizViewDown.setFactory(switcherFactory);
        quizViewDown.setText(currentQuiz.quizContent);
        getXMLId();
        //layout1.setBackground(getResources().getDrawable(R.drawable.player_2));//layout1 uper hai
        //layout2.setBackground(getResources().getDrawable(R.drawable.player_1));
        setVisiblity(false);
        flyinAnimation();
        addDelay();
        getReadyTop.setVisibility(View.INVISIBLE);
        getReadyDown.setVisibility(View.INVISIBLE);
        layout1.setBackground(getResources().getDrawable(BackgroundsRotate[PartyGame.currentComponentIndex]));//layout1 uper hai
        layout2.setBackground(getResources().getDrawable(Backgrounds[PartyGame.moderatorIndex]));
        buttons1 = new FButton[]{answerButton1, answerButton2, answerButton3, answerButton4};
        buttons2 = new FButton[]{answerButton5, answerButton6, answerButton7, answerButton8};

        for (FButton button : buttons1) {

            //button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p2));
            //button.setShadowColor(getResources().getColor(R.color.challenge_quiz_button_p2_shadow));
            button.setButtonColor(getResources().getColor(buttonColor[PartyGame.moderatorIndex]));
            // button.setShadowColor(getResources().getColor(buttonColorShadow[PartyGame.moderatorIndex]));
        }

        for (FButton button : buttons2) {

            //button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p2));
            //button.setShadowColor(getResources().getColor(R.color.challenge_quiz_button_p2_shadow));
            button.setButtonColor(getResources().getColor(buttonColor[PartyGame.currentComponentIndex]));
            //button.setShadowColor(getResources().getColor(buttonColorShadow[PartyGame.currentComponentIndex]));
        }
        nextButtondown.setButtonColor(getResources().getColor(buttonColor[PartyGame.moderatorIndex]));
        nextButtonup.setButtonColor(getResources().getColor(buttonColor[PartyGame.currentComponentIndex]));
        rightAnswerButtonDown = buttons1[currentQuiz.correctIndex];
        rightAnswerButtonUp = buttons2[currentQuiz.correctIndex];
        player1Name.setText(moderator.name);
        player1Name.setTextSize(50f);
        player1Score.setText("Score: " + CenterController.controller().partyGame.getModerator().partyModeScore);
        player2Name.setText(component.name);
        player2Score.setText("Score: " + CenterController.controller().partyGame.getComponent().partyModeScore);
        player2Name.setTextSize(50f);
        //startTimer();

        buzzup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buzzup.setVisibility(v.GONE);
                    layout2.setVisibility(LinearLayout.INVISIBLE);
                    glayout2.setVisibility(LinearLayout.VISIBLE);
                    glayout1.setVisibility(LinearLayout.VISIBLE);
                    ResizeAnimation resizeAnimation = new ResizeAnimation(layout1, layout1.getWidth(), targetHeight);
                    //ResizeAnimation resizeAnimation1 = new ResizeAnimation(player2BG, player2BG.getWidth(), targetHeight);
                    resizeAnimation.setDuration(animationDuration);
                    layout1.startAnimation(resizeAnimation);
                    //player2BG.startAnimation(resizeAnimation);
                    int animationDelay = quizAnswerFlyingDelayStep;
                    setAnswerButtonsWithQuizNew(currentQuiz);
                    for (Button button : buttons2) {
                        button.startAnimation(quizAnimation(animationDelay += quizAnswerFlyingDelayStep));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                answerClickedUp((Button) v);
                            }
                        });
                    }
                }
                return false;
            }
        });
        buzzdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buzzdown.setVisibility(v.GONE);
                    layout1.setVisibility(LinearLayout.INVISIBLE);
                    glayout1.setVisibility(LinearLayout.VISIBLE);
                    glayout2.setVisibility(LinearLayout.VISIBLE);
                    ResizeAnimation resizeAnimation = new ResizeAnimation(layout1, layout2.getWidth(), 0);
                    resizeAnimation.setDuration(animationDuration);
                    layout2.startAnimation(resizeAnimation);
                    int animationDelay = quizAnswerFlyingDelayStep;
                    setAnswerButtonsWithQuiz(currentQuiz);
                    for (Button button : buttons1) {
                       // Log.i("buttons >>", "" + getButtonNumber(button));
                        button.startAnimation(quizAnimation(animationDelay += quizAnswerFlyingDelayStep));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                answerClickedDown((Button) v);
                            }
                        });
                    }
                }
                return false;
            }
        });

        nextButtondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestionDown();
            }
        });
        nextButtonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestionUp();
            }
        });

        BackGroundController.playTriviaBGM(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solo_challenge, menu);
        return true;
    }

    private void flyinAnimation(){
        int animationDelay = 200;
       Animation  quizIn = quizAnimation(animationDelay += 200);
        getReadyTop.startAnimation(quizIn);
        getReadyDown.startAnimation(quizIn);

    }
    private void flyoutAnimation(){
        int animationDelay = 200;
        quizOut = quizAnimationOut(animationDelay += 200);
        getReadyTop.startAnimation(quizOut);
        getReadyDown.startAnimation(quizOut);
        quizOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //do nothing
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                quizViewDown.setVisibility(View.VISIBLE);
                quizViewUp.setVisibility(View.VISIBLE);
                startTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //do nothing
            }
        });


    }

    private Quiz getNextQuiz(){
        Quiz returnQuiz = category.nextQuiz();
        quizs.add(returnQuiz);
        return returnQuiz;
    }


    public boolean setVisiblity(boolean isVisible) {

        if(!isVisible) {

            nextButtondown.setVisibility(View.INVISIBLE);
            nextButtonup.setVisibility(View.INVISIBLE);
            answerButton1.setVisibility(View.INVISIBLE);
            answerButton2.setVisibility(View.INVISIBLE);
            answerButton3.setVisibility(View.INVISIBLE);
            answerButton4.setVisibility(View.INVISIBLE);
            answerButton5.setVisibility(View.INVISIBLE);
            answerButton6.setVisibility(View.INVISIBLE);
            answerButton7.setVisibility(View.INVISIBLE);
            answerButton8.setVisibility(View.INVISIBLE);
            countDownTextViewUp.setVisibility(View.INVISIBLE);
            countDownTextViewDown.setVisibility(View.INVISIBLE);
            buzzup.setVisibility(View.INVISIBLE);
            buzzdown.setVisibility(View.INVISIBLE);
            countDownTextViewUp.setVisibility(View.INVISIBLE);
            countDownTextViewDown.setVisibility(View.INVISIBLE);
            quizViewDown.setVisibility(View.INVISIBLE);
            quizViewUp.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public void getXMLId() {
        buzzup        = (FButton) findViewById(R.id.buzzup);
        buzzdown      = (FButton) findViewById(R.id.buzzdown);
        answerButton1 = (FButton)findViewById(R.id.answer1);
        answerButton2 = (FButton)findViewById(R.id.answer2);
        answerButton3 = (FButton)findViewById(R.id.answer3);
        answerButton4 = (FButton)findViewById(R.id.answer4);
        answerButton5 = (FButton)findViewById(R.id.answer5);
        answerButton6 = (FButton)findViewById(R.id.answer6);
        answerButton7 = (FButton)findViewById(R.id.answer7);
        answerButton8 = (FButton)findViewById(R.id.answer8);
        nextButtondown = (FButton)findViewById(R.id.nextButtondown);
        nextButtonup = (FButton)findViewById(R.id.nextButtonup);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        glayout1 = (GridLayout) findViewById(R.id.glayout1);
        glayout2 = (GridLayout) findViewById(R.id.glayout2);
        countDownTextViewUp = (TextView) findViewById(R.id.timerUp);
        countDownTextViewDown = (TextView) findViewById(R.id.timerDown);
        player1Name = (TextView) findViewById(R.id.player1Name);
        player2Name = (TextView) findViewById(R.id.player2Name);
        player2Score = (TextView) findViewById(R.id.player2Score);
        player1Score = (TextView) findViewById(R.id.player1Score);
        // player1BGTop = (ImageView) findViewById(R.id.player1BGTop);
        //player2BGTop = (ImageView) findViewById(R.id.player2BGTop);
        getReadyTop =   (TextView) findViewById(R.id.getReadyTop);
        getReadyDown =   (TextView) findViewById(R.id.getReadyDown);

    }

    public void startTimer() {
        countDownTextViewDown.setVisibility(View.VISIBLE);
        countDownTextViewUp.setVisibility(View.VISIBLE);
        countDownTicking=true;
        countDownTimer = new CountDownTimer(totalTime, 100) {

            public void onTick(long millisUntilFinished) {
                buzzdown.setVisibility(View.INVISIBLE);
                buzzup.setVisibility(View.INVISIBLE);
                totalTime=millisUntilFinished;
                countDownTextViewUp.setVisibility((View.VISIBLE));
                countDownTextViewDown.setVisibility((View.VISIBLE));
                countDownTextViewUp.setText(" " + millisUntilFinished / 1000 % 60);
                countDownTextViewDown.setText(" " + millisUntilFinished / 1000 % 60);
                //Log.i("test", "ms=" + millisUntilFinished);

            }

            public void onFinish() {
                buzzdown.setVisibility(View.VISIBLE);
                buzzup.setVisibility(View.VISIBLE);
                countDownTextViewUp.setVisibility((View.INVISIBLE));
                countDownTextViewDown.setVisibility((View.INVISIBLE));
                totalTime=6000;
                countDownTicking=false;

            }
        }.start();
    }

    public void addDelay() {
        countDownTimer = new CountDownTimer(4000, 100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                flyoutAnimation();
                getReadyTop.setVisibility(View.INVISIBLE);
                getReadyDown.setVisibility(View.INVISIBLE);
            }
        }.start();
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
    private int getRandomIndex(){
        Random randomizer = new Random();
        int resultIndex = randomizer.nextInt(indexPool.size());
        int result = indexPool.get(resultIndex);
        indexPool.remove(resultIndex);
        return result;
    }

    // TextView Factory for quizView TextSwitcher
    private ViewSwitcher.ViewFactory switcherFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            // Create a new TextView
            TextView t = new TextView(QuizChallengeActivity.this);
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            t.setTypeface(null, Typeface.BOLD_ITALIC);
            t.setTextSize(25f);
            t.setTextColor(Color.WHITE);
            t.setHeight(quizViewExpansionHeight);
            t.setBackgroundColor(Color.TRANSPARENT);
            return t;
        }
    };

    private void playWrongSound(){
        MediaPlayer wrong_sound = MediaPlayer.create(this, R.raw.answer_wrong);
        wrong_sound.start();
    }

    private void playRightSound(){
        MediaPlayer wrong_sound = MediaPlayer.create(this, R.raw.answer_right);
        wrong_sound.start();
    }

    //Animation for quiz view to animate
    private void quizViewAnimate1(String quizContent, int height, boolean nextButtonHidden) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(quizViewUp, quizViewUp.getWidth(), height);
        resizeAnimation.setDuration(animationDuration);
        quizViewUp.startAnimation(resizeAnimation);
        quizViewUp.setText(quizContent);
        if(nextButtonHidden)
            nextButtondown.setVisibility(View.INVISIBLE);
        else
            nextButtondown.setVisibility(View.VISIBLE);
    }

    //Animation for quiz view to animate
    private void quizViewAnimate2(String quizContent, int height, boolean nextButtonHidden) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(quizViewDown, quizViewDown.getWidth(), height);
        resizeAnimation.setDuration(animationDuration);
        quizViewDown.startAnimation(resizeAnimation);
        quizViewDown.setText(quizContent);
        if(nextButtonHidden)
            nextButtonup.setVisibility(View.INVISIBLE);
        else
            nextButtonup.setVisibility(View.VISIBLE);
    }
    private void setAnswerButtonsWithQuiz(Quiz quiz){
        answerButton1.setText(quiz.choices.get(0));
        answerButton2.setText(quiz.choices.get(1));
        answerButton3.setText(quiz.choices.get(2));
        answerButton4.setText(quiz.choices.get(3));
    }
    private void setAnswerButtonsWithQuizNew(Quiz quiz){
        answerButton5.setText(quiz.choices.get(0));
        answerButton6.setText(quiz.choices.get(1));
        answerButton7.setText(quiz.choices.get(2));
        answerButton8.setText(quiz.choices.get(3));
    }

    // Animation for right answer selection
    private void answerCorrect(FButton button){

        button.setButtonColor(getResources().getColor(R.color.quiz_answer_right_color));

    }

    // Animation for wrong answer selection
    private void answerWrong(FButton button) {

        button.setButtonColor(getResources().getColor(R.color.quiz_answer_wrong_color));
    }

    private Animation answerFade(int time) {
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f,quizButtonsLayoutOffset,0f,0f);
        translation.setStartOffset(time);
        translation.setDuration(animationDuration);
        translation.setFillAfter(true);
        translation.setInterpolator(new DecelerateInterpolator());
        return translation;
    }

    //region Utility
    // Animation for answers to go in
    private Animation quizAnimation(int time) {
        TranslateAnimation translation;
        translation = new TranslateAnimation(-quizButtonsLayoutOffset, 0f, 0f, 0f);
        translation.setStartOffset(time);
        translation.setDuration(animationDuration);
        translation.setFillAfter(true);
        translation.setInterpolator(new DecelerateInterpolator());
        return translation;
    }

    private Animation quizAnimationOut(int time) {
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f,quizButtonsLayoutOffset, 0f, 0f);
        translation.setStartOffset(time);
        translation.setDuration(animationDuration);
        translation.setFillAfter(true);
        translation.setInterpolator(new DecelerateInterpolator());
        return translation;
    }

    // When Answer clicked, go to review state
    private void answerClickedDown(Button selectedButton) {
        chance+=1;
        if(chance<=2) {
            if (selectedButton != null) {
                selectedAnswerButton = (FButton) selectedButton;
                if (selectedButton != rightAnswerButtonDown) {
                    //Wrong answer
                    playWrongSound();
                    answerWrong((FButton) selectedButton);
                    selectedButton.animate().x(quizButtonsLayoutOffset).setDuration(animationDuration);
                    layout2.setRotation(-180);
                    //layout2.setBackground(getResources().getDrawable(R.drawable.player_2_rotate));
                    layout2.setBackground(getResources().getDrawable(Backgrounds[PartyGame.currentComponentIndex]));
                    //player1BGTop.setBackground(getResources().getDrawable(R.drawable.p1_name_box));
                    //player1BGTop.setRotation(-180);
                    player1Name.setText(component.name);
                    player1Score.setText("Score: " + CenterController.controller().partyGame.getComponent().partyModeScore);
                    //nextButtondown.setBackgroundColor(Color.parseColor("#665577"));
                    nextButtondown.setButtonColor(getResources().getColor(buttonColor[PartyGame.currentComponentIndex]));

                    for (FButton button : buttons1) {

                        //button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p2));
                        //button.setShadowColor(getResources().getColor(R.color.challenge_quiz_button_p2_shadow));
                        button.setButtonColor(getResources().getColor(buttonColor[PartyGame.currentComponentIndex]));
                        //button.setShadowColor(getResources().getColor(buttonColorShadow[PartyGame.currentComponentIndex]));
                    }
                    if(chance==2){
                        for (Button answerButton : buttons1) {
                            answerButton.setClickable(false);
                        }
                        answerCorrect(rightAnswerButtonDown);
                        chance=0;
                        quizViewAnimate1(currentQuiz.quizContent, quizViewExpansionHeight, false);
                    }
                } else {
                    //Right answer
                    playRightSound();
                    answerCorrect(rightAnswerButtonDown);
                    if(chance ==2){
                        CenterController.controller().partyGame.getComponent().partyModeScore+=20;
                    }else{
                        CenterController.controller().partyGame.getModerator().partyModeScore += 20;
                    }
                    for (FButton answerButton : buttons1) {
                        answerButton.setClickable(false);
                    }
                    chance=0;
                    quizViewAnimate1(currentQuiz.quizContent, quizViewExpansionHeight, false);
                }
            }
        }

    }
    private void answerClickedUp(Button selectedButton) {
        chance+=1;
        if(chance<=2) {
            if (selectedButton != null) {
                selectedAnswerButton = (FButton)selectedButton;
                if (selectedButton != rightAnswerButtonUp) {
                    //Wrong answer
                    playWrongSound();
                    answerWrong((FButton) selectedButton);
                    selectedButton.animate().x(quizButtonsLayoutOffset).setDuration(animationDuration);
                    layout1.setRotation(-180);
                    //layout1.setBackground(getResources().getDrawable(R.drawable.player_1_rotate));
                    layout1.setBackground(getResources().getDrawable(BackgroundsRotate[PartyGame.moderatorIndex]));
                    // player2BGTop.setBackground(getResources().getDrawable(R.drawable.p2_name_box));
                    // player2BGTop.setRotation(-180);
                    player2Name.setText(moderator.name);
                    player2Score.setText("Score: " + CenterController.controller().partyGame.getModerator().partyModeScore);
                    // nextButtonup.setBackgroundColor(Color.parseColor("#445566"));
                    nextButtonup.setButtonColor(getResources().getColor(buttonColor[PartyGame.moderatorIndex]));

                    for (FButton button : buttons2) {

                        //button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p1));
                        //button.setShadowColor(getResources().getColor(R.color.challenge_quiz_button_p1_shadow));
                        button.setButtonColor(getResources().getColor(buttonColor[PartyGame.moderatorIndex]));
                        //button.setShadowColor(getResources().getColor(buttonColorShadow[PartyGame.moderatorIndex]));
                    }
                    if(chance==2){
                        for (FButton answerButton : buttons2) {
                            answerButton.setClickable(false);
                        }
                        answerCorrect(rightAnswerButtonUp);
                        chance=0;
                        quizViewAnimate2(currentQuiz.quizContent, quizViewExpansionHeight, false);
                    }
                } else {
                    //Right answer
                    playRightSound();
                    answerCorrect(rightAnswerButtonUp);
                    if(chance ==2){
                        CenterController.controller().partyGame.getModerator().partyModeScore+=20;
                    }else{
                        CenterController.controller().partyGame.getComponent().partyModeScore += 20;
                    }
                    for (FButton answerButton : buttons2) {
                        answerButton.setClickable(false);
                    }
                    chance=0;
                    quizViewAnimate2(currentQuiz.quizContent, quizViewExpansionHeight, false);
                }
            }
        }

    }


    // Go to the next question

    private void goToNextQuestionDown() {
        layout1.setRotation(0);
        layout2.setRotation(0);
        //layout2.setBackground(getResources().getDrawable(R.drawable.player_1));
        layout2.setBackground(getResources().getDrawable(Backgrounds[PartyGame.moderatorIndex]));
        // player1BGTop.setBackground(getResources().getDrawable(R.drawable.p2_name_box));
        //player1BGTop.setRotation(0);
        player1Score.setText("Score: " + CenterController.controller().partyGame.getModerator().partyModeScore);
        player2Score.setText("Score: " + CenterController.controller().partyGame.getComponent().partyModeScore);
        player1Name.setText(moderator.name);
        nextButtondown.setButtonColor(getResources().getColor(buttonColor[PartyGame.moderatorIndex]));
        nextButtondown.setVisibility(View.INVISIBLE);
        startTimer();
        layout1.setVisibility(LinearLayout.VISIBLE);
        layout2.setVisibility(LinearLayout.VISIBLE);
        glayout1.setVisibility(LinearLayout.INVISIBLE);
        glayout2.setVisibility(LinearLayout.INVISIBLE);

        // Go to summary when timeout
        if(quizIndex == maxQuizIndex){
            Intent intent = new Intent(this, PartyHintActivity.class);
            PartyGame.currentComponentIndex++;
            if(PartyGame.currentComponentIndex == PartyGame.moderatorIndex){
                PartyGame.currentComponentIndex++;
            }

            if(PartyGame.currentComponentIndex <= PartyGame.players.size()-1) {
                intent.putExtra("type",1);
                intent.putExtra("title", "");
                intent.putExtra("content", "");
            }else
            {
                PartyGame.currentComponentIndex = 0;
                intent.putExtra("type",2);
                intent.putExtra("title", "");
                intent.putExtra("content", "");
            }
            intent.putExtra("Category",categoryName);
            startActivity(intent);
            return;
        }

        ResizeAnimation resizeAnimation = new ResizeAnimation(layout1, layout1.getWidth(), 1200);
        resizeAnimation.setDuration(animationDuration);
        layout1.startAnimation(resizeAnimation);
        ResizeAnimation resizeAnimation1 = new ResizeAnimation(layout2, layout2.getWidth(), 1000);
        resizeAnimation1.setDuration(animationDuration);
        layout2.startAnimation(resizeAnimation);

        quizIndex += 1;
        //currentQuiz = CenterController.controller().quizCategories.get(categoryName).get(getRandomIndex());
        currentQuiz= getNextQuiz();
        quizViewUp.setText(currentQuiz.quizContent);
        quizViewDown.setText(currentQuiz.quizContent);
        player1Score.setText("Score " + CenterController.controller().partyGame.getModerator().partyModeScore);
        player2Score.setText("Score "+ CenterController.controller().partyGame.getComponent().partyModeScore);
        rightAnswerButtonDown.startAnimation(answerFade(0));
        if(selectedAnswerButton != null)
            selectedAnswerButton.startAnimation(answerFade(0));

        //Next question animation starts
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                quizViewAnimate1(currentQuiz.quizContent, quizViewFoldHeight, true);
                setAnswerButtonsWithQuiz(currentQuiz);
                int animationDelay = quizAnswerFlyingDelayStep;
                for (FButton button : buttons1) {
                    button.setEnabled(true);
                    button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p1));
                    button.setX((button == answerButton1 || button == answerButton3) ? 0 : 400);
                    button.startAnimation(quizAnimation(animationDelay += quizAnswerFlyingDelayStep));
                }
                rightAnswerButtonDown = buttons1[currentQuiz.correctIndex];
                rightAnswerButtonUp = buttons2[currentQuiz.correctIndex];
                // countDownTimer.start();
                nextButtondown.setEnabled(true);

            }
        }, animationDuration);
    }
    private void goToNextQuestionUp() {
        layout1.setRotation(0);
        layout2.setRotation(0);
        //layout1.setBackground(getResources().getDrawable(R.drawable.player_2));
        layout1.setBackground(getResources().getDrawable(BackgroundsRotate[PartyGame.currentComponentIndex]));
        //player2BGTop.setBackground(getResources().getDrawable(R.drawable.p1_name_box));
        //player2BGTop.setRotation(0);
        player2Name.setText(component.name);
        nextButtonup.setButtonColor(getResources().getColor(buttonColor[PartyGame.currentComponentIndex]));
        nextButtonup.setVisibility(View.INVISIBLE);
        startTimer();
        layout1.setVisibility(LinearLayout.VISIBLE);
        layout2.setVisibility(LinearLayout.VISIBLE);
        glayout1.setVisibility(LinearLayout.INVISIBLE);
        glayout2.setVisibility(LinearLayout.INVISIBLE);

        // Go to summary when timeout
        if(quizIndex == maxQuizIndex){
            Intent intent = new Intent(this, PartyHintActivity.class);
            PartyGame.currentComponentIndex++;
            if(PartyGame.currentComponentIndex == PartyGame.moderatorIndex){
                PartyGame.currentComponentIndex++;
            }

            if(PartyGame.currentComponentIndex <= PartyGame.players.size()-1) {
                intent.putExtra("type",1);
                intent.putExtra("title", "");
                intent.putExtra("content", "");
            }else
            {
                PartyGame.currentComponentIndex = 0;
                intent.putExtra("type",2);
                intent.putExtra("title", "");
                intent.putExtra("content", "");

            }
            intent.putExtra("Category",categoryName);
            startActivity(intent);
            return;
        }
        ResizeAnimation resizeAnimation = new ResizeAnimation(layout1, layout1.getWidth(), 1200);
        resizeAnimation.setDuration(animationDuration);
        layout1.startAnimation(resizeAnimation);
        ResizeAnimation resizeAnimation1 = new ResizeAnimation(layout2, layout2.getWidth(), 1100);
        resizeAnimation1.setDuration(animationDuration);
        layout2.startAnimation(resizeAnimation);

        quizIndex += 1;
        //currentQuiz = CenterController.controller().quizCategories.get(categoryName).get(getRandomIndex());
        currentQuiz= getNextQuiz();
        quizViewUp.setText(currentQuiz.quizContent);
        quizViewDown.setText(currentQuiz.quizContent);
        player1Score.setText("Score " + CenterController.controller().partyGame.getModerator().partyModeScore);
        player2Score.setText("Score " + CenterController.controller().partyGame.getComponent().partyModeScore);
        rightAnswerButtonUp.startAnimation(answerFade(0));

        if(selectedAnswerButton != null)
            selectedAnswerButton.startAnimation(answerFade(0));

        //Next question animation starts
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                quizViewAnimate2(currentQuiz.quizContent, quizViewFoldHeight, true);
                setAnswerButtonsWithQuizNew(currentQuiz);
                int animationDelay = quizAnswerFlyingDelayStep;
                for (FButton button : buttons2) {
                    button.setEnabled(true);
                    button.setButtonColor(getResources().getColor(R.color.challenge_quiz_button_p2));
                    button.setX((button == answerButton5 || button == answerButton7) ? 0 : 400);
                    button.startAnimation(quizAnimation(animationDelay += quizAnswerFlyingDelayStep));
                }
                rightAnswerButtonDown = buttons1[currentQuiz.correctIndex];
                rightAnswerButtonUp = buttons2[currentQuiz.correctIndex];
                // countDownTimer.start();
                nextButtonup.setEnabled(true);

            }
        }, animationDuration);
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
        return;

    }

    public int getButtonNumber(Button button){

        String IdAsString = getResources().getResourceEntryName(button.getId());
        //Log.i("BUTTON ID>>", "" + IdAsString);
        int idLength =IdAsString.length();
        return Integer.parseInt(IdAsString.substring(idLength - 1, idLength));

    }

    private void showQuitDialog(){

        countDownTimer.cancel();
        new AlertDialog.Builder(QuizChallengeActivity.this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit? You will lose all your progress!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        countDownTimer.cancel();
                        dialougeShown=false;
                        //finish();
                        Intent intent = new Intent(QuizChallengeActivity.this, MainMenuActivity.class);
                        //intent.putExtra("type",3);
                        startActivity(intent);
                    }
                }).setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (countDownTicking) {
                            startTimer();
                        } else {
                            // do nothing
                        }
                        dialougeShown = false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
