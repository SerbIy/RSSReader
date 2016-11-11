package com.example.serj_.rssreader.activities;

import android.content.Intent;

final class IntentCreator {
    static private String COMMAND_TAG = "command";
    static private String URL_TAG = "url";
    static final public int GET_CHANNEL_FROM_NET = 1;
    static  Intent addCommand(Intent intent,int command){
        intent.putExtra(COMMAND_TAG,command);

        return intent;
    }
    static Intent addUrl(Intent intent,String url){
        return intent.putExtra(URL_TAG,url);
    }
    static int getCommand(Intent intent){

        return intent.getIntExtra(COMMAND_TAG,0);
    }
    static String getUrl(Intent intent){

        return intent.getStringExtra(URL_TAG);
    }
}
