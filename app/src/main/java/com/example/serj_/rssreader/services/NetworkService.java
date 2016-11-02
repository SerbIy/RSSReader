package com.example.serj_.rssreader.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.serj_.rssreader.network.ConnectionToNet;
import com.example.serj_.rssreader.process.FeedParser;
import com.sun.istack.internal.NotNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

/**
 * Created by serj_ on 31.10.2016.
 */
final public class NetworkService extends Service {

    final private ExecutorService taskpool;
    final private int NUMBER_OF_THREADS = 1;

    NetworkService(){
        taskpool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
    @Override
    public IBinder onBind(final Intent intent) {


        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();

    }
    @Override
    public int onStartCommand(@NotNull final Intent intent,final int flags,final int startId){
        super.onStartCommand(intent,flags,startId);
        final int action = intent.getExtras().getInt("Action");
                    taskpool.execute(new ReadFromNetTask());
                    final String url = intent.getExtras().getString("URL");
                    ConnectionToNet net = new ConnectionToNet(url);

        return(0);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public boolean onUnbind(@NotNull final Intent intent){

        return (false);
    }
}
