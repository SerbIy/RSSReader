package com.example.serj_.rssreader.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.serj_.rssreader.network.ConnectionToNet;
import com.example.serj_.rssreader.process.FeedParser;
import com.sun.istack.internal.NotNull;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by serj_ on 31.10.2016.
 */
final public class NetworkService extends Service {
    @Nullable
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
        new Thread(new Runnable() {
            public void run() {
                try {
                    final String url = intent.getExtras().getString("URL");
                    ConnectionToNet net = new ConnectionToNet(url);
                    switch (action) {
                        case 1:{
                            FeedParser parse = new FeedParser(net.getStream());
                            parse.getChannelInfo();
                            parse.getListOfItems();
                            break;
                        }
                        default:{

                        }
                    }
                    net.closeStream();
                }
                catch (Exception e){

                }
            }
        }).start();

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
