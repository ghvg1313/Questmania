package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.QuizFinishActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;

import extension.PartyModeActivity;

public class AddPlayerActivity extends AppCompatActivity {

    private EditText player1Text;
    private EditText player2Text;
    private EditText player3Text;
    private EditText player4Text;
    private EditText player5Text;
    private EditText player6Text;

    private ImageView p1_color;
    private ImageView p2_color;
    private ImageView p3_color;
    private ImageView p4_color;
    private ImageView p5_color;
    private ImageView p6_color;

    private EditText[] playerTexts;
    private ImageView[] playerColor;

    private Button addPlayerButton, startButton, deleteButton;

    private int currentPlayerIndex = 2;
    private int maxPlayerNumbers = 0;

    private ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.init(this, 1, 0, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));


        player1Text = (EditText)findViewById(R.id.player1InputTextView);
        player2Text = (EditText)findViewById(R.id.player2InputTextView);
        player3Text = (EditText)findViewById(R.id.player3InputTextView);
        player4Text = (EditText)findViewById(R.id.player4InputTextView);
        player5Text = (EditText)findViewById(R.id.player5InputTextView);
        player6Text = (EditText)findViewById(R.id.player6InputTextView);

        p1_color    =  (ImageView)findViewById(R.id.p1_color);
        p2_color    =  (ImageView)findViewById(R.id.p2_color);
        p3_color    =  (ImageView)findViewById(R.id.p3_color);
        p4_color    =  (ImageView)findViewById(R.id.p4_color);
        p5_color    =  (ImageView)findViewById(R.id.p5_color);
        p6_color    =  (ImageView)findViewById(R.id.p6_color);

        //addPlayerButton = (FloatingActionButton)findViewById(R.id.addPlayerButton);
        startButton = (Button)findViewById(R.id.startButton);
        //deleteButton = (FloatingActionButton)findViewById(R.id.deleteButton);

        //startButton.setLineMorphingState(1, true);
        startButton.setVisibility(View.INVISIBLE);

        playerTexts = new EditText[]{player1Text, player2Text, player3Text, player4Text, player5Text, player6Text};
        playerColor = new ImageView[]{p1_color,p2_color,p3_color,p4_color,p5_color,p6_color,};

        Bundle extras = getIntent().getExtras();
        if(extras != null){
             maxPlayerNumbers = (int)extras.get("maxPlayers");
        }
        player1Text.setVisibility(View.VISIBLE);
        player2Text.setVisibility(View.VISIBLE);
        player4Text.setVisibility(View.INVISIBLE);
        player5Text.setVisibility(View.INVISIBLE);
        player6Text.setVisibility(View.INVISIBLE);

        p1_color.setVisibility(View.VISIBLE);
        p2_color.setVisibility(View.VISIBLE);
        p4_color.setVisibility(View.INVISIBLE);
        p5_color.setVisibility(View.INVISIBLE);
        p6_color.setVisibility(View.INVISIBLE);

        for(int i=2;i<maxPlayerNumbers;i++) {
            playerTexts[i].setVisibility(View.VISIBLE);
            playerColor[i].setVisibility(View.VISIBLE);
            //names.add(playerTexts[i].getText().toString());

        }


      /*  addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });*/

     /*   deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayer();
            }
        });*/

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlayerActivity.this, PartyHintActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("title", "Round 1");
                intent.putExtra("content", "Jack Blue is the moderator for round 1");
                ArrayList<String> names = new ArrayList<String>();
                for(int i=0;i<maxPlayerNumbers;i++) {
                    names.add(playerTexts[i].getText().toString());
                }
                CenterController.controller().startPartyGame(names);
                startActivity(intent);
                return;
            }
        });

        for(final EditText playerText : playerTexts){
            playerText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    validateNextButtonState();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_player, menu);
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

    private void validateNextButtonState(){
        boolean hasEmptyTextView = false;
        for(int i=0;i<maxPlayerNumbers;i++){
            EditText playerText = playerTexts[i];
            if(playerText.getText().length()==0){
                hasEmptyTextView = true;
                break;
            }
        }
        if(hasEmptyTextView) {

            startButton.setVisibility(View.INVISIBLE);
        } else{

            startButton.setVisibility(View.VISIBLE);
        }
    }

    private void addPlayer()
    {
        if(currentPlayerIndex<playerTexts.length-1){
            currentPlayerIndex++;
            playerTexts[currentPlayerIndex].setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomOut)
                    .duration(250)
                    .playOn(deleteButton);
            //deleteButton.setVisibility(View.VISIBLE);
            validateNextButtonState();
        }
    }

    private void deletePlayer()
    {
        if(currentPlayerIndex>2){
            playerTexts[currentPlayerIndex].setVisibility(View.INVISIBLE);
            currentPlayerIndex--;
            validateNextButtonState();
        }
        if(currentPlayerIndex == 2){
            YoYo.with(Techniques.ZoomOut)
                    .duration(250)
                    .playOn(deleteButton);
            //deleteButton.setVisibility(View.INVISIBLE);
        }
    }
}
