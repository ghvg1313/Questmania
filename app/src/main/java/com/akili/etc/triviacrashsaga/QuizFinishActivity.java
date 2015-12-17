package com.akili.etc.triviacrashsaga;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;

import extension.ResourceTool;
import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class QuizFinishActivity extends SoloModeActivity {

    private ImageView imageView;
    private FButton replayButton, summaryButton, onboardingButton;
    private ArrayList<Integer> indices, userChoices;
    private String categoryName;
    private View backgroundView, bottomBar;
    private TextView infoView,categoryTextView, callerTextView, onboardingTextView;
    private RelativeLayout onboardingLayout;
    private int score = 0;


    //TODO:Sync base score with Quiz Activity
    private int baseScore = 12;
    private int maxQuizCount = 5;

    private TextView scoreProgressText, answerProgressText, levelProgressText, levelText;
    private RoundCornerProgressBar scoreProgress, answerProgress, levelProgress;

    @Override
    public void onBackPressed() {
        if(!CenterController.controller().firstOnboardingShown) return;
        Intent intent = new Intent(QuizFinishActivity.this, RootCategoryActivity.class);
        intent.putExtra("RootType", categoryName);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(CenterController.controller().firstOnboardingShown){
            onboardingLayout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);

        imageView = (ImageView)findViewById(R.id.QuizFinishImageView);
        replayButton = (FButton)findViewById(R.id.replayButton);
        summaryButton = (FButton)findViewById(R.id.summaryButton);
        backgroundView = findViewById(R.id.backgroundView);

        scoreProgress = (RoundCornerProgressBar) findViewById(R.id.scoreProgress);
        answerProgress = (RoundCornerProgressBar) findViewById(R.id.answerProgress);
        levelProgress = (RoundCornerProgressBar) findViewById(R.id.levelProgress);

        scoreProgressText = (TextView) findViewById(R.id.scorePercentageTextView);
        answerProgressText = (TextView) findViewById(R.id.answerProgressTextView);
        levelProgressText = (TextView) findViewById(R.id.levelPercentageTextView);
        levelText = (TextView) findViewById(R.id.levelText);
        categoryTextView = (TextView) findViewById(R.id.categoryText);
        callerTextView = (TextView) findViewById(R.id.callerText);

        onboardingLayout = (RelativeLayout) findViewById(R.id.onboardingView);
        onboardingTextView = (TextView) findViewById(R.id.onboardTextView);
        onboardingButton = (FButton) findViewById(R.id.onboardButton);

        bottomBar = findViewById(R.id.bottomBar);

        infoView = (TextView)findViewById(R.id.infoView);

        Bundle extras = getIntent().getExtras();
        int rightCount = 0;
        float timeUsed = 0;

        if(extras != null){
            if(extras != null){
                indices = (ArrayList)extras.get("Quizs");
                categoryName = extras.get("Category").toString();
                userChoices = (ArrayList)extras.get("Choices");
                score = (int)extras.get("Score");
                timeUsed = (int)extras.get("Time");
                rightCount = (int)extras.get("RightCount");
            }
        }

        showTitleAndBackOnly(categoryName + " Knowledge Challenge");
        changeNavigationBarColor(categoryName, backgroundView);

        int myScore = CenterController.controller().playerScores.get(categoryName);

        CenterController.controller().score(this, categoryName, score, rightCount);
        levelText.setText("Level " + myScore + score / 100);
        categoryTextView.setText(categoryName);
        callerTextView.setText(CenterController.controller().getCaller(this, AchievementSystem.categoryTypeFromName(categoryName)));

        int colorRef = 0;
        int colorProgress = 0;

        AchievementSystem.CategoryType categoryType = AchievementSystem.categoryTypeFromName(categoryName);
        imageView.setBackground(ResourceTool.getIdentityIcon(this, categoryType));
        colorRef = ResourceTool.getColorRef(this, categoryType);
        colorProgress = ResourceTool.getShadowColor(this, categoryType);

        bottomBar.setBackgroundColor(colorRef);
        levelProgress.setProgressColor(colorProgress);
        levelProgress.setProgressBackgroundColor(colorRef);

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizFinishActivity.this, QuizStartActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("level", "1");
                startActivity(intent);
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizFinishActivity.this, QuizActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("reviewMode", true);
                intent.putExtra("Quizs", indices);
                intent.putExtra("Choices", userChoices);
                startActivity(intent);
            }
        });

        infoView.setText("Time " + String.format("%d:%02d", (int) (timeUsed / 60), (int) (timeUsed % 60)) + "   Bonus "+ (score-rightCount*baseScore));
        //categoryTextView.setText(categoryName + "\n" + "Time:" + String.format("%02d:%02d", (int) (timeUsed / 60), (int) (timeUsed % 60)));
        scoreProgressText.setText("Score:" + score);
        levelText.setText("Level "+  ((myScore+score)/CenterController.controller().scorePerLevel));
        levelProgressText.setText("Level Progress " + String.format("%.1f", ((((float) myScore + score) % CenterController.controller().scorePerLevel) / CenterController.controller().scorePerLevel) * 100) + "%");
        answerProgressText.setText("Answered " + rightCount + " out of " + maxQuizCount);
        scoreProgress.setMax(((baseScore + 20) * maxQuizCount));
        answerProgress.setMax((float) maxQuizCount);
        levelProgress.setMax(CenterController.controller().scorePerLevel);

        ObjectAnimator anim = ObjectAnimator.ofFloat(scoreProgress, "progress", 0f, score);
        anim.setDuration(2000);
        anim.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(answerProgress, "progress", 0f, rightCount);
        anim2.setDuration(2000);
        anim2.start();

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(levelProgress, "progress", 0f, (myScore+score)%CenterController.controller().scorePerLevel);
        anim3.setDuration(2000);
        anim3.start();

        if(CenterController.controller().firstOnboardingShown){
            onboardingLayout.setVisibility(View.INVISIBLE);
        }else{
            Button nextButton = (Button)findViewById(R.id.onboardButton);
            replayButton.setEnabled(false);
            summaryButton.setEnabled(false);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuizFinishActivity.this, OnBoardingFinishActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
