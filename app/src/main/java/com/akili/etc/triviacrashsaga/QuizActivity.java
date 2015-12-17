package com.akili.etc.triviacrashsaga;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.view.animation.TranslateAnimation;
import android.widget.ViewSwitcher;

import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.Quiz;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.rey.material.app.ThemeManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import extension.ResizeAnimation;
import extension.ResourceTool;
import info.hoang8f.widget.FButton;

public class QuizActivity extends ActionBarActivity{

    final static int countDownMilliseconds = 1000 * 20;
    final static int quizAnswerFlyingDelayStep = 100;
    final static float quizButtonsLayoutOffset = 1000f;
    final static int animationDuration = 500;
    final static int quizViewExpansionHeight = 300;
    final static int quizViewFoldHeight = 300;

    private String categoryName;

    private TextSwitcher quizView;
    private TextView feedBackTextView;
    private FButton answerButton1, answerButton2, answerButton3, answerButton4;
    private FButton rightAnswerButton, selectedAnswerButton;
    private Button  nextButton;
    private FButton[] buttons;
    private TextView infoView;

    private float buttonStartXMin = -1, buttonStartXMax = -1;

    // Use this to record timer mark
    private CountDownTimer countDownTimer;
    private long currentQuizScore = 0;
    private int baseScore = 12;
    private int maxBonus = 10;
    private int quizScore = 0;

    // quiz index related
    private Category category;
    private int quizIndex = 0;
    private int  maxQuizIndex = 4;
    private Quiz currentQuiz;
    private ArrayList<Quiz> quizs;
    private ArrayList<Integer> userChoices;

    private RelativeLayout infoLayout;

    private View backgroundView;

    private int bonus = maxBonus;

    //Review mode
    private boolean reviewMode = false;
    private FButton reviewNextbutton, reviewPreviousButton;

    //storing users answers
    public static ArrayList<String> selectedAnswers;
    public int rightAnswerCount = 0;
    public long timeUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ThemeManager.init(this, 1, 0, null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryName = extras.get("categoryName").toString();
            reviewMode = (boolean)extras.get("reviewMode");
        }

        backgroundView = findViewById(R.id.backgroundView);

        infoLayout = (RelativeLayout)findViewById(R.id.infoLayout);

        AchievementSystem.CategoryType categoryType = AchievementSystem.categoryTypeFromName(categoryName);
        backgroundView.setBackground(ResourceTool.getBackGroundImage(this, categoryType));

        selectedAnswers = new ArrayList<>(5);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AchievementSystem.CategoryType type = AchievementSystem.categoryTypeFromName(categoryName);
        if(type == AchievementSystem.CategoryType.DOCTOR){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#2196f3")));
        }else if(type == AchievementSystem.CategoryType.WRITER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
        }else if(type == AchievementSystem.CategoryType.TEACHER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#673ab7")));
        }else if(type == AchievementSystem.CategoryType.GAMER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
        }else if(type == AchievementSystem.CategoryType.JOURNALIST){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
        }else if(type == AchievementSystem.CategoryType.ARTIST){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#673ab7")));
        }

