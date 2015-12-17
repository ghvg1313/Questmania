package extension;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengePreTextActivity;
import com.akili.etc.triviacrashsaga.ProfileActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.rey.material.app.ThemeManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kangleif on 10/27/2015.
 */
public class SoloModeActivity extends AppCompatActivity implements ActionBar.TabListener{

    private boolean showButtons = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.init(this, 1, 0, null);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

//        getSupportActionBar()
//                .setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        ActionBar.Tab newTab0 = getSupportActionBar().newTab();
//        newTab0.setText("Tab 0 title");
//        ActionBar.Tab newTab1 = getSupportActionBar().newTab();
//        newTab1.setText("Tab 1 title");
//
//        newTab0.setTabListener(this);
//        newTab1.setTabListener(this);
//
//        getSupportActionBar().addTab(newTab0);
//        getSupportActionBar().addTab(newTab1);

        //setHasEmbeddedTabs(getSupportActionBar(), true);
    }

    protected void showTitleAndBackOnly(String title)
    {
        showTitle(title);
        showBackButton();
        hideActionButtons();
    }

    public void hideNavigationBar(){
        getSupportActionBar().hide();
    }

    public void changeNavigationBarColor(String categoryName, View backgroundView){
        AchievementSystem.CategoryType type = AchievementSystem.categoryTypeFromName(categoryName);
        changeNavigationBarColor(type, backgroundView);
    }

    public void changeNavigationBarColor(AchievementSystem.CategoryType type, View backgroundView){
        if(type == AchievementSystem.CategoryType.DOCTOR){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#2196f3")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_doctor));
        }else if(type == AchievementSystem.CategoryType.WRITER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_writer));
        }else if(type == AchievementSystem.CategoryType.TEACHER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#673ab7")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_teacher));
        }else if(type == AchievementSystem.CategoryType.GAMER){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_writer));
        }
        else if(type == AchievementSystem.CategoryType.JOURNALIST){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#8bc34a")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_writer));
        }
        else if(type == AchievementSystem.CategoryType.ARTIST){
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable(Color.parseColor("#673ab7")));
            backgroundView.setBackground(getResources().getDrawable(R.drawable.background_teacher));
        }

    }

    protected void showTitle(String title){

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    protected void showBackButton()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void hideActionButtons()
    {
        showButtons = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(!showButtons){
            MenuItem homeButton = menu.findItem(R.id.action_home);
            MenuItem profileButton = menu.findItem(R.id.action_profile);
            if(homeButton!=null)homeButton.setVisible(false);
            if(profileButton!=null)profileButton.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home) {
            // app icon in action bar clicked; goto parent activity.
//            onBackPressed();
            Intent intent = new Intent(SoloModeActivity.this, MainMenuActivity.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = new Intent(SoloModeActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    /**
     * On Android 3.0 and above, while using the ActionBar tabbed navigation style, the tabs sometimes appear above the action bar.
     * This helper method allows you to control the 'hasEmbeddedTabs' behaviour.
     * A value of true will put the tabs inside the ActionBar, a value of false will put it above or below the ActionBar.
     * <p/>
     * You should call this method while initialising your ActionBar tabs.
     * Don't forget to also call this method during orientation changes (in the onConfigurationChanged() method).
     *
     * @param inActionBar
     * @param inHasEmbeddedTabs
     */
    public static void setHasEmbeddedTabs(Object inActionBar, final boolean inHasEmbeddedTabs) {
        // get the ActionBar class
        Class<?> actionBarClass = inActionBar.getClass();

        // if it is a Jelly Bean implementation (ActionBarImplJBMR2), get the super super class (ActionBarImplICS)
        if ("android.support.v7.app.ActionBarImplJBMR2".equals(actionBarClass.getName())) {
            actionBarClass = actionBarClass.getSuperclass().getSuperclass();
        }
        // if it is a Jelly Bean implementation (ActionBarImplJB), get the super class (ActionBarImplICS)
        else if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass.getName())) {
            actionBarClass = actionBarClass.getSuperclass();
        }

        try {
            // try to get the mActionBar field, because the current ActionBar is probably just a wrapper Class
            // if this fails, no worries, this will be an instance of the native ActionBar class or from the ActionBarImplBase class
            final Field actionBarField = actionBarClass.getDeclaredField("mActionBar");
            actionBarField.setAccessible(true);
            inActionBar = actionBarField.get(inActionBar);
            actionBarClass = inActionBar.getClass();
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (NoSuchFieldException e) {
        }

        try {
            // now call the method setHasEmbeddedTabs, this will put the tabs inside the ActionBar
            // if this fails, you're on you own <img draggable="false" class="emoji" alt="😉" src="http://s.w.org/images/core/emoji/72x72/1f609.png">
            final Method method = actionBarClass.getDeclaredMethod("setHasEmbeddedTabs", new Class[]{Boolean.TYPE});
            method.setAccessible(true);
            method.invoke(inActionBar, new Object[]{inHasEmbeddedTabs});
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        }
    }
}
