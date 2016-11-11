package com.example.serj_.rssreader.activities;

import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import com.example.serj_.rssreader.network.ConnectionToNet;
import com.example.serj_.rssreader.process.FeedParser;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import java.util.ArrayList;
import java.util.logging.Logger;


class ReadFromNetTask implements Runnable {
    final private String url;
    final private RSSDatabaseHelper rssDatabase;
    private static final Logger logger = Logger.getLogger("MyLogger");

    ReadFromNetTask(String url,RSSDatabaseHelper rssDatabaseHelper){
        this.url = url;
        this.rssDatabase = rssDatabaseHelper;
    }
    @Override
    public void run() {
        try {
            ConnectionToNet connection = new ConnectionToNet(this.url);
            FeedParser parse = new FeedParser(connection.getStream());
            Channel channel = parse.getChannelInfo();
            ArrayList<Item> items = parse.getListOfItems();
            connection.close();
            rssDatabase.addChannel(channel);
            rssDatabase.addItems(items);
        }
        catch (Throwable exception){
            logger.warning("Cannot read from net to database");
        }

    }
}
