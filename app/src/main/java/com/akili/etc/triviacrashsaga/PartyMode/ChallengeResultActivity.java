package com.akili.etc.triviacrashsaga.PartyMode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akili.etc.triviacrashsaga.Entity.Player;
import com.akili.etc.triviacrashsaga.MainMenuActivity;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.BackGroundController;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import extension.PartyModeActivity;
import info.hoang8f.widget.FButton;

public class ChallengeResultActivity extends PartyModeActivity {

    protected HorizontalBarChart mChart;
    private FButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_result);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#009688")));

        BackGroundController.playWinBGM(this);

        nextButton = (FButton)findViewById(R.id.nextButton);

        mChart = (HorizontalBarChart) findViewById(R.id.playerChart);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);

        // mChart.setDrawYLabels(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
//        yr.setInverted(true);

        setData(100);
        mChart.animateY(2500);



        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        // mChart.setDrawLegend(false);
        if(CenterController.controller().partyGame.nextRound()) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackGroundController.playNormalBGM(ChallengeResultActivity.this);
                    Intent intent = new Intent(ChallengeResultActivity.this, PartyHintActivity.class);
                    intent.putExtra("type", 0);
                    startActivity(intent);
                    return;
                }
            });
        } else{
            //End
            nextButton.setText("Home");
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackGroundController.playNormalBGM(ChallengeResultActivity.this);
                    Intent intent = new Intent(ChallengeResultActivity.this, PartyHintActivity.class);
                    intent.putExtra("type", 6);
                    startActivity(intent);
                    return;
                }
            });
        }

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxis(YAxis.AxisDependency.LEFT).setDrawTopYLabelEntry(false);
        mChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawTopYLabelEntry(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTextSize(30);
        mChart.getLegend().setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void setData(float range) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Player> players = (ArrayList<Player>)CenterController.controller().partyGame.players.clone();

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {

                return player1.partyModeScore-player2.partyModeScore;
            }
        });

        int index = 0;
        for(Player player : players){
            xVals.add(player.name);
            yVals1.add(new BarEntry(player.partyModeScore, index));
            index++;
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Score until now");
        set1.setColors(new int[]{getResources().getColor(R.color.disqualify_button_p1),
                getResources().getColor(R.color.disqualify_button_p2),
                        getResources().getColor(R.color.disqualify_button_p3),
                                getResources().getColor(R.color.disqualify_button_p4),
                                        getResources().getColor(R.color.disqualify_button_p5),
                getResources().getColor(R.color.disqualify_button_p6)});

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, set1);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }
}
