package com.tuananh2.newspaper;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;

public class MainActivity extends Activity {
    public static final String TAG = "anhlt2";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVnExpressParser = (Button) findViewById(R.id.btn_vnexpress_parser);
        btnVnExpressParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVnExpressParser();
            }
        });

            Button btnWebBrowser =(Button) findViewById(R.id.btn_web_browser);
            btnWebBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMyWebBrowser("http://google.com");
                }
            });
            Button btnDemoFragment =(Button) findViewById(R.id.btn_test_fragment);
            btnDemoFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startTestedFragment();
                }
            });
    }

    public void startVnExpressParser()
    {
        Intent intent = new Intent(getApplicationContext(),VnExpressParser.class);
        startActivity(intent);
    }

    public void startMyWebBrowser(String url)
    {
        Intent intent = new Intent(getApplicationContext(),WebBrowserActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    public void startTestedFragment()
    {
        Intent intent = new Intent(getApplicationContext(), TestFragmentActivity.class);
        startActivity(intent);

    }

}
