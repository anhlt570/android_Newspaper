package com.tuananh2.newspaper.VnExpressNews;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuananh2.newspaper.R;

/**
 * Created by Tuan Anh 2 on 3/7/2017.
 */

public class VnExpressMainActivity extends Activity {

    public static final class Categories {
        public static final String MAIN_PAGE = "Trang chủ";
        public static final String DAILY_NEWS = "Thời sự";
        public static final String INTERNATIONAL = "Thế giới";
        public static final String BUSINESS = "Kinh doanh";
        public static final String ENTERTAINMENT = "Giải trí";
        public static final String SPORTS = "Thể thao";
        public static final String LAW = "Pháp luật";
        public static final String EDUCATION = "Giáo dục";
        public static final String HEATH = "Sức khỏe";
    }

    //VnExpress's links
    public static final class Links {
        public static final String MAIN_PAGE = "http://vnexpress.net/rss/tin-moi-nhat.rss";
        public static final String DAILY_NEWS = "http://vnexpress.net/rss/thoi-su.rss";
        public static final String INTERNATIONAL = "http://vnexpress.net/rss/the-gioi.rss";
        public static final String BUSINESS = "http://vnexpress.net/rss/kinh-doanh.rss";
        public static final String ENTERTAINMENT = "http://vnexpress.net/rss/giai-tri.rss";
        public static final String SPORTS = "http://vnexpress.net/rss/the-thao.rss";
        public static final String LAW = "http://vnexpress.net/rss/phap-luat.rss";
        public static final String EDUCATION = "http://vnexpress.net/rss/giao-duc.rss";
        public static final String HEATH = "http://vnexpress.net/rss/suc-khoe.rss";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnexpress_main);

        Intent intent = getIntent();
        String titleString = intent.getExtras().getString("title");
        TextView title = (TextView) findViewById(R.id.news_title);
        title.setText(titleString);
        String urlString = intent.getExtras().getString("link");
        addCategories();
        displayNews(urlString);

    }

    public void displayNews(String RSSLink) {
        Fragment listNews = new VnExpressParser(RSSLink);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.news_container_fragment, listNews);
        fragmentTransaction.commit();
    }

    public void addCategories() {
        LinearLayout categoryContainer = (LinearLayout) findViewById(R.id.categories_container);
        categoryContainer.addView(createCategoryNameButton(Categories.MAIN_PAGE, Links.MAIN_PAGE));
        categoryContainer.addView(createCategoryNameButton(Categories.DAILY_NEWS, Links.DAILY_NEWS));
        categoryContainer.addView(createCategoryNameButton(Categories.INTERNATIONAL, Links.INTERNATIONAL));
        categoryContainer.addView(createCategoryNameButton(Categories.BUSINESS, Links.BUSINESS));
        categoryContainer.addView(createCategoryNameButton(Categories.ENTERTAINMENT, Links.ENTERTAINMENT));
        categoryContainer.addView(createCategoryNameButton(Categories.SPORTS, Links.SPORTS));
        categoryContainer.addView(createCategoryNameButton(Categories.LAW, Links.LAW));
        categoryContainer.addView(createCategoryNameButton(Categories.EDUCATION, Links.EDUCATION));
        categoryContainer.addView(createCategoryNameButton(Categories.HEATH, Links.HEATH));
    }

    public Button createCategoryNameButton(final String name, final String link) {
        Button button = new Button(getApplicationContext());
        button.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.parseColor("#821720"));
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = (TextView) findViewById(R.id.news_title);
                title.setText(name);
                Fragment listNews = new VnExpressParser(link);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.news_container_fragment, listNews);
                fragmentTransaction.commit();
            }
        });
        return button;
    }

}
