package com.example.serj_.rssreader.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.adapters.ChannelListAdapter;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.process.IntentEditor;


import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    static final private int  ADD_CHANNEL_DIALOG = 0;
    static final private int RESULT_URL = 1;



    private static final Logger logger = Logger.getLogger("MyLogger");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(IntentEditor.FILTER));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
        ChannelListAdapter adapter = new ChannelListAdapter(this,new ArrayList<Channel>());
        ListView channelList = (ListView) findViewById(R.id.list_of_channels);
        channelList.setAdapter(adapter);
        startService(IntentEditor.askServiceForChannels(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

            logger.info("Command to read from net");
            startService(IntentEditor.sendURLToService(this,url));


        }
        else{
            logger.info("We have no url");
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.info("We receive something!");
           switch (IntentEditor.getCommand(intent)){
               case IntentEditor.NEW_CHANNEL_ADDED:{
                   logger.info("New channel added");
                   startService(IntentEditor.askServiceForChannels(context));
                   break;
               }
               case IntentEditor.NOTHING_TO_ADD:{
                   logger.info("There is nothing new");
                   break;
               }
               case IntentEditor.OLD_CHANNEL_TO_ADD:{
                   logger.info("Channel updated");
                   break;
               }
               case  IntentEditor.ALL_CHANNELS_FROM_DATABASE:{
                   logger.info("We receive all channels from database");
                   ArrayList<Channel> channels = IntentEditor.getChannels(intent);
                   updateChannelList(channels);
                   break;
               }
               default:{

               }
           }
        }
    };
    private void updateChannelList(ArrayList<Channel> channels){
        ListView listview = (ListView) findViewById(R.id.list_of_channels);
        ChannelListAdapter adapt = (ChannelListAdapter) listview.getAdapter();
        adapt.clear();
        logger.info(channels.get(0).getChannelName());
        adapt.addAll(channels);
        adapt.notifyDataSetChanged();
    }

}
