package com.tuananh2.newspaper.DemoFragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.tuananh2.newspaper.R;

/**
 * Created by anh.letuan2 on 3/6/2017.
 */

public class TestFragmentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_test_fragment);

        MyFragment fragment = new MyFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
