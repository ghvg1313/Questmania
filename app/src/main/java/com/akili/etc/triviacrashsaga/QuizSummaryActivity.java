package com.akili.etc.triviacrashsaga;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akili.etc.triviacrashsaga.Entity.Quiz;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.andtinder.view.CardContainer;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

public class QuizSummaryActivity extends AppCompatActivity {

    private Quiz currentQuiz;
    private String categoryName;
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    private Button rightAnswerButton,nextButton;
    private Button[] buttons;
    private TextSwitcher quizView;
    private ArrayList<Quiz> quizs;
    private int quizIndex = 0;
    final static int quizViewExpansionHeight = 300;
    private int  maxQuizIndex = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_summary);

        answerButton1 = (Button)findViewById(R.id.answer1);
        answerButton2 = (Button)findViewById(R.id.answer2);
        answerButton3 = (Button)findViewById(R.id.answer3);
        answerButton4 = (Button)findViewById(R.id.answer4);
        nextButton = (Button)findViewById(R.id.nextButton);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            quizs = (ArrayList<Quiz>)extras.get("Quizs");
            categoryName = extras.get("Category").toString();
        }

        currentQuiz = quizs.get(quizIndex);
        buttons = new Button[]{answerButton1, answerButton2, answerButton3, answerButton4};
        rightAnswerButton = buttons[currentQuiz.correctIndex];
        quizView = (TextSwitcher)findViewById(R.id.quizView);
        quizView.setFactory(switcherFactory);
        quizView.setBackgroundColor(Color.WHITE);
        quizView.setText(currentQuiz.quizContent);
        setAnswerButtonsWithQuiz(currentQuiz);
        rightAnswerButton.setBackgroundColor(Color.GREEN);
        String answerbutton=QuizActivity.selectedAnswers.get(0);
        if(!rightAnswerButton.toString().contains(answerbutton.trim())) {
            for (int i = 0; i < 4; i++) {
                if (buttons[i].toString().contains(answerbutton.trim())) {
                    buttons[i].setBackgroundColor(Color.RED);
                }
            }
        }
//
//        CardContainer mCardContainer = (CardContainer) findViewById(R.id.layoutview);
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View buttonLayout = layoutInflater.inflate(R.layout.activity_quiz_summary, null);
//        mCardContainer.addView(buttonLayout);

        //int index=Collections.indexOfSubList(Arrays.asList(buttons), Arrays.asList(answerbutton));
        //System.out.println(index);
        //buttons[index].setBackgroundColor(Color.RED);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestion();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TextView Factory for quizView TextSwitcher
    private ViewSwitcher.ViewFactory switcherFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            // Create a new TextView
            TextView t = new TextView(QuizSummaryActivity.this);
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            t.setTypeface(null, Typeface.BOLD_ITALIC);
            t.setTextSize(20f);
            t.setHeight(quizViewExpansionHeight);
            t.setBackgroundColor(Color.TRANSPARENT);
            return t;
        }
    };

    private void setAnswerButtonsWithQuiz(Quiz quiz){
        answerButton1.setText(quiz.choices.get(0));
        answerButton2.setText(quiz.choices.get(1));
        answerButton3.setText(quiz.choices.get(2));
        answerButton4.setText(quiz.choices.get(3));
        answerButton1.setBackgroundResource(android.R.drawable.btn_default);
        answerButton2.setBackgroundResource(android.R.drawable.btn_default);
        answerButton3.setBackgroundResource(android.R.drawable.btn_default);
        answerButton4.setBackgroundResource(android.R.drawable.btn_default);

    }

    // Go to the next question
    private void goToNextQuestion() {
        // Go to summary when timeout
        if(quizIndex == maxQuizIndex){
            Intent intent = new Intent(this, QuizFinishActivity.class);
          //  intent.putExtra("Score", quizScore);
            startActivity(intent);
            return;
        }

        quizIndex += 1;
        currentQuiz = quizs.get(quizIndex);
        quizView.setText(currentQuiz.quizContent);
        setAnswerButtonsWithQuiz(currentQuiz);
        String answer = QuizActivity.selectedAnswers.get(quizIndex);
        rightAnswerButton = buttons[currentQuiz.correctIndex];
        rightAnswerButton.setBackgroundColor(Color.GREEN);
        if(!rightAnswerButton.toString().contains(answer.trim())) {
            for (int i = 0; i < 4; i++) {
                if (buttons[i].toString().contains(answer.trim())) {
                    buttons[i].setBackgroundColor(Color.RED);
                }
            }
        }
    }
}
