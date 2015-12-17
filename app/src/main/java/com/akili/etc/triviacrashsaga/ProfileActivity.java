package com.akili.etc.triviacrashsaga;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;

import org.w3c.dom.Text;

import extension.SoloModeActivity;
import info.hoang8f.widget.FButton;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class ProfileActivity extends SoloModeActivity implements MaterialTabListener{

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FButton achievementButton, latestButton, chamyButton;
    private MaterialTabHost tabHost;
    private TextView levelText, nameText;

    public ProfileActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        showTitleAndBackOnly("Profile");

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }

        levelText = (TextView) findViewById(R.id.levelText);
        nameText = (TextView) findViewById(R.id.nameText);

        String levelString = "";
        for(int i=0;i< AchievementSystem.categoryNames.size();i++) {
            String categoryName = AchievementSystem.categoryNames.get(i);
            AchievementSystem.CategoryType categoryType = AchievementSystem.categoryTypeFromName(categoryName);
            levelString += "Level " + (CenterController.controller().playerScores.get(categoryName) / CenterController.scorePerLevel);
            levelString += " " + CenterController.controller().getCaller(this, categoryType);
            levelString += "\n";
        }
        levelText.setText(levelString);
        levelText.setText("");

        nameText.setText("Abdullah");

//        achievementButton = (FButton) findViewById(R.id.achievementButton);
//        latestButton = (FButton) findViewById(R.id.latestButton);
//        chamyButton = (FButton) findViewById(R.id.chamyButton);

//        achievementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//            }
//        });
//
//        latestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(1);
//            }
//        });
//
//        chamyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(2);
//            }
//        });
    }

    private Drawable getIcon(int position){
        if(position == 0)
            return getResources().getDrawable(R.drawable.profile_achievements);
        if(position == 1)
            return getResources().getDrawable(R.drawable.profile_latest);
        if(position == 2)
            return getResources().getDrawable(R.drawable.profile_chamy);

        return getResources().getDrawable(R.drawable.profile_tab_achievements);
    }

    private String getTabTitle(int position){
        if(position == 0)
            return "Achievements";
        if(position == 1)
            return "Latest";
        if(position == 2)
            return "Journal";

        return "";
    }


    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        return;

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new BadgeProfilePage();
            }else if(position == 1){
                LatestProfilePage page = new LatestProfilePage();
                page.type = LatestProfilePage.LatestType.LATEST;
                return page;
            }else {
                LatestProfilePage page = new LatestProfilePage();
                page.type = LatestProfilePage.LatestType.JOURNAL;
                return page;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
