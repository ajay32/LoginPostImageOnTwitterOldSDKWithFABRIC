package com.javacodegeeks.androidtwitterexample;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;


public class StatusFragment extends Fragment {
    Button btn_select, btn_post_image;
    EditText edt_status;
    String status;
    ImageView img_status;
    private static final int RESULT_LOAD_IMAGE = 1;
    SharedPreferences mSharedPreferences;
    String selectedImagePath;
    Uri selectedImage;
    TypedFile typedFile;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Twitter";
    String mCurrentPhotoPath;
    File mFile;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
        btn_post_image = (Button) rootView.findViewById(R.id.btn_post_image);
        btn_select = (Button) rootView.findViewById(R.id.btn_select);
        edt_status = (EditText) rootView.findViewById(R.id.edt_status);
        img_status = (ImageView) rootView.findViewById(R.id.img_status);
        btn_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusFragment.CheckNetwork.isInternetAvailable(getActivity()))  //if connection available
                {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Uploading......");
                    pDialog.show();
                    final TwitterSession session = Twitter.getSessionManager().getActiveSession();
                    mFile = new File(getRealPathFromURI(selectedImage));
                    TypedFile typedFile = new TypedFile("application/octet-stream", mFile);
                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                    MediaService ms = twitterApiClient.getMediaService();
                    ms.upload(typedFile, null, null, new Callback<Media>() {

                        @Override
                        public void success(Result<Media> mediaResult) {
                            // show on User Timeline

//                            StatusesService statusesService =    TwitterCore.getInstance().getApiClient(session).getStatusesService();
//                                 statusesService.update(edt_status.getText().toString(), null, false, null, null, null, true, false, mediaResult.data.mediaIdString, new Callback<Tweet>() {
//                                @Override
//                                public void success(Result<Tweet> tweetResult) {
//                                    //...
//                                    Toast.makeText(getActivity(), "Upload Complete", Toast.LENGTH_SHORT).show();
//                                    pDialog.dismiss();
//                                }
//
//                                @Override
//                                public void failure(TwitterException e) {
//                                    //...
//                                    Toast.makeText(getActivity(), "Upload Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    pDialog.dismiss();
//                                }
//
//                            });

                            // Show on Home Timeline
                            StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();

                            statusesService.update( " content: " + edt_status.getText().toString(), null, false, null, null, null, true, false, mediaResult.data.mediaIdString, new Callback<Tweet>() {
                                @Override
                                public void success(Result<Tweet> tweetResult) {
                                    pDialog.dismiss();
                                    Toast.makeText(getActivity(), "Upload Completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    //...
                                    pDialog.dismiss();
                                    Toast.makeText(getActivity(), "Upload Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });


                        }

                        @Override
                        public void failure(TwitterException e) {
                            //...
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }


        });


        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }

        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            img_status.setImageURI(selectedImage);

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


    public static class CheckNetwork {
        static String TAG = CheckNetwork.class.getSimpleName();

        public static boolean isInternetAvailable(Context context) {
            NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info == null) {
                Log.d(TAG, "no internet connection");
                return false;
            } else {
                if (info.isConnected()) {
                    Log.d(TAG, " internet connection available...");
                    return true;
                } else {
                    Log.d(TAG, " internet connection");
                    return true;
                }

            }
        }
    }

    public class MyTwitterApiClient extends TwitterApiClient {

        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public UploadMediaService getUploadMediaService() {

            return getService(UploadMediaService.class);
        }


    }

    interface UploadMediaService {

        @Multipart
        @POST("1.1/media/upload.json")
        void upload(@Part("media") TypedFile file, @Part("additional_owners") String owners, Callback<MediaEntity> cb);

    }

    public void openPath(Uri uri) {
        InputStream is = null;
        try {
            is = getActivity().getContentResolver().openInputStream(uri);
            //Convert your stream to data here
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
