package com.example.serj_.rssreader.backgroundwork;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.FeedState;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

class GetAllChannelsTask implements Runnable{

    private final RSSDatabaseHelper rssDatabase;
    private final Context context;
    private static final Logger logger = Logger.getLogger(GetAllItemsTask.class.getName());

    GetAllChannelsTask(@NonNull RSSDatabaseHelper rssDatabaseHelper, @NonNull Context context, @NonNull FeedState currentFeedState){
        this.rssDatabase=rssDatabaseHelper;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            logger.info("Task <Get channels> started");
            final ArrayList<Channel> channels = rssDatabase.getAllChannels();
            final Intent intent = IntentEditor.sendChannels(channels);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            logger.info("Task <Get channels> finished");
        } catch (final Throwable exception){
            logger.warning("Cannot get channels from database");
        }
    }
}
