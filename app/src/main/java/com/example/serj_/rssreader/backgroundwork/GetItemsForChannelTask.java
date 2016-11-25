package com.example.serj_.rssreader.backgroundwork;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.model.Item;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

class GetItemsForChannelTask implements Runnable{

    private final int id;

    private final  RSSDatabaseHelper rssDatabase;
    private final Context context;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public GetItemsForChannelTask(final int id,@NonNull RSSDatabaseHelper rssDatabaseHelper, @NonNull Context context){
        this.rssDatabase=rssDatabaseHelper;
        this.context = context;
        this.id = id;
    }
    @Override
    public void run() {
        try {
            final ArrayList<Item> items = rssDatabase.getItemsById(id);
            final Intent intent = IntentEditor.sendItems(items);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (final Throwable exception){
            logger.warning("Cannot get items from database");
        }
    }
}
