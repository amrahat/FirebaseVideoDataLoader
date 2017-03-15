package com.innovestudio.firebasevideodataloader.utils;

/**
 * Created by AMRahat on 3/11/2017.
 */

public class YoutubeApi {
    static String firstPartUrl = "https://www.googleapis.com/youtube/v3/videos?id=";
    public static final String API_KEY = "AIzaSyDzm8kggHcd1t4rC9_SbGBg1CfO71za0gM";

    public static String getDataUrl(String videoId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstPartUrl);
        stringBuilder.append(videoId);
        stringBuilder.append("&key=");
        stringBuilder.append(API_KEY);
        stringBuilder.append("&part=snippet,statistics");

        return stringBuilder.toString();
    }
}
