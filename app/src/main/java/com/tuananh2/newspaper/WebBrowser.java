package com.tuananh2.newspaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.PrivilegedExceptionAction;

/**
 * Created by anh.letuan2 on 2/24/2017.
 */

public class WebBrowser extends Activity {
    private class MyWebBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_browser);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView webView = (WebView) findViewById(R.id.my_web_browser);
        webView.setWebViewClient(new MyWebBrowser());
        webView.loadUrl(url);
    }
}
