package com.example.serj_.rssreader.backgroundwork;

import lombok.NonNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


final class ConnectionToNet implements Closeable {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private InputStream stream;
    private HttpURLConnection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    ConnectionToNet(@NonNull final String urlStr) {
        try{

            final URL url = new URL(urlStr);
            this.connection = (HttpURLConnection) url.openConnection();
            this.connection.setRequestProperty("Content-Type", " application/xml; charset=utf-8");
            this.connection.setReadTimeout(READ_TIMEOUT);
            this.connection.setConnectTimeout(CONNECT_TIMEOUT);
            this.connection.setRequestMethod("GET");
            this.connection.setDoInput(true);
            this.connection.connect();
            this.stream = this.connection.getInputStream();
        } catch (final Exception E) {
            logger.warning("Can't connect to url");
        }
    }
    InputStream getStream(){

        return this.stream;

    }
    public void close(){
        try {
            this.stream.close();
            this.connection.disconnect();
        }
        catch(final IOException exception){
            logger.warning("Can't close connection");
        }
    }
}

