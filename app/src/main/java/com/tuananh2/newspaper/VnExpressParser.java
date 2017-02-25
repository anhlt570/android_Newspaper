package com.tuananh2.newspaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.zip.Inflater;

/**
 * Created by anh.letuan2 on 2/24/2017.
 */

public class VnExpressParser extends Activity {
    public class NewsEntry {
        public String m_link, m_title, m_imageLink;

        NewsEntry(String title, String link) {
            this.m_link = link;
            this.m_title = title;
        }

        NewsEntry(String title, String link, String imageLink) {
            this.m_link = link;
            this.m_title = title;
            this.m_imageLink = imageLink;
        }
    }

    public static final String TAG = "anhlt2";
    public List<NewsEntry> vnExpressNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnexpress_news);
        vnExpressNews = new ArrayList<NewsEntry>();
        m_inflater = this.getLayoutInflater();
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
        List<NewsEntry> listEntries = new ArrayList<NewsEntry>();
        InputStream inputStream = getInputStream(urlString);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, null);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.nextTag();
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            Log.d(TAG, "fetchXML: event= " + parser.getEventType() + " name= " + parser.getName());
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                //  Log.d(TAG, "balala: event= " + parser.getEventType() + " name= " + parser.getName());
                listEntries.add(getNewsEntry(parser));
            } else parser.next();
        }
        return listEntries;
    }

//    public List<String> getLinks(XmlPullParser parser) throws IOException, XmlPullParserException {
//        List<String> links = new ArrayList<String>();
//
//        parser.require(XmlPullParser.START_TAG, null, "channel");
//        while (parser.getEventType() != XmlPullParser.END_TAG) {
//
//            Log.d(TAG, "getLinks: event= " + parser.getEventType() + " name= " + parser.getName());
//            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("link")) {
//                if (parser.next() == XmlPullParser.TEXT) {
//                    links.add(parser.getText());
//                    //Log.d(TAG, "getLinks: " + parser.getText());
//                }
//            }
//            parser.next();
//        }
//        return links;
//    }

    public NewsEntry getNewsEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = "";
        String link = "";
        String imageLink = "";
        Log.d(TAG, "getNewsEntry: ");
        while (true) {
            if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("item")) break;
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals("link")) {
                    link = getLink(parser);
                    Log.d(TAG, "getNewsEntry: link= " + link);
                } else if (parser.getName().equals("title")) {
                    title = getTitle(parser);
                    Log.d(TAG, "getNewsEntry: title= " + title);
                } else if (parser.getName().equals("description")) {
                    String description = getDescription(parser);
                    imageLink = getImageLinkFromDescription(description);
                }
            }
            parser.next();
        }
        parser.nextTag();
        return new NewsEntry(title, link, imageLink);
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

    public String getDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        String description = "";
        parser.require(XmlPullParser.START_TAG, null, "description");
        if (parser.next() == XmlPullParser.TEXT) {
            description = parser.getText();
            getImageLinkFromDescription(description);
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }

    public String getImageLinkFromDescription(String description) {
        String tmpDescription = description.substring(description.indexOf("<img"));
        Log.d(TAG, "getLinkFromDescription: tmpstring1 " + tmpDescription);
        tmpDescription = tmpDescription.substring(tmpDescription.indexOf("http"));
        Log.d(TAG, "getLinkFromDescription: tmpstring2 " + tmpDescription);
        for (int i = 0; i < tmpDescription.length(); i++) {
            if (tmpDescription.charAt(i) == '\"') {
                tmpDescription = tmpDescription.substring(0, i);
                Log.d(TAG, "getLinkFromDescription: tmpstring3 " + tmpDescription);
                break;
            }
        }

        return tmpDescription;
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

    public LayoutInflater m_inflater;

    private class DownloadXML extends AsyncTask<Void, Void, Void> {
        List<NewsEntry> news;
        MyArrayAdapter myArrayAdapter;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                news = new ArrayList<NewsEntry>();
                news = fetchXML("http://vnexpress.net/rss/thoi-su.rss");
                myArrayAdapter = new MyArrayAdapter(getApplicationContext(), news);

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
            // Log.d(TAG, "onProgressUpdate: list link size= "+links.size());
            listNews.setAdapter(myArrayAdapter);

            listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), WebBrowser.class);
                    intent.putExtra("url", news.get(i).m_link);
                    startActivity(intent);
                }
            });
            Log.d(TAG, "onProgressUpdate: ");
        }
    }

    private class MyArrayAdapter extends ArrayAdapter<NewsEntry> {
        public MyArrayAdapter(Context context, List<NewsEntry> entries) {
            super(context, R.layout.news_item, entries);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsEntry entry = getItem(position);
            if (convertView == null) {
                convertView = m_inflater.inflate(R.layout.news_item, parent, false);
            }
            TextView titleContent = (TextView) convertView.findViewById(R.id.title_content);
            ImageView titleImage = (ImageView) convertView.findViewById(R.id.title_image);
            titleContent.setText(entry.m_title);
            titleImage.setTag(entry.m_imageLink);
            new DownloadImagesTask().execute(titleImage);
            return convertView;
        }
        public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

            ImageView imageView = null;

            @Override
            protected Bitmap doInBackground(ImageView... imageViews) {
                this.imageView = imageViews[0];
                return download_Image((String)imageView.getTag());
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
            }

            private Bitmap download_Image(String url) {

                Bitmap bmp =null;
                try{
                    URL ulrn = new URL(url);
                    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                    InputStream is = con.getInputStream();
                    bmp = BitmapFactory.decodeStream(is);
                    if (null != bmp)
                        return bmp;

                }catch(Exception e){}
                return bmp;
            } }
    }

}
