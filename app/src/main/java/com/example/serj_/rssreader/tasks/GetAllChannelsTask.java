package com.example.serj_.rssreader.tasks;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.process.IntentEditor;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GetAllChannelsTask implements Runnable{
    final private RSSDatabaseHelper rssDatabase;
    private final Context context;
    private static final Logger logger = Logger.getLogger("MyLogger");
    public GetAllChannelsTask(@NotNull RSSDatabaseHelper rssDatabaseHelper, @NotNull Context context){
        this.rssDatabase=rssDatabaseHelper;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            final ArrayList<Channel> channels = rssDatabase.getAllChannels();
            final Intent intent = IntentEditor.sendChannels(channels);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Throwable exception){
            logger.warning("Cannot read from net to database");
        }
    }
}
