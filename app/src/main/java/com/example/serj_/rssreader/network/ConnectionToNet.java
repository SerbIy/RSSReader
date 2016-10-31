package com.example.serj_.rssreader.network;
import android.os.AsyncTask;
import android.util.Log;
import com.example.serj_.rssreader.process.FeedParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by serj_ on 21.10.2016.
 */
public final class ConnectionToNet {
    final private int READ_TIMEOUT = 10000;
    final private int CONNECT_TIMEOUT = 15000;
    private InputStream stream;
    private HttpURLConnection connection;
    public ConnectionToNet(String urlStr) {
        try {
            final URL url = new URL(urlStr);
            this.connection = (HttpURLConnection) url.openConnection();
            this.connection.setRequestProperty("Content-Type", " application/xml; charset=utf-8");
            this.connection.setReadTimeout(READ_TIMEOUT);
            this.connection.setConnectTimeout(CONNECT_TIMEOUT);
            this.connection.setRequestMethod("GET");
            this.connection.setDoInput(true);
            this.connection.connect();
            this.stream = this.connection.getInputStream();
        } catch (Exception E) {

        }
    }
    public InputStream getStream(){

        return this.stream;

    }
    public void closeStream()throws IOException{
        this.stream.close();
        this.connection.disconnect();
    }
}

