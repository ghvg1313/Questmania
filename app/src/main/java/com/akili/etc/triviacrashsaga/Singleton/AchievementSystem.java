package com.akili.etc.triviacrashsaga.Singleton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.BadgeItem;
import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.PersonalRecord;
import com.akili.etc.triviacrashsaga.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by kangleif on 11/21/2015.
 * Notice that this class is initialized after the first activity is initialized
 * Use this in other Singletons with caution
 */
public class AchievementSystem {

    public enum AchievementType{
        QUESTION10,
        QUESTION20,
        QUESTION30,
        LEVEL1,
        LEVELMAX,
        SCORE80,
        SCORE100,
        CHALLENGE1
    }

    public enum CategoryType{
        DOCTOR,
        TEACHER,
        WRITER,
        GAMER,
        JOURNALIST,
        ARTIST
    }

    //Record whether the user has unlocked the achievement
    public ArrayList<Boolean> achievementRecord = new ArrayList<>();

    public HashMap<String, BadgeItem> allBadges = new HashMap<>();

    //Size 3, doctor, teacher, writer
    public static ArrayList<String> categoryNames = new ArrayList<>();

    public static ArrayList<CategoryType> categoryTypes = new ArrayList<>();

    //Size max, different achievement names
    private static ArrayList<String> achievementName = new ArrayList<>();

    private static AchievementSystem ourInstance = new AchievementSystem();

    //Transfer String category name into category type
    public static CategoryType categoryTypeFromName(String categoryName)
    {
        if(categoryName.equals("Doctor"))
            return CategoryType.DOCTOR;
        else if(categoryName.equals("Teacher"))
            return CategoryType.TEACHER;
        else if(categoryName.equals("Writer"))
            return CategoryType.WRITER;
        else if(categoryName.equals("Game Designer"))
            return CategoryType.GAMER;
        else if(categoryName.equals("Journalist"))
            return CategoryType.JOURNALIST;
        else if(categoryName.equals("Artist"))
            return CategoryType.ARTIST;

        return CategoryType.DOCTOR;
    }

    public static String categoryNameFromType(CategoryType type){
        if(type == CategoryType.DOCTOR)
            return "Doctor";
        else if(type == CategoryType.WRITER)
            return "Writer";
        else if(type == CategoryType.TEACHER)
            return "Teacher";
        else if(type == CategoryType.GAMER)
            return "Game Designer";
        else if(type == CategoryType.JOURNALIST)
            return "Journalist";
        else if(type == CategoryType.ARTIST)
            return "Artist";
        return "Doctor";
    }

    public static AchievementSystem system() {
        return ourInstance;
    }

    private AchievementSystem() {
        categoryNames.add("Doctor");
        categoryNames.add("Teacher");
        categoryNames.add("Writer");
        categoryNames.add("Game Designer");
        categoryNames.add("Journalist");
        categoryNames.add("Artist");

        categoryTypes.add(CategoryType.DOCTOR);
        categoryTypes.add(CategoryType.TEACHER);
        categoryTypes.add(CategoryType.WRITER);
        categoryTypes.add(CategoryType.GAMER);
        categoryTypes.add(CategoryType.JOURNALIST);
        categoryTypes.add(CategoryType.ARTIST);
    }

    private int getAchievementIndex(AchievementType achievement, CategoryType category) {
        return (achievement.ordinal())*categoryNames.size()+category.ordinal();
    }

    private boolean achievementIsUnlocked(AchievementType achievement, CategoryType category){
        return achievementRecord.get(getAchievementIndex(achievement, category));
    }

    public boolean achievementUnlocked(Context context, AchievementType achievement, CategoryType category)
    {
        if(achievementIsUnlocked(achievement, category))
            return false;
        BadgeItem item = getBadge(achievement, category);
        CenterController.showDialogWithImage(context, "You earned the badge\n"+item.getTitle(), item.getDescription(), item.getImage());
        BackGroundController.playBadgeBGM(context);
        achievementRecord.set(getAchievementIndex(achievement, category), Boolean.TRUE);
        ProfileController.addToLatest("Earned the badge " + item.getTitle());
        return true;
    }

    //Similar method for category String name
    public boolean achievementUnlocked(Context context, AchievementType achievement, String categoryName)
    {
        CategoryType type = categoryTypeFromName(categoryName);
        boolean result = achievementUnlocked(context, achievement, type);
        saveData();
        return result;
    }

    public BadgeItem getBadge(AchievementType achievement, CategoryType category)
    {
        return allBadges.get(achievementName.get(getAchievementIndex(achievement, category)));
    }

    public ArrayList<BadgeItem> getAvailableBadges()
    {
        ArrayList<BadgeItem> list = new ArrayList<>();
        for(int i=0;i<achievementName.size(); i++) {
            if(achievementRecord.get(i)) {
                list.add(allBadges.get(achievementName.get(i)));
            }
        }
        return list;
    }

    public void loadBadges(Context context)
    {
        //Has total number of badges
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.badge_icons);
        TypedArray names = context.getResources().obtainTypedArray(R.array.badge_names);
        //Has total number/3 of descriptions, need to add Category Name
        TypedArray descriptions = context.getResources().obtainTypedArray(R.array.badge_descriptions);
        for(int i=0;i<descriptions.length(); i++) {
            for (int j = 0; j < categoryNames.size(); j++) {
                String descrition = descriptions.getString(i)+categoryNames.get(j);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgs.getResourceId(categoryNames.size() * i + j, -1));
                allBadges.put(names.getString(categoryNames.size() * i + j), new BadgeItem(bitmap, names.getString(categoryNames.size()*i+j), descrition));
                achievementName.add(names.getString(categoryNames.size()*i+j));
                achievementRecord.add(Boolean.FALSE);
            }
        }
        getData();
    }

    public void saveData(){
        Hawk.chain()
                .put("AchievementRecord", achievementRecord)
                .commit();
    }

    public void getData()
    {
        achievementRecord = Hawk.get("AchievementRecord", achievementRecord);
    }

}
