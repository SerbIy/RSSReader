package com.example.serj_.rssreader.backgroundwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


final public class BackgroundService extends Service {

    private static final int NUMBER_OF_THREADS = 1;
    private ExecutorService taskPool;
    private RSSDatabaseHelper rssDatabase;
    private ArrayList<Item> itemsToShow;
    private ArrayList<Channel> channelsFilter;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public BackgroundService(){
        super();

    }
    @Override
    public IBinder onBind(@NonNull final Intent intent) {


        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        rssDatabase = new RSSDatabaseHelper(this);
        taskPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
    @Override
    public int onStartCommand(final Intent intent,final int flags,final int startId){
        logger.info("onStartCommand started");
        if(intent==null){
            return (0);
        }
        super.onStartCommand(intent,flags,startId);
        final int command = IntentEditor.getCommand(intent);
        switch (command) {
            case IntentEditor.GET_CHANNEL_FROM_NET:{
                logger.info("Try to download channel");
                final String url = IntentEditor.getUrl(intent);
                taskPool.execute(new ReadFromNetTask(url, rssDatabase,this));
                break;
            }
            case IntentEditor.ASK_FOR_CHANNELS:{
                logger.info("Try to get channels from database");
                taskPool.execute(new GetAllChannelsTask(rssDatabase,this));
                break;
            }
            case IntentEditor.ASK_FOR_ALL_ITEMS:{
                logger.info("Try to get items from database");
                taskPool.execute(new GetAllItemsTask(rssDatabase,this));
                break;
            }
            case IntentEditor.ASK_FOR_ITEMS_OF_CHANNEL:{
                logger.info("Try to get items from database");
                final int id = IntentEditor.getIdOfChannel(intent);
                taskPool.execute(new GetItemsForChannelTask(id,rssDatabase,this));
                break;
            }
            default:{
                logger.info("Unknown command: "+command);
            }
        }
        logger.info("onStartCommand finished");
        return(0);
    }
    @Override
    public void onDestroy(){
        rssDatabase.close();
        super.onDestroy();
    }
    @Override
    public boolean onUnbind(@NonNull final Intent intent){

        return (false);
    }
    private ArrayList<Channel> setChannelsFilter(@NonNull final ArrayList<Channel> channelsFromDatabase,SimpleDateFormat format){
        ArrayList<Channel> filteredList = channelsFromDatabase;
        for (int i = 0; i < filteredList.size(); i++) {
            Channel channel = filteredList.get(i);
            final String date = channel.getLastUpdate();
            try {
                final long timeMs = format.parse(date).getTime();
                long updated = System.currentTimeMillis() - timeMs;
                channel.setLastUpdate(String.valueOf(updated));
                filteredList.set(i,channel);
            }
            catch (final ParseException exception){
                logger.warning("Can't parse date: "+date);
            }

        }
        return filteredList;

    }
}
