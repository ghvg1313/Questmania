package com.akili.etc.triviacrashsaga.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangleif on 9/23/2015.
 */
public class Quiz implements Serializable {
    public String quizContent;
    public ArrayList<String> choices;
    public int correctIndex;
    public String questString;

    public Quiz(String quizContent, String choice1, String choice2, String choice3, String choice4, int correctIndex){
        this.quizContent = quizContent;
        setChoices(choice1, choice2,choice3,choice4);
        this.correctIndex = correctIndex;
    }

    public Quiz(String quizContent, String choice1, String choice2, String choice3, String choice4, int correctIndex, String quest){
        this.quizContent = quizContent;
        setChoices(choice1, choice2,choice3,choice4);
        this.correctIndex = correctIndex;
        questString = quest;
    }

    public void setChoices(String choice1, String choice2, String choice3, String choice4){
        choices = new ArrayList<String>();
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);
        choices.add(choice4);
    }
}
