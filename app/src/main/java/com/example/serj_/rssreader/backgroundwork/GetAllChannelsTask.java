package com.example.serj_.rssreader.backgroundwork;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

class GetAllChannelsTask implements Runnable{
    final private RSSDatabaseHelper rssDatabase;
    private final Context context;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    GetAllChannelsTask(@NonNull RSSDatabaseHelper rssDatabaseHelper,@NonNull Context context){
        this.rssDatabase=rssDatabaseHelper;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            final ArrayList<Channel> channels = rssDatabase.getAllChannels();
            final Intent intent = IntentEditor.sendChannels(channels);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (final Throwable exception){
            logger.warning("Cannot get channels from database");
        }
    }
}
