package com.javacodegeeks.androidtwitterexample;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

public class MediaPostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_post);



        callingFragment();
    }

    private void callingFragment() {

        StatusFragment f1 = new StatusFragment();
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.base_layout,f1,"frag1");   // here 1st parameter tell thell base layout onto we gonna put on our fragments..
        transaction.commit();
    }
}
