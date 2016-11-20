package com.example.serj_.rssreader.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import com.example.serj_.rssreader.network.ConnectionToNet;
import com.example.serj_.rssreader.process.FeedParser;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.example.serj_.rssreader.process.IntentEditor;
import com.sun.istack.internal.NotNull;

public final class ReadFromNetTask implements Runnable {
    final private String url;
    final private RSSDatabaseHelper rssDatabase;
    private final Context context;
    private static final Logger logger = Logger.getLogger("MyLogger");
    public ReadFromNetTask(@NotNull String url, @NotNull RSSDatabaseHelper rssDatabaseHelper, @NotNull Context context){
        this.url = url;
        this.rssDatabase = rssDatabaseHelper;
        this.context = context;
    }
    @Override
    public void run() {
        try {
                Intent intent;
                logger.info("Connection started");
                ConnectionToNet connection = new ConnectionToNet(this.url);
                final FeedParser parse = new FeedParser(connection.getStream());
                final Channel channel = parse.getChannelInfo();
                final ArrayList<Item> items = parse.getListOfItems();
                connection.close();
                logger.info("Connection closed");

                final boolean channelAdded = rssDatabase.addChannel(channel);
                final int newItems = rssDatabase.addItems(items);
                if (channelAdded) {
                    intent = IntentEditor.informAboutNewChannel();
                    rssDatabase.updateNewItemsOfChannel(channel.getChannelID(), newItems);
                } else {
                    if(newItems>0) {
                        intent = IntentEditor.informAboutNewItems(channel);
                        rssDatabase.updateNewItemsOfChannel(channel.getChannelID(), newItems);
                    }
                    else {
                        intent = IntentEditor.informNoNewItems();
                    }
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                logger.info("Task works fine :"+channel.getNewItems());

        }
        catch (Throwable exception){
            logger.warning("Cannot read from net to database");
        }

    }
}