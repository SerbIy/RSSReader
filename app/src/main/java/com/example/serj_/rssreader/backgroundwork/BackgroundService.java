package com.example.serj_.rssreader.backgroundwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.FeedState;

import com.example.serj_.rssreader.process.IntentEditor;

import lombok.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


final public class BackgroundService extends Service {

    private static final int NUMBER_OF_THREADS = 2;

    private ExecutorService taskPool;
    private RSSDatabaseHelper rssDatabase;
    private static final Logger logger = Logger.getLogger(BackgroundService.class.getName());
    private FeedState currentFeedState;


    @Override
    public IBinder onBind(@NonNull final Intent intent) {


        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        rssDatabase = new RSSDatabaseHelper(this);
        taskPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        currentFeedState = new FeedState();
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
                taskPool.execute(new GetAllChannelsTask(rssDatabase,this,currentFeedState));
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
            case IntentEditor.ASK_TO_UPDATE:{
                logger.info("Try to update database");
                taskPool.execute(new UpdateAllChannelsTask(rssDatabase,this));
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

}
