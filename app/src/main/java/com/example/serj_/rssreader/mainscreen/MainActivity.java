package com.example.serj_.rssreader.mainscreen;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.backgroundwork.BackgroundService;
import com.example.serj_.rssreader.dialogscreen.AddChannelDialog;
import com.example.serj_.rssreader.itemdetailscreen.ItemInfoActivity;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.Item;
import com.example.serj_.rssreader.process.IntentEditor;
import com.example.serj_.rssreader.process.PreferenceHelper;
import com.example.serj_.rssreader.settingsscreen.SettingsActivity;
import lombok.NonNull;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity{

    private static final int ADD_CHANNEL_DIALOG = 111;
    private static final int GO_TO_SETTINGS = 222;
    private static final int RESULT_URL = 122;
    private static final String CHANNEL_LIST_POSITION = "channel_pos";
    private static final String ITEM_LIST_POSITION = "item_pos";
    private static AlarmManager alarmMgr;
    private static ListView channelList;
    private static ListView itemList;
    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.channels_list_title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();

        ChannelListAdapter channelsAdapter = new ChannelListAdapter(this,new ArrayList<Channel>());
        channelList = (ListView) findViewById(R.id.list_of_channels);
        channelList.setAdapter(channelsAdapter);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Channel channel =(Channel)parent.getItemAtPosition(position);
                startService(IntentEditor.askServiceForItemsOfChannel(parent.getContext(),
                                                                      BackgroundService.class,
                                                                         channel.getChannelID()));
                toolbar.setTitle(channel.getChannelName());
            }
        });
        registerForContextMenu(channelList);
        ItemListAdapter itemsAdapter = new ItemListAdapter(this,new ArrayList<Item>());
        itemList = (ListView) findViewById(R.id.list_of_items);
        itemList.setAdapter(itemsAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                final String url = (String) view.getTag(R.integer.LINK_TAG);
                Intent intent;
                if(PreferenceHelper.openinBrowser(view.getContext())){
                    intent = IntentEditor.openItemInBrowser(url);
                }
                else {
                    intent = IntentEditor.startItemInfoActivity(view.getContext(), ItemInfoActivity.class, url);
                }
                startActivity(intent);
            }

        });

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_channel);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(), AddChannelDialog.class),ADD_CHANNEL_DIALOG);
            }
        });

        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        updateAlarm();
        startService(IntentEditor.askServiceForChannels(this, BackgroundService.class));
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.channel_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, IntentEditor.getIntentFiter());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MessageReceiver);

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CHANNEL_LIST_POSITION,channelList.getFirstVisiblePosition());
        savedInstanceState.putInt(ITEM_LIST_POSITION,itemList.getFirstVisiblePosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        channelList.setSelection(savedInstanceState.getInt(CHANNEL_LIST_POSITION));
        itemList.setSelection(savedInstanceState.getInt(ITEM_LIST_POSITION));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();


        if (id == R.id.settings) {
            startActivityForResult(IntentEditor.startSettingsActivity(this, SettingsActivity.class),GO_TO_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult (int requestCode,int resultCode,Intent data) {

        switch (resultCode) {
            case RESULT_URL: {
                final String url = data.getExtras().getString("URL");

                logger.info("Command to read from net");
                startService(IntentEditor.sendURLToService(this, url, BackgroundService.class));
                break;
            }
            case R.integer.PREFERENCE_CHANGED: {
                updateAlarm();
                break;
            }
            default: {
                logger.info("We have no url");
            }
        }
    }
    private static final  BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.info("We receive something!");
           switch (IntentEditor.getCommand(intent)){
               case IntentEditor.NEW_CHANNEL_ADDED:{
                   logger.info("New channel added");
                   context.startService(IntentEditor.askServiceForChannels(context,BackgroundService.class));
                   break;
               }
               case IntentEditor.NOTHING_TO_ADD:{
                   logger.info("There is nothing new");
                   break;
               }
               case IntentEditor.OLD_CHANNEL_TO_ADD:{
                   logger.info("Channel updated");
                   context.startService(IntentEditor.askServiceForChannels(context,BackgroundService.class));
                   break;
               }
               case  IntentEditor.ALL_CHANNELS_FROM_DATABASE:{
                   logger.info("We receive all channels from database");
                   final ArrayList<Channel> channels = IntentEditor.getChannels(intent);
                   if(channels!=null) {
                       updateChannelList(channels);
                       context.startService(IntentEditor.askServiceForItems(context,BackgroundService.class));
                   }
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
    private static void updateChannelList(@NonNull final ArrayList<Channel> channels){
        final DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ArrayList<Channel> modifiedChannels = new ArrayList<>();
        for (Channel channel:channels){
            if(channel.convertDate(formatter)){
                modifiedChannels.add(channel);
            }
        }
        Collections.sort(modifiedChannels);
        ChannelListAdapter adapt = (ChannelListAdapter) channelList.getAdapter();
        adapt.clear();
        adapt.addAll(modifiedChannels);
    }
    private static void updateItemList(@NonNull final ArrayList<Item> items){
        final DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ArrayList<Item> modifiedItems = new ArrayList<>();
        for (Item item:items){
            if(item.convertDate(formatter)){
                modifiedItems.add(item);
            }
        }
        ItemListAdapter adapt = (ItemListAdapter) itemList.getAdapter();
        adapt.clear();
        adapt.addAll(modifiedItems);
    }

    private void updateAlarm(){
        final long period =  PreferenceHelper.returnPeriod(this);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                period, IntentEditor.callServiceFromAlarm(this,BackgroundService.class));
        logger.info("Alarm updated");
    }
}
