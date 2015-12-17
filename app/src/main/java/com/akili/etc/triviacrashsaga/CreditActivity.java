package com.akili.etc.triviacrashsaga;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import extension.SoloModeActivity;

public class CreditActivity extends SoloModeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        showTitleAndBackOnly("Credit");
    }
}
