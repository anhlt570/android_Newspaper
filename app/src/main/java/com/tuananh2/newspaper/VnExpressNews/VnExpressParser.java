package com.tuananh2.newspaper.VnExpressNews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tuananh2.newspaper.R;
import com.tuananh2.newspaper.MyWebBrowser.WebBrowserActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh.letuan2 on 2/24/2017.
 */

public class VnExpressParser extends Activity {
    public class NewsEntry {
        public String m_link, m_title;

        NewsEntry(String title, String link) {
            this.m_link = link;
            this.m_title = title;
        }
    }

    public static final String TAG = "anhlt2";
    public List<NewsEntry> vnExpressNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnexpress_news);
        vnExpressNews = new ArrayList<>();
        DownloadXML myAsyncTask = new DownloadXML();
        myAsyncTask.execute();
    }

    public InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream();
    }


    public List<NewsEntry> fetchXML(String urlString) throws IOException, XmlPullParserException {
        List<NewsEntry> listEntries = new ArrayList<>();
        InputStream inputStream = getInputStream(urlString);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, null);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.nextTag();
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            Log.d(TAG, "fetchXML: event= " + parser.getEventType() + " name= " + parser.getName());
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                listEntries.add(getNewsEntry(parser));
            } else parser.next();
        }
        return listEntries;
    }

    public NewsEntry getNewsEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = "";
        String link = "";
        Log.d(TAG, "getNewsEntry: ");
        while (true) {
           if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("item")) break;
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals("link")) {
                    link = getLink(parser);
                    Log.d(TAG, "getNewsEntry: link= " + link);
                } else if (parser.getName().equals("title")) {
                    title = getTitle(parser);
                    Log.d(TAG, "getNewsEntry: title= " + title);
                }
            }
            parser.next();
    }
        parser.nextTag();
        return new NewsEntry(title, link);
    }

    public String getLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        if (parser.next() == XmlPullParser.TEXT) {
            link = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    public String getTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = "";
        parser.require(XmlPullParser.START_TAG, null, "title");
        if (parser.next() == XmlPullParser.TEXT) {
            title = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private class DownloadXML extends AsyncTask<Void, Void, Void> {
        List<NewsEntry> news;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                news = new ArrayList<>();
                news= fetchXML("http://vnexpress.net/rss/thoi-su.rss");
                publishProgress();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            final ListView listNews = (ListView) findViewById(R.id.list_vnexpress_news);
            List<String> titles= new ArrayList<>() ;
            for(NewsEntry entry:news)
            {
                titles.add(entry.m_title);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.news_item,titles);
            listNews.setAdapter(adapter);
            listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
                    String link = news.get(i).m_link;
                    intent.putExtra("url",link);
                    startActivity(intent);
                }
            });
            Log.d(TAG, "onProgressUpdate: ");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class myArrayAdapter extends ArrayAdapter<NewsEntry>
    {

        public myArrayAdapter(Context context,  List<NewsEntry> entries) {
            super(context,0 ,entries);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }
}
