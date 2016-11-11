package com.example.serj_.rssreader.activities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.example.serj_.rssreader.database.RSSDatabaseHelper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


final public class BackgroundService extends Service {
    final private static  String PREFERENCE_NAME = "MySharedPreference";
    private SharedPreferences sharedPreferences;
    private ExecutorService taskpool;
    final private int NUMBER_OF_THREADS = 1;
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
        rssDatabase = new RSSDatabaseHelper(getApplicationContext());
        taskpool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        logger.info("Service created");
    }
    @Override
    public int onStartCommand(final Intent intent,final int flags,final int startId){
        super.onStartCommand(intent,flags,startId);
        logger.info("Service online");
        final int command = IntentCreator.getCommand(intent);
        switch (command) {
            case IntentCreator.GET_CHANNEL_FROM_NET:{
                logger.info("Try to download channel");
                String url = IntentCreator.getUrl(intent);
                taskpool.execute(new ReadFromNetTask(url, rssDatabase));}
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
