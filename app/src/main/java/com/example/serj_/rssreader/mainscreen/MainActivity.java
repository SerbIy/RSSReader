package com.example.serj_.rssreader.mainscreen;

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
import com.example.serj_.rssreader.backgroundwork.BackgroundService;
import com.example.serj_.rssreader.dialogscreen.AddChannelDialog;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import com.example.serj_.rssreader.process.IntentEditor;
import lombok.NonNull;


import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int  ADD_CHANNEL_DIALOG = 1351;
    private static final int RESULT_URL = 2331;


    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();

        final ChannelListAdapter channelsAdapter = new ChannelListAdapter(this,new ArrayList<Channel>());
        final ListView channelList = (ListView) findViewById(R.id.list_of_channels);
        channelList.setAdapter(channelsAdapter);


        final ItemListAdapter itemsAdapter = new ItemListAdapter(this,new ArrayList<Item>());
        final ListView itemList = (ListView) findViewById(R.id.list_of_items);
        itemList.setAdapter(itemsAdapter);

        startService(IntentEditor.askServiceForChannels(this, BackgroundService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter(IntentEditor.FILTER));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        final int id = item.getItemId();


        if (id == R.id.add_new_rrs) {
            startActivityForResult(new Intent(this, AddChannelDialog.class),ADD_CHANNEL_DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult (@NonNull final int requestCode,@NonNull final int resultCode,@NonNull final Intent data){

        if(resultCode==RESULT_URL) {
            final String url = data.getExtras().getString("URL");

            logger.info("Command to read from net");
            startService(IntentEditor.sendURLToService(this,url,BackgroundService.class));
        }
        else{
            logger.info("We have no url");
        }
    }
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.info("We receive something!");
           switch (IntentEditor.getCommand(intent)){
               case IntentEditor.NEW_CHANNEL_ADDED:{
                   logger.info("New channel added");
                   startService(IntentEditor.askServiceForChannels(context,BackgroundService.class));
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
               case  IntentEditor.ALL_ITEMS_FROM_DATABASE:{
                   logger.info("We receive all items from database");
                   ArrayList<Item> items = IntentEditor.getItems(intent);
                   updateItemList(items);
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
    private void updateItemList(ArrayList<Item> items){
        ListView listview = (ListView) findViewById(R.id.list_of_items);
        ItemListAdapter adapt = (ItemListAdapter) listview.getAdapter();
        adapt.clear();
        adapt.addAll(items);
        adapt.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MessageReceiver);
        super.onPause();
    }
}
