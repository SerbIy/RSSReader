package com.example.serj_.rssreader.process;

import android.content.Context;
import android.content.Intent;
import com.example.serj_.rssreader.activities.BackgroundService;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;

import java.util.ArrayList;


public final  class IntentEditor {
    static final private String COMMAND_TAG = "command";
    static final private String URL_TAG = "url";
    static final private String CHANNEL_TAG = "channel";
    static final private String CHANNELS_TAG = "channels";
    static final private String ITEMS_TAG = "items";


    static final public int GET_CHANNEL_FROM_NET = 1;
    static final public int ASK_FOR_CHANNELS = 5;
    static final public int ASK_FOR_ALL_ITEMS = 7;

    static final public int NEW_CHANNEL_ADDED = 2;
    static final public int OLD_CHANNEL_TO_ADD = 3;
    static final public int NOTHING_TO_ADD = 0;
    static final public int ALL_CHANNELS_FROM_DATABASE = 6;
    static final public  int ALL_ITEMS_FROM_DATABASE = 8;

    public static final String FILTER = "CallbackToUi";


    public static Intent sendURLToService(final Context context,final String url){
        Intent intent = new Intent(context,BackgroundService.class);
        intent = addCommand(intent,GET_CHANNEL_FROM_NET);
        intent = addUrl(intent,url);
        return intent;
    }
    public static Intent askServiceForChannels(Context context){
        Intent intent = new Intent(context,BackgroundService.class);
        intent = addCommand(intent,ASK_FOR_CHANNELS);
        return intent;
    }
    public static Intent askServiceForItems(Context context){
        Intent intent = new Intent(context,BackgroundService.class);
        intent = addCommand(intent,ASK_FOR_ALL_ITEMS);
        return intent;
    }
    public static Intent informAboutNewChannel(){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,NEW_CHANNEL_ADDED);
        return intent;
    }
    public static Intent informAboutNewItems(final Channel channel){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,OLD_CHANNEL_TO_ADD);
        intent.putExtra(CHANNEL_TAG,channel);
        return intent;
    }
    public static Intent informNoNewItems(){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,NOTHING_TO_ADD);
        return intent;
    }
    public static Intent sendChannels(ArrayList<Channel> channels){
        Intent intent = new Intent(FILTER);
        if(channels.size()>0) {
            intent = addCommand(intent, ALL_CHANNELS_FROM_DATABASE);
            intent.putParcelableArrayListExtra(CHANNELS_TAG, channels);
        }
        else{
            intent = addCommand(intent,NOTHING_TO_ADD);
        }
        return intent;
    }
    public static Intent sendItems(ArrayList<Item> items){
        Intent intent = new Intent(FILTER);
        if(items.size()>0) {
            intent = addCommand(intent, ALL_ITEMS_FROM_DATABASE);
            intent.putParcelableArrayListExtra(ITEMS_TAG, items);
        }
        else{
            intent = addCommand(intent,NOTHING_TO_ADD);
        }
        return intent;
    }
    public static ArrayList<Channel> getChannels(final Intent intent){

        return(intent.getParcelableArrayListExtra(CHANNELS_TAG));
    }
    public static ArrayList<Item> getItems(final Intent intent){

        return(intent.getParcelableArrayListExtra(ITEMS_TAG));
    }


    private static  Intent addCommand(final Intent intent, final int command){
        intent.putExtra(COMMAND_TAG,command);

        return intent;
    }
    private static Intent addUrl(final Intent intent,final String url){

        return intent.putExtra(URL_TAG,url);
    }
    public static int getCommand(Intent intent){

        return intent.getIntExtra(COMMAND_TAG,0);
    }
    public static String getUrl(Intent intent){

        return intent.getStringExtra(URL_TAG);
    }
}
