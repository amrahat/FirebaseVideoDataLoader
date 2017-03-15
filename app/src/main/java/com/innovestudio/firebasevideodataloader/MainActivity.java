package com.innovestudio.firebasevideodataloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.innovestudio.firebasevideodataloader.adapters.AdapterForList;
import com.innovestudio.firebasevideodataloader.models.Videos;
import com.innovestudio.firebasevideodataloader.utils.Constants;
import com.innovestudio.firebasevideodataloader.utils.FireBaseClass;
import com.innovestudio.firebasevideodataloader.utils.ProgressDialogManager;
import com.innovestudio.firebasevideodataloader.utils.VolleyRequest;
import com.innovestudio.firebasevideodataloader.utils.YoutubeApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private String TAG = "firebaseloader";
    private ProgressDialogManager progressDialogManager;
    ArrayList<Videos> videosList = new ArrayList<>();
    private FireBaseClass fireBaseClass;
    private AdapterForList adapterForList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialogManager = new ProgressDialogManager(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        fireBaseClass = new FireBaseClass(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progressDialogManager.showLoader(getString(R.string.loading));
        getVideoIds();
    }

    private void getVideoIds() {
        firebaseDatabase.getReference("zvideos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> snapshotIterator = dataSnapshot.getChildren().iterator();
                while (snapshotIterator.hasNext()) {
                    DataSnapshot dataSnapshot1 = snapshotIterator.next();
                    String id = dataSnapshot1.getKey();
                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getValue().toString().equals("1"));
                    Videos videos = new Videos();
                    videos.setId(id);
                    videos.setFlag(dataSnapshot1.getValue().toString().equals("1"));
                    videosList.add(videos);
                }

                adapterForList = new AdapterForList(videosList, getApplicationContext(), MainActivity.this);
                recyclerView.setAdapter(adapterForList);
                progressDialogManager.hideLoader();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(int position) {
        Log.d(TAG, "onClick: " + videosList.get(position).getId());
        progressDialogManager.showLoader("Inserting into firebase");
        getVideoDetails(position);
    }

    private void getVideoDetails(final int position) {
        String url = YoutubeApi.getDataUrl(videosList.get(position).getId());
        Log.d(TAG, "getVideoDetails: "+url);
        VolleyRequest.sendRequestGet(this, url, new VolleyRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //Log.d("result", result);
                parseJson(result, position);


            }
        });
    }


    private void parseJson(String result, int position) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            JSONObject itemsObject = jsonObject.getJSONArray("items").getJSONObject(0);
            JSONObject snippetObject = itemsObject.getJSONObject("snippet");
            JSONObject statisticsObject = itemsObject.getJSONObject("statistics");
            final String videoId = itemsObject.getString("id");
            String thumbnail = snippetObject.getJSONObject("thumbnails").getJSONObject("default").getString("url");
            String publishedAt = snippetObject.getString("publishedAt");
            String title = snippetObject.getString("title");
            String description = snippetObject.getString("description");
            String viewCount = statisticsObject.getString("viewCount");
            String likeCount = statisticsObject.getString("likeCount");
            String commentCount = statisticsObject.getString("commentCount");
            String tags = getTags(snippetObject.getJSONArray("tags"));

            fireBaseClass.addVideoToDatabase(videoId, Constants.VIDEO_TITLE, title);
            fireBaseClass.addVideoToDatabase(videoId, Constants.VIDEO_THUMBNAIL, thumbnail);
            fireBaseClass.addVideoToDatabase(videoId, Constants.VIDEO_DESCRIPTION, description);
            fireBaseClass.addVideoToDatabase(videoId, Constants.VIEW_COUNT, viewCount);
            fireBaseClass.addVideoToDatabase(videoId, Constants.PUBLISHED_TIME, publishedAt);
            fireBaseClass.addVideoToDatabase(videoId, Constants.LIKE_COUNT, likeCount);
            fireBaseClass.addVideoToDatabase(videoId, Constants.COMMENT_COUNT, commentCount);
            fireBaseClass.addVideoToDatabase(videoId, Constants.TAGS, tags);
            fireBaseClass.addVideoToDatabase(videoId, Constants.LAST_VIEW_TIME, "23/09/00");
            firebaseDatabase.getReference("zvideos").child(videoId).setValue(1);
            firebaseDatabase.getReference("search").child(videoId).child("original_value").setValue(title);
            firebaseDatabase.getReference("search").child(videoId).child("sort_value").setValue(title.toLowerCase());


            firebaseDatabase.getReference("videos").child(videoId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(Constants.FACEBOOK_POST_ID)){
                        fireBaseClass.addVideoToDatabase(videoId, Constants.FACEBOOK_POST_ID, "null");
                    }
                    if(!dataSnapshot.hasChild(Constants.FACEBOOK_LIKE_COUNT)){
                        fireBaseClass.addVideoToDatabase(videoId, Constants.FACEBOOK_LIKE_COUNT, "null");

                    }
                    if(!dataSnapshot.hasChild(Constants.FACEBOOK_COMMENT_COUNT)){
                        fireBaseClass.addVideoToDatabase(videoId, Constants.FACEBOOK_COMMENT_COUNT, "null");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            videosList.get(position).setFlag(true);
            adapterForList.notifyItemChanged(position);
            progressDialogManager.hideLoader();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getTags(JSONArray tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<tags.length();i++){
            try {
                stringBuilder.append(tags.getString(i));
                if(i==tags.length()-1) break;
                stringBuilder.append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "getTags: "+stringBuilder.toString());
        return stringBuilder.toString();
    }

}

