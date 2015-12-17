package extension;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.rey.material.app.ThemeManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kangleif on 10/27/2015.
 */
public class PartyModeActivity extends SoloModeActivity{

    @Override
    public void changeNavigationBarColor(AchievementSystem.CategoryType type, View backgroundView) {
        super.changeNavigationBarColor(type, backgroundView);
        backgroundView.setBackground(getResources().getDrawable(R.drawable.challenge_forum_background));
    }
}
