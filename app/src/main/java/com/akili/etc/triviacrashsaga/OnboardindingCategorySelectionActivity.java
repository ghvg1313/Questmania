package com.akili.etc.triviacrashsaga;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengePreTextActivity;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import extension.ResourceTool;
import extension.SoloModeActivity;

public class OnboardindingCategorySelectionActivity extends SoloModeActivity {

    public enum CategorySelectionContext{
        ONBOARDING,
        ADD_INTEREST,
        ALL_INTEREST,
        PERSONAL_CHALLENGE
    }

    public CategorySelectionContext context = CategorySelectionContext.ONBOARDING;
    private ImageButton button1, button2, button3, button4, button5, button6;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6;
    private ImageButton[] buttons;
    private TextView[] textViews;
    private View onboardingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboardinding_category_selection);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            context = (CategorySelectionContext)extras.get("context");
        }

        button1 = (ImageButton)findViewById(R.id.icon1);
        button2 = (ImageButton)findViewById(R.id.icon2);
        button3 = (ImageButton)findViewById(R.id.icon3);
        button4 = (ImageButton)findViewById(R.id.icon4);
        button5 = (ImageButton)findViewById(R.id.icon5);
        button6 = (ImageButton)findViewById(R.id.icon6);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView6 = (TextView)findViewById(R.id.textView6);
        onboardingView = (RelativeLayout)findViewById(R.id.onboardingLayout);

        if(context != CategorySelectionContext.ONBOARDING){
            ((RelativeLayout)onboardingView.getParent()).removeView(onboardingView);
        }

        buttons = new ImageButton[]{button1, button2, button3, button4, button5, button6};
        for(ImageButton button : buttons){
            button.setScaleType(ImageView.ScaleType.FIT_XY);
            button.setBackgroundColor(getResources().getColor(R.color.float_transparent));
        }
        textViews = new TextView[]{textView1, textView2, textView3, textView4, textView5, textView6};

        if(context == CategorySelectionContext.PERSONAL_CHALLENGE){
            for(int i=0;i<buttons.length;i++){
                buttons[i].setImageDrawable(ResourceTool.getColoredChallengeIcon(this, AchievementSystem.categoryTypes.get(i)));
                Challenge challenge = CenterController.controller().personalChallenge.get(AchievementSystem.categoryNameFromType(AchievementSystem.categoryTypes.get(i))).get(0);
                textViews[i].setText(challenge.challengeTitle);
                showTitleAndBackOnly("Browse Challenges");
            }
        } else if(context == CategorySelectionContext.ALL_INTEREST || context == CategorySelectionContext.ONBOARDING){
            for(int i=0;i<buttons.length;i++){
                buttons[i].setImageDrawable(ResourceTool.getIdentityIcon(this,AchievementSystem.categoryTypes.get(i)));
                textViews[i].setText(AchievementSystem.categoryNames.get(i));
                showTitleAndBackOnly("Browse Identities");
            }
        }else if(context == CategorySelectionContext.ADD_INTEREST){
            int index = 0;
            for(int i=0;i<AchievementSystem.categoryTypes.size();i++){
                if(!CenterController.controller().playerInterests.contains(AchievementSystem.categoryTypes.get(i))){
                    buttons[index].setImageDrawable(ResourceTool.getIdentityIcon(this,AchievementSystem.categoryTypes.get(i)));
                    textViews[index].setText(AchievementSystem.categoryNames.get(i));
                    index ++;
                }
            }
            if(index == 0){
                AchievementSystem.CategoryType type = CenterController.controller().playerInterests.get(CenterController.controller().playerInterests.size()-1);
                buttons[0].setImageDrawable(ResourceTool.getIdentityIcon(this,type));
                textViews[0].setText(AchievementSystem.categoryNameFromType(type));
            }
            showTitleAndBackOnly("Browse Interests");
        }

        for(int i=0;i<6;i++){
            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIconClicked(index);
                }
            });
        }

    }

    private void onIconClicked(int index)
    {
        String categoryName = "";
        categoryName = AchievementSystem.categoryNameFromType(AchievementSystem.categoryTypes.get(index));
        if(context == CategorySelectionContext.ADD_INTEREST){
            categoryName = textViews[index].getText().toString();
        }

        if(categoryName.length() == 0)
            return;

        if(context == CategorySelectionContext.ONBOARDING ) {
            CenterController.controller().changeCategoryInterest(AchievementSystem.categoryTypeFromName(categoryName));
            Intent intent = new Intent(OnboardindingCategorySelectionActivity.this, QuizStartActivity.class);
            intent.putExtra("categoryName", categoryName);
            startActivity(intent);
        }else if(context == CategorySelectionContext.PERSONAL_CHALLENGE){
            Intent intent = new Intent(this, ChallengePreTextActivity.class);
            intent.putExtra("categoryName", categoryName);
            intent.putExtra("challengeIndex", 0);
            startActivity(intent);
        }else if(context == CategorySelectionContext.ALL_INTEREST){
            Intent intent = new Intent(OnboardindingCategorySelectionActivity.this, QuizStartActivity.class);
            intent.putExtra("categoryName", categoryName);
            startActivity(intent);
        }else if(context == CategorySelectionContext.ADD_INTEREST){
            CenterController.controller().addCategoryInterest(AchievementSystem.categoryTypeFromName(categoryName));
            onBackPressed();
        }
    }
}
