package com.akili.etc.triviacrashsaga.PartyMode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.akili.etc.triviacrashsaga.R;

import extension.SoloModeActivity;

public class GetPlayerNumber extends SoloModeActivity {

    private int maxPlayers=0;
    private ImageButton playerNumber3,playerNumber4,playerNumber5,playerNumber6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_player_number);

        playerNumber3 = (ImageButton) findViewById(R.id.PlayerNumber3);
        playerNumber4 = (ImageButton) findViewById(R.id.PlayerNumber4);
        //playerNumber5 = (ImageButton) findViewById(R.id.PlayerNumber5);
        //playerNumber6 = (ImageButton) findViewById(R.id.PlayerNumber6);


        playerNumber3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maxPlayers = 3;
                        Intent intent = new Intent(GetPlayerNumber.this, AddPlayerActivity.class);
                        intent.putExtra("maxPlayers", 3);
                        startActivity(intent);

                    }
                });
        playerNumber4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        maxPlayers = 4;
                        Intent intent = new Intent(GetPlayerNumber.this, AddPlayerActivity.class);
                        intent.putExtra("maxPlayers", 4);
                        startActivity(intent);
            }
        });
/*
        playerNumber5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        maxPlayers = 5;
                        Intent intent = new Intent(GetPlayerNumber.this, AddPlayerActivity.class);
                        intent.putExtra("maxPlayers", 5);
                        startActivity(intent);

            }
        });

        playerNumber6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        maxPlayers = 6;
                        Intent intent = new Intent(GetPlayerNumber.this, AddPlayerActivity.class);
                        intent.putExtra("maxPlayers", 6);
                        startActivity(intent);
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_player_number, menu);
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
}
