package com.example.serj_.rssreader.backgroundwork;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.Item;
import com.example.serj_.rssreader.process.FeedParser;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

final class UpdateAllChannelsTask implements Runnable{
    private final RSSDatabaseHelper rssDatabase;
    private final Context context;
    private static final Logger logger = Logger.getLogger(UpdateAllChannelsTask.class.getName());

    public UpdateAllChannelsTask(@NonNull final RSSDatabaseHelper rssDatabase,@NonNull  final Context context) {
        this.rssDatabase = rssDatabase;
        this.context = context;
    }

    @Override
    public void run() {
        int counter = 0;
        try {
            final ArrayList<Channel> channels = rssDatabase.getAllChannels();
            if(channels.size()>0){
                for(Channel channel:channels){
                    final String url = channel.getChannelLink();
                    final ConnectionToNet connection = new ConnectionToNet(url);
                    final FeedParser parse = new FeedParser(connection.getStream());
                    final ArrayList<Item> items = parse.getListOfItems();
                    connection.close();
                    final int counterNewItems = rssDatabase.addItems(items);
                    if (counterNewItems>0) {
                        rssDatabase.updateNewItemsOfChannel(channel.getChannelID(), counterNewItems);
                        counter++;
                    }
                    else {
                        logger.info("There is no new items in channel "+channel.getChannelName());
                    }
                }
            }
            logger.info("There is no channels to get");
        }
        catch (final Throwable exception){
            logger.warning("Can't get all channels");
        }
        finally{
            Intent intent;
            if (counter!=0){
                intent = IntentEditor.informAboutNewItems();
            }
            else {
                intent = IntentEditor.informNoNewItems();
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

    }
}
