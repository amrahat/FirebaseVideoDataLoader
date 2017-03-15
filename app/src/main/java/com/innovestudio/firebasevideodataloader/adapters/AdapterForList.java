package com.innovestudio.firebasevideodataloader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovestudio.firebasevideodataloader.R;
import com.innovestudio.firebasevideodataloader.RecyclerViewOnItemClickListener;
import com.innovestudio.firebasevideodataloader.models.Videos;

import java.util.ArrayList;

/**
 * Created by AMRahat on 3/11/2017.
 */
public class AdapterForList extends RecyclerView.Adapter<AdapterForList.VideoHolder> {
    private ArrayList<Videos> videos;
    private Context context;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    private String TAG = "adapters";

    public AdapterForList(ArrayList<Videos> videos, Context context, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.videos = videos;
        this.context = context;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.id_view_row, parent, false);
        return new VideoHolder(itemView);

    }



    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Videos video = videos.get(position);
        Log.d(TAG, "onBindViewHolder: "+video.isFlag());
        holder.statusTv.setText(video.isFlag()?context.getString(R.string.loaded):context.getString(R.string.not_loaded));
        holder.videoIdTv.setText(video.getId());


    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        private TextView videoIdTv,statusTv;
        private VideoHolder(View itemView) {
            super(itemView);
            videoIdTv = (TextView) itemView.findViewById(R.id.video_id);
            statusTv = (TextView) itemView.findViewById(R.id.status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    recyclerViewOnItemClickListener.onClick(position);
                }
            });

        }







    }
}
