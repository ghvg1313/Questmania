package com.akili.etc.triviacrashsaga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Adapter.RootCategoryAdapterItem;
import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.RootCategoryRowItem;
import com.akili.etc.triviacrashsaga.PartyMode.GetPlayerNumber;
import com.akili.etc.triviacrashsaga.PartyMode.PartyHintActivity;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.google.android.gms.games.achievement.Achievement;

import java.util.ArrayList;
import java.util.Random;

import extension.ResourceTool;
import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;

public class RootCategoryActivity extends SoloModeActivity {

    ImageButton button1, button2, button3, button4, button5, button6;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    ImageButton[] buttons;
    FButton featureedInterestButton,seeAllButton, practiseButton, profileButton;
    TextView[] textViews;

    private TextView challengeDescriptionTextView;
    private ImageButton challengeButton;
    private TextView challengeTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_category);
        getView();
        setView();
        randomizeChallenge();
        showTitleAndBackOnly("Solo Play");

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        setView();
        randomizeChallenge();

    }

    void getView()
    {
        button1 = (ImageButton)findViewById(R.id.imageButton1);
        button2 = (ImageButton)findViewById(R.id.imageButton2);
        button3 = (ImageButton)findViewById(R.id.imageButton3);
        button4 = (ImageButton)findViewById(R.id.imageButton4);
        button5 = (ImageButton)findViewById(R.id.imageButton5);
        button6 = (ImageButton)findViewById(R.id.imageButton6);

        buttons = new ImageButton[]{button1, button2, button3, button4, button5, button6};

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView6 = (TextView)findViewById(R.id.textView6);

        textViews = new TextView[]{textView1, textView2, textView3, textView4, textView5, textView6};

        challengeDescriptionTextView = (TextView)findViewById(R.id.challengeDescriptionText);

        challengeTitleTextView = (TextView)findViewById(R.id.challengeTitleTextView);
        challengeButton = (ImageButton)findViewById(R.id.challengeButton);

        featureedInterestButton = (FButton)findViewById(R.id.seeMoreButton);
        practiseButton = (FButton)findViewById(R.id.practiseButton);
        profileButton = (FButton)findViewById(R.id.profileButton);
        seeAllButton = (FButton)findViewById(R.id.allInterestButton);
    }

    void setView()
    {
        ArrayList<AchievementSystem.CategoryType> personalInterests = CenterController.controller().playerInterests;
        int imageButtonCount = Math.min(personalInterests.size(), 5);

        int imageButtonIndex = 0;

        for(;imageButtonIndex<imageButtonCount;imageButtonIndex++){
            final AchievementSystem.CategoryType type = personalInterests.get(imageButtonIndex);
            int level = CenterController.controller().getLevel(type);
            buttons[imageButtonIndex].setImageDrawable(ResourceTool.getIdentityIcon(this, type));
            textViews[imageButtonIndex].setText(AchievementSystem.categoryNameFromType(type) + "\nLevel " + level);
            buttons[imageButtonIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RootCategoryActivity.this, QuizStartActivity.class);
                    intent.putExtra("categoryName", AchievementSystem.categoryNameFromType(type));
                    startActivity(intent);
                }
            });
        }

        for(;imageButtonIndex<buttons.length;imageButtonIndex++){
            buttons[imageButtonIndex].setImageDrawable(getResources().getDrawable(R.drawable.root_no_interest));
            textViews[imageButtonIndex].setText("");
        }

        buttons[5].setImageDrawable(getResources().getDrawable(R.drawable.home_add_interest_icon));
        textViews[5].setText("Add an Interest!");
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, OnboardindingCategorySelectionActivity.class);
                intent.putExtra("context", OnboardindingCategorySelectionActivity.CategorySelectionContext.ADD_INTEREST);
                startActivity(intent);
            }
        });

        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, OnboardindingCategorySelectionActivity.class);
                intent.putExtra("context", OnboardindingCategorySelectionActivity.CategorySelectionContext.ALL_INTEREST);
                startActivity(intent);
            }
        });

        practiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, OnboardindingCategorySelectionActivity.class);
                intent.putExtra("context", OnboardindingCategorySelectionActivity.CategorySelectionContext.PERSONAL_CHALLENGE);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    //Featured
    void randomizeChallenge()
    {
        ArrayList<AchievementSystem.CategoryType> featuredInterests = new ArrayList<>();
        for(int i=0;i<AchievementSystem.categoryTypes.size();i++){
            if(!CenterController.controller().playerInterests.contains(AchievementSystem.categoryTypes.get(i))){
                featuredInterests.add(AchievementSystem.categoryTypes.get(i));
            }
        }
        if(featuredInterests.size()==0)
            featuredInterests = AchievementSystem.categoryTypes;
        Random randomizer = new Random();
        int resultIndex = randomizer.nextInt(featuredInterests.size());
        final AchievementSystem.CategoryType category = featuredInterests.get(resultIndex);
        //Category categoryEntry = CenterController.controller().categoryMap.get(AchievementSystem.categoryNameFromType(category));

        challengeButton.setImageDrawable(ResourceTool.getIdentityIcon(this, category));
        challengeTitleTextView.setText(AchievementSystem.categoryNameFromType(category));
        challengeDescriptionTextView.setText(ResourceTool.getQuizTitle(category));
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, QuizStartActivity.class);
                intent.putExtra("categoryName", AchievementSystem.categoryNameFromType(category));
                startActivity(intent);
            }
        });
        featureedInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootCategoryActivity.this, QuizStartActivity.class);
                intent.putExtra("categoryName", AchievementSystem.categoryNameFromType(category));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RootCategoryActivity.this, MainMenuActivity.class));
    }
}
