package com.akili.etc.triviacrashsaga;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import info.hoang8f.widget.FButton;

public class SoloChallenge extends AppCompatActivity {

    private FButton nextButton, acceptButton, declineButton;
    private TextView titleView, bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_challenge);

        nextButton = (FButton)findViewById(R.id.challengeNextButton);
        acceptButton = (FButton)findViewById(R.id.challengeAcceptButton);
        declineButton = (FButton)findViewById(R.id.challengeNoyNowButton);
        titleView = (TextView)findViewById(R.id.challengeTitleView);
        bodyView = (TextView)findViewById(R.id.challengeBodyView);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcceptButton();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeAccepted();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeDeclined();
            }
        });

        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solo_challenge, menu);
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

    private void showAcceptButton(){
        nextButton.setVisibility(View.INVISIBLE);
        acceptButton.setVisibility(View.VISIBLE);
        declineButton.setVisibility(View.VISIBLE);
        titleView.setText("");
        titleView.setHeight(0);
        bodyView.setText("Write down a goal you want to achieve and why you want to achieve it.\n\nMake a poster with your goal and reason written on it. Hang ti in your room or somewhere meaningful.");
    }

    private void challengeAccepted()
    {
        titleView.setText("Accepted Doctor Challenge!");
        titleView.setHeight(150);
        bodyView.setText("This challenge has been added to your profile");
        nextButton.setText("Done");
        nextButton.setVisibility(View.VISIBLE);
        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void challengeDeclined()
    {
        titleView.setText("");
        titleView.setHeight(0);
        bodyView.setText("OKay! Let me know if you change your mind");
        nextButton.setText("Done");
        nextButton.setVisibility(View.VISIBLE);
        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
