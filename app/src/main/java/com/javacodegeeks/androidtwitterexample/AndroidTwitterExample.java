package com.javacodegeeks.androidtwitterexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import static android.content.ContentValues.TAG;


public class AndroidTwitterExample extends Activity {
    private TwitterLoginButton twitterButton;


    public static int IMAGE_GETTING = 2;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(requestCode == IMAGE_GETTING) {

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // i guess we just take 1 permiision at a time when we acces camera like ..or another time when we click get libaray ....on their own time they will store at grant[0] location u are just required to check request code


                }

            }
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        if(Build.VERSION.SDK_INT < 23) {


        } else
            // for alove L version we need to get permission at the door to get throught that location ...
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {  // checkSelfPermission is a method avail in 23 api ..without if condition of (SDK_INT < 23 ) you cant implement it..

                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_GETTING );

            } else {


            }















        setUpViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterButton.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpViews() {
        setUpTwitterButton();
    }
    // ======= add below condition  =================================
    //=================== take care there is not condition here to check wheather user is already authenticated ...  if     if (getTwitterSession() == null) {    if seestion is not null you could directly uploaded the status of add this

    private void setUpTwitterButton() {
        twitterButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        twitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();

              //  setUpViewsForTweetComposer();   // Getting twitter button to post
             //   updateStatus();   //upload to twitter without twitter button


             updateMediaStatus();    //   //upload post on twitter with media..

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpViewsForTweetComposer() {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Just setting up my Fabric!");
              //  .image(myImageUri);  //its a file uri
        builder.show();
    }



    public void updateStatus(){
     //   EditText editText = (EditText) findViewById(R.id.editText);
        TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = apiClient.getStatusesService();
        statusesService.update("hey yaa", 1l, false, 0d, 0d, "", false, false, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> tweetResult) {
                Log.d(TAG, "posté");
                Toast.makeText(AndroidTwitterExample.this, "success posted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(TAG, "Erreur");
                Toast.makeText(AndroidTwitterExample.this, "failed post", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //=========================================


    public void updateMediaStatus() {

        StatusFragment f1 = new StatusFragment();
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.base_layout,f1,"frag1");   // here 1st parameter tell thell base layout onto we gonna put on our fragments..
        transaction.commit();
    }




}
