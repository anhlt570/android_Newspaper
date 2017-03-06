package com.tuananh2.newspaper.VnExpressNews;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tuananh2.newspaper.R;

/**
 * Created by Tuan Anh 2 on 3/7/2017.
 */

public class VnExpressMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnexpress_main);

        Intent intent= getIntent();
        String titleString = intent.getExtras().getString("title");
        TextView title = (TextView) findViewById(R.id.news_title);
        title.setText(titleString);

        String urlString = intent.getExtras().getString("link");
        Fragment listNews = new VnExpressParser(urlString);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.news_container_fragment, listNews);
        fragmentTransaction.commit();
    }
}
