package com.akili.etc.triviacrashsaga.Entity;

import android.content.Intent;

import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kangleif on 11/5/2015.
 */
public class Category implements Serializable {

    public String name;
    public ArrayList<Quiz> quizPool;
    public ArrayList<Integer> answeredPool;
    public ArrayList<Integer> remainingQuizPool;
    public int rightCount = 0;

    private Integer lastPickIndex = -1;

    public Category(String name, ArrayList<Quiz> quiz){
        remainingQuizPool = new ArrayList<>();
        this.name = name;
        this.quizPool = (ArrayList<Quiz>)quiz.clone();
        for(int i=0;i<quizPool.size();i++){
            remainingQuizPool.add(i);
        }
        answeredPool = new ArrayList<>();
    }

    public Category(String name, ArrayList<Quiz> quiz, ArrayList<Integer> answeredIndexes){
        this(name, quiz);
        for(Integer index : answeredIndexes){
            answerIndex(index);
        }
    }

    //When user answered a quiz
    public void answerIndex(Integer index){
        if(answeredPool.indexOf((Integer)index) == -1 && remainingQuizPool.indexOf((Integer)index) != -1){
            answeredPool.add(index);
            remainingQuizPool.remove(index);
        }
    }

    public Quiz nextQuiz(){
        Random randomizer = new Random();
        if(remainingQuizPool.size()>0){
            int resultIndex = randomizer.nextInt(remainingQuizPool.size());
            int result = remainingQuizPool.get(resultIndex);
            answerIndex(result);
            lastPickIndex = result;
            return quizPool.get(result);
        } else{
            int resultIndex = randomizer.nextInt(quizPool.size()-1);
            lastPickIndex = resultIndex;
            return quizPool.get(resultIndex);
        }
    }

    public Quiz getQuiz(int index){
        return quizPool.get(index);
    }
}
