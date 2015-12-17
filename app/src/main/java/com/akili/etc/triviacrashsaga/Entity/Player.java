package com.akili.etc.triviacrashsaga.Entity;

/**
 * Created by kangleif on 10/30/2015.
 */
public class Player {
    public int partyModeScore = 0;
    public String name;

    public Player(String name){
        this.name = name;
        partyModeScore = 0;
    }

    public Player(String name, int score){
        this.name = name;
        partyModeScore = score;
    }

}
