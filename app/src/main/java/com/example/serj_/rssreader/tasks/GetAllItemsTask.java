package com.example.serj_.rssreader.tasks;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import com.example.serj_.rssreader.process.IntentEditor;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GetAllItemsTask implements Runnable{
    final private RSSDatabaseHelper rssDatabase;
    private final Context context;
    private static final Logger logger = Logger.getLogger("MyLogger");
    public GetAllItemsTask(RSSDatabaseHelper rssDatabaseHelper, Context context){
        this.rssDatabase=rssDatabaseHelper;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            final ArrayList<Item> items = rssDatabase.getAllItems();
            final Intent intent = IntentEditor.sendItems(items);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Throwable exception){
            logger.warning("Cannot get items from database");
        }
    }
}
