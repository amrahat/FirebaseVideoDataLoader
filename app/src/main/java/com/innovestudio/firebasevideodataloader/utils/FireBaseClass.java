package com.innovestudio.firebasevideodataloader.utils;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

/**
 * Created by Santo on 1/3/2017.
 */

public class FireBaseClass {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference homeBannerRef, tagRef,videoRef;
    private String baseUrl = "https://videoapp-32254.firebaseio.com/";
    Context context;
    Gson gson;
    public FireBaseClass(Context context) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        homeBannerRef = firebaseDatabase.getReference(Constants.HOME_BANNER_REF);
        tagRef = firebaseDatabase.getReference(Constants.TAG_REF);
        videoRef = firebaseDatabase.getReference(Constants.VIDEO_REF);
        this.context = context;
        gson = new Gson();

    }

    public void addVideoToDatabase(String videoId){
       // videoRef.child(videoId)
    }

    public void addVideoToDatabase(String videoId, String key,String value){
        videoRef.child(videoId).child(key).setValue(value);
    }
}
