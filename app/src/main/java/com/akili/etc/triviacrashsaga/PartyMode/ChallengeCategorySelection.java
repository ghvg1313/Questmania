package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.QuizChallengeActivity;
import com.akili.etc.triviacrashsaga.QuizStartActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.rey.material.app.ThemeManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class  ChallengeCategorySelection extends AppCompatActivity {

    ImageButton button1, button2, button3, button4;
    TextView textView1, textView2, textView3, textView4,instructionView,chooseIdentity;
    ImageButton[] imageButtons;
    TextView[] textViews;

    private Integer categoryNumber;
    private String categoryName;
    private Integer textCount=0;

    private ArrayList<Integer> totalSize;
    private HashMap<Integer,String> categoryText = new HashMap<>();

    public static final String[] cateGoryName ={
            "Doctor","Teacher",
            "Writer","Artist",
            "Game Designer","Journalist"
    };

    public static final Integer[] categoryButton = {
            R.drawable.id_doctor, R.drawable.id_teacher,
            R.drawable.id_writer, R.drawable.id_artist,
            R.drawable.id_game_designer, R.drawable.id_journalist
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.init(this, 1, 0, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_category_selection);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        button1 = (ImageButton)findViewById(R.id.imageButton1);
        button2 = (ImageButton)findViewById(R.id.imageButton2);
        button3 = (ImageButton)findViewById(R.id.imageButton3);
        //button4 = (ImageButton)findViewById(R.id.imageButton4);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        chooseIdentity =(TextView) findViewById(R.id.chooseIdentity);

        instructionView = (TextView) findViewById(R.id.insText);
        imageButtons = new ImageButton[]{button1, button2, button3};
        textViews = new TextView[]{textView1, textView2, textView3};



        totalSize = new ArrayList<Integer>();
        for(int i=0;i< 6;i++) {
            totalSize.add(new Integer(i));
        }

        for (ImageButton button : imageButtons) {

            categoryNumber = getRandomIndex();
            final String categoryName = cateGoryName[categoryNumber].toString();
            Log.i("categoryName>",""+categoryName);
            Log.i("textCount>",""+textCount);
            Log.i("categoryNumber>",""+categoryNumber);

            textViews[textCount].setText(categoryName);
            button.setBackgroundDrawable(getResources().getDrawable(categoryButton[categoryNumber]));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChallengeCategorySelection.this, QuizChallengeActivity.class);
                    PartyGame.categoryName = categoryName;
                    intent.putExtra("categoryName", categoryName);
                    startActivity(intent);
                }
            });
            textCount++;
        }



       // textView1.setText("Doctor");
       // textView2.setText("Teacher");
       // textView3.setText("Writer");

        instructionView.setText(CenterController.controller().partyGame.getModerator().name+" will play a \n" +
                "Knowledge Challenge against each Challenger \n" +
                "to earn points!");

        chooseIdentity.setText(CenterController.controller().partyGame.getModerator().name + ", choose an identity");

/*

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeCategorySelection.this, QuizChallengeActivity.class);
                int categoryNumber = getRandomNumber();
                String categoryName = cateGoryName[categoryNumber].toString();
                PartyGame.categoryName = categoryName;
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeCategorySelection.this, QuizChallengeActivity.class);
                int categoryNumber = getRandomNumber();
                String categoryName = cateGoryName[categoryNumber].toString();
                PartyGame.categoryName = categoryName;
                intent.putExtra("categoryName", categoryName);
                startActivity(intent);
            }
        });
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_category_selection, menu);
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

    public int getRandomNumber()
    {
        Random r = new Random();
        int result = r.nextInt(5-0) + 0;
        return result;
    }


    private int getRandomIndex(){
        Random randomizer = new Random();
        int resultIndex = randomizer.nextInt(totalSize.size());
        int result = totalSize.get(resultIndex);
        totalSize.remove(resultIndex);
        return result;
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
        return;

    }
    private void showQuitDialog(){


        new AlertDialog.Builder(ChallengeCategorySelection.this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quiz? You will loose all your progress!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PartyGame.roundCategorySelected= false;
                        //finish();
                        Intent intent = new Intent(ChallengeCategorySelection.this, MainMenuActivity.class);
                        //intent.putExtra("type",3);
                        startActivity(intent);
                    }
                }).setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
