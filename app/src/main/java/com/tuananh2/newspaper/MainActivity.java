package com.tuananh2.newspaper;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.StatFs;
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
                    startWebBrowser("http://google.com");
                }
            });
    }

    public void startVnExpressParser()
    {
        Intent intent = new Intent(getApplicationContext(),VnExpressParser.class);
        startActivity(intent);
    }

    public void startWebBrowser(String url)
    {
        Intent intent = new Intent(getApplicationContext(),WebBrowser.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

}
