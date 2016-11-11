package com.example.serj_.rssreader.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public final class ConnectionToNet implements Closeable {

    private InputStream stream;
    private HttpURLConnection connection;
    private static final Logger logger = Logger.getLogger("MyLogger");
    public ConnectionToNet(String urlStr) {
        try {
            final  int READ_TIMEOUT = 10000;
            final  int CONNECT_TIMEOUT = 15000;
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
            logger.warning("Cannot connect to url");
        }
    }
    public InputStream getStream(){

        return this.stream;

    }
    public void close()throws IOException{
        this.stream.close();
        this.connection.disconnect();
    }
}

