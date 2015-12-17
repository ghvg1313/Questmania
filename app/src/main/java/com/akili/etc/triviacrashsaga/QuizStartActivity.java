package com.akili.etc.triviacrashsaga;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengePreTextActivity;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;

import org.w3c.dom.Text;

import extension.ResourceTool;
import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class QuizStartActivity extends SoloModeActivity {

    private enum OnboardStage{
        stage1,
        stage2,
        stage3,
        stage4,
        stage5,
        stage6
    }

    private ImageView imageView;
    private Button playButton, challengeButton, challengebutton2;
    private FButton addInterestButton;
    private TextView categoryTextView, levelText, callerText, descriptionText, percentageText, onboardingTextView;
    private String categoryName;
    private View backgroundView, bottomDivider, onboardingView;
    private Category category;
    private AchievementSystem.CategoryType categoryType;

    private ImageView arrow1, arrow2, arrow3;

    private OnboardStage onboardStage = OnboardStage.stage1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        imageView = (ImageView)findViewById(R.id.QuizFinishImageView);
        playButton = (Button)findViewById(R.id.playButton);
        callerText = (TextView)findViewById(R.id.callerTextView);
        challengeButton = (Button)findViewById(R.id.challengeButton);
        challengebutton2 = (Button)findViewById(R.id.challengeButton2);
        categoryTextView = (TextView)findViewById(R.id.categoryText);
        levelText = (TextView)findViewById(R.id.levelText);
        backgroundView = findViewById(R.id.backgroundView);
        descriptionText = (TextView) findViewById(R.id.categoryDescriptionView);
        percentageText = (TextView) findViewById(R.id.categoryPercentageTextView);
        bottomDivider = findViewById(R.id.bottomBar);
        onboardingView = findViewById(R.id.onboardingView);
        onboardingTextView = (TextView)findViewById(R.id.onboardTextView);
        addInterestButton = (FButton)findViewById(R.id.addInterestButton);

        arrow1 = (ImageView)findViewById(R.id.arrow1);
        arrow2 = (ImageView)findViewById(R.id.arrow2);
        arrow3 = (ImageView)findViewById(R.id.arrow3);

        challengebutton2.setEnabled(false);

        RoundCornerProgressBar progressBar = (RoundCornerProgressBar) findViewById(R.id.levelProgress);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            categoryName = extras.get("categoryName").toString();
            levelText.setText("Level " + CenterController.controller().playerScores.get(categoryName)/CenterController.controller().scorePerLevel);
            callerText.setText(CenterController.controller().getCaller(this, AchievementSystem.categoryTypeFromName(categoryName)));
            categoryTextView.setText(extras.get("categoryName").toString());
            int colorRef = 0;
            int colorProgress = 0;
            if(extras.getBoolean("Unlock")){
                CenterController.controller().completeChallenge(this,categoryName);
                BackGroundController.playBadgeBGM(this);
            }

            categoryType = AchievementSystem.categoryTypeFromName(categoryName);
            challengeButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ResourceTool.getChallengeIcon(this, categoryType));
            imageView.setBackground(ResourceTool.getIdentityIcon(this, categoryType));
            backgroundView.setBackground(ResourceTool.getBackGroundImage(this, categoryType));
            descriptionText.setText(ResourceTool.getQuizTitle(categoryType));
            colorRef = ResourceTool.getColorRef(this, categoryType);
            colorProgress = ResourceTool.getShadowColor(this, categoryType);

            category = CenterController.controller().categoryMap.get(categoryName);
            bottomDivider.setBackgroundColor(colorRef);
            progressBar.setProgressColor(colorProgress);
            progressBar.setProgressBackgroundColor(colorRef);

            showTitleAndBackOnly(categoryName);
            changeNavigationBarColor(categoryName, backgroundView);
        }

        addInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterController.controller().changeCategoryInterest(categoryType);
                validateInterestButton();
            }
        });
        validateInterestButton();

        progressBar.setMax(CenterController.controller().scorePerLevel);
        progressBar.setProgress(10);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(progressBar, "progress", 0f, 100*(1.0f * (((CenterController.controller().playerScores.get(categoryName))%CenterController.controller().scorePerLevel) / CenterController.controller().scorePerLevel)));
        anim3.setDuration(2000);
        anim3.start();
        percentageText.setText("Level Progress "+ String.format("%.1f", (((CenterController.controller().playerScores.get(categoryName) * 1.0f)%CenterController.controller().scorePerLevel) / CenterController.controller().scorePerLevel)*100.0f) + "%");

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, QuizActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("reviewMode", false);
                startActivity(intent);
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, ChallengePreTextActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("challengeIndex", 0);
                startActivity(intent);
            }
        });

        challengebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartActivity.this, ChallengePreTextActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("challengeIndex", 0);
                startActivity(intent);
            }
        });

        TranslateAnimation mAnimation = new TranslateAnimation(0, 0, -15, 10);
        mAnimation.setDuration(2000);
        mAnimation.setFillAfter(true);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        arrow1.setAnimation(mAnimation);
        arrow2.setAnimation(mAnimation);
        arrow3.setAnimation(mAnimation);

        arrow1.setVisibility(View.GONE);
        arrow2.setVisibility(View.GONE);
        arrow3.setVisibility(View.GONE);

        if(CenterController.controller().firstOnboardingShown){
            onboardingView.setVisibility(View.INVISIBLE);
        }else{
            Button nextButton = (Button)findViewById(R.id.onboardButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnboardingView();
                }
            });
            setOnboardingView();
        }
    }

    private void validateInterestButton()
    {
        if(CenterController.controller().playerInterests.contains(categoryType)){
            addInterestButton.setText("Remove from Interest");
            addInterestButton.setButtonColor(getResources().getColor(R.color.disable_gray));
            addInterestButton.setShadowColor(getResources().getColor(R.color.disable_gray_shadow));
        }else{
            addInterestButton.setText("+ Add as Interest");
            addInterestButton.setButtonColor(getResources().getColor(R.color.trivia_quiz_next_button_color));
            addInterestButton.setShadowColor(getResources().getColor(R.color.trivia_quiz_next_button_shadow_color));
        }
    }

    private void setOnboardingView()
    {
        RelativeLayout maskView = (RelativeLayout)findViewById(R.id.maskView);
        if(onboardStage == OnboardStage.stage1){
            maskView.setBackground(getResources().getDrawable(R.drawable.onboarding_overlay_1));
            onboardingTextView.setText("Great choice!");
            addInterestButton.setEnabled(false);
            playButton.setEnabled(false);
            challengeButton.setEnabled(false);
            challengebutton2.setEnabled(false);
            onboardStage = OnboardStage.stage2;

        }else if(onboardStage == OnboardStage.stage2){
            maskView.setBackground(getResources().getDrawable(R.drawable.onboarding_overlay_2));
            onboardingTextView.setText("Here you can practice the knowledge and skills that "+categoryName+"s need by playing challenges.");
            onboardStage = OnboardStage.stage3;
        }else if(onboardStage == OnboardStage.stage3){
            onboardingTextView.setText("Do enough, and you will master the Identity.");
            onboardStage = OnboardStage.stage4;
        }else if(onboardStage == OnboardStage.stage4){
            maskView.setBackground(getResources().getDrawable(R.drawable.onboarding_overlay_3));
            onboardingTextView.setText("Knowledge Challenges test you in knowledge that "+categoryName+"s use.");
            arrow1.setVisibility(View.GONE);
            onboardStage = OnboardStage.stage5;
        }else if(onboardStage == OnboardStage.stage5){
            maskView.setBackground(getResources().getDrawable(R.drawable.onboarding_overlay_4));
            onboardingTextView.setText("Skill Challenges let you practice skills used by  " + categoryName + "s.");
            arrow1.setVisibility(View.GONE);
            //arrow2.setVisibility(View.VISIBLE);
            //arrow3.setVisibility(View.VISIBLE);
            onboardStage = OnboardStage.stage6;
        }else if(onboardStage == OnboardStage.stage6){
            maskView.setVisibility(View.GONE);
            Button button = (Button)findViewById(R.id.onboardButton);
            button.setVisibility(View.GONE);
            onboardingTextView.setText("Tap the Knowledge Challenge to begin!");
            arrow1.setVisibility(View.VISIBLE);
            arrow2.clearAnimation();
            arrow3.clearAnimation();
            arrow2.setVisibility(View.GONE);
            arrow3.setVisibility(View.GONE);
            playButton.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if(!CenterController.controller().firstOnboardingShown) return;
            Intent intent = new Intent(QuizStartActivity.this, RootCategoryActivity.class);
            startActivity(intent);
    }
}
