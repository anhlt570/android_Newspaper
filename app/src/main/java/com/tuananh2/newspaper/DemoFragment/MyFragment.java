package com.tuananh2.newspaper.DemoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuananh2.newspaper.R;

/**
 * Created by anh.letuan2 on 3/6/2017.
 */

public class MyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_fragment,container,false);
    }
}
