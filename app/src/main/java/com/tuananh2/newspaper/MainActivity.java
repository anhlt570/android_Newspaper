package com.tuananh2.newspaper;

import android.app.Activity;
import android.app.ActivityManager;
import android.support.v4.content.ParallelExecutorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    public static final String TAG = "anhlt2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchXML();

    }

    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://vnexpress.net/rss/thoi-su.rss");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout((15000));
                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserFactory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    String text = "";
                    int event = parser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {

                        Log.d(TAG, "run: ");
                        String name = parser.getName();
                        switch (event) {
                            case XmlPullParser.START_TAG: {
                                break;
                            }
                            case XmlPullParser.TEXT: {
                                text = parser.getText();
                                break;
                            }
                            case XmlPullParser.END_TAG: {
                                if (name.equals("title")) {
                                    Log.d(TAG, "title: " + text);
                                } else if (name.equals("link")) {
                                    Log.d(TAG, "link: " + text);
                                } else if (name.equals("pubDate")) {
                                    Log.d(TAG, "public date: " + text);
                                } else if (name.equals("description")) {
                                    Log.d(TAG, "description: " + text);
                                }
                                break;
                            }
                        }
                        parser.next();
                        event=parser.getEventType();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
}
