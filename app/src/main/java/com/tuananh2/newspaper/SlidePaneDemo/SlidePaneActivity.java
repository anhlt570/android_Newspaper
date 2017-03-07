package com.tuananh2.newspaper.SlidePaneDemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;

import com.tuananh2.newspaper.R;

public class SlidePaneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_pane);
        SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        slidingPaneLayout.setSliderFadeColor(ContextCompat.getColor(this, android.R.color.transparent));
    }
}