package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.rey.material.widget.Button;

import extension.PartyModeActivity;
import info.hoang8f.widget.FButton;

public class ChooseWinerActivity extends PartyModeActivity {

    private FButton player1Button, player2Button, player3Button, player4Button, player5Button, player6Button;
    private FButton[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_winer);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        player1Button = (FButton)findViewById(R.id.button1);
        player2Button = (FButton)findViewById(R.id.button2);
        player3Button = (FButton)findViewById(R.id.button3);
        player4Button = (FButton)findViewById(R.id.button4);
        player5Button = (FButton)findViewById(R.id.button5);
        player6Button = (FButton)findViewById(R.id.button6);

        buttons = new FButton[] {player1Button, player2Button, player3Button, player4Button, player5Button, player6Button};

        for(int i=0;i<CenterController.controller().partyGame.players.size();i++){
            buttons[i].setText(CenterController.controller().partyGame.players.get(i).name);
            if(i==CenterController.controller().partyGame.moderatorIndex){
                buttons[i].setVisibility(View.INVISIBLE);
            }
        }
        for(int i=CenterController.controller().partyGame.players.size();i<buttons.length;i++){
            buttons[i].setVisibility(View.INVISIBLE);
        }


        for(FButton button : buttons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePlayer((FButton)v);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_winer, menu);
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

    @Override
    public void onBackPressed() {
        return;
    }

    private void choosePlayer(FButton playerButton)
    {
        //playerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        int buttonIndex = 0;
        for(int i=0;i<buttons.length;i++){
            if(playerButton == buttons[i]){
                buttonIndex = i;
                break;
            }
        }
        CenterController.controller().partyGame.players.get(buttonIndex).partyModeScore+=30;
        Intent intent = new Intent(ChooseWinerActivity.this, ChallengeResultActivity.class);
        startActivity(intent);
        return;
    }
}
