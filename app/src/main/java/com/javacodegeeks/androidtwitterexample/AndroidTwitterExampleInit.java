package com.javacodegeeks.androidtwitterexample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class AndroidTwitterExampleInit extends Activity {
    private static final String TWITTER_KEY = "eXRs5IX95mUuslzpzxjvYdwVD";
    private static final String TWITTER_SECRET = "2gzttr1zQ7HDmSL2tlmGMcjexc1x2VWt6VkNR4Wi58pQlkIU8b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        startLoginActivity();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, AndroidTwitterExample.class));
    }

}
