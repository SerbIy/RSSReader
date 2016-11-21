package com.example.serj_.rssreader.activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import com.example.serj_.rssreader.process.IntentEditor;
import com.example.serj_.rssreader.tasks.GetAllChannelsTask;
import com.example.serj_.rssreader.tasks.ReadFromNetTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


final public class BackgroundService extends Service {


    private ExecutorService taskpool;
    private RSSDatabaseHelper rssDatabase;

    private static final Logger logger = Logger.getLogger("MyLogger");

    public BackgroundService(){
        super();

    }
    @Override
    public IBinder onBind(final Intent intent) {


        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        final int NUMBER_OF_THREADS = 2;
        rssDatabase = new RSSDatabaseHelper(getApplicationContext());
        taskpool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        logger.info("Service created");
    }
    @Override
    public int onStartCommand(final Intent intent,final int flags,final int startId){
        super.onStartCommand(intent,flags,startId);
        logger.info("Service online");
        final int command = IntentEditor.getCommand(intent);
        switch (command) {
            case IntentEditor.GET_CHANNEL_FROM_NET:{
                logger.info("Try to download channel");
                String url = IntentEditor.getUrl(intent);
                taskpool.execute(new ReadFromNetTask(url, rssDatabase,this));
                break;
            }
            case IntentEditor.ASK_FOR_CHANNELS:{
                logger.info("Try to get channels from database");
                taskpool.execute(new GetAllChannelsTask(rssDatabase,this));
                break;
            }
            case IntentEditor.ASK_FOR_ALL_ITEMS:{
                logger.info("Try to get channels from database");
                taskpool.execute(new GetAllChannelsTask(rssDatabase,this));
                break;
            }
            default:{
                logger.info("No action");
            }
        }
        return(0);
    }
    @Override
    public void onDestroy(){
        rssDatabase.close();
        super.onDestroy();
    }
    @Override
    public boolean onUnbind(final Intent intent){

        return (false);
    }
}