        if(!reviewMode) {
            quizs = new ArrayList<>();
            userChoices = new ArrayList<>();
            category = CenterController.controller().categoryMap.get(categoryName);
            currentQuiz = getNextQuiz();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else{
            quizs = (ArrayList)extras.get("Quizs");
            userChoices = (ArrayList)extras.get("Choices");
            currentQuiz = quizs.get(0);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Review");
        }

        quizView = (TextSwitcher) findViewById(R.id.quizView);
        answerButton1 = (FButton) findViewById(R.id.answer1);
        answerButton2 = (FButton) findViewById(R.id.answer2);
        answerButton3 = (FButton) findViewById(R.id.answer3);
        answerButton4 = (FButton) findViewById(R.id.answer4);
        nextButton = (Button) findViewById(R.id.nextButton);
        infoView = (TextView) findViewById(R.id.infoText);
        feedBackTextView = (TextView)findViewById(R.id.feedBackTextView);
        reviewNextbutton = (FButton) findViewById(R.id.reviewNextButton);
        reviewPreviousButton = (FButton) findViewById(R.id.reviewBackButton);

        buttons = new FButton[]{answerButton1, answerButton2, answerButton3, answerButton4};
        rightAnswerButton = buttons[currentQuiz.correctIndex];

        quizView.setFactory(switcherFactory);
        quizView.setBackgroundColor(getResources().getColor(R.color.trivia_quiz_content_background));
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(animationDuration);
        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(animationDuration);
        quizView.setInAnimation(in);
        quizView.setOutAnimation(out);
        quizView.setText(currentQuiz.quizContent);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestion();
            }
        });

        reviewNextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewNext();
            }
        });

        reviewPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewPrevious();
            }
        });


        ImageButton closeButton = (ImageButton) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuitDialog();
            }
        });

        if(!reviewMode) {
            nextButton.setVisibility(View.INVISIBLE);
            reviewNextbutton.setVisibility(View.INVISIBLE);
            reviewPreviousButton.setVisibility(View.INVISIBLE);
            setAnswerButtonsWithQuiz(currentQuiz);
            for (Button button : buttons) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answerClicked((Button) v);
                    }
                });
            }

            countDownTimer = new CountDownTimer(countDownMilliseconds + 500, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeUsed+=1;
                    bonus-=1;
                    if(bonus<0)
                        bonus = 0;
                    currentQuizScore =bonus + baseScore;
                    int progress = (int)(100*((millisUntilFinished / 1000.0 % 60)/10.0));
                    infoView.setText("Score "+quizScore + "   Bonus "+(bonus));
                    invalidateOptionsMenu();
                }

                public void onFinish() {
                    //playWrongSound();
                    currentQuizScore = baseScore;
                    infoView.setText("Score " + quizScore + "   Bonus 0");
                    bonus = 0;
                    invalidateOptionsMenu();
                    //answerClicked(null);

                }
            }.start();
            infoView.setVisibility(View.INVISIBLE);
            ((LinearLayout)infoLayout.getParent()).removeView(infoLayout);
        } else{
            nextButton.setVisibility(View.INVISIBLE);
            reviewPreviousButton.setVisibility(View.INVISIBLE);
            for (FButton button : buttons) {
                button.setButtonColor(getResources().getColor(R.color.review_gray));
                button.setShadowColor(getResources().getColor(R.color.review_gray));
                button.setEnabled(false);
            }
            setAnswerButtonsWithQuiz(currentQuiz);
            answerCorrect(buttons[currentQuiz.correctIndex]);
            if(currentQuiz.correctIndex!=userChoices.get(quizIndex)){
                answerWrong(buttons[userChoices.get(quizIndex)]);
            }
            infoView.setGravity(Gravity.CENTER);
            infoView.setBackgroundColor(getResources().getColor(R.color.merica_orange));
            infoView.setText("This question comes from <"+currentQuiz.questString+">\nTap here to go the Quest");
        }

        closeButton.setVisibility(View.INVISIBLE);
    }

    private void feedback(float positionX, float positionY, int score){
        feedBackTextView.setX(0);
        feedBackTextView.setY(0);
        feedBackTextView.setText("+"+score);
        //feedBackTextView.clearAnimation();
        YoYo.with(Techniques.FadeOutUp)
                .duration(1500)
                .playOn(feedBackTextView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
           if(!reviewMode){
               for (Button button : buttons) {
                   answerGoIn(button);
               }
           }
        }
    }

    //region Menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);

        MenuItem scoreItem = menu.findItem(R.id.action_score);
        scoreItem.setTitle("Score "+quizScore);
        MenuItem bonusItem = menu.findItem(R.id.action_bonus);
        bonusItem.setTitle("Bonus "+bonus);

        if(reviewMode){
            scoreItem.setVisible(false);
            bonusItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!reviewMode) {
            showQuitDialog();
            return;
        }
        super.onBackPressed();
    }
    //endregion

    // When Answer clicked, go to review state
    private void answerClicked(Button selectedButton) {
        if(buttonStartXMin == -1) {
            buttonStartXMin = answerButton1.getX();
            buttonStartXMax = answerButton2.getX();
        }

        for(int i=0; i<4;i++){
            if(selectedButton == buttons[i]){
                userChoices.add(new Integer(i));
            }
        }
        countDownTimer.cancel();
        answerCorrect(rightAnswerButton);

        //Handle countdown ends
        if(selectedButton != null) {
            selectedAnswerButton = (FButton)selectedButton;
            int id =selectedAnswerButton.getId();
            String buttonName = getResources().getResourceEntryName(id);
            selectedAnswers.add(buttonName);
            if (selectedButton != rightAnswerButton) {
                //Wrong answer
                playWrongSound();
                answerWrong((FButton)selectedButton);
                currentQuizScore = 0;
                invalidateOptionsMenu();
            }else{
                //Right answer
                playRightSound();
                rightAnswerCount++;
                quizScore += baseScore+bonus;
                infoView.setText("Score "+quizScore + "   Bonus " + bonus);
                invalidateOptionsMenu();
                feedback(selectedButton.getX() + selectedButton.getWidth() / 2f, selectedButton.getY() + selectedButton.getHeight()/2f, baseScore + bonus);
            }
        }

        //Other buttons animation
        for(Button answerButton : buttons){
            answerButton.setEnabled(false);
            if(answerButton != selectedButton && answerButton != rightAnswerButton) {
                answerFade(answerButton);
            }
        }
        quizViewAnimate(currentQuiz.quizContent, quizViewExpansionHeight, false);
    }

    // Go to the last question
    private void reviewPrevious(){
        quizIndex--;
        currentQuiz =quizs.get(quizIndex);
        infoView.setText("This question comes from <"+currentQuiz.questString+">\nTap here to go the Quest");
        quizViewAnimate(currentQuiz.quizContent, quizViewFoldHeight, true);
        setAnswerButtonsWithQuiz(currentQuiz);
        reviewPreviousButton.setVisibility(View.VISIBLE);
        reviewNextbutton.setVisibility(View.VISIBLE);
        if(quizIndex==0){
            reviewPreviousButton.setVisibility(View.INVISIBLE);
        }

        for(FButton button : buttons){
            button.setButtonColor(getResources().getColor(R.color.review_gray));
            button.setShadowColor(getResources().getColor(R.color.review_gray));
        }
        answerCorrect(buttons[currentQuiz.correctIndex]);
        if(currentQuiz.correctIndex!=userChoices.get(quizIndex)){
            answerWrong(buttons[userChoices.get(quizIndex)]);
        }
    }

    private void reviewNext() {
            quizIndex++;
            currentQuiz = quizs.get(quizIndex);
            quizViewAnimate(currentQuiz.quizContent, quizViewFoldHeight, true);
            infoView.setText("This question comes from <" + currentQuiz.questString + ">\nTap here to go the Quest");
            setAnswerButtonsWithQuiz(currentQuiz);
            reviewPreviousButton.setVisibility(View.VISIBLE);
            reviewNextbutton.setVisibility(View.VISIBLE);
            if(quizIndex==maxQuizIndex){
                reviewNextbutton.setVisibility(View.INVISIBLE);
            }
            for(FButton button : buttons){
                button.setButtonColor(getResources().getColor(R.color.review_gray));
                button.setShadowColor(getResources().getColor(R.color.review_gray));
            }
            answerCorrect(buttons[currentQuiz.correctIndex]);
            if(currentQuiz.correctIndex!=userChoices.get(quizIndex)) {
                answerWrong(buttons[userChoices.get(quizIndex)]);
            }
    }

    // Go to the next question
    private void goToNextQuestion() {
        nextButton.setEnabled(false);
        // Go to summary when all quizzes are answered
        if(quizIndex == maxQuizIndex){
            Intent intent = new Intent(this, QuizFinishActivity.class);
            intent.putExtra("Score", quizScore);
            intent.putExtra("Quizs", quizs);
            intent.putExtra("Category",categoryName);
            intent.putExtra("Choices", userChoices);
            intent.putExtra("Time", (int)(timeUsed));
            intent.putExtra("RightCount", rightAnswerCount);
            startActivity(intent);
            return;
        }

        quizIndex += 1;
        currentQuiz = getNextQuiz();

//        rightAnswerButton.startAnimation(answerFade(0));
        answerFade(rightAnswerButton);
        if(selectedAnswerButton != null) {
//            selectedAnswerButton.startAnimation(answerFade(0));
            answerFade(selectedAnswerButton);
        }

        bonus = maxBonus;

        //Next question animation starts
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                quizViewAnimate(currentQuiz.quizContent, quizViewFoldHeight, true);
                setAnswerButtonsWithQuiz(currentQuiz);
                for (FButton button : buttons) {
                    button.setEnabled(true);
                    button.setButtonColor(getResources().getColor(R.color.trivia_quiz_answer_color));
                    button.setShadowColor(getResources().getColor(R.color.trivia_quiz_answer_shadow));
                    button.setX((button == answerButton1 || button == answerButton3) ? buttonStartXMin : buttonStartXMax);
                    answerGoIn(button);
                }
                rightAnswerButton = buttons[currentQuiz.correctIndex];
                countDownTimer.start();
                nextButton.setEnabled(true);
            }
        }, 700);
    }

    //region Utility
    // Animation for answers to go in
    private void answerGoIn(Button button) {
        YoYo.with(Techniques.BounceIn)
                .duration(500)
                .playOn(button);
    }

    // Animation for the answer to go away after selection
    private void answerFade(Button button) {
        YoYo.with(Techniques.FadeOutRight)
                .duration(500)
                .playOn(button);
    }

    // Animation for right answer selection
    private void answerCorrect(FButton button){
        button.setButtonColor(getResources().getColor(R.color.quiz_answer_right_color));
        button.setShadowColor(getResources().getColor(R.color.quiz_answer_right_shadow));
    }

    // Animation for wrong answer selection
    private void answerWrong(FButton button){
        button.setButtonColor(getResources().getColor(R.color.quiz_answer_wrong_color));
        button.setShadowColor(getResources().getColor(R.color.quiz_answer_wrong_shadow));
    }

    //Animation for quiz view to animate
    private void quizViewAnimate(String quizContent, int height, boolean nextButtonHidden) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(quizView, quizView.getWidth(), height);
        resizeAnimation.setDuration(animationDuration);
        quizView.startAnimation(resizeAnimation);
        quizView.setText(quizContent);
        if(!reviewMode) {
            if (nextButtonHidden)
                nextButtonShow(false);
            else
                nextButtonShow(true);
        }
    }

    // TextView Factory for quizView TextSwitcher
    private ViewSwitcher.ViewFactory switcherFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            // Create a new TextView
            TextView t = new TextView(QuizActivity.this);
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            t.setTypeface(null, Typeface.BOLD_ITALIC);
            t.setTextSize(24f);
            t.setTextColor(getResources().getColor(R.color.trivia_quiz_content_text_color));
            t.setHeight(quizViewExpansionHeight);
            t.setPadding(20,20,20,20);
            t.setBackgroundColor(getResources().getColor(R.color.trivia_quiz_content_background));

            return t;
        }
    };

    private void setAnswerButtonsWithQuiz(Quiz quiz){
        answerButton1.setText(quiz.choices.get(0));
        answerButton2.setText(quiz.choices.get(1));
        answerButton3.setText(quiz.choices.get(2));
        answerButton4.setText(quiz.choices.get(3));
    }

    private Quiz getNextQuiz(){
        Quiz returnQuiz = category.nextQuiz();
        quizs.add(returnQuiz);
        return returnQuiz;
    }

    private void playWrongSound(){
        BackGroundController.playSoundEffect(this, R.raw.answer_wrong);
    }

    private void playRightSound(){
        BackGroundController.playSoundEffect(this, R.raw.answer_right);
    }

    private void nextButtonShow(Boolean show){
        if(show){
            nextButton.setVisibility(View.VISIBLE);
            answerGoIn(nextButton);
        } else{
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    private void showQuitDialog(){
        if(!reviewMode) {
            final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
            dialogBuilder
                    .withTitle("Quit")
                    .withMessage("Are you sure you want to quit? You will lose your progress")
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
                            countDownTimer.cancel();
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
        }else{
            finish();
        }
    }
    //endregion
}
