package com.tuananh2.newspaper.VnExpressNews;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tuananh2.newspaper.MyWebBrowser.WebBrowserActivity;
import com.tuananh2.newspaper.R;

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

public class VnExpressParser extends Fragment{
    public class NewsEntry {
        public String m_link, m_title,m_imageLink;

        NewsEntry(String title, String link, String imageLink) {
            this.m_link = link;
            this.m_title = title;
            this.m_imageLink = imageLink;
        }
    }

    public static final String TAG = "anhlt2";
    public List<NewsEntry> m_listNews;
    public String m_RSSLink;

    VnExpressParser(String link)
    {
        m_RSSLink= link;
        DownloadXML myAsyncTask = new DownloadXML();
        myAsyncTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vnexpress_news,container,false);
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
        String imageLink = "";
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
                }else if (parser.getName().equals("description")) {
                    String description = getDescription(parser);
                    imageLink = getImageLinkFromDescription(description);
                }
            }
            parser.next();
    }
        parser.nextTag();
        return new NewsEntry(title, link,imageLink);
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
        //link example: <img width=130 height=100 src="http://img.f31.vnecdn.net/2017/03/06/cong-an-bi-giang-ho-danh.jpg" >
        String imageLink = description.substring(description.indexOf("<img"));
        imageLink = imageLink.substring(imageLink.indexOf("http"));
        for (int i = 0; i < imageLink.length(); i++) {
            if (imageLink.charAt(i) == '\"') {
                   return imageLink.substring(0, i);
            }
        }
        return imageLink;
    }

    private class DownloadXML extends AsyncTask<Void, Void, Void> {
        MyArrayAdapter myArrayAdapter;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                m_listNews = new ArrayList<NewsEntry>();
                m_listNews = fetchXML(m_RSSLink);
                myArrayAdapter = new MyArrayAdapter(getActivity(), m_listNews);

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
            final ListView listNews = (ListView) getActivity().findViewById(R.id.list_vnexpress_news);
            // Log.d(TAG, "onProgressUpdate: list link size= "+links.size());
            listNews.setAdapter(myArrayAdapter);

            listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
                    intent.putExtra("url", m_listNews.get(i).m_link);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.news_item, parent, false);
            }
            TextView titleContent = (TextView) convertView.findViewById(R.id.news_title_text);
            ImageView titleImage = (ImageView) convertView.findViewById(R.id.news_title_image);
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
