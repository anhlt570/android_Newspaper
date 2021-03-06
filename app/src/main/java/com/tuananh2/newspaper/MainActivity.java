package com.tuananh2.newspaper;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import com.tuananh2.newspaper.DemoFragment.TestFragmentActivity;
import com.tuananh2.newspaper.MyWebBrowser.WebBrowserActivity;
import com.tuananh2.newspaper.SlidePaneDemo.SlidePaneActivity;
import com.tuananh2.newspaper.VnExpressNews.VnExpressMainActivity;
import com.tuananh2.newspaper.VnExpressNews.VnExpressParser;

public class MainActivity extends Activity {
    public static final String TAG = "anhlt2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVnExpressParser = (Button) findViewById(R.id.btn_vnexpress_news);
        btnVnExpressParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVnExpressNews();
            }
        });

        Button btnWebBrowser = (Button) findViewById(R.id.btn_web_browser);
        btnWebBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyWebBrowser("http://google.com");
            }
        });
        Button btnDemoFragment = (Button) findViewById(R.id.btn_test_fragment);
        btnDemoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTestedFragment();
            }
        });
        Button btnSlidePane = (Button) findViewById(R.id.btn_slide_pane);
        btnSlidePane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSlidePaneActivity();
            }
        });
    }

    public void startVnExpressNews() {
        Intent intent = new Intent(getApplicationContext(), VnExpressMainActivity.class);
        intent.putExtra("title", "Trang chủ");
        intent.putExtra("link", "http://vnexpress.net/rss/tin-moi-nhat.rss");
        startActivity(intent);
    }

    public void startMyWebBrowser(String url) {
        Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void startTestedFragment() {
        Intent intent = new Intent(getApplicationContext(), TestFragmentActivity.class);
        startActivity(intent);
    }

    public void startSlidePaneActivity()
    {
        Intent intent = new Intent(getApplicationContext(), SlidePaneActivity.class);
        startActivity(intent);
    }

}
