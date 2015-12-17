package com.akili.etc.triviacrashsaga;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.PartyMode.AddPlayerActivity;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengeInstructionActivity;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengePreTextActivity;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengeResultActivity;
import com.akili.etc.triviacrashsaga.PartyMode.GetPlayerNumber;
import com.akili.etc.triviacrashsaga.PartyMode.PartyHintActivity;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.akili.etc.triviacrashsaga.SlideMenu.ContentFragment;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import extension.FontsOverride;
import extension.SoloModeActivity;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainMenuActivity extends SoloModeActivity{

    private List<SlideMenuItem> list = new ArrayList<>();
    private int res = R.drawable.content_music;
    public static char modeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        FontsOverride.setDefaultFont(this, "DEFAULT", "roboto.ttf");
        Button myButton = (Button) findViewById(R.id.button);
        Button partyButton = (Button) findViewById(R.id.button2);
        Button testButton = (Button) findViewById(R.id.button3);
        ImageButton creditButton = (ImageButton)findViewById(R.id.creditButton);

        hideNavigationBar();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect = 'S';
                startActivity(new Intent(MainMenuActivity.this, RootCategoryActivity.class));
            }
        });
        partyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackGroundController.playNormalBGM(MainMenuActivity.this);
//                modeSelect = 'C';
//                startActivity(new Intent(MainMenuActivity.this, RootCategoryActivity.class));
                if (CenterController.startShown) {
                    startActivity(new Intent(MainMenuActivity.this, GetPlayerNumber.class));
                } else {
                    Intent intent = new Intent(MainMenuActivity.this, PartyHintActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                }
            }
        });

        creditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreditActivity.class);
                startActivity(intent);
            }
        });

        if(!CenterController.loaded) {
            CenterController.controller().initalizeDatabase(this);
            AchievementSystem.system().loadBadges(this);
            CenterController.loaded = true;
        }

        testButton.setVisibility(View.INVISIBLE);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> names = new ArrayList<String>();
                names.add("A");
                names.add("B");
                names.add("C");
                names.add("D");
                names.add("E");
                CenterController.controller().startPartyGame(names);
                PartyGame.players.get(0).partyModeScore = 100;
                PartyGame.players.get(1).partyModeScore = 500;
                PartyGame.players.get(2).partyModeScore = 200;
                PartyGame.players.get(3).partyModeScore = 1000;
                PartyGame.players.get(4).partyModeScore = 700;
                PartyGame.categoryName = "Game Designer";
                Intent intent = new Intent(MainMenuActivity.this, PartyHintActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        if(!CenterController.controller().firstOnboardingShown){
            Intent intent = new Intent(MainMenuActivity.this, OnboardingActivity.class);
            intent.putExtra("Type", OnboardingActivity.OnboardingType.welcomeTo);
            startActivity(intent);
        }

        BackGroundController.stop();
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }


//    private void setActionBar() {
//        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.custome_toolbar);
//        //setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
//                //R.drawable.ic_drawer, //nav menu toggle icon
//                R.string.app_name, // nav drawer open - description for accessibility
//                R.string.app_name // nav drawer close - description for accessibility
//        ) {
//
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                linearLayout.removeAllViews();
//                linearLayout.invalidate();
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
//                    viewAnimator.showMenuContent();
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//        };
//        drawerLayout.setDrawerListener(drawerToggle);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
