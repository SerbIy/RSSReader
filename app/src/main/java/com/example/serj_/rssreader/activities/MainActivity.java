package com.example.serj_.rssreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.serj_.rssreader.R;
import com.sun.istack.internal.NotNull;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    static final private int  ADD_CHANNEL_DIALOG = 0;
    static final private int RESULT_NONE = 0;
    static final private int RESULT_URL = 1;
    private static final Logger logger = Logger.getLogger("MyLogger");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        logger.info("TestLog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.add_new_rrs) {
            startActivityForResult(new Intent(this, AddChannelDialog.class),ADD_CHANNEL_DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_URL) {
            String url = data.getExtras().getString("URL");
            Intent intent = new Intent(this, BackgroundService.class);
            intent = IntentCreator.addCommand(intent,IntentCreator.GET_CHANNEL_FROM_NET);
            intent = IntentCreator.addUrl(intent,url);
            logger.info("Command to read from net");
            startService(intent);


        }
        else{
            logger.info("We have no url");
        }
    }
    void updateInterface(@NotNull int command){

    }
}
