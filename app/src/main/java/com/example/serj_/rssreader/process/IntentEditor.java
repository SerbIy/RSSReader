package com.example.serj_.rssreader.process;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.Item;
import lombok.NonNull;

import java.util.ArrayList;


public final  class IntentEditor {

    private static final String COMMAND_TAG = "command";
    private static final String URL_TAG = "url";
    private static final String CHANNEL_ID = "channel";
    private static final String CHANNELS_TAG = "channels";
    private static final String ITEMS_TAG = "items";

    public static final int ASK_FOR_FEED_STATE = 1111;
    public static final int GET_CHANNEL_FROM_NET = 11;
    public static final int ASK_FOR_CHANNELS = 5;
    public static final int ASK_FOR_ALL_ITEMS = 7;
    public static final int ASK_FOR_ITEMS_OF_CHANNEL = 9;
    public static final int NEW_CHANNEL_ADDED = 2;
    public static final int OLD_CHANNEL_TO_ADD = 3;
    public static final int NOTHING_TO_ADD = 0;
    public static final int ALL_CHANNELS_FROM_DATABASE = 6;
    public static final int ALL_ITEMS_FROM_DATABASE = 8;
    public static final int ASK_TO_UPDATE= 12;
    public static final String FILTER = "CallbackToUi";


    public static Intent sendURLToService(@NonNull final Context context,
                                          @NonNull final String url,
                                          @NonNull final Class<?> receiver){
        Intent intent = new Intent(context,receiver);
        intent = addCommand(intent,GET_CHANNEL_FROM_NET);
        intent = addUrl(intent,url);
        return intent;
    }
    public static Intent askServiceForChannels(@NonNull final Context context,@NonNull final Class<?> receiver){
        Intent intent = new Intent(context,receiver);
        intent = addCommand(intent,ASK_FOR_CHANNELS);
        return intent;
    }
    public static Intent askServiceForItemsOfChannel(@NonNull final Context context,
                                                     @NonNull final Class<?> receiver,
                                                              final int idOfChannel){
        Intent intent = new Intent(context,receiver);
        intent = addCommand(intent,ASK_FOR_ITEMS_OF_CHANNEL);
        intent.putExtra(CHANNEL_ID,idOfChannel);
        return intent;
    }
    public static Intent askServiceForItems(@NonNull final Context context,@NonNull final Class<?> receiver){
        Intent intent = new Intent(context,receiver);
        intent = addCommand(intent,ASK_FOR_ALL_ITEMS);
        return intent;
    }
    public static Intent askServiceToUpdate(@NonNull final Context context,@NonNull final Class<?> receiver){
        Intent intent = new Intent(context,receiver);
        intent = addCommand(intent,ASK_TO_UPDATE);
        return intent;
    }
    public static PendingIntent callServiceFromAlarm(@NonNull final Context context, @NonNull final Class<?> receiver){

        return  PendingIntent.getService(context,0,IntentEditor.askServiceToUpdate(context,receiver),0);
    }
    public static Intent startItemInfoActivity(@NonNull final Context context,
                                               @NonNull final Class<?> receiver,
                                               @NonNull String url){
        Intent intent = new Intent(context,receiver);
        intent = addUrl(intent,url);
        return intent;
    }
    public static Intent startSettingsActivity(@NonNull final Context context, @NonNull final Class<?> receiver){

        return new Intent(context,receiver);
    }
    public static Intent openItemInBrowser(@NonNull String url){

        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
    public static Intent informAboutNewChannel(){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,NEW_CHANNEL_ADDED);
        return intent;
    }
    public static Intent informAboutNewItems(){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,OLD_CHANNEL_TO_ADD);
        return intent;
    }
    public static Intent informNoNewItems(){
        Intent intent = new Intent(FILTER);
        intent = addCommand(intent,NOTHING_TO_ADD);
        return intent;
    }
    public static Intent sendChannels(@NonNull final ArrayList<Channel> channels){
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
    public static Intent sendItems(@NonNull final ArrayList<Item> items){
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
    public static ArrayList<Channel> getChannels(@NonNull final Intent intent){

        return(intent.getParcelableArrayListExtra(CHANNELS_TAG));
    }
    public static ArrayList<Item> getItems(@NonNull final Intent intent){

        return(intent.getParcelableArrayListExtra(ITEMS_TAG));
    }

    public static IntentFilter getIntentFiter(){

        return new IntentFilter(FILTER);
    }
    private static  Intent addCommand(@NonNull final Intent intent, final int command){

        intent.putExtra(COMMAND_TAG,command);
        return intent;
    }
    private static Intent addUrl(@NonNull final Intent intent,@NonNull final String url){

        return intent.putExtra(URL_TAG,url);
    }
    public static int getCommand(@NonNull final Intent intent){

        return intent.getIntExtra(COMMAND_TAG,0);
    }
    public static String getUrl(@NonNull final Intent intent){

        return intent.getStringExtra(URL_TAG);
    }
    public static int getIdOfChannel(@NonNull final Intent intent){

        return intent.getIntExtra(CHANNEL_ID,0);
    }
}
