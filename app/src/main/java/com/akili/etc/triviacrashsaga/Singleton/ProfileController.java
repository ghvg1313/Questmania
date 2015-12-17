package com.akili.etc.triviacrashsaga.Singleton;

import com.akili.etc.triviacrashsaga.Entity.PersonalRecord;

import java.util.ArrayList;

/**
 * Created by kangleif on 12/3/2015.
 */
public class ProfileController {
    private static ProfileController ourInstance = new ProfileController();

    public static ArrayList<PersonalRecord> journalist = new ArrayList<>();

    public static ArrayList<PersonalRecord> latestRecords = new ArrayList<>();

    public static ProfileController controller() {
        return ourInstance;
    }

    public static void addToJournalist(String title, String content){
        ProfileController.controller().journalist.add(new PersonalRecord(title, content));
    }

    public static void addToLatest(String content){
        ProfileController.controller().latestRecords.add(new PersonalRecord(content));
    }
}
